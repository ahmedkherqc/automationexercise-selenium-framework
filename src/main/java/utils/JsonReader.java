package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Loads JSON test data from the classpath (src/test/resources/testdata).
 */
public final class JsonReader {

    private static final String DATA_DIR = "testdata/";
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private JsonReader() {
    }

    public static <T> T read(String fileName, Class<T> type) {
        try (InputStream in = open(fileName)) {
            return MAPPER.readValue(in, type);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to parse " + fileName, e);
        }
    }

    public static <T> T read(String fileName, TypeReference<T> type) {
        try (InputStream in = open(fileName)) {
            return MAPPER.readValue(in, type);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to parse " + fileName, e);
        }
    }

    public static JsonNode readTree(String fileName) {
        try (InputStream in = open(fileName)) {
            return MAPPER.readTree(in);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to parse " + fileName, e);
        }
    }

    /** Converts a JSON array of objects into DataProvider rows, one Map per row. */
    public static Object[][] toDataProvider(String fileName) {
        List<Map<String, Object>> rows = read(fileName, new TypeReference<List<Map<String, Object>>>() {
        });
        Object[][] data = new Object[rows.size()][1];
        for (int i = 0; i < rows.size(); i++) {
            data[i][0] = rows.get(i);
        }
        return data;
    }

    private static InputStream open(String fileName) {
        InputStream in = JsonReader.class.getClassLoader().getResourceAsStream(DATA_DIR + fileName);
        if (in == null) {
            throw new IllegalArgumentException("Test data file not found: " + DATA_DIR + fileName);
        }
        return in;
    }
}
