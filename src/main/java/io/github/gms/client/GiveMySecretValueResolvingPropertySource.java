package io.github.gms.client;

import io.github.gms.client.exception.GiveMySecretPropertyResolutionException;
import io.github.gms.client.service.GiveMySecretClientService;
import io.github.gms.client.util.Constants;
import io.github.gms.client.util.FileUtil;
import org.springframework.core.env.PropertySource;
import org.springframework.lang.NonNull;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import static io.github.gms.client.util.Constants.PLACEHOLDER_PREFIX;
import static io.github.gms.client.util.Constants.PLACEHOLDER_SUFFIX;
import static io.github.gms.client.validator.InputValidator.validatePlaceholderKey;

/**
 * @author Peter Szrnka
 * @version 1.0
 */
public class GiveMySecretValueResolvingPropertySource extends PropertySource<PropertySource<?>> {

    private static final Map<String, Properties> PROPERTIES_CACHE = new HashMap<>();
    private static final Map<String, Map<String, String>> RESPONSE_CACHE = new HashMap<>();

    private final GiveMySecretClientService giveMySecretClientService;

    public GiveMySecretValueResolvingPropertySource(PropertySource<?> delegate, GiveMySecretClientService giveMySecretClientService) {
        super(delegate.getName(), delegate);
        this.giveMySecretClientService = giveMySecretClientService;
    }

    @Override
    public Object getProperty(@NonNull String name) {
        Object value = super.getSource().getProperty(name);

        if (value instanceof String placeholder) {
            return resolvePlaceholders(placeholder);
        }

        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GiveMySecretValueResolvingPropertySource that = (GiveMySecretValueResolvingPropertySource) o;
        return Objects.equals(giveMySecretClientService, that.giveMySecretClientService);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), giveMySecretClientService);
    }

    public static void clearCache() {
        PROPERTIES_CACHE.clear();
        RESPONSE_CACHE.clear();
    }

    private String resolvePlaceholders(String value) {
        if (value.contains(PLACEHOLDER_PREFIX)) {
            int startIndex = value.indexOf(PLACEHOLDER_PREFIX) + PLACEHOLDER_PREFIX.length();
            int endIndex = value.indexOf(PLACEHOLDER_SUFFIX, startIndex);
            if (endIndex > startIndex) {
                String key = value.substring(startIndex, endIndex);
                String resolvedValue = resolvePlaceholder(key);
                return value.replace(PLACEHOLDER_PREFIX + key + PLACEHOLDER_SUFFIX, resolvedValue);
            }
        }

        return value;
    }

    private String resolvePlaceholder(String key) {
        validatePlaceholderKey(key);
        String[] parts = key.split(":");

        try {
            Properties properties = PROPERTIES_CACHE.get(parts[0]);

            if (properties == null) {
                properties = new Properties();
                try (InputStream fis = FileUtil.loadPropertiesFile(parts[0])) {
                    properties.load(fis);
                }

                PROPERTIES_CACHE.put(parts[0], properties);
            }

            String contentKey = parts[1];
            String secretId = properties.getProperty(Constants.GIVE_MY_SECRET_SECRET_ID);

            if (RESPONSE_CACHE.get(secretId) == null) {
                Map<String, String> response = giveMySecretClientService.getSecret(properties);
                RESPONSE_CACHE.put(secretId, response);
            }

            return RESPONSE_CACHE.get(secretId).get(contentKey);
        } catch (Exception e) {
            throw new GiveMySecretPropertyResolutionException(e);
        }
    }
}