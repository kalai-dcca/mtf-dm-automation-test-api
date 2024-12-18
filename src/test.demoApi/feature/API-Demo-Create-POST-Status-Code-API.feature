@REG-API-CREATE-POST @REG-API
Feature: DEMO Create API Testing POST

  Scenario Outline: Validate request and response Expected values are passed excel file
    When TestCaseDataSetup, File-"demoData.xlsx", Sheet-"Create", TestCase-"<TestCaseId>"
    When Launch "/api/users", Method: "POST"
    Then Verify status code 201 and message "2024"
    Examples:
      | TestCaseId |
      | C-TC001    |
      | C-TC002    |
      | C-TC003    |
      | C-TC004    |


  Scenario Outline: Validate request and response Expected values from examples
    When TestCaseDataSetup, File-"demoData.xlsx", Sheet-"Create", TestCase-"<TestCaseId>"
    When Launch "/api/users", Method: "POST"
    Then Verify status code 201 and message "2024"
    Examples:
      | TestCaseId |
      | C-TC001    |
      | C-TC002    |
      | C-TC003    |
      | C-TC004    |


  Scenario Outline: Validate response Expected values are passed as json file
    When TestCaseDataSetup, File-"demoData.xlsx", Sheet-"List users", TestCase-"<TestCaseId>"
    When Launch "/api/users", QParam:"page" Method: "GET"
    Then Verify status code 200
    Examples:
      | TestCaseId |
      | LU-TC001   |
      | LU-TC002   |
      | LU-TC003   |
      | LU-TC004   |

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