package mtf.dm.cms.hhs.gov.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtils {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Method to format JSON strings
    public static String formatJson(String jsonString) {
        try {
            Object jsonObject = gson.fromJson(jsonString, Object.class);
            return gson.toJson(jsonObject);
        } catch (Exception e) {
            return "Invalid JSON: " + jsonString;
        }
    }
}
