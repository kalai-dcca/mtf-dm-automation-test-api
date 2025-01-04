package mtf.dm.cms.hhs.gov.utilities;

import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.Assertions;

public class AssertionUtils {

    /**
     * Validates that the response status code matches the expected value.
     *
     * @param response          The Response object.
     * @param expectedStatusCode The expected status code.
     */
    public static void verifyStatusCode(Response response, int expectedStatusCode) throws SuppressedStackTraceException {
        //boolean status = false;
        if(Objects.isNull(response)){
            MyLogger.error(String.format("Error: Actual[%s]::Expected[%s]::Status[%s]%n",
                    null,expectedStatusCode,false));
            throw new SuppressedStackTraceException(String.format("Error: Actual[%s]::Expected[%s]::Status[%s]%n",
                    null,expectedStatusCode,false));
        }
        AssertionHandler.logAssertionError(() ->{
            Assertions.assertThat(response.getStatusCode()).isEqualTo((expectedStatusCode));
        },"FIELD NAME: " + "Status Code" +  "\n" + "Status Code" + " value in File: " +  + expectedStatusCode + "\n" + "Status Code" + " value in the Response: " + response.getStatusCode());


    }

    /**
     * Validates that the response body contains a specific field.
     *
     * @param response The Response object.
     * @param field    The field to check.
     */
    public static void assertFieldExists(Response response, String field) throws SuppressedStackTraceException {
        //boolean status = false;
        if(Objects.isNull(response)){
            MyLogger.error(String.format("Error: Actual[%s]::Expected[%s]::Status[%s]%n",
                    null,field,false));
            throw new SuppressedStackTraceException(String.format("Error: Actual[%s]::Expected[%s]::Status[%s]%n",
                    null,field,false));
        } else if ( StringUtils.isEmpty(field)) {
            MyLogger.error(String.format("Error: Actual[%s]::Expected[%s]::Status[%s]%n",
                    "",field,false));
            throw new SuppressedStackTraceException(String.format("Error: Actual[%s]::Expected[%s]::Status[%s]%n",
                    "",field,false));
        }
        AssertionHandler.logAssertionError(() ->{
            Assertions.assertThat(response.jsonPath().getString(field)).isNotNull();
        },"Expecting actual: " + "\n   " + response.jsonPath().toString() + "\n" + "to contain: " + "\n   " + field);
        //return status;
    }


    /**
     * Validates that the response body contains a specific field with the expected value.
     *
     * @param response   The Response object.
     * @param field      The field to check.
     * @param expectedValue The expected value of the field.
     */
    public static void assertFieldValue(Response response, String field, Object expectedValue) throws SuppressedStackTraceException {
        //boolean status = false;
        if(Objects.isNull(response)){
            MyLogger.error(String.format("Error: Actual[%s]::Expected[%s]::Status[%s]%n",
                    null,expectedValue,false));
            throw new SuppressedStackTraceException(String.format("Error: Actual[%s]::Expected[%s]::Status[%s]%n",
                    null,expectedValue,false));
        } else if ( StringUtils.isEmpty(field)) {
            MyLogger.error(String.format("Error: Actual[%s]::Expected[%s]::Status[%s]%n",
                    "",expectedValue,false));
            throw new SuppressedStackTraceException(String.format("Error: Actual[%s]::Expected[%s]::Status[%s]%n",
                    "",expectedValue,false));
        }

        AssertionHandler.logAssertionError(() ->{
            Assertions.assertThat(response.jsonPath().getString(field)).isEqualTo(expectedValue);
        },"Expecting actual: " + "\n   " + field + "\n" + "to contain: " + "\n   " + expectedValue);
        //return status;
    }

