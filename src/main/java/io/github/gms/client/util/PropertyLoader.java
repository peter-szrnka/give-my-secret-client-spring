package io.github.gms.client.util;

import io.github.gms.client.model.Property;

import java.util.Properties;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;

/**
 * @author Peter Szrnka
 * @version 1.0
 */
public class PropertyLoader {

    public static String getStringProperty(Properties properties, Property clientProperty) {
        return properties.getProperty(clientProperty.getKey(), (String) clientProperty.getDefaultValue());
    }

    public static int getIntProperty(Properties properties, Property clientProperty) {
        String propertyValue = properties.getProperty(clientProperty.getKey());

        if (propertyValue == null) {
            return (int) clientProperty.getDefaultValue();
        }

        return parseInt(propertyValue);
    }

    public static boolean getBooleanProperty(Properties properties, Property clientProperty) {
        String propertyValue = properties.getProperty(clientProperty.getKey());

        if (propertyValue == null) {
            return (boolean) clientProperty.getDefaultValue();
        }

        return parseBoolean(propertyValue);
    }
}
