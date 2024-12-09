package io.github.gms.client.validator;

import java.util.Properties;

import static io.github.gms.client.util.Constants.*;

/**
 * @author Peter Szrnka
 * @version 1.0
 */
public class InputValidator {

    private InputValidator() {
    }

    public static void validatePlaceholderKey(String key) {
        if (key.split(":").length != 2) {
            throw new IllegalArgumentException("Invalid placeholder format: " + key);
        }
    }

    public static void validateKeystoreParameters(Properties properties) {
        if (properties.getProperty(GIVE_MY_SECRET_KEYSTORE_FILE) == null) {
            throw new IllegalArgumentException("Keystore file property is missing");
        }

        if (properties.getProperty(GIVE_MY_SECRET_KEYSTORE_TYPE) == null) {
            throw new IllegalArgumentException("Keystore type property is missing");
        }

        if (properties.getProperty(GIVE_MY_SECRET_KEYSTORE_CREDENTIAL) == null) {
            throw new IllegalArgumentException("Keystore credential property is missing");
        }

        if (properties.getProperty(GIVE_MY_SECRET_KEYSTORE_ALIAS) == null) {
            throw new IllegalArgumentException("Keystore alias property is missing");
        }

        if (properties.getProperty(GIVE_MY_SECRET_KEYSTORE_ALIAS_CREDENTIAL) == null) {
            throw new IllegalArgumentException("Keystore alias credential property is missing");
        }
    }
}
