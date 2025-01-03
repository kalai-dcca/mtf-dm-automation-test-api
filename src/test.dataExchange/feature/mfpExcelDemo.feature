@FILE-HANDLER
Feature: Data Exchange MFP Excel File Ingestion

  @smoke
  Scenario Outline: Dynamically validate MFP Excel File against Database per Excel Test File
    Given TestCaseDataSetup-"dataExchange", File-"dataExchangeDemoData.xlsx", Sheet-"MFP", TestCase-"<TestCaseId>"
    # Given the file "mfpDemo.xlsx" is downloaded from the S3 bucket //WILL IMPLEMENT AFTER WE CONFIRM DEVELOPMENT IMPLEMENTATION
    And the "dataExchange" file "mfpDemo.xlsx" follows the specification in "mfpDemo" in sheet "MFP Pricing File - Data"
    # When the "dataExchange" microservice processes the file //WILL IMPLEMENT AFTER WE CONFIRM DEVELOPMENT IMPLEMENTATION
    Then verify file data is in the "dataExchange" database
    Examples:
      | TestCaseId |
      | 1          |

  Scenario: Dynamically validate MFP Excel File against Database
    # Given the file "mfpDemo.xlsx" is downloaded from the S3 bucket //WILL IMPLEMENT AFTER WE CONFIRM DEVELOPMENT IMPLEMENTATION
    Given the "dataExchange" file "mfpDemo.xlsx" follows the specification in "mfpDemo" in sheet "MFP Pricing File - Data"
    # When the "dataExchange" microservice processes the file //WILL IMPLEMENT AFTER WE CONFIRM DEVELOPMENT IMPLEMENTATION
    When "dataExchange" database is queried
	 """
      SELECT
        ndc_9, ndc_11
      FROM
        price_ndc_cd;
      """
    Then the file data matches the database for columns
      | ndc_9  | NDC-9  |
      | ndc_11 | NDC-11 |
    When "dataExchange" database is queried
	 """
      SELECT
        ipay, ndc_brand_name, ndc_active_ingredient_name
      FROM
        price_ipay_name;
      """
    Then the file data matches the database for columns
      | ipay                       | IPAY                                         |
      | ndc_brand_name             | Selected Drug Name                           |
      | ndc_active_ingredient_name | Active Ingredient Name or Active Moiety Name |
    When "dataExchange" database is queried
      """
      SELECT
        mfp_per_30, mfp_per_unit, mfp_per_package, update_type, remark,
        FORMAT(mfp_eff_dt, "dd-mmm-yyyy") AS mfp_eff_dt_formatted,
        FORMAT(mfp_end_dt, "dd-mmm-yyyy") AS mfp_end_dt_formatted,
        FORMAT(asof_dt, "dd-mmm-yyyy") AS asof_dt_formatted
      FROM
        price_eff_dt;
      """
    Then the file data matches the database for columns
      | mfp_per_30           | Single MFP per 30 DES        |
      | mfp_per_unit         | NDC-9 MFP per Unit Price     |
      | mfp_per_package      | NDC-11 MFP per Package Price |
      | update_type          | Type of Update               |
      | remark               | Remark                       |
      | mfp_eff_dt_formatted | MFP Effective Date           |
      | mfp_end_dt_formatted | MFP End Date                 |
      | asof_dt_formatted    | As of Date                   |