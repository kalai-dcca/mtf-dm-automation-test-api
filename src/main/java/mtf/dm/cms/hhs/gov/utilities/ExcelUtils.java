package mtf.dm.cms.hhs.gov.utilities;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ExcelUtils {
  
   private Workbook workbook;
    private static Sheet sheet;
    private String filePath;

    // Constructor to initialize Excel file and sheet
    public ExcelUtils(String filePath, String sheetName) {

        this.filePath = filePath;
        try {
            FileInputStream fis = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet: " + sheetName + " does not exist in the file: " + filePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Excel file: " + e.getMessage());
        }
    }

    public static Row getRow(String testCase){
        Row row = null;
        for (Row cells : sheet) {
            row = cells;
            if (row.getCell(0).getStringCellValue().equals(testCase)) {
                break;
            }
        }
        return row;
    }

    public static int getUserId(String testCase){
        Row row = getRow(testCase);
        return (int)row.getCell(1).getNumericCellValue();
    }

    public static JSONObject getDataBasedOnTestCaseAndCallType(String testCase, String sheetType) throws Exception {
        JSONObject js = new JSONObject();
        Row row = getRow(testCase);
        if(Objects.nonNull(row)){
            switch (Objects.requireNonNull(SheetType.getSheetTypeEnum(sheetType))){
                case CREATE:
                    js.put("name",row.getCell(1).getStringCellValue());
                    js.put("job",row.getCell(2).getStringCellValue());
                    break;
                case UPDATE_PUT:
                    js.put("name",row.getCell(2).getStringCellValue());
                    js.put("job",row.getCell(3).getStringCellValue());
                    break;
                case UPDATE_PATCH:
                    if(row.getCell(4).getStringCellValue().equals("ALL")){
                        js.put("name",row.getCell(2).getStringCellValue());
                        js.put("job",row.getCell(3).getStringCellValue());
                    }else if(row.getCell(4).getStringCellValue().equals("FIRST")){
                        js.put("name",row.getCell(2).getStringCellValue());
                    }else if(row.getCell(4).getStringCellValue().equals("SECOND")){
                        js.put("job",row.getCell(3).getStringCellValue());
                    }
                    break;
                case DELETE:
                case LIST_USERS:
                case SINGLE_USER:
                case SINGLE_RESOURCE:
                    break;
                default:
                    throw new Exception();
            }
        }
        MyLogger.info(js.toString());
        return js;
    }

    public static String readTestCaseIdFromExcel(String testcaseFile) {
        // Read TestCaseId from the specified Excel file
        return "TC1000"; // For simplicity
    }

    public static String readJsonFileForTestCase(String testCaseId) {
        // Read the JSON file associated with the TestCaseId
        return testCaseId + ".json"; // For simplicity, we assume json file is named as TestCaseId.json
    }

    public static String getEndpointFromTestCase(String testCaseId) {
        // Fetch endpoint from Excel sheet
        return "/api/demo"; // Dummy endpoint for illustration
    }

    public static String getHttpMethodFromTestCase(String testCaseId) {
        // Fetch HTTP method (GET/POST/PUT/DELETE) from the Excel sheet
        return "POST"; // Dummy method for illustration
    }

    public static String getTestDataFromSheet(String sheetName, String testCaseId) {
        // Implement logic to fetch test data from the specified sheet in the Excel file
        return "Test data for " + testCaseId;
    }
   
    
    // Method to get cell data as String

    public String getCellData(int rowNum, int colNum) {
        Row row = sheet.getRow(rowNum);
        if (row == null) return null;
        Cell cell = row.getCell(colNum);
        if (cell == null) return null;
        return cell.toString();
    }

    // Method to get all data from the sheet
    public List<List<String>> getSheetData() {

        List<List<String>> data = new ArrayList<>();
        for (Row row : sheet) {
            List<String> rowData = new ArrayList<>();
            for (Cell cell : row) {
                rowData.add(cell.toString());
            }
            data.add(rowData);
        }
        return data;
    }
    // Method to set data into a specific cell
    public void setCellData(int rowNum, int colNum, String value) {
        Row row = sheet.getRow(rowNum);
        if (row == null) row = sheet.createRow(rowNum);
        Cell cell = row.getCell(colNum);
        if (cell == null) cell = row.createCell(colNum);
        cell.setCellValue(value);
        save();
    }
    // Method to save changes to the Excel file

    private void save() {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save Excel file: " + e.getMessage());
        }
    }
    // Method to close the workbook

    public void close() {
        try {
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to close Excel workbook: " + e.getMessage());
        }
    }

    public String getStringCellData(String testCaseId, String attributeName) {
        // Get the row for the testCaseId
        Row row = getRow(testCaseId);
        if (row == null) {
            throw new RuntimeException("Test case ID not found: " + testCaseId);
        }

        // Find the column index for the attribute name
        Row headerRow = sheet.getRow(0); // Assuming the first row contains headers
        int columnIndex = -1;
        for (Cell cell : headerRow) {
            if (cell.getStringCellValue().equalsIgnoreCase(attributeName)) {
                columnIndex = cell.getColumnIndex();
                break;
            }
        }

        if (columnIndex == -1) {
            throw new RuntimeException("Attribute name not found: " + attributeName);
        }

        // Use getCellData to fetch the value
        int rowIndex = row.getRowNum();
        return getCellData(rowIndex, columnIndex);
    }
}

