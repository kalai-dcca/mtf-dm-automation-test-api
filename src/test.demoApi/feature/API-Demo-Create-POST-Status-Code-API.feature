@REG-API-CREATE-POST @REG-API
Feature: DEMO Create API Testing POST

  Scenario Outline: Validate API submission Create POST request
    When TestCaseDataSetup, File-"demoData.xlsx", Sheet-"Create", TestCase-"<TestCaseId>"
    When DemoAPI: Launch "/api/users", Method: "POST"
    Then Verify status code 201 and message "2024"
    Examples:
      | TestCaseId |
      | C-TC001    |
      | C-TC002    |
      | C-TC003    |
      | C-TC004    |