    /**
     * Validates that the response time is within the acceptable limit.
     *
     * @param response        The Response object.
     * @param maxResponseTime The maximum acceptable response time in milliseconds.
     */
    public static void assertResponseTime(Response response, long maxResponseTime) throws SuppressedStackTraceException {
       // boolean status = false;
        if(Objects.isNull(response)){
            MyLogger.error(String.format("Error: Actual[%s]::Status[%s]%n",
                    null,false));
            throw new SuppressedStackTraceException(String.format("Error: Actual[%s]::Status[%s]%n",
                    null,false));
        }
        AssertionHandler.logAssertionError(() ->{
            Assertions.assertThat(response.getTime()).isLessThanOrEqualTo(maxResponseTime);
        },"Response time exceeded: " + maxResponseTime);
       // return status;
    }



    /**
     * Validates that a field in the response matches the provided regular expression.
     *
     * @param response The Response object.
     * @param field    The field to check.
     * @param regex    The regular expression to match.
     */
    public static void assertFieldMatchesRegex(Response response, String field, String regex) throws SuppressedStackTraceException {
        //boolean status = false;
        if(Objects.isNull(response)){
            MyLogger.error(String.format("Error: Actual[%s]::Expected[%s]::Status[%s]%n",
                    null,regex,false));
            throw new SuppressedStackTraceException(String.format("Error: Actual[%s]::Expected[%s]::Status[%s]%n",
                    null,regex,false));
        }
        AssertionHandler.logAssertionError(() ->{
            Assertions.assertThat(response.jsonPath().getString(field)).matches(regex);
        },"Expecting actual: " + "\n   " + field + "\n" + "to contain: " + "\n   " + regex);
        //return status;
    }

    /**
     * Validates that a map field in the response contains all expected key-value pairs.
     *
     * @param response      The Response object.
     * @param mapField      The map field to check.
     * @param expectedEntries The expected key-value pairs.
     */
    public static void assertMapContains(Response response, String mapField, Map<String, Object> expectedEntries) throws SuppressedStackTraceException {
        //boolean status = false;
        if(Objects.isNull(response)){
            MyLogger.error(String.format("Error: Actual[%s]::Status[%s]%n",
                    null,false));
            throw new SuppressedStackTraceException(String.format("Error: Actual[%s]::Status[%s]%n",
                    null,false));
        }
        SoftAssertions soft = new SoftAssertions();
        List<String> customMessages = new ArrayList<>();

        Map<String, Object> actualEntries = response.jsonPath().getMap(mapField);
        for (Map.Entry<String, Object> entry : expectedEntries.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            Object actualValue = response.jsonPath().get(key);
            soft.assertThat(actualValue).isEqualTo(value);

            // Create custom messages for each assertion
            AssertionHandler.handleSoftAssertFailures(soft, "FIELD NAME: " + key + "\n" + key + " value in File: " + actualValue + "\n" + key + " value in the Response: " + value + "\n");
        }

        //AssertionHandler.handleSoftAssertFailures(soft, customMessages);
        //return status;
    }

    /**
     * Validates that the response status code and a specific message in the response body match the expected values.
     *
     * @param response          The Response object.
     * @param expectedStatusCode The expected status code.
     * @param expectedMessage    The expected message value.
     */

