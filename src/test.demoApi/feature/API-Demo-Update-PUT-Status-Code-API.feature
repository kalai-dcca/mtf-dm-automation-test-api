@REG-API-CREATE-PUT @REG-API
Feature: DEMO Create API Testing PUT

  Scenario Outline: Validate API submission Create PUT request
    When TestCaseDataSetup, File-"demoData.xlsx", Sheet-"Update-PUT", TestCase-"<TestCaseId>"
    When DemoAPI: Launch "/api/users", Method: "PUT"
    Then Verify status code 200
    Examples:
      | TestCaseId  |
      | U-PUT-TC001 |
      | U-PUT-TC002 |
      | U-PUT-TC003 |