@REG-API-CREATE-POST @REG-API
Feature: DEMO Create API Testing POST

  Scenario Outline: Validate request and response Expected values are passed excel file
    When TestCaseDataSetup, File-"demoData.xlsx", Sheet-"Create", TestCase-"<TestCaseId>"
    When Launch "/api/users", Method: "POST"
    Then Verify response values from Excel for attributes "STATUS_CODE,createdAt"
    Examples:
      | TestCaseId |
      | C-TC001    |


  Scenario: Validate request and response Expected values from example table
    When TestCaseDataSetup
      | userName | testUserName |
      | userRole | Manager      |
    When Launch "/api/users", Method: "POST"
    Then Verify response values:
      | statusCode | message |
      | 201        | 2024    |


  Scenario: Validate both request and response from json file
    When TestCaseDataSetup, JSONFile-"create.json"
    When Fetch all pages from "/api/users" with query param "page" and method "GET"
    Then Verify status code 200 and the response array "data" matches expected values from "ExpectedResponse-Page-1.json"


  Scenario Outline: request from Excel and validate response Expected values from json file
    When TestCaseDataSetup, File-"demoData.xlsx", Sheet-"List-Users", TestCase-"<TestCaseId>"
    When Fetch all pages from "/api/users" with query param "page" and method "GET"
    Then Verify status code 200 and the response array "data" matches expected values from "<ExpectedJson>"
    Examples:
      | TestCaseId    | ExpectedJson |
      | LU-TC001      |    ExpectedResponse-Page-1.json         |


  Scenario Outline: Request from excel and  response validation using data table multi scenarios
    When TestCaseDataSetup, File-"demoData.xlsx", Sheet-"Update-PATCH", TestCase-"<TestCaseId>"
    When Launch "/api/users", Method: "PATCH"
    Then Verify response values:
      | statusCode | message |
      | 200        | 2024    |
    Examples:
      | TestCaseId    |
      | U-PATCH-TC001 |
      | U-PATCH-TC002 |
      | U-PATCH-TC003 |


  Scenario Outline: Request from excel and validate response status only
    When TestCaseDataSetup, File-"demoData.xlsx", Sheet-"Delete", TestCase-"<TestCaseId>"
    When Launch "/api/users", Method: "DELETE"
    Then Verify status code 204
    Examples:
      | TestCaseId    |
      | DEL-TC001 |

  Scenario Outline: Request from excel and  response validation using data table
    When TestCaseDataSetup, File-"demoData.xlsx", Sheet-"Update-PUT", TestCase-"<TestCaseId>"
    When Launch "/api/users", Method: "PUT"
    Then Verify response values:
      | statusCode | message |
      | 200        | 2024    |
    Examples:
      | TestCaseId    |
      | U-PUT-TC001 |