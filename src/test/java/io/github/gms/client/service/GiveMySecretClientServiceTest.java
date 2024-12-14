package io.github.gms.client.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Peter Szrnka
 * @version 1.0
 */
class GiveMySecretClientServiceTest {

    private final GiveMySecretClientService service = new GiveMySecretClientService();

    private final WireMockServer wireMockServer = new WireMockServer();

    @Test
    void getSecret_whenServerIsAvailable_thenLoadValues() throws Exception {
        // arrange
        try (FileInputStream fis = new FileInputStream("src/test/resources/test-config1.properties")) {
            wireMockServer.start();

            Properties mockProperties = new Properties();
            mockProperties.load(fis);
            stubFor(get(urlEqualTo("/api/secret/secret1")).willReturn(aResponse().withBody("{\"value\":\"yes!\"}")));

            // act
            Map<String, String> response = service.getSecret(mockProperties);

            // assert
            assertEquals("yes!", response.get("value"));
        } finally {
            wireMockServer.stop();
        }
    }

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