    public static void verifyStatusCodeAndMessage(Response response, int expectedStatusCode, String expectedMessage) throws SuppressedStackTraceException {
        //boolean status = false;
        // Suggest separating into two granular assertion methods for fail status clarity!!!!!!!
        if(Objects.isNull(response)){
            MyLogger.error(String.format("Error: Actual[%s]::Expected[%s]::Status[%s]%n",
                    null,expectedStatusCode,false));
            throw new SuppressedStackTraceException(String.format("Error: Actual[%s]::Expected[%s]::Status[%s]%n",
                    null,expectedStatusCode,false));
        } else if (StringUtils.isEmpty(expectedMessage)) {
            MyLogger.error(String.format("Error: Actual[%s]::Expected[%s]::Status[%s]%n",
                    "", expectedMessage, false));
            throw new SuppressedStackTraceException(String.format("Error: Actual[%s]::Expected[%s]::Status[%s]%n",
                    "", expectedMessage, false));
        }

        SoftAssertions soft = new SoftAssertions();
        //List<String> customMessages = new ArrayList<>();

        soft.assertThat(response.getStatusCode()).isEqualTo((expectedStatusCode));
        // Create custom messages for each assertion
        AssertionHandler.handleSoftAssertFailures(soft, "FIELD NAME: " + "Status Code" + "\n" +  "Status Code" + " value in File: " +  expectedStatusCode + "\n" + "Status Code" + " value in the Response: " + response.getStatusCode() + "\n");

        soft.assertThat(response.getBody().asString()).contains((expectedMessage));
        AssertionHandler.handleSoftAssertFailures(soft, "Expecting actual: " + "\n   " + response.getBody().asString() + "\n" + "to contain: " + "\n   " + expectedMessage + "\n");



    }

    public static void verifyStatusCodeAndAttributesFromExcel(String attributeNames) throws SuppressedStackTraceException {
        // Split the comma-separated attribute names
        String[] attributes = attributeNames.split(",");

        // Retrieve test case ID and Excel utility
        String testCaseId = TestScenarioClass.getTestScenarioClass().getTestCaseID();
        ExcelUtils excelUtils = TestScenarioClass.getTestScenarioClass().getExcelUtils();

        Map<String, String> attributeValues = new HashMap<>();

        // Loop through attributes and retrieve their values
        for (String attribute : attributes) {
            String value = excelUtils.getStringCellData(testCaseId, attribute.trim());
            attributeValues.put(attribute.trim(), value);
        }

        MyLogger.info(String.format("Retrieved attribute values from Excel: {%s}", attributeValues));

        Response response = TestScenarioClass.getTestScenarioClass().getResponse();

        SoftAssertions soft = new SoftAssertions();
        List<String> customMessages = new ArrayList<>();

        //Loop through each attribute and then validate the result as a soft assert
        for (Map.Entry<String, String> entry : attributeValues.entrySet()) {
            String attribute = entry.getKey();
            String expectedValue = entry.getValue();
            if (attribute.equals("STATUS_CODE")) {
                soft.assertThat(response.getStatusCode()).isEqualTo((Integer.parseInt(expectedValue)));
                // Create custom messages for each assertion
                AssertionHandler.handleSoftAssertFailures(soft, "FIELD NAME: " + "Status Code" + "\n" +  "Status Code" + " value in File: " + response.getStatusCode() + "\n" + "Status Code" + " value in the Response: " + Integer.parseInt(expectedValue) + "\n");
            } else {
                soft.assertThat(response.jsonPath().getString(attribute)).contains(expectedValue);
                // Create custom messages for each assertion
                AssertionHandler.handleSoftAssertFailures(soft, "Expecting actual: " + "\n   " + response.jsonPath().getString(attribute) + "\n" + "to contain: " + "\n   " + expectedValue + "\n");
            }
        }

        //AssertionHandler.handleSoftAssertFailures(soft, customMessages);
    }

    /**
     * Validates that the array field in the response contains all expected key-value pairs, regardless of order.
     *
     * @param response       The Response object.
     * @param arrayField     The JSON path to the array (e.g., "data").
     * @param expectedEntries The list of expected objects (key-value pairs).
     */
    public static void assertArrayContainsEntriesFromFile(Response response, String arrayField, JsonNode expectedEntries) throws SuppressedStackTraceException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode actualData;

        if (Objects.isNull(response)) {
            throw new SuppressedStackTraceException("Error: Response is null");
        }

        // Fetch the array as a list of maps
        List<Map<String, Object>> actualArray = response.jsonPath().getList(arrayField);
        if (actualArray == null || actualArray.isEmpty()) {
            throw new SuppressedStackTraceException("Error: The array field named '" + arrayField + "' is empty or does not exist.");
        }

