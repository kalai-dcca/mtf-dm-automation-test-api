@example
Feature: Example feature file to create scenario

#  complex validation of attribute and Json array in the response

  @T1
  Scenario Outline: request from Excel and validate response Expected values from json file
    When TestCaseDataSetup, File-"demoData.xlsx", Sheet-"List-Users", TestCase-"<TestCaseId>"
    When Fetch all pages from "/api/users" with query param "page" and method "GET"
    Then Verify status code 200 and the response array "data" matches expected values from "<ExpectedJson>"
    Then Validate the attribute "support.url" equals to "https://contentcaddy.io?utm_source=reqres&utm_medium=json&utm_campaign=referral"
    Then Validate the array "data" has size of "6"
    Then Validate the attribute "support.text" contains "Tired of writing endless"

    Examples:
      | TestCaseId    | ExpectedJson |
      | LU-TC001      |    ExpectedResponse-Page-1.json         |

#  Request Excel
#  Response Excel
  Scenario Outline: Validate request and response Expected values are passed excel file
    When TestCaseDataSetup, File-"demoData.xlsx", Sheet-"Create", TestCase-"<TestCaseId>"
    When Launch "/api/users", Method: "POST"
    Then Verify response values from Excel for attributes "STATUS_CODE,createdAt"
    Examples:
      | TestCaseId | TestDescription |
      | C-TC001    |       create user with valid request body      |
      | C-TC002   |        N_create user with missing request body   |

# Request feature Data table
#  Response feature Data table
  Scenario: Validate request and response Expected values from example table
    When TestCaseDataSetup
      | userName | testUserName |
      | userRole | Manager      |
    When Launch "/api/users", Method: "POST"
    Then Verify response values:
      | statusCode | message |
      | 201        | 2025    |

# Request JSON file
#  Response JSON file
  Scenario: Validate both request and response from json file
    When TestCaseDataSetup, JSONFile-"create.json"
    When Fetch all pages from "/api/users" with query param "page" and method "GET"
    Then Verify status code 200 and the response array "data" matches expected values from "ExpectedResponse-Page-1.json"

# Request Excel file
#  Response JSON file
  Scenario Outline: request from Excel and validate response Expected values from json file
    When TestCaseDataSetup, File-"demoData.xlsx", Sheet-"List-Users", TestCase-"<TestCaseId>"
    When Fetch all pages from "/api/users" with query param "page" and method "GET"
    Then Verify status code 200 and the response array "data" matches expected values from "<ExpectedJson>"
    Examples:
      | TestCaseId    | ExpectedJson |
      | LU-TC001      |    ExpectedResponse-Page-1.json         |
