package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads config.properties from the classpath. A JVM system property with the same key
 * (e.g. -Dheadless=true) always wins over the file value.
 */
public final class ConfigReader {

    private static final String CONFIG_FILE = "config.properties";
    private static final Properties PROPERTIES = load();

    private ConfigReader() {
    }

    private static Properties load() {
        Properties properties = new Properties();
        try (InputStream in = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (in == null) {
                throw new IllegalStateException(CONFIG_FILE + " not found on classpath");
            }
            properties.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load " + CONFIG_FILE, e);
        }
        return properties;
    }

    public static String get(String key) {
        String override = System.getProperty(key);
        if (override != null && !override.isBlank()) {
            return override.trim();
        }
        String value = PROPERTIES.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing config key: " + key);
        }
        return value.trim();
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    public static String baseUrl() {
        return get("baseUrl");
    }

    public static String browser() {
        return get("browser");
    }

    public static boolean headless() {
        return getBoolean("headless");
    }

    public static int explicitWait() {
        return getInt("explicitWait");
    }

    public static int implicitWait() {
        return getInt("implicitWait");
    }

    public static int pageLoadTimeout() {
        return getInt("pageLoadTimeout");
    }
}