        actualData = objectMapper.readTree(response.getBody().asString()).get(arrayField);

        // Compare JSON Objects
        SoftAssertions soft = new SoftAssertions();
        compareJsonArrays(expectedEntries, actualData, soft);
        soft.assertAll();
        
    }


    /**
     * Converts a map into a normalized string for consistent comparison.
     *
     * @param map The map to normalize.
     * @return A string representation of the map with sorted keys and values.
     */
    private static String normalizeMap(Map<String, Object> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining(","));
    }

    private static void compareJsonArrays(JsonNode expectedArray, JsonNode actualArray, SoftAssertions soft) {

        // Validate the size of arrays
        soft.assertThat(actualArray.size()).isEqualTo(expectedArray.size());
        if (expectedArray.size() != actualArray.size()) {
            ExtentCucumberAdapter.addTestStepLog("<pre>" + "Array size mismatch: Expected " + expectedArray.size() + ", but got " + actualArray.size() + "</pre>");
            ExtentCucumberAdapter.getCurrentStep().info(MarkupHelper.createLabel("RESPONSE ARRAY SIZE NOT MATCHED - TEST FAIL", ExtentColor.RED));
        }

        // Iterate through the arrays
        for (int i = 0; i < expectedArray.size(); i++) {
            ExtentCucumberAdapter.getCurrentStep().info(MarkupHelper.createLabel("Validating for Resource "+ (i+1), ExtentColor.BLACK));
            JsonNode expectedObject = expectedArray.get(i);
            JsonNode actualObject = actualArray.get(i);

            compareJsonObjects(expectedObject, actualObject, soft);

        }
    }

    private static void compareJsonObjects(JsonNode expected, JsonNode actual, SoftAssertions soft) {

        // Iterate through fields in the expected object
        for (String key : IterableAsList(expected.fieldNames())) {
            soft.assertThat(actual.has(key)).isTrue();
            if (actual.has(key)) {
                JsonNode expectedValue = expected.get(key);
                JsonNode actualValue = actual.get(key);
                ExtentCucumberAdapter.addTestStepLog("<pre>" + "FIELD NAME: "+ key + "\n" +
                        key + " value in File : " + expectedValue + "\n" +
                        key + " value in Response : " + actualValue + "</pre>");
                soft.assertThat(actualValue).isEqualTo(expectedValue);
                if (!expectedValue.equals(actualValue)) {
                    ExtentCucumberAdapter.getCurrentStep().info(MarkupHelper.createLabel("VALUE NOT MATCHED - TEST FAIL", ExtentColor.RED));
                } else {
                    ExtentCucumberAdapter.getCurrentStep().info(MarkupHelper.createLabel("VALUE MATCHED - TEST PASS", ExtentColor.GREEN));
                }
            } else {
                ExtentCucumberAdapter.addTestStepLog("<pre>" + "FIELD NAME :" + key + " is missing in actual response" + "</pre>");
                ExtentCucumberAdapter.getCurrentStep().info(MarkupHelper.createLabel("KEY MISSING - TEST FAIL", ExtentColor.RED));

            }
        }

        // Check for extra keys in the actual object
        for (String key : IterableAsList(actual.fieldNames())) {
            soft.assertThat(expected.has(key)).isTrue();
            if (!expected.has(key)) {
                ExtentCucumberAdapter.addTestStepLog("<pre>" + "FIELD NAME :" + key + " is coming extra in actual response" + "</pre>");
                ExtentCucumberAdapter.getCurrentStep().info(MarkupHelper.createLabel("EXTRA KEY FOUND - TEST FAIL", ExtentColor.RED));
            }
        }
    }

    // Helper method to iterate through field names
    private static Iterable<String> IterableAsList(Iterator<String> iterator) {
        List<String> list = new ArrayList<>();
        iterator.forEachRemaining(list::add);
        return list;
    }

}

