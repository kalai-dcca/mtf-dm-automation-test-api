package mtf.dm.cms.hhs.gov.utilities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class FileHandlerUtility {
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
        List<String> list = FileHandlerUtility.readFileLines(file);

        Map<String,List<String>> fakeFileMap = new HashMap<>();

        for(String each : list){
            String[] s = each.trim().split(" ");
            List<String> dataList = Arrays.stream(s).filter(p -> Objects.nonNull(p) && !p.trim().isEmpty()).collect(Collectors.toList());
            dataList.remove(s[0]);
            fakeFileMap.put(s[0],dataList);
        }
        FileHandlerUtility.createJsonFile(System.getProperty("user.dir"),fakeFileMap);


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

    //DONT NEED THIS CODE. REMOVE BEFORE COMMITTING
    public static Map<String, Object> loadJsonSpecification(String jsonFilePath) throws IOException {
        // Use a JSON library like Jackson or Gson to parse the specification
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(jsonFilePath), new TypeReference<Map<String, Object>>() {});
    }

    public static Map<String, Object> transformResultSetToHashMap(ResultSet resultSet, Map<String, Object> jsonSpec) throws SQLException {
        Map<String, Object> dbDataMap = new HashMap<>();

        resultSet.last(); // Moves to the last row
        int rowCount = resultSet.getRow(); // Get the row count
        System.out.println("Row count in ResultSet: " + rowCount);
        resultSet.beforeFirst(); // Reset cursor to before the first row for iteration


        while (resultSet.next()) {
            for (Map.Entry<String, Object> entry : jsonSpec.entrySet()) {
                String columnName = entry.getKey();
                Map<String, String> columnSpec = (Map<String, String>) entry.getValue();
                String columnType = columnSpec.get("type");
                String pattern = columnSpec.get("pattern");

                Object value = transformDatabaseValue(resultSet, columnName, columnType, pattern);
                dbDataMap.put(columnName, value);
            }
        }

        return dbDataMap;
    }

    private static Object transformDatabaseValue(ResultSet resultSet, String columnName, String columnType, String pattern) throws SQLException {
        Object value;

        switch (columnType.toLowerCase()) {
            case "string":
                value = resultSet.getString(columnName);
                break;
            case "date":
                value = resultSet.getDate(columnName).toString();
                break;
            case "dollar":
                value = String.format("$%.2f", resultSet.getDouble(columnName));
                break;
            case "number":
                value = resultSet.getInt(columnName);
                break;
            default:
                throw new IllegalArgumentException("Unsupported column type: " + columnType);
        }

        return value; // Apply further transformation based on the pattern if needed
    }

    public static void compareHashMaps(Map<String, Object> fileDataMap, Map<String, Object> dbDataMap) {
        // Compare the two hashmaps and log/report mismatches
        if (!fileDataMap.equals(dbDataMap)) {
            throw new AssertionError("File data and database data do not match.");
        }
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
