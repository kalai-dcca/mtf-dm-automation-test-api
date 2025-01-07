# Set up

## Installation

Please install the project dependencies and required JDK 21

## Executing tests

Make sure to provide the values for the following environment variables

- PenvName: we have added multiple environments in maven profile, so for any env execution use -Pdev

If you have to provide all three env values:

```shell
mvn clean test -Pdev
```

more configs we can do with it:

```shell
mvn test -Dsurefire.includeJUnit5Engines=cucumber -Dcucumber.plugin=pretty -Dcucumber.features=path/to/example.feature:10 
```


## API Steps

### Prep Data

read data and prep request body from excel and specific Sheet based on test case Id
```cucumber
TestCaseDataSetup, File-"demoData.xlsx", Sheet-"Create", TestCase-"<TestCaseId>"
```

read data and prep request body from Cucumber example data table on test case Id
```cucumber
TestCaseDataSetup
      | userName | testUserName |
      | userRole | Manager      |
```

read data and prep request body from Json file
```cucumber
TestCaseDataSetup, JSONFile-"create.json"
```

### Sent request

send requst with endpoint and Method
```cucumber
Launch "/api/users", Method: "POST"
```
