package mtf.dm.cms.hhs.gov.impl;

import io.restassured.response.Response;
import mtf.dm.cms.hhs.gov.utilities.ExcelUtils;
import mtf.dm.cms.hhs.gov.utilities.SheetType;

import java.util.Objects;

import static mtf.dm.cms.hhs.gov.utilities.BaseClass.getTestScenarioClass;

public class DemoApi {

    private final ApiImpl apiRequestClient = new ApiImpl();

    // Method to handle Demo API request logic
    public Response launchDemoApi(String endpoint, String method, String jsonFile) {
        // Send API request using ApiRequestClient
        Response response = apiRequestClient.sendApiRequest(endpoint, method, jsonFile);
        // Return wrapped response as ApiResponse
        return response;
    }

    public Response launchDemoApi(String endpoint, String method){
        Response response = apiRequestClient.sendApiRequest(endpoint, method);
        return response;
    }

    public Response launchDemoApiAndGetResponse(String endpoint, String method){
        if(Objects.nonNull(getTestScenarioClass().getSheet())){
            if(!getTestScenarioClass().getSheet().equalsIgnoreCase(SheetType.CREATE.getEnumData())){
                endpoint = endpoint + "/" + getTestScenarioClass().getUserID();
            }
        }
        return apiRequestClient.sendApiRequest(endpoint, method);
    }

    public Response launchQueryDemoApiAndGetResponse(String endpoint, String queryParam, String method){
        endpoint = endpoint + "?" + queryParam + "=" + ExcelUtils.getUserId(getTestScenarioClass().getTestCaseID());
        return apiRequestClient.sendApiRequest(endpoint, method);
    }

}
