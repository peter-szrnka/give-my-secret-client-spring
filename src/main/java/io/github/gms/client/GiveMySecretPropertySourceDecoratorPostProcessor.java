package io.github.gms.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.Set;

/**
 * @author Peter Szrnka
 * @version 1.0
 */
public class GiveMySecretPropertySourceDecoratorPostProcessor implements EnvironmentPostProcessor {

    private static final Set<String> PROPERTY_SOURCES_TO_IGNORE = Set.of("configurationProperties");

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        MutablePropertySources propertySources = environment.getPropertySources();

        for (PropertySource<?> source : propertySources) {
            if (PROPERTY_SOURCES_TO_IGNORE.contains(source.getName())) {
                continue;
            }

            propertySources.replace(source.getName(), new GiveMySecretValueResolvingPropertySource(source));
        }
    }
}