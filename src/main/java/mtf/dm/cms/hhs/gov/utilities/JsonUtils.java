package mtf.dm.cms.hhs.gov.utilities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Map;

public class JsonUtils {


    /**
     * Reads a JSON file and returns its content as a Map.
     *
     * @param jsonFilePath path to the JSON file
     * @return a Map representing the JSON structure
     */
    public static Map<String, Map<String, Object>> readJsonFile(String jsonFilePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(new File(jsonFilePath), new TypeReference<Map<String, Map<String, Object>>>() {});
        } catch (Exception e) {
            System.out.println("Failed to read JSON file: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}