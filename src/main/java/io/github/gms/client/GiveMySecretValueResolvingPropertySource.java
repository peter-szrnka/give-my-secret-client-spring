package io.github.gms.client;

import io.github.gms.client.builder.GiveMySecretClientBuilder;
import io.github.gms.client.enums.KeystoreType;
import io.github.gms.client.model.GetSecretRequest;
import io.github.gms.client.model.GiveMySecretClientConfig;
import io.github.gms.client.util.Constants;
import org.springframework.core.env.PropertySource;
import org.springframework.lang.NonNull;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static io.github.gms.client.util.Constants.EMPTY;
import static io.github.gms.client.util.Constants.PLACEHOLDER_PREFIX;
import static io.github.gms.client.validator.InputValidator.validateKeystoreParameters;
import static io.github.gms.client.validator.InputValidator.validatePlaceholderKey;

/**
 * @author Peter Szrnka
 * @version 1.0
 */
public class GiveMySecretValueResolvingPropertySource extends PropertySource<PropertySource<?>> {

    private static final Map<String, Properties> PROPERTIES_CACHE = new HashMap<>();
    private static final Map<String, Map<String, String>> RESPONSE_CACHE = new HashMap<>();

    public GiveMySecretValueResolvingPropertySource(PropertySource<?> delegate) {
        super(delegate.getName(), delegate);
    }

    @Override
    public Object getProperty(@NonNull String name) {
        Object value = super.getSource().getProperty(name);

        if (value instanceof String) {
            return resolvePlaceholders((String) value);
        }

        return value;
    }

    private static String resolvePlaceholders(String value) {
        if (value.contains(PLACEHOLDER_PREFIX)) {
            int startIndex = value.indexOf(PLACEHOLDER_PREFIX) + PLACEHOLDER_PREFIX.length();
            int endIndex = value.indexOf(")", startIndex);
            if (endIndex > startIndex) {
                String key = value.substring(startIndex, endIndex);
                String resolvedValue = resolvePlaceholder(key);
                return value.replace(PLACEHOLDER_PREFIX + key + ")", resolvedValue);
            }
        }

        return value;
    }

    private static String resolvePlaceholder(String key) {
        String[] parts = key.split(":");
        validatePlaceholderKey(key, parts);

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
                Map<String, String> response = getSecret(properties);
                RESPONSE_CACHE.put(secretId, response);
            }

            return RESPONSE_CACHE.get(secretId).get(contentKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private static Map<String, String> getSecret(Properties properties) throws Exception {
        GiveMySecretClientConfig config = GiveMySecretClientConfig.builder()
                .url(properties.getProperty("giveMySecret.baseUrl", "http://localhost:8080/"))
                .build();
        GiveMySecretClient client = GiveMySecretClientBuilder.create(config);

        GetSecretRequest.Builder builder = GetSecretRequest.builder()
                .apiKey(properties.getProperty("giveMySecret.apiKey", EMPTY))
                .secretId(properties.getProperty(Constants.GIVE_MY_SECRET_SECRET_ID, EMPTY));

        if (Constants.TRUE.equals(properties.getProperty("giveMySecret.decrypt", Constants.TRUE))) {
            validateKeystoreParameters(properties);
            builder.keystore(new FileInputStream(properties.getProperty(Constants.GIVE_MY_SECRET_KEYSTORE_FILE)))
                    .keystoreType(KeystoreType.valueOf(properties.getProperty(Constants.GIVE_MY_SECRET_KEYSTORE_TYPE, "PKCS12")))
                    .keystoreCredential(properties.getProperty(Constants.GIVE_MY_SECRET_KEYSTORE_CREDENTIAL))
                    .keystoreAlias(properties.getProperty(Constants.GIVE_MY_SECRET_KEYSTORE_ALIAS))
                    .keystoreAliasCredential(properties.getProperty(Constants.GIVE_MY_SECRET_KEYSTORE_ALIAS_CREDENTIAL));
        }

        return client.getSecret(builder.build());
    }
}