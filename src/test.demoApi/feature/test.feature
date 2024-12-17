Feature: DEMO API Testing

  Scenario Outline: Validate API submission
#    PREP DATA
    When Launch Demo API service and Review API service with test data from the testcase file "demoData.xlsx"
    Then Read test data from the sheet "create" for the "<TestCaseId>"

    When DemoAPI: Launch "/api/users", Method: "POST", request params: File "create.json"
    Then Verify status code 201 and message "2024"

    Examples:
      | TestCaseId |
      | TC1000    |