package io.github.gms.client.util;

import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Peter Szrnka
 * @version 1.0
 */
public class FileUtilTest {

    @Test
    void getProperty_whenPropertiesAreLoadedFromClassPath_thenResolve() throws Exception {
        try (InputStream stream = FileUtil.loadPropertiesFile("test-config1.properties")) {
            assertNotNull(stream);
        }
    }
}
