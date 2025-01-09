package mtf.dm.cms.hhs.gov.utilities;

import mtf.dm.cms.hhs.gov.utilities.fileHandlerUtilities.ExcelColumnSpec;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class FileUtility {
    /**
     * Create Copy of Provided File
     * @param sourcePath Source Path
     * @param copyPath Duplicate Path
     */
    public static void createFileCopy(String sourcePath, String copyPath) {
        File sourceFile = new File(sourcePath);
        File copyFile = new File(copyPath);

        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;

        try{
            fileInputStream = new FileInputStream(sourceFile);
            fileOutputStream = new FileOutputStream(copyFile);

            if(fileInputStream.available() > 0){
                int i;
                while((i = fileInputStream.read()) != -1) {
                    fileOutputStream.write(i);
                }
            }else{
                throw new Exception();
            }
        }catch (FileNotFoundException e){
            System.out.println(Arrays.toString(e.getStackTrace()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try{
                if(Objects.nonNull(fileInputStream)){
                    fileInputStream.close();
                }
                if(Objects.nonNull(fileOutputStream)){
                    fileOutputStream.close();
                }
            }catch (IOException x){
                System.out.println(Arrays.toString(x.getStackTrace()));
            }
        }
    }


    /**
     * Return Map<String,String> of the provided Directory
     * @param filePath Directory
     * @return data map
     */
    public static Map<String,String> getFileDirectory(String filePath){
        Map<String,String> data = new HashMap<>();
        File file = new File(filePath);
        File[] dir = file.listFiles();
        if(Objects.nonNull(dir)){
            Arrays.sort(dir);
            Arrays.stream(dir).filter(File::isFile).forEach(c -> data.computeIfAbsent("File: "+c.getName(), s -> "Path: "+c.getAbsolutePath()));
            Arrays.stream(dir).filter(File::isDirectory).forEach(c -> data.computeIfAbsent("Directory: "+c.getName(), s -> "Directory: "+c.getAbsolutePath()));
        }
        return data;
    }

    /**
     * Create New File
     * @param path location
     * @param name name
     * @param extension extension
     */
    public static void createFile(String path, String name, String extension){
        String dir = path + File.separator + name + extension;
        try{
            File file = new File(Objects.requireNonNull(dir));
            if(Objects.requireNonNull(file).createNewFile()){
                System.out.println("New File Created at: " + dir);
            }else{
                System.out.println("File Already Exists");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Read Given File and return StringBuilder
     * @param filePath file location
     * @return StringBuilder
     */
    public static StringBuilder readFile(String filePath) {
        BufferedReader br;
        StringBuilder sb;
        try{
            File file = new File(filePath);
            br = new BufferedReader(new FileReader(file));

            int i;
            sb = new StringBuilder();
            while((i = br.read()) != -1) {
                sb.append((char)i);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb;
    }


    /**
     * Return All Lines in a form of List<String> from Provided File
     * @param filePath provided file path
     * @return List<String>
     */
    public static List<String> readFileLines(String filePath) {
        List<String> li;
        try{
            li = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return li;
    }


    /**
     * Collect data from CSV File
     * @param filePath file location
     * @param delimiter delimiter
     * @return CSV Data Map
     */
    public static Map<Integer,List<String>> getDataFromCsv(String filePath, String delimiter){
        Map<Integer,List<String>> fileData = new HashMap<>();
        try{
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            int row =1;
            while(Objects.nonNull((line = bufferedReader.readLine()))){
                String[] data = line.split(delimiter);
                fileData.put(row,Arrays.stream(data).collect(Collectors.toList()));
                row++;
            }
            return fileData;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Function to create JSON File
     * @param filePath location
     * @param keyValueMap value
     */
    public static void createJsonFile(String filePath, Map<String,List<String>> keyValueMap){
        JSONObject jsonObject = new JSONObject();

        for(Map.Entry<String,List<String>> entry : keyValueMap.entrySet()){
            jsonObject.put(entry.getKey(),new JSONArray(entry.getValue()));
        }
        createFile(filePath,"test",".json");

        try(FileWriter fileWriter = new FileWriter("test.json")){
            fileWriter.write(jsonObject.toString(4));
        }catch (IOException e){
            e.getStackTrace();
        }
    }

    public static JSONObject getJsonObjectFromTxtFile(String file){
        List<String> list = FileUtility.readFileLines(file);

        Map<String,List<String>> fakeFileMap = new HashMap<>();

        for(String each : list){
            String[] s = each.trim().split(" ");
            List<String> dataList = Arrays.stream(s).filter(p -> Objects.nonNull(p) && !p.trim().isEmpty()).collect(Collectors.toList());
            dataList.remove(s[0]);
            fakeFileMap.put(s[0],dataList);
        }
        FileUtility.createJsonFile(System.getProperty("user.dir"),fakeFileMap);


        JSONObject jsonObject = new JSONObject();

        for(Map.Entry<String,List<String>> entry : fakeFileMap.entrySet()){
            jsonObject.put(entry.getKey(),new JSONArray(entry.getValue()));
        }
        return jsonObject;
    }

    public static void createJsonFile(String filePath, Map<String,String> keyValueMap, String fileName){
        JSONObject jsonObject = new JSONObject();
        for(Map.Entry<String,String> entry : keyValueMap.entrySet()){
            jsonObject.put(entry.getKey(),entry.getValue());
        }
        createFile(filePath,fileName,".json");

        try(FileWriter fileWriter = new FileWriter(fileName+".json")){
            fileWriter.write(jsonObject.toString(4));
        }catch (IOException e){
            e.getStackTrace();
        }

    }

    public static List<List<String>> transformResultSetToList(ResultSet resultSet, Map<String, ExcelColumnSpec> columnSpecMap, String expectedValue) throws SQLException {
        Map<String, Object> dbDataMap = new HashMap<>();

        resultSet.last(); // Moves to the last row
        int rowCount = resultSet.getRow(); // Get the row count
        System.out.println("Row count in ResultSet: " + rowCount);
        resultSet.beforeFirst(); // Reset cursor to before the first row for iteration

        System.out.println(columnSpecMap.toString());

        // Initialize the list to hold column-wise data
        List<List<String>> sqlDataByColumn = new ArrayList<>();

        // Parse the expected column names from the expectedValue
        String[] expectedColumns = expectedValue.split(",\\s*"); // Split by comma and optional spaces

        // Initialize lists for each column with column names as the first element
        for (String columnName : expectedColumns) {
            List<String> columnData = new ArrayList<>();
            columnData.add(columnName.trim()); // Add the column name as the first element
            sqlDataByColumn.add(columnData); // Add the column list to the main list
        }

        // Iterate through the ResultSet
        while (resultSet.next()) {
            for (int i = 0; i < expectedColumns.length; i++) {
                String columnName = expectedColumns[i].trim();

                // Get the column spec for the current column
                ExcelColumnSpec columnSpec = columnSpecMap.get(columnName);

                // Fetch the column type and pattern from the columnSpec
                String columnType = columnSpec.getType();
                String pattern = columnSpec.getPattern();

                // Retrieve the value from the ResultSet
                Object value = resultSet.getObject(i + 1); // ResultSet column index starts from 1

                // Format the value based on the pattern
                String formattedValue = formatValue(value, columnType, pattern);

                // Add the formatted value to the corresponding column list
                sqlDataByColumn.get(i).add(formattedValue);
            }
        }

        return sqlDataByColumn;

    }

    // Helper method to format values based on column type and pattern
    private static String formatValue(Object value, String columnType, String pattern) {
        if (value == null) {
            return ""; // Handle null values
        }

        switch (columnType.toLowerCase()) {
            case "string":
                return value.toString().trim();
            case "number":
                // Format number with regex or required decimal places
                return value.toString();
            case "date":
                // Format date (use SimpleDateFormat for custom formats)
                if (value instanceof java.sql.Date || value instanceof java.util.Date) {
                    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                    return sdf.format(value);
                }
                break;
            case "dollar":
                // Format as US currency
                if (value instanceof Number) {
                    return String.format("$%.2f", value);
                }
                break;
            default:
                return value.toString(); // Default formatting
        }

        // Return raw value if no specific format applied
        return value.toString();
    }

    public static Boolean compareLists(List<List<String>> dataByColumn, List<List<String>> sqlDataByColumn) {
        boolean isMatch = true; // Assume data matches until proven otherwise

        // Iterate through the expected columns
        for (List<String> expectedColumn : dataByColumn) {
            String columnName = expectedColumn.get(0); // First element is the column name
            List<String> expectedValues = expectedColumn.subList(1, expectedColumn.size()); // Skip the column name

            // Find the matching column in the actual data
            List<String> actualColumn = sqlDataByColumn.stream()
                    .filter(column -> column.get(0).equals(columnName)) // Match by column name
                    .findFirst()
                    .orElse(null);

            if (actualColumn == null) {
                // Column is missing in actual data
                System.out.println("Column was not included in SQL. Do not need to compare column data: " + columnName);
                //isMatch = false;
                continue;
            }

            // Get the actual values (excluding the column name)
            List<String> actualValues = actualColumn.subList(1, actualColumn.size());

            // Compare values between expected and actual
            for (int i = 0; i < expectedValues.size(); i++) {
                String expectedValue = expectedValues.get(i);
                String actualValue = i < actualValues.size() ? actualValues.get(i) : "MISSING"; // Handle shorter actual values

                if (!expectedValue.equals(actualValue)) {
                    System.out.println("Mismatch in column: " + columnName);
                    System.out.println("FAILED==>Expected: " + expectedValue + ", Actual: " + actualValue);
                    isMatch = false;
                } else{
                    System.out.println("PASSED==>Expected: " + expectedValue + ", Actual: " + actualValue);
                }

            }
        }

        // Final status
        if (isMatch) {
            System.out.println("All columns and values match!");
        } else {
            System.out.println("There are mismatches in the data.");
        }

        return isMatch;
    }

    /**
     * Determine extension of file
     * @param fileName
     * @return extension after final period
     */
    public static String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        return (lastIndex == -1) ? "" : fileName.substring(lastIndex + 1).toLowerCase();
    }
}
