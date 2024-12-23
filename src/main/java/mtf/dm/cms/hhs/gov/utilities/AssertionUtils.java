package mtf.dm.cms.hhs.gov.utilities;

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
        },"-- failure -- \n" + "expected: " +  + expectedStatusCode + "\n" + "but was: " + response.getStatusCode());
        //return status;

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
        },"-- failure -- \n" + "Expecting actual: " + response.jsonPath().toString() + "\n" + "to contain: " + field);
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
        },"-- failure -- \n" + "Expecting actual: " + field + "\n" + "to contain: " + expectedValue);
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
        },"-- failure -- \n" + "Response time exceeded: " + maxResponseTime);
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
        },"-- failure -- \n" + "Expecting actual: " + field + "\n" + "to contain: " + regex);
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

        Map<String, Object> actualEntries = response.jsonPath().getMap(mapField);
        expectedEntries.forEach((key, value) -> {
            Object actualValue = response.jsonPath().get(key);
            soft.assertThat(actualValue).isEqualTo(value);
        });

        AssertionHandler.handleSoftAssertFailures(soft);
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

        soft.assertThat(response.getStatusCode()).isEqualTo((expectedStatusCode));
        soft.assertThat(response.getBody().asString()).contains((expectedMessage));

        AssertionHandler.handleSoftAssertFailures(soft);
        //return status;
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

        //Loop through each attribute and then validate the result as a soft assert
        attributeValues.forEach((attribute, expectedValue) -> {
            if (attribute.equals("STATUS_CODE")) {
                soft.assertThat(response.getStatusCode()).isEqualTo((Integer.parseInt(expectedValue)));
            } else{
                soft.assertThat(response.jsonPath().getString(attribute)).contains(expectedValue);
            }
        });

        AssertionHandler.handleSoftAssertFailures(soft);
    }

    /**
     * Validates that the array field in the response contains all expected key-value pairs, regardless of order.
     *
     * @param response       The Response object.
     * @param arrayField     The JSON path to the array (e.g., "data").
     * @param expectedEntries The list of expected objects (key-value pairs).
     */
    public static void assertArrayContainsEntriesFromFile(Response response, String arrayField, List<Map<String, Object>> expectedEntries) throws SuppressedStackTraceException {
        if (Objects.isNull(response)) {
            throw new SuppressedStackTraceException("Error: Response is null");
        }

        // Fetch the array as a list of maps
        List<Map<String, Object>> actualArray = response.jsonPath().getList(arrayField);
        if (actualArray == null || actualArray.isEmpty()) {
            throw new SuppressedStackTraceException("Error: The array field named '" + arrayField + "' is empty or does not exist.");
        }

        // Normalize actual and expected arrays: Convert maps to sorted strings for comparison
        Set<String> actualSet = actualArray.stream()
                .map(AssertionUtils::normalizeMap)
                .collect(Collectors.toSet());

        Set<String> expectedSet = expectedEntries.stream()
                .map(AssertionUtils::normalizeMap)
                .collect(Collectors.toSet());

        // Compare sets
        AssertionHandler.logAssertionError(() ->{
            Assertions.assertThat(actualSet).containsExactlyInAnyOrderElementsOf(expectedSet);
        },"-- failure -- \n" + "Expecting actual: " + actualSet + "\n" + "to contain: " + expectedSet);

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
}

