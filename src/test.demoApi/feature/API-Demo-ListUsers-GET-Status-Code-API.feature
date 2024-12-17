@REG-API-CREATE-LIST-USERS @REG-API
Feature: DEMO Create API Testing LIST-USERS

  Scenario Outline: Validate API submission Create LIST-USERS GET request
    When TestCaseDataSetup, File-"demoData.xlsx", Sheet-"List users", TestCase-"<TestCaseId>"
    When DemoAPI: Launch "/api/users", QParam:"page" Method: "GET"
    Then Verify status code 200
    Examples:
      | TestCaseId |
      | LU-TC001   |
      | LU-TC002   |
      | LU-TC003   |
      | LU-TC004   |