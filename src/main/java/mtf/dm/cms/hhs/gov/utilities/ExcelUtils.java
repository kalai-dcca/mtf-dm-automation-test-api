package mtf.dm.cms.hhs.gov.utilities;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


public class ExcelUtils {
  
   private Workbook workbook;
    private static Sheet sheet;
    private String filePath;

    // Constructor to initialize Excel file and sheet
    public ExcelUtils(String filePath, String sheetName) throws SuppressedStackTraceException {

        this.filePath = filePath;
        try {
            FileInputStream fis = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                MyLogger.error("Sheet: " + sheetName + " does not exist in the file: " + filePath);
                throw new SuppressedStackTraceException("Sheet: " + sheetName + " does not exist in the file: " + filePath);
            }
        } catch (IOException e) {
            MyLogger.error("Failed to load Excel file: " + filePath, e);
            throw new SuppressedStackTraceException("Failed to load Excel file: " + filePath);
        }
    }

    public static Row getRow(String testCase) {
        Row row = null;

        for (Row cells : sheet) {
            row = cells;

            Cell cell = row.getCell(0); // Assuming the first column contains the test case ID
            if (cell == null) {
                continue; // Skip if the cell is empty
            }

            String cellValue;

            // Handle cell types dynamically
            switch (cell.getCellType()) {
                case STRING:
                    cellValue = cell.getStringCellValue(); // Get string value directly
                    break;
                case NUMERIC:
                    cellValue = String.valueOf((int) cell.getNumericCellValue()); // Convert numeric to string
                    break;
                case BLANK:
                    continue; // Skip blank cells
                default:
                    throw new RuntimeException("Unsupported cell type: " + cell.getCellType());
            }

            // Compare cell value with the provided test case ID
            if (cellValue.equals(testCase)) {
                break;
            }
        }

        if (row == null) {
            throw new RuntimeException("Row not found for test case: " + testCase);
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
    public List<List<String>> getSheetDataByRow() {

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

    public List<List<String>> getSheetDataByColumn() {
        List<List<String>> dataByColumn = new ArrayList<>();
        Row headerRow = sheet.getRow(0); // Assuming the first row contains headers

        if (headerRow == null) {
            throw new RuntimeException("Header row is missing in the sheet.");
        }

        // Initialize columns based on the headers
        int columnCount = headerRow.getLastCellNum();
        for (int colIndex = 0; colIndex < columnCount; colIndex++) {
            Cell headerCell = headerRow.getCell(colIndex);
            String header = (headerCell == null) ? "" : headerCell.toString().trim();

            if (!header.isEmpty()) {
                dataByColumn.add(new ArrayList<>()); // Create a new list for each non-empty header
            } else {
                dataByColumn.add(null); // Mark this column as empty to ignore it later
            }
        }

        // Process rows up to the last meaningful row
        int lastRowNum = getLastMeaningfulRow();
        for (int rowIndex = 0; rowIndex <= lastRowNum; rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) continue;

            for (int colIndex = 0; colIndex < columnCount; colIndex++) {
                if (dataByColumn.get(colIndex) == null) {
                    continue; // Skip columns with empty headers
                }

                Cell cell = row.getCell(colIndex);
                String cellValue = (cell == null) ? "" : cell.toString().trim();
                dataByColumn.get(colIndex).add(cellValue);
            }
        }

        // Remove null columns from the final result
        dataByColumn.removeIf(Objects::isNull);

        return dataByColumn;
    }

    // Helper method to find the last meaningful row
    private int getLastMeaningfulRow() {
        int lastMeaningfulRow = sheet.getLastRowNum();

        // Iterate from the last row upwards to find the last non-empty row
        for (int rowIndex = lastMeaningfulRow; rowIndex >= 0; rowIndex--) {
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                for (Cell cell : row) {
                    if (cell != null && !cell.toString().trim().isEmpty()) {
                        return rowIndex; // Found a meaningful row
                    }
                }
            }
        }

        return 0; // Default to the header row if no meaningful rows are found
    }

    // Method to set data into a specific cell
    public void setCellData(int rowNum, int colNum, String value) throws SuppressedStackTraceException {
        Row row = sheet.getRow(rowNum);
        if (row == null) row = sheet.createRow(rowNum);
        Cell cell = row.getCell(colNum);
        if (cell == null) cell = row.createCell(colNum);
        cell.setCellValue(value);
        save();
    }
    // Method to save changes to the Excel file

    private void save() throws SuppressedStackTraceException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        } catch (IOException e) {
            MyLogger.error("Failed to load Excel file: " + filePath, e);
            throw new SuppressedStackTraceException("Failed to save Excel file: " + filePath);
        }
    }
    // Method to close the workbook

    public void close() throws SuppressedStackTraceException {
        try {
            workbook.close();
        } catch (IOException e) {
            MyLogger.error("Failed to load Excel file: " + workbook, e);
            throw new SuppressedStackTraceException("Failed to close Excel workbook: " + workbook);
        }
    }

    public String getStringCellData(String testCaseId, String attributeName) throws SuppressedStackTraceException {
        // Get the row for the testCaseId
        Row row = getRow(testCaseId);
        if (row == null) {
            MyLogger.error("Test case ID not found: " + testCaseId);
            throw new SuppressedStackTraceException("Test case ID not found: " + testCaseId);
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
            MyLogger.error(String.format(attributeName + " column was not found in excel file %s for test case ID %s %n" ,filePath, testCaseId));
            throw new SuppressedStackTraceException(String.format(attributeName + " column was not found in excel file %s for test case ID %s %n" ,filePath, testCaseId));
        }

        // Use getCellData to fetch the value
        int rowIndex = row.getRowNum();
        return getCellData(rowIndex, columnIndex);
    }

    public static Map<String, String> getAllDataFromRow(String testCase) {
        Map<String, String> dataMap = new HashMap<>();
        Row row = getRow(testCase);

        if (row != null) {
            Row headerRow = sheet.getRow(0); // Assuming the first row contains headers
            for (int i = 0; i < row.getLastCellNum(); i++) {
                String key = headerRow.getCell(i).getStringCellValue(); // Column header
                Cell cell = row.getCell(i);

                // Handle different cell types
                String value;
                if (cell == null) {
                    value = ""; // Handle null cells
                } else {
                    switch (cell.getCellType()) {
                        case STRING:
                            value = cell.getStringCellValue();
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                value = cell.getDateCellValue().toString(); // Format date cells if needed
                            } else {
                                value = String.valueOf(cell.getNumericCellValue()); // Convert numeric values to string
                            }
                            break;
                        case BOOLEAN:
                            value = String.valueOf(cell.getBooleanCellValue());
                            break;
                        case FORMULA:
                            value = cell.getCellFormula(); // Retrieve formula as string
                            break;
                        default:
                            value = ""; // Handle unexpected cell types
                            break;
                    }
                }

                dataMap.put(key, value);
            }
        } else {
            throw new RuntimeException("Row not found for test case: " + testCase);
        }

        return dataMap;
    }


}

