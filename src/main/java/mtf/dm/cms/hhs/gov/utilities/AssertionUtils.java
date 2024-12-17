package mtf.dm.cms.hhs.gov.utilities;

import io.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.Assertions;

public class AssertionUtils {

    /**
     * Validates that the response status code matches the expected value.
     *
     * @param response          The Response object.
     * @param expectedStatusCode The expected status code.
     */
    public static void verifyStatusCode(Response response, int expectedStatusCode){
        //boolean status = false;
        if(Objects.isNull(response)){
            LoggerUtil.logger.error("Error: Response is null");
            throw new RuntimeException(String.format("Actual[%s]::Expected[%s]::Status[%s]%n",
                    null,expectedStatusCode,false));
        }
        AssertionHandler.logAssertionError(() ->{
            Assertions.assertThat(response.getStatusCode()).isEqualTo((expectedStatusCode));
        },"Error: Received status code " + response.getStatusCode() + " instead of " + expectedStatusCode);
        //return status;

    }

    /**
     * Validates that the response body contains a specific field.
     *
     * @param response The Response object.
     * @param field    The field to check.
     */
    public static void assertFieldExists(Response response, String field){
        //boolean status = false;
        if(Objects.isNull(response)){
            LoggerUtil.logger.error("Error: Response is null");
            throw new RuntimeException(String.format("Actual[%s]::Expected[%s]::Status[%s]%n",
                    null,field,false));
        } else if ( StringUtils.isEmpty(field)) {
            LoggerUtil.logger.error("Error: Field is empty");
            throw new RuntimeException(String.format("Actual[%s]::Expected[%s]::Status[%s]%n",
                    "",field,false));
        }
        AssertionHandler.logAssertionError(() ->{
            Assertions.assertThat(response.jsonPath().getString(field)).isNotNull();
        },"Field '" + field + "' is missing in the response!");
        //return status;
    }


    /**
     * Validates that the response body contains a specific field with the expected value.
     *
     * @param response   The Response object.
     * @param field      The field to check.
     * @param expectedValue The expected value of the field.
     */
    public static void assertFieldValue(Response response, String field, Object expectedValue){
        //boolean status = false;
        if(Objects.isNull(response)){
            LoggerUtil.logger.error("Error: Response is null");
            throw new RuntimeException(String.format("Actual[%s]::Expected[%s]::Status[%s]%n",
                    null,expectedValue,false));
        } else if ( StringUtils.isEmpty(field)) {
            LoggerUtil.logger.error("Error: Field is empty");
            throw new RuntimeException(String.format("Actual[%s]::Expected[%s]::Status[%s]%n",
                    "",expectedValue,false));
        }

        AssertionHandler.logAssertionError(() ->{
            Assertions.assertThat(response.jsonPath().getString(field)).isEqualTo(expectedValue);
        },"Field '" + field + "' does not have the expected value " + expectedValue + "!");
        //return status;
    }

    /**
     * Validates that the response time is within the acceptable limit.
     *
     * @param response        The Response object.
     * @param maxResponseTime The maximum acceptable response time in milliseconds.
     */
    public static void assertResponseTime(Response response, long maxResponseTime){
       // boolean status = false;
        if(Objects.isNull(response)){
            LoggerUtil.logger.error("Error: Response is null");
            throw new RuntimeException(String.format("Actual[%s]::Status[%s]%n",
                    null,false));
        }
        AssertionHandler.logAssertionError(() ->{
            Assertions.assertThat(response.getTime()).isLessThanOrEqualTo(maxResponseTime);
        },"Response time exceeded " + maxResponseTime);
       // return status;
    }



    /**
     * Validates that a field in the response matches the provided regular expression.
     *
     * @param response The Response object.
     * @param field    The field to check.
     * @param regex    The regular expression to match.
     */
    public static void assertFieldMatchesRegex(Response response, String field, String regex){
        //boolean status = false;
        if(Objects.isNull(response)){
            LoggerUtil.logger.error("Error: Response is null");
            throw new RuntimeException(String.format("Actual[%s]::Expected[%s]::Status[%s]%n",
                    null,regex,false));
        }
        AssertionHandler.logAssertionError(() ->{
            Assertions.assertThat(response.jsonPath().getString(field)).matches(regex);
        },"Field '" + field + "' does not match the regex " + regex + "!");
        //return status;
    }

    /**
     * Validates that a map field in the response contains all expected key-value pairs.
     *
     * @param response      The Response object.
     * @param mapField      The map field to check.
     * @param expectedEntries The expected key-value pairs.
     */
    public static void assertMapContains(Response response, String mapField, Map<String, Object> expectedEntries){
        //boolean status = false;
        if(Objects.isNull(response)){
            LoggerUtil.logger.error("Error: Response is null");
            throw new RuntimeException(String.format("Actual[%s]::Status[%s]%n",
                    null,false));
        }

        Map<String, Object> actualEntries = response.jsonPath().getMap(mapField);
        expectedEntries.forEach((key, value) -> {
            AssertionHandler.logAssertionError(() ->{
                Object actualValue = response.jsonPath().get(key);
                Assertions.assertThat(actualValue).isEqualTo(value);
            },"Map '" + mapField + "' does not contain expected entry " + value);
        });

        //return status;
    }

    /**
     * Validates that the response status code and a specific message in the response body match the expected values.
     *
     * @param response          The Response object.
     * @param expectedStatusCode The expected status code.
     * @param expectedMessage    The expected message value.
     */

    public static void verifyStatusCodeAndMessage(Response response, int expectedStatusCode, String expectedMessage) {
        //boolean status = false;
        // Suggest separating into two granular assertion methods for fail status clarity!!!!!!!
        if(Objects.isNull(response)){
            LoggerUtil.logger.error("Error: Response is null");
            throw new RuntimeException(String.format("Actual[%s]::Expected[%s]::Status[%s]%n",
                    null,expectedStatusCode,false));
        } else if (StringUtils.isEmpty(expectedMessage)) {
            LoggerUtil.logger.error("Error: Message is empty");
            throw new RuntimeException(String.format("Actual[%s]::Expected[%s]::Status[%s]%n",
                    "", expectedMessage, false));
        }

        SoftAssertions soft = new SoftAssertions();
        AssertionHandler.logAssertionError(() ->{
            soft.assertThat(response.getStatusCode()).isEqualTo((expectedStatusCode));
            soft.assertThat(response.getBody().asString()).contains((expectedMessage));
            soft.assertAll();
        }, "Error: Status code or message validation failed!");

        //return status;
    }

}

