@REG-API-CREATE-SINGLE_RESOURCE @REG-API
Feature: DEMO Create API Testing LIST-USERS

  Scenario Outline: Validate API submission Create SINGLE_RESOURCE GET request
    When TestCaseDataSetup, File-"demoData.xlsx", Sheet-"Single Resource", TestCase-"<TestCaseId>"
    When DemoAPI: Launch "/api/unknown", Method: "GET"
    Then Verify status code 200
    Examples:
      | TestCaseId |
      | SR-TC001   |
      | SR-TC002   |
      | SR-TC003   |
      | SR-TC004   |