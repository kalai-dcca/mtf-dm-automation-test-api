package mtf.dm.cms.hhs.gov.cucumber;

import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import mtf.dm.cms.hhs.gov.base.BaseClass;
import mtf.dm.cms.hhs.gov.impl.DbImpl;
import mtf.dm.cms.hhs.gov.impl.RestImpl;
import mtf.dm.cms.hhs.gov.utilities.assertionUtilities.AssertionUtilities;
import mtf.dm.cms.hhs.gov.utilities.fileHandlerUtilities.ExcelColumnSpec;
import mtf.dm.cms.hhs.gov.utilities.fileHandlerUtilities.ExcelSheetSpec;
import mtf.dm.cms.hhs.gov.utilities.*;
import mtf.dm.cms.hhs.gov.utilities.FileUtility;
import mtf.dm.cms.hhs.gov.utilities.loggerUtilities.MyLogger;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static mtf.dm.cms.hhs.gov.base.BaseClass.getTestScenarioClass;
import static mtf.dm.cms.hhs.gov.impl.DbImpl.executeSqlQuery;
import static mtf.dm.cms.hhs.gov.utilities.FileUtility.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CommonStep {
    private Response apiResponse;
    private final RestImpl apiRequestClient = new RestImpl();

    @When("Launch Demo API service and Review API service with test data from the testcase file {string}")
    public void launchApiService(String testcaseFile) {
        // Read TestCaseId and data from Excel
        String testCaseId = ExcelUtils.readTestCaseIdFromExcel(testcaseFile);
        String jsonFile = ExcelUtils.readJsonFileForTestCase(testCaseId);

        // Fetch API endpoint, HTTP method, and expected values from the test case
        String endpoint = ExcelUtils.getEndpointFromTestCase(testCaseId);
        String method = ExcelUtils.getHttpMethodFromTestCase(testCaseId);

        // Launch API request
        apiResponse = apiRequestClient.sendApiRequest(endpoint, method, jsonFile);
    }

    @Then("Read test data from the sheet {string} for the {string}")
    public void readTestDataFromExcel(String sheetName, String testCaseId) {
        // Validate the test data read from the Excel file (for demonstration purposes)
        String testData = ExcelUtils.getTestDataFromSheet(sheetName, testCaseId);
        assertNotNull(testData, "Test data not found for TestCaseId: " + testCaseId);
    }

    @When("DemoAPI: Launch {string}, Method: {string}, request params: File {string}")
    public void launchApiWithParams(String endpoint, String method, String jsonFile) {
        // Launch the API using the DemoApiMethods
        apiResponse = apiRequestClient.sendApiRequest(endpoint, method, jsonFile);
    }

    //@Then("Verify status code {int} and message {string}")
    //public void verifyStatusCodeAndMessage(int expectedStatusCode, String expectedMessage) {
    // Validate the status code and response message
    //  assertEquals(expectedStatusCode, apiResponse.getStatusCode(), "Status code mismatch");
    // assertTrue(apiResponse.containsMessage(expectedMessage), "Response message mismatch");
    //}

    @Then("Verify status code {int} and message {string}")
    public void verifyStatusCodeAndMessage(int expectedStatusCode, String expectedMessage) throws SuppressedStackTraceException {

        // Comment about entering state
        MyLogger.info(String.format("Verifying status code {%s} and message {%s}", expectedStatusCode, expectedMessage));

        // Validate the status code and response message using the AssertionUtils method
        AssertionUtilities.verifyStatusCodeAndMessage(getTestScenarioClass().getResponse(), expectedStatusCode, expectedMessage);

        // Comment about closing state
        MyLogger.info(String.format("Validation completed successfully for status code {%s} and message {%s}",
                expectedStatusCode, expectedMessage));

    }


    @When("TestCaseDataSetup, File-{string}, Sheet-{string}, TestCase-{string}")
    public void testcasedatasetupFileSheetTestCase(String fileName, String sheet, String testCase) throws SuppressedStackTraceException {
        try{
            ExcelUtils excelUtils = new ExcelUtils(BaseClass.TEST_DATA_PATH+fileName,sheet);
            getTestScenarioClass().setExcelUtils(excelUtils);
            getTestScenarioClass().setJsonObject(ExcelUtils.getDataBasedOnTestCaseAndCallType(testCase, sheet));
            getTestScenarioClass().setTestCaseID(testCase);
            getTestScenarioClass().setSheet(sheet);
            if(!sheet.equalsIgnoreCase(SheetType.CREATE.getEnumData())){
                getTestScenarioClass().setUserID(ExcelUtils.getUserId(testCase));
            }
            //extentReports.createTest("Data Table").info(MarkupHelper.createJsonCodeBlock(getTestScenarioClass().getJsonObject()));
            //ExtentCucumberAdapter.getCurrentStep().info(MarkupHelper.createLabel("REQUEST BODY", ExtentColor.BLUE));
            //ExtentCucumberAdapter.getCurrentStep().info(MarkupHelper.createJsonCodeBlock(getTestScenarioClass().getJsonObject()));

        }
        catch (Exception e) {
            MyLogger.error("Failed to setup test case: " + fileName, e);
            throw new SuppressedStackTraceException("Failed to setup test case for " + fileName + "\n" + e.getMessage());
        }
    }

    @When("Launch {string}, Method: {string}")
    public void demoapiLaunchMethod(String url, String APICall) {

        if(Objects.nonNull(getTestScenarioClass().getSheet())){
            if(!getTestScenarioClass().getSheet().equalsIgnoreCase(SheetType.CREATE.getEnumData())){
                url = url + "/" + getTestScenarioClass().getUserID();
            }
        }
        getTestScenarioClass().setResponse(apiRequestClient.sendApiRequest(url, APICall));
    }

    @Then("Verify status code {int}")
    public void verifyStatusCode(int expectedStatusCode) throws SuppressedStackTraceException {

        // Comment about entering state
        MyLogger.info(String.format("Verifying status code {%s}", expectedStatusCode));

        // Validate the status code
        AssertionUtilities.verifyStatusCode(getTestScenarioClass().getResponse(), expectedStatusCode);

        // Comment about closing state
        MyLogger.info(String.format("Validation completed successfully for status code {%s}", expectedStatusCode));
    }

    @When("Launch {string}, QParam:{string} Method: {string}")
    public void demoapiLaunchQParamMethod(String url, String queryParam, String APICall) throws SuppressedStackTraceException {
        url = url + "?" + queryParam + "=" + ExcelUtils.getUserId(getTestScenarioClass().getTestCaseID());
        getTestScenarioClass().setResponse(apiRequestClient.sendApiRequest(url, APICall));
    }

    @When("TestCaseDataSetup")
    public void testcasedatasetup(Map<String,String> keyValueMap) {
        JSONObject jsonObject = new JSONObject();
        for(Map.Entry<String,String> entry : keyValueMap.entrySet()){
            jsonObject.put(entry.getKey(),entry.getValue());
        }
        getTestScenarioClass().setJsonObject(jsonObject);
    }

    @When("TestCaseDataSetup, JSONFile-{string}")
    public void testcasedatasetupJSONFile(String fileName) throws SuppressedStackTraceException {
        try{
            String fileLocation = "src/test."+System.getProperty("projectName")+"/resources/request/" + fileName;
            String body = new String(Files.readAllBytes(Paths.get(fileLocation)));
            JSONObject jsonObject = new JSONObject(body);
            getTestScenarioClass().setJsonObject(jsonObject);
            ExtentCucumberAdapter.getCurrentStep().info(MarkupHelper.createLabel("INPUT JSON DATA", ExtentColor.BLUE));
            ExtentCucumberAdapter.addTestStepLog("<pre>"+ body + "</pre>");
        } catch (Exception e) {
            MyLogger.error("Failed to load JSON file: " + fileName, e);
            throw new SuppressedStackTraceException("Failed to load JSON file: " + fileName);
        }
    }

    @Then("Verify response values:")
    public void verifyResponseValuesWithDatatable(DataTable dataTable) throws SuppressedStackTraceException {
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);

        // Loop through the datatable rows for validation
        for (Map<String, String> row : data) {
            int expectedStatusCode = Integer.parseInt(row.get("statusCode"));
            String expectedMessage = row.get("message");

            // Comment about entering state
            MyLogger.info(String.format("Verifying status code {%s} and message {%s}", expectedStatusCode, expectedMessage));

            // Validate the status code and response message using the AssertionUtils method
            AssertionUtilities.verifyStatusCodeAndMessage(getTestScenarioClass().getResponse(), expectedStatusCode, expectedMessage);

            // Comment about closing state
            MyLogger.info(String.format("Validation completed successfully for status code {%s} and message {%s}", expectedStatusCode, expectedMessage));
        }
    }

    @Then("Verify response values from Excel for attributes {string}")
    public void verifyResponseValuesFromExcelForAttributes(String attributeNames) throws SuppressedStackTraceException {
        MyLogger.info(String.format("Starting validation for response attributes: {%s}", attributeNames));

        // Delegate validation to a helper method
        AssertionUtilities.verifyStatusCodeAndAttributesFromExcel(attributeNames);

        MyLogger.info(String.format("Validation completed successfully for attributes: {%s}", attributeNames));
    }

    @When("Fetch all pages from {string} with query param {string} and method {string}")
    public void fetchAllPages(String endpoint, String queryParam, String method) {
        //MyLogger.error("Fetching all pages from endpoint '{}' using query param '{}'", endpoint, queryParam);

        List<Map<String, Object>> allPagesData = new ArrayList<>();
        int currentPage = getTestScenarioClass().getUserID();

        // Dynamically append the query parameter to the endpoint
        String finalEndpoint = endpoint + "?" + queryParam + "=" + String.valueOf(currentPage);

        // Send the request for the current page
        getTestScenarioClass().setResponse(
                apiRequestClient.sendApiRequest(finalEndpoint, method)
        );
        Response response = getTestScenarioClass().getResponse();

        // Extract the 'data' array from the current page and add it to allPagesData
        List<Map<String, Object>> currentPageData = response.jsonPath().getList("data");
        allPagesData.addAll(currentPageData);



        // Store the combined data in the scenario class for validation
        getTestScenarioClass().setCombinedData(allPagesData);

        //MyLogger.error("Fetched all pages successfully. Total records: {}", allPagesData.size());
    }

    @Then("Verify status code {int} and the response array {string} matches expected values from {string}")
    public void validateResponseArrayFromFile(int expectedStatusCode, String arrayField, String expectedFilePath) throws Exception {

        // Load expected values from the JSON file
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode expectedData;
        String body;
        try {
            body = new String(Files.readAllBytes(Paths.get("src/test."+System.getProperty("projectName")+"/resources/response/" + expectedFilePath)));
            expectedData = objectMapper.readTree(body);
        } catch (Exception e) {
            MyLogger.error("Unable to load expected data from file: " + expectedFilePath, e);
            throw new SuppressedStackTraceException("Unable to load expected data from file: " + expectedFilePath);
        }

        // Ensure the expected data is valid
        assertNotNull(expectedData, "Expected data file is empty or invalid!");

        // Validate the status code
        Response response = getTestScenarioClass().getResponse();
        AssertionUtilities.verifyStatusCode(response, expectedStatusCode);

        // Validate the response array matches the expected values
        AssertionUtilities.assertResponseFromFile(response, arrayField, expectedData);

        // Log successful completion of the validation
        //LoggerUtil.logger.info("Validation completed successfully for status code {} and response array '{}'",
                //expectedStatusCode, arrayField);
    }

    @Then("verify {string} data is in the {string} database")
    public void verify_data_is_in_the_database(String dataString, String directoryString) throws SuppressedStackTraceException {
        String configFilePath = String.format("src/test.%s/resources/database/database.config.json", directoryString);
        try {
            // Create the database connection
            DbImpl.createConnectionFromConfig(configFilePath);

            // Run query against database
            ResultSet resultSet = DbImpl.runQuery("SELECT * FROM price_eff_dt");

            // Display results
            DbImpl.displayAllData();
            ExtentCucumberAdapter.getCurrentStep().info(MarkupHelper.createLabel("DB DATA", ExtentColor.WHITE));
            ExtentCucumberAdapter.getCurrentStep().info(MarkupHelper.createJsonCodeBlock(DbImpl.getAllDataAsMap()));

            // Close resources
            DbImpl.destroy();
        } catch (Exception e) {
            MyLogger.error("Error verifying data in the database: " + e.getMessage(), e);
            throw new SuppressedStackTraceException("Error verifying data in the database: " + e.getMessage());
        }

    }

    @When("TestCaseDataSetup-{string}, File-{string}, Sheet-{string}, TestCase-{string}")
    public void testCaseDataSetupFileSheetTestCase(String directoryString, String fileName, String sheet, String testCase) throws SuppressedStackTraceException {
        String testCaseFileName = String.format("src/test.%s/resources/testData/%s", directoryString, fileName);
        try {
            // Initialize ExcelUtils
            ExcelUtils excelUtils = new ExcelUtils(testCaseFileName, sheet);
            getTestScenarioClass().setExcelUtils(excelUtils);

            // Retrieve all data from the row as a HashMap
            Map<String, String> testCaseData = ExcelUtils.getAllDataFromRow(testCase);
            getTestScenarioClass().setTestCaseData(testCaseData); // Save the data for subsequent steps
            ExtentCucumberAdapter.getCurrentStep().info(MarkupHelper.createLabel("TEST DATA", ExtentColor.BLUE));
            ExtentCucumberAdapter.getCurrentStep().info(MarkupHelper.createJsonCodeBlock(testCaseData));

            // Set other test context values
            getTestScenarioClass().setTestCaseID(testCase);
            getTestScenarioClass().setSheet(sheet);

        } catch (Exception e) {
            MyLogger.error("Error during test case data setup: " + e.getMessage(), e);
            throw new SuppressedStackTraceException("Error during test case data setup: " + e.getMessage());
        }
    }

    @Then("verify file data is in the {string} database")
    public void verifyFileDataIsInDatabase(String directoryString) {
        try {
            // Step 1: Retrieve Test Case Data Setup
            String testCaseId = getTestScenarioClass().getTestCaseID(); // Implement method to retrieve TestCase ID

            Map<String, String> testCaseData = getTestScenarioClass().getTestCaseData();

            String sheet = getTestScenarioClass().getSheet();

            ExcelSheetSpec sheetSpec = (ExcelSheetSpec) BaseClass.getScenarioVariable("sheetSpecForFileData");
            Map<String, ExcelColumnSpec> columnSpecs = sheetSpec.getColumnSpecs();

            @SuppressWarnings("unchecked")
            List<List<String>> dataByColumn = (List<List<String>>) BaseClass.getScenarioVariable("dataByColumnForFileToIngest");
            System.out.println("Expected Data: " + dataByColumn.toString());

            //Get database json file
            String configFilePath = String.format("src/test.%s/resources/database/database.config.json", directoryString);


            if (!testCaseData.containsKey("sql_query")) {
                throw new IllegalArgumentException("SQL not found in Test Case Data.");
            }

            String sqlQuery = testCaseData.get("sql_query");
            String expectedValue = testCaseData.get("expected_value");

            // Step 2: Execute SQL Query
            ResultSet resultSet = executeSqlQuery(configFilePath, sqlQuery);
            // Display results
            DbImpl.displayAllData();

            // Step 3: Transform ResultSet to List<List<String>>
            List<List<String>> sqlDataByColumn  = transformResultSetToList(resultSet, columnSpecs, expectedValue);
            System.out.println("Actual Data: " + sqlDataByColumn);

            // Step 4: Compare List<List<String>>
            Boolean isMatch = compareLists(dataByColumn, sqlDataByColumn);

        } catch (Exception e) {
            throw new RuntimeException("Error verifying file data in database: " + e.getMessage(), e);
        }
    }

    @Given("the {string} file {string} follows the specification in {string}")
    public void the_file_follows_the_specification_in(String directoryName, String fileToValidate, String specsFileName) throws SuppressedStackTraceException {
        String specsFilePath = String.format("src/test.%s/resources/specifications/%s.specs.json", directoryName, specsFileName);
        String fileExtension = FileUtility.getFileExtension(fileToValidate);

        Map<String, Map<String, Object>> specsJson = JsonUtils.readJsonFile(specsFilePath);

        switch (fileExtension) {
            case "xlsx":
                // Iterate over the top-level keys (sheet names)
                for (String sheetName : specsJson.keySet()) {
                    the_file_follows_the_specification_in_sheet(directoryName, fileToValidate, specsFileName, sheetName);
                }
                break;
            default:
                throw new SuppressedStackTraceException("Unsupported file format: " + fileToValidate);
        }

    }

    @Given("the {string} file {string} follows the specification in {string} in sheet {string}")
    public void the_file_follows_the_specification_in_sheet(String directoryName, String fileToValidate, String specsFileName, String sheetName) throws SuppressedStackTraceException {
        String specsFilePath = String.format("src/test.%s/resources/specifications/%s.specs.json", directoryName, specsFileName);
        String fileToValidatePath = String.format("src/test.%s/resources/filesToIngest/%s", directoryName, fileToValidate);

        // Parse the sheet specification using JsonUtils
        ExcelSheetSpec sheetSpec = JsonUtils.getSheetSpec(specsFilePath, sheetName);
        BaseClass.setScenarioVariable("sheetSpecForFileData", sheetSpec);
        // Create a new ExcelUtils instance for the file and sheet
        ExcelUtils excelUtils = new ExcelUtils(fileToValidatePath, sheetName);


        // Get all data from the current sheet by column
        List<List<String>> dataByColumn = excelUtils.getSheetDataByColumn();
        BaseClass.setScenarioVariable("dataByColumnForFileToIngest", dataByColumn);

        // Validate each column
        sheetSpec.validateSheetDataByColumn(dataByColumn);

        System.out.println("Validation successful for sheet: " + sheetName);
    }

    @Then("Validate the attribute {string} equals to {string}")
    public void validateTheAttributeEqualsTo(String attribute, String expectValue) throws SuppressedStackTraceException {
        AssertionUtilities.assertFieldValue(getTestScenarioClass().getResponse(), attribute, expectValue);
    }

    @Then("Validate the attribute {string} contains {string}")
    public void validateTheAttributeContains(String attribute, String expectValue) throws SuppressedStackTraceException {

        Response response = getTestScenarioClass().getResponse();

        AssertionUtilities.assertFieldContainsValue(response, attribute, expectValue);
    }

    @Then("Validate the array {string} has size of {string}")
    public void validateTheArrayHasSizeOf(String attribute, String expectValue) throws SuppressedStackTraceException {
        Response response = getTestScenarioClass().getResponse();

        AssertionUtilities.assertArrayContainsElementsCount(response, attribute, Integer.parseInt(expectValue));

    }
}
