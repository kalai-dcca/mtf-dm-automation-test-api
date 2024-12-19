@REG-API-CREATE-POST @REG-API
Feature: DEMO Create API Testing POST

  Scenario Outline: Validate request and response Expected values are passed excel file
    When TestCaseDataSetup, File-"demoData.xlsx", Sheet-"Create", TestCase-"<TestCaseId>"
    When Launch "/api/users", Method: "POST"
    Then Verify response values from Excel for attributes "STATUS_CODE,createdAt"
    Examples:
      | TestCaseId |
      | C-TC001    |


  Scenario: Validate request and response Expected values from examples
    When TestCaseDataSetup
      | userName | testUserName |
      | userRole | Manager      |
    When Launch "/api/users", Method: "POST"
    Then Verify response values:
      | statusCode | message |
      | 201        | 2024    |


  Scenario: Validate response Expected values are passed as json file
    When TestCaseDataSetup, JSONFile-"create.json"
    When Launch "/api/users", Method: "POST"
    Then Verify status code 201 and message "2024"

  Scenario Outline: Validate request and response Expected values are passed as json file
    When TestCaseDataSetup, File-"demoData.xlsx", Sheet-"Update-PATCH", TestCase-"<TestCaseId>"
    When Launch "/api/users", Method: "PATCH"
    Then Verify status code 200
    Examples:
      | TestCaseId    |
      | U-PATCH-TC001 |
      | U-PATCH-TC002 |
      | U-PATCH-TC003 |


  Scenario Outline: we need examples for JsonArray request body and response validation using excel
    When TestCaseDataSetup, File-"demoData.xlsx", Sheet-"Update-PATCH", TestCase-"<TestCaseId>"
    When Launch "/api/users", Method: "PATCH"
    Then Verify status code 200
    Examples:
      | TestCaseId    |
      | U-PATCH-TC001 |
      | U-PATCH-TC002 |
      | U-PATCH-TC003 |