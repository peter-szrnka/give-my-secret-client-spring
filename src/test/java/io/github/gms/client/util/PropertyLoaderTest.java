package io.github.gms.client.util;

import io.github.gms.client.model.ClientProperty;
import io.github.gms.client.model.Property;
import io.github.gms.client.model.TestProperty;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.Properties;

import static io.github.gms.client.model.GiveMySecretClientConfig.DEFAULT_CONNECTION_TIMEOUT;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Peter Szrnka
 * @version 1.0
 */
class PropertyLoaderTest {

    @ParameterizedTest
    @MethodSource("getStringPropertyTestData")
    void getStringProperty_whenPropertyProvided_thenReturnExpectedValue(Property inputClientProperty, String expectedValue) throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("test-string-property.properties"));

        String result = PropertyLoader.getStringProperty(properties, inputClientProperty);

        assertEquals(expectedValue, result);
    }

    @ParameterizedTest
    @MethodSource("getIntPropertyTestData")
    void getIntProperty_whenPropertyProvided_thenReturnExpectedValue(Property inputClientProperty, int expectedValue) throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("test-int-property.properties"));

        int result = PropertyLoader.getIntProperty(properties, inputClientProperty);

        assertEquals(expectedValue, result);
    }

    @ParameterizedTest
    @MethodSource("getBooleanPropertyTestData")
    void getBooleanProperty_whenPropertyProvided_thenReturnExpectedValue(Property inputClientProperty, boolean expectedValue) throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("test-boolean-property.properties"));

        boolean result = PropertyLoader.getBooleanProperty(properties, inputClientProperty);

        assertEquals(expectedValue, result);
    }

    private static Object[][] getStringPropertyTestData() {
        return new Object[][]{
                {TestProperty.API_KEY, "test-api-key"},
                {TestProperty.TEST_STRING, "test"}
        };
    }

    private static Object[][] getIntPropertyTestData() {
        return new Object[][]{
                {ClientProperty.CONNECTION_TIMEOUT, DEFAULT_CONNECTION_TIMEOUT},
                {ClientProperty.MAX_RETRY, 1}
        };
    }

    private static Object[][] getBooleanPropertyTestData() {
        return new Object[][]{
                {TestProperty.TEST_PROPERTY, true},
                {TestProperty.DISABLE_SSL_VERIFICATION, true}
        };
    }
}
