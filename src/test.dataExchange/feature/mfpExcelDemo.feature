@FILE-HANDLER
Feature: Data Exchange MFP Excel File Ingestion

  Scenario Outline: Dynamically validate MFP Excel File against Database per Excel Test File
    Given FileHandler-TestCaseDataSetup, File-"dataExchangeDemoData.xlsx", Sheet-"MFP", TestCase-"<TestCaseId>"
    # Given the file "mfpDemo.xlsx" is downloaded from the S3 bucket //WILL IMPLEMENT AFTER WE CONFIRM DEVELOPMENT IMPLEMENTATION
    #And the file "mfpDemo.xlsx" follows the specification in "mfpDemoSpecs"
    # When the "dataExchange" microservice processes the file //WILL IMPLEMENT AFTER WE CONFIRM DEVELOPMENT IMPLEMENTATION
    #Then verify "mfpDemo.xlsx" data is in the "dataExchange" database
    Examples:
      | TestCaseId |
      | 1          |