@REG-API-CREATE-PATCH @REG-API
Feature: DEMO Create API Testing PATCH

  Scenario Outline: Validate API submission Create PATCH request
    When TestCaseDataSetup, File-"demoData.xlsx", Sheet-"Update-PATCH", TestCase-"<TestCaseId>"
    When DemoAPI: Launch "/api/users", Method: "PATCH"
    Then Verify status code 200
    Examples:
      | TestCaseId    |
      | U-PATCH-TC001 |
      | U-PATCH-TC002 |
      | U-PATCH-TC003 |