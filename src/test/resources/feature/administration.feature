###this feature file will execute the scenario and generate auth bearer token for any request
#
#@REG-API @smoke
#Feature: DEMO Create API Testing POST111
#
#  @smoke
#  Scenario Outline: Validate request and response Expected values are passed excel file
#    When TestCaseDataSetup, File-"demoData.xlsx", Sheet-"Create", TestCase-"<TestCaseId>"
#    When Launch "/api/users", Method: "POST"
#    Then Verify status code 201 and message "2024"
#    Examples:
#      | TestCaseId |
#      | C-TC001    |