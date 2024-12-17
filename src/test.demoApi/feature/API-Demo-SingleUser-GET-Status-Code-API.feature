@REG-API-CREATE-SINGLE-USER @REG-API
Feature: DEMO Create API Testing SINGLE-USER

  Scenario Outline: Validate API submission Create SINGLE-USER GET request
    When TestCaseDataSetup, File-"demoData.xlsx", Sheet-"Single User", TestCase-"<TestCaseId>"
    When DemoAPI: Launch "/api/users", Method: "GET"
    Then Verify status code 200
    Examples:
      | TestCaseId |
      | SU-TC001   |
      | SU-TC002   |
      | SU-TC003   |