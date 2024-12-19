package mtf.dm.cms.hhs.gov.cucumber;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.service.ExtentService;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import mtf.dm.cms.hhs.gov.utilities.MyLogger;

import static mtf.dm.cms.hhs.gov.utilities.ExtentReportListener.getGitBranch;

public class Hooks {
    public static final ExtentReports extentReports = ExtentService.getInstance();

    @BeforeAll
    public static void before_all(){
        String testerName = System.getProperty("tester.name", System.getProperty("user.name", "Unknown Tester"));
        extentReports.setSystemInfo("Tester Name", testerName);
        extentReports.setSystemInfo("OS Type", System.getProperty("os.name"));
        extentReports.setSystemInfo("Branch", getGitBranch());
        extentReports.setSystemInfo("Environment", "QA");
        extentReports.setSystemInfo("Execution Time", java.time.LocalDateTime.now().toString());
    }
    @Before
    public void setup(Scenario scenario) {
        MyLogger.startTestCase(scenario.getName());
    }

    @After
    public void tearDown(Scenario scenario) {
        MyLogger.endTestCase(scenario.getName());
    }

}
