package io.github.gms.client.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Peter Szrnka
 * @version 1.0
 */
public final class FileUtil {

    public static InputStream loadPropertiesFile(String key) {
        try {
            return new FileInputStream(key);
        } catch (IOException ignored) {
            return FileUtil.class.getClassLoader().getResourceAsStream(key);
        }
    }
}
