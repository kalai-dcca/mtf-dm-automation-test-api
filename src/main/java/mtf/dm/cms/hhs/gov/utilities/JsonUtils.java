package mtf.dm.cms.hhs.gov.utilities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mtf.dm.cms.hhs.gov.utilities.fileHandlerUtilities.ExcelSheetSpec;

import java.io.File;
import java.io.IOException;
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

    /**
     * Reads the JSON file at the specified path and retrieves the specifications
     * for the specified sheet. The returned {@link ExcelSheetSpec} object is
     * initialized using the JSON node corresponding to the sheet name.
     *
     * @param jsonFilePath the file path to the JSON file containing the sheet specifications.
     * @param sheetName the name of the sheet for which the specifications are to be retrieved.
     * @return an {@link ExcelSheetSpec} object containing the sheet name and column specifications.
     * @throws RuntimeException if the file cannot be read, or if the sheet specification
     *                          for the given sheet name is not found in the file.
     */
    public static ExcelSheetSpec getSheetSpec(String jsonFilePath, String sheetName) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(new File(jsonFilePath));
            JsonNode sheetNode = rootNode.get(sheetName);

            if (sheetNode == null) {
                throw new RuntimeException("Sheet specification not found for sheet: " + sheetName);
            }

            // Pass the JsonNode directly to the ExcelSheetSpec constructor
            return new ExcelSheetSpec(sheetName, sheetNode);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse the specification file: " + jsonFilePath, e);
        }
    }
}