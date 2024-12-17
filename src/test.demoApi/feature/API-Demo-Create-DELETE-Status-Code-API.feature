@REG-API-CREATE-DELETE @REG-API
Feature: DEMO Create API Testing DELETE

  Scenario Outline: Validate API submission Create DELETE request
    When TestCaseDataSetup, File-"demoData.xlsx", Sheet-"Delete", TestCase-"<TestCaseId>"
    When DemoAPI: Launch "/api/users", Method: "DELETE"
    Then Verify status code 204
    Examples:
      | TestCaseId  |
      | DEL-TC001 |
      | DEL-TC002 |
      | DEL-TC003 |
      | DEL-TC004 |