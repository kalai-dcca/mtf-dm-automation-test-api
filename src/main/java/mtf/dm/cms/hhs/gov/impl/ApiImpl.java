package mtf.dm.cms.hhs.gov.impl;

import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;

import java.io.File;

import static mtf.dm.cms.hhs.gov.utilities.BaseClass.getTestScenarioClass;
import static io.restassured.RestAssured.given;

public class ApiImpl {

    private String baseUrl = "https://reqres.in";  // Example base URL, should be parameterized or configurable.

    // Method to send API requests (GET, POST, PUT, DELETE)
    public Response sendApiRequest(String endpoint, String method, String requestBodyFile) {
        // Read JSON body from file
        String requestBody = readJsonFromFile(requestBodyFile);

        // Send request based on HTTP method
        Response response;
        switch (method.toUpperCase()) {
            case "GET":
                response = given().get(baseUrl + endpoint);
                break;
            case "POST":
                response = given().body(requestBody).post(baseUrl + endpoint);
                break;
            case "PUT":
                response = given().body(requestBody).put(baseUrl + endpoint);
                break;
            case "DELETE":
                response = given().delete(baseUrl + endpoint);
                break;
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }

        return response;
    }

    public Response sendApiRequest(String endpoint, String method) {

        // Send request based on HTTP method
        Response response;
        switch (method.toUpperCase()) {
            case "GET":
                response = given().get(baseUrl + endpoint);
                break;
            case "POST":
                response = given().body(getTestScenarioClass().getJsonObject()).post(baseUrl + endpoint);
                break;
            case "PUT":
                response = given().body(getTestScenarioClass().getJsonObject()).put(baseUrl + endpoint);
                break;
            case "PATCH":
                response = given().body(getTestScenarioClass().getJsonObject()).patch(baseUrl + endpoint);
                break;
            case "DELETE":
                response = given().delete(baseUrl + endpoint);
                break;
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }
        response.getBody().prettyPrint();
        return response;
    }

    // Utility method to read a JSON file as string
    private String readJsonFromFile(String fileName) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File("src/test/resources/json/" + fileName);
            return objectMapper.writeValueAsString(objectMapper.readTree(file));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading JSON file: " + fileName);
        }
    }
}
