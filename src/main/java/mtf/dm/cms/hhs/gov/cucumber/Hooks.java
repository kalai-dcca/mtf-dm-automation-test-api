package mtf.dm.cms.hhs.gov.cucumber;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.cucumber.java.*;
import mtf.dm.cms.hhs.gov.report.ExtentReportManager;
import mtf.dm.cms.hhs.gov.utilities.MyLogger;


import static mtf.dm.cms.hhs.gov.utilities.BaseClass.getTestScenarioClass;

public class Hooks {
    private static ExtentTest extentTest;

    public static ExtentReports extentReports = ExtentReportManager.getInstance();

    @BeforeAll
    public static void before_all() {
        String testerName = System.getProperty("tester.name", System.getProperty("user.name", "Unknown Tester"));
        extentReports.setSystemInfo("Tester Name", testerName);
        extentReports.setSystemInfo("OS Type", System.getProperty("os.name"));

        extentReports.setSystemInfo("Environment", "QA");
        extentReports.setSystemInfo("Execution Time", java.time.LocalDateTime.now().toString());
    }

    @Before
    public void setup(Scenario scenario) {

        MyLogger.startTestCase(scenario.getName());

        extentTest = ExtentReportManager.test;
        extentTest.info("Starting scenario: " + scenario.getName());
    }


    @After
    public void afterScenario(Scenario scenario) {
        // Log the scenario result
        scenario.attach(getTestScenarioClass().getJsonObject().toString(), "application/json", "Input Data");
        ExtentReportManager.logJson(getTestScenarioClass().getJsonObject().toString(), "Input data: ");
        if (scenario.isFailed()) {
            extentTest.fail("Scenario failed: " + scenario.getName());
        } else {
            extentTest.pass("Scenario passed: " + scenario.getName());
        }

        extentTest.info("Scenario " + scenario.getStatus().name());

    }

}
