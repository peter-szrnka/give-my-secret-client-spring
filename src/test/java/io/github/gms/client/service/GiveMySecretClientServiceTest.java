package io.github.gms.client.service;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Peter Szrnka
 * @version 1.0
 */
class GiveMySecretClientServiceTest {

    private final GiveMySecretClientService service = new GiveMySecretClientService();

    @Test
    void getSecret_whenKeystoreSettingsMissing_thenLoadDecryptedValue() throws Exception {
        // arrange
        Properties mockProperties = new Properties();
        mockProperties.load(new FileInputStream("src/test/resources/test-config1.properties"));

        // act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.getSecret(mockProperties));

        // assert
        assertEquals("Request failed after 3 retries! Error(s): null", exception.getMessage());
    }

    @Test
    void getSecret_whenKeystoreSettingsPresent_thenLoadEncryptedValue() throws Exception {
        // arrange
        Properties mockProperties = new Properties();
        mockProperties.load(new FileInputStream("src/test/resources/test-config2.properties"));

        // act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.getSecret(mockProperties));

        // assert
        assertEquals("Request failed after 3 retries! Error(s): null", exception.getMessage());
    }
}
