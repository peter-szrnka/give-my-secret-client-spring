package io.github.gms.client.validator;

import org.junit.jupiter.api.Test;

import java.util.Properties;

import static io.github.gms.client.util.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Peter Szrnka
 * @version 1.0
 */
class InputValidatorTest {

    @Test
    void validatePlaceholderKey_whenKeyHasInvalidFormat_thenThrowIllegalArgumentException() {
        // arrange
        String key = "abc";

        // act and assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> InputValidator.validatePlaceholderKey(key));
        assertEquals("Invalid placeholder format: abc", exception.getMessage());
    }

    @Test
    void validatePlaceholderKey_whenKeyHasValidFormat_thenDoNothing() {
        // arrange
        String key = "abc:def";

        // act and assert
        assertDoesNotThrow(() -> InputValidator.validatePlaceholderKey(key));
    }

    @Test
    void validateKeystoreParameters_whenKeystoreFilePropertyIsMissing_thenThrowIllegalArgumentException() {
        // arrange
        Properties mockProperties = mock(Properties.class);

        // act and assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> InputValidator.validateKeystoreParameters(mockProperties));
        assertEquals("Keystore file property is missing", exception.getMessage());
    }

    @Test
    void validateKeystoreParameters_whenKeystoreTypePropertyIsMissing_thenThrowIllegalArgumentException() {
        // arrange
        Properties mockProperties = mock(Properties.class);
        when(mockProperties.getProperty(GIVE_MY_SECRET_KEYSTORE_FILE)).thenReturn("file.properties");

        // act and assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> InputValidator.validateKeystoreParameters(mockProperties));
        assertEquals("Keystore type property is missing", exception.getMessage());
    }

    @Test
    void validateKeystoreParameters_whenKeystoreCredentialPropertyIsMissing_thenThrowIllegalArgumentException() {
        // arrange
        Properties mockProperties = mock(Properties.class);
        when(mockProperties.getProperty(GIVE_MY_SECRET_KEYSTORE_FILE)).thenReturn("file.properties");
        when(mockProperties.getProperty(GIVE_MY_SECRET_KEYSTORE_TYPE)).thenReturn("type");

        // act and assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> InputValidator.validateKeystoreParameters(mockProperties));
        assertEquals("Keystore credential property is missing", exception.getMessage());
    }

    @Test
    void validateKeystoreParameters_whenKeystoreAliasPropertyIsMissing_thenThrowIllegalArgumentException() {
        // arrange
        Properties mockProperties = mock(Properties.class);
        when(mockProperties.getProperty(GIVE_MY_SECRET_KEYSTORE_FILE)).thenReturn("file.properties");
        when(mockProperties.getProperty(GIVE_MY_SECRET_KEYSTORE_TYPE)).thenReturn("type");
        when(mockProperties.getProperty(GIVE_MY_SECRET_KEYSTORE_CREDENTIAL)).thenReturn("credential");

        // act and assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> InputValidator.validateKeystoreParameters(mockProperties));
        assertEquals("Keystore alias property is missing", exception.getMessage());
    }

    @Test
    void validateKeystoreParameters_whenKeystoreAliasCredentialPropertyIsMissing_thenThrowIllegalArgumentException() {
        // arrange
        Properties mockProperties = mock(Properties.class);
        when(mockProperties.getProperty(GIVE_MY_SECRET_KEYSTORE_FILE)).thenReturn("file.properties");
        when(mockProperties.getProperty(GIVE_MY_SECRET_KEYSTORE_TYPE)).thenReturn("type");
        when(mockProperties.getProperty(GIVE_MY_SECRET_KEYSTORE_CREDENTIAL)).thenReturn("credential");
        when(mockProperties.getProperty(GIVE_MY_SECRET_KEYSTORE_ALIAS)).thenReturn("alias");

        // act and assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> InputValidator.validateKeystoreParameters(mockProperties));
        assertEquals("Keystore alias credential property is missing", exception.getMessage());
    }

    @Test
    void validateKeystoreParameters_whenAllPropertiesArePresent_thenDoNothing() {
        // arrange
        Properties mockProperties = mock(Properties.class);
        when(mockProperties.getProperty(GIVE_MY_SECRET_KEYSTORE_FILE)).thenReturn("file.properties");
        when(mockProperties.getProperty(GIVE_MY_SECRET_KEYSTORE_TYPE)).thenReturn("type");
        when(mockProperties.getProperty(GIVE_MY_SECRET_KEYSTORE_CREDENTIAL)).thenReturn("credential");
        when(mockProperties.getProperty(GIVE_MY_SECRET_KEYSTORE_ALIAS)).thenReturn("alias");
        when(mockProperties.getProperty(GIVE_MY_SECRET_KEYSTORE_ALIAS_CREDENTIAL)).thenReturn("aliasCredential");

        // act and assert
        assertDoesNotThrow(() -> InputValidator.validateKeystoreParameters(mockProperties));
    }
}
