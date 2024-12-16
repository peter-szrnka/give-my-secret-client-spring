package io.github.gms.client.model;

import io.github.gms.client.util.Constants;

import static io.github.gms.client.model.GiveMySecretClientConfig.*;

/**
 * Default implementation for {@link Property}.
 *
 * @author Peter Szrnka
 * @version 1.0
 */
public enum ClientProperty implements Property {
    BASE_URL("giveMySecret.baseUrl", null),
    API_KEY("giveMySecret.apiKey", null),
    SECRET_ID(Constants.GIVE_MY_SECRET_SECRET_ID, null),

    DECRYPT("giveMySecret.decrypt", true),
    KEYSTORE_FILE(Constants.GIVE_MY_SECRET_KEYSTORE_FILE, null),
    KEYSTORE_TYPE(Constants.GIVE_MY_SECRET_KEYSTORE_TYPE, "PKCS12"),
    KEYSTORE_CREDENTIAL(Constants.GIVE_MY_SECRET_KEYSTORE_CREDENTIAL, null),
    KEYSTORE_ALIAS(Constants.GIVE_MY_SECRET_KEYSTORE_ALIAS, null),
    KEYSTORE_ALIAS_CREDENTIAL(Constants.GIVE_MY_SECRET_KEYSTORE_ALIAS_CREDENTIAL, null),

    MAX_RETRY("giveMySecret.maxRetry", DEFAULT_MAX_RETRY),
    RETRY_DELAY("giveMySecret.retryDelay", DEFAULT_RETRY_DELAY),
    CONNECTION_TIMEOUT("giveMySecret.defaultConnectionTimeout", DEFAULT_CONNECTION_TIMEOUT),
    READ_TIMEOUT("giveMySecret.defaultReadTimeout", DEFAULT_READ_TIMEOUT),
    DISABLE_SSL_VERIFICATION("giveMySecret.disableSslVerification", DEFAULT_DISABLE_SSL_VERIFICATION);

    private final String key;
    private final Object defaultValue;

    ClientProperty(String key, Object defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Object getDefaultValue() {
        return defaultValue;
    }
}
