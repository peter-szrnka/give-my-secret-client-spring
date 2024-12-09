package io.github.gms.client;

import io.github.gms.client.service.GiveMySecretClientService;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Peter Szrnka
 * @version 1.0
 */
class GiveMySecretValueResolvingPropertySourceTest {

    private static final String PROPERTY_NAME1 = "name1";
    private static final String PROPERTY_NAME2 = "name2";
    private static final String PROPERTY_NAME3 = "name3";
    private static final String INVALID_MOCK_VALUE = "value";
    private static final String MOCK_PROPERTY_SOURCE_NAME = "mockPropertySource";

    @Test
    void getProperty_whenValueIsNotString_thenDoNotResolve() {
        // arrange
        PropertySource<?> propertySource = mock(PropertySource.class);
        when(propertySource.getName()).thenReturn(MOCK_PROPERTY_SOURCE_NAME);
        when(propertySource.getProperty(PROPERTY_NAME1)).thenReturn(1);
        GiveMySecretClientService mockClientService = mock(GiveMySecretClientService.class);
        GiveMySecretValueResolvingPropertySource giveMySecretValueResolvingPropertySource =
                new GiveMySecretValueResolvingPropertySource(propertySource, mockClientService);

        // act
        Object result = giveMySecretValueResolvingPropertySource.getProperty(PROPERTY_NAME1);

        // assert
        assertEquals(1, result);
    }

    @Test
    void getProperty_whenValueDoesNotContainPrefixAndSuffix_thenDoNotResolve() {
        // arrange
        PropertySource<?> propertySource = mock(PropertySource.class);
        when(propertySource.getName()).thenReturn(MOCK_PROPERTY_SOURCE_NAME);
        when(propertySource.getProperty(PROPERTY_NAME1)).thenReturn(INVALID_MOCK_VALUE);
        GiveMySecretClientService mockClientService = mock(GiveMySecretClientService.class);
        GiveMySecretValueResolvingPropertySource giveMySecretValueResolvingPropertySource =
                new GiveMySecretValueResolvingPropertySource(propertySource, mockClientService);

        // act
        Object result = giveMySecretValueResolvingPropertySource.getProperty(PROPERTY_NAME1);

        // assert
        assertEquals(INVALID_MOCK_VALUE, result);
    }

    @Test
    void getProperty_whenEndIndexIsAfterStartIndex_thenDoNotResolve() {
        // arrange
        PropertySource<?> propertySource = mock(PropertySource.class);
        when(propertySource.getName()).thenReturn(MOCK_PROPERTY_SOURCE_NAME);
        when(propertySource.getProperty(PROPERTY_NAME1)).thenReturn(")giveMySecret(key");
        GiveMySecretClientService mockClientService = mock(GiveMySecretClientService.class);
        GiveMySecretValueResolvingPropertySource giveMySecretValueResolvingPropertySource =
                new GiveMySecretValueResolvingPropertySource(propertySource, mockClientService);

        // act
        Object result = giveMySecretValueResolvingPropertySource.getProperty(PROPERTY_NAME1);

        // assert
        assertEquals(")giveMySecret(key", result);
    }

    @Test
    void getProperty_whenPropertiesAreValid_thenResolve() throws Exception {
        // arrange
        AtomicInteger counter = new AtomicInteger(0);
        PropertySource<?> propertySource = mock(PropertySource.class);
        when(propertySource.getName()).thenReturn(MOCK_PROPERTY_SOURCE_NAME);
        when(propertySource.getProperty(PROPERTY_NAME1)).thenReturn("giveMySecret(src/test/resources/test-config1.properties:value)");
        when(propertySource.getProperty(PROPERTY_NAME2)).thenReturn("giveMySecret(src/test/resources/test-config2.properties:username)");
        when(propertySource.getProperty(PROPERTY_NAME3)).thenReturn("giveMySecret(src/test/resources/test-config2.properties:password)");
        GiveMySecretClientService mockClientService = mock(GiveMySecretClientService.class);
        when(mockClientService.getSecret(any(Properties.class))).thenAnswer((Answer<Map<String, String>>) invocation -> {
            Map<String, String> result = new HashMap<>();

            if (counter.get() == 0) {
                result.put("value", "my-value-1");
            } else if (counter.get() > 0) {
                result.put("username", "test-user-1");
                result.put("password", "test-password-1");
            }

            counter.incrementAndGet();
            return result;
        });

        GiveMySecretValueResolvingPropertySource giveMySecretValueResolvingPropertySource =
                new GiveMySecretValueResolvingPropertySource(propertySource, mockClientService);

        // act & assert (first call)
        Object result = giveMySecretValueResolvingPropertySource.getProperty(PROPERTY_NAME1);
        assertEquals("my-value-1", result);

        // act & assert (second call)
        result = giveMySecretValueResolvingPropertySource.getProperty(PROPERTY_NAME2);
        assertEquals("test-user-1", result);

        // act & assert (third call)
        result = giveMySecretValueResolvingPropertySource.getProperty(PROPERTY_NAME3);
        assertEquals("test-password-1", result);
    }

    @Test
    void getProperty_whenApiThrowsException_thenThrowRuntimeException() throws Exception {
        // arrange
        PropertySource<?> propertySource = mock(PropertySource.class);
        when(propertySource.getName()).thenReturn(MOCK_PROPERTY_SOURCE_NAME);
        when(propertySource.getProperty(PROPERTY_NAME1)).thenReturn("giveMySecret(src/test/resources/test-config1.properties:value)");
        GiveMySecretClientService mockClientService = mock(GiveMySecretClientService.class);
        when(mockClientService.getSecret(any(Properties.class))).thenThrow(new IllegalArgumentException());

        GiveMySecretValueResolvingPropertySource giveMySecretValueResolvingPropertySource =
                new GiveMySecretValueResolvingPropertySource(propertySource, mockClientService);

        GiveMySecretValueResolvingPropertySource.clearCache();

        // act
        assertThrows(RuntimeException.class, () -> giveMySecretValueResolvingPropertySource.getProperty(PROPERTY_NAME1));
    }
}
