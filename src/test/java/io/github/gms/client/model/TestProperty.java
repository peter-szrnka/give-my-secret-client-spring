package io.github.gms.client.model;

import static io.github.gms.client.model.GiveMySecretClientConfig.DEFAULT_DISABLE_SSL_VERIFICATION;

/**
 * Default implementation for {@link Property}.
 *
 * @author Peter Szrnka
 * @version 1.0
 */
public enum TestProperty implements Property {

    API_KEY("giveMySecret.apiKey", null),
    TEST_STRING("giveMySecret.testString", "test"),
    DISABLE_SSL_VERIFICATION("giveMySecret.disableSslVerification", DEFAULT_DISABLE_SSL_VERIFICATION),
    TEST_PROPERTY("giveMySecret.testProperty", true);

    private final String key;
    private final Object defaultValue;

    TestProperty(String key, Object defaultValue) {
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
