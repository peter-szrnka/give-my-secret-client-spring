package io.github.gms.client;

import org.junit.jupiter.api.Test;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author Peter Szrnka
 * @version 1.0
 */
class GiveMySecretPropertySourceDecoratorPostProcessorTest {

    private final GiveMySecretPropertySourceDecoratorPostProcessor giveMySecretPropertySourceDecoratorPostProcessor =
            new GiveMySecretPropertySourceDecoratorPostProcessor();

    @Test
    void postProcessEnvironment_whenDifferentPropertySourcesProvided_thenFilterApplicable() {
        // arrange
        ConfigurableEnvironment environment = mock(ConfigurableEnvironment.class);
        MutablePropertySources propertySources = mock(MutablePropertySources.class);
        PropertySource<?> mockPropertySource1 = mock(PropertySource.class);
        when(mockPropertySource1.getName()).thenReturn("configurationProperties");
        PropertySource<?> mockPropertySource2 = mock(PropertySource.class);
        when(mockPropertySource2.getName()).thenReturn("applicationProperties");
        List<PropertySource<?>> mockPropertySources = List.of(mockPropertySource1, mockPropertySource2);
        when(propertySources.iterator()).thenReturn(mockPropertySources.iterator());
        when(environment.getPropertySources()).thenReturn(propertySources);

        // act
        giveMySecretPropertySourceDecoratorPostProcessor.postProcessEnvironment(environment, null);

        // assert
        verify(propertySources).replace(anyString(), any(GiveMySecretValueResolvingPropertySource.class));
    }
}
