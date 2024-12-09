package io.github.gms.client;

import io.github.gms.client.service.GiveMySecretClientService;
import io.github.gms.client.util.Constants;
import org.springframework.core.env.PropertySource;
import org.springframework.lang.NonNull;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
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

        if (value instanceof String) {
            return resolvePlaceholders((String) value);
        }

        return value;
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
                properties.load(new FileInputStream(parts[0]));
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
            throw new RuntimeException(e);
        }
    }
}