package mtf.dm.cms.hhs.gov.cucumber;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import mtf.dm.cms.hhs.gov.utilities.LoggerUtil;
import mtf.dm.cms.hhs.gov.utilities.MyLogger;

public class Hooks {

    @Before
    public void setup(Scenario scenario) {
        MyLogger.startTestCase(scenario.getName());
        MyLogger.info("Starting the test...");
    }

    @After
    public void tearDown() {
        MyLogger.info("Test execution completed.");
    }

}
