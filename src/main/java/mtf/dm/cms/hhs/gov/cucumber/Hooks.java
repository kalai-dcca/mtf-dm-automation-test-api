package mtf.dm.cms.hhs.gov.cucumber;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import mtf.dm.cms.hhs.gov.report.ExtentReportManager;
import mtf.dm.cms.hhs.gov.utilities.LoggerUtil;

public class Hooks {

    @Before
    public void setup() {
        LoggerUtil.logger.info("Starting the test...");
        ExtentReportManager.createTest("Cucumber Test");
    }

    @After
    public void tearDown() {
        LoggerUtil.logger.info("Test execution completed.");
        ExtentReportManager.flush();
    }
}
