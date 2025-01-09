# Set up

## Installation
Please install the project dependencies and required JDK 21

## Folder Structure -Main
src/main/....[other than Module] : Use for any common methods used across the application.It should be used by API Lead and Test leads

## Folder Structure -Main/Module
src/main/Module: Every team should use their corresponding folders for customized methods if you want to implement for the microservice

## Folder Structure -Test
testrunner : Use for executing the test
authservice: Use to grab the auth bearer token

## How to add feature file for your testing
1. Check the folder test.AExampleAPI
2. Go over the steps in the feature file which has already been added in the file. There are many combinations already provided to read the request from data tables, excel and json etc
   Utilize as much as possible and if any customized steps needed please implement within your module
3. Add testcaseId and testcasedescription for every test
6. Add positive and negative test
7. Use N_ for any negative scenarios

## Tags
1. Feature - @module-featurename
2. Scenario tier 1: @module-api, @module-db
3. Scenario tier 2: @module-component
4. Scneario tier 3: @module-smoke, @module-regression, @module-endtoend

## Execution
Make sure to have unique tags for each scneario and feature file
Make sure to provide the values for the following environment variables
When using Maven, tags can be provided from the CLI using the **groups** and **excludedGroups** parameters.

any tags need to execute, use: `-Dgroups=tag`
any tags need to be excluded, use: `-DexcludedGroups=tag`


```shell
mvn clean test -Denv=dev -DprojectName=anExampleApi -Dgroups=example
```

## PR Rejection If
1. common methods are not utlized
2. features are not grouped properly
3. tags are not added correctly
4. failing tests
5. code that doesn't adhere to project guidelines
6. incomplete features
7. merge conflicts

