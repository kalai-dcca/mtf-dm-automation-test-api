package mtf.dm.cms.hhs.gov.cucumber;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.service.ExtentService;
import io.cucumber.core.exception.ExceptionUtils;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import mtf.dm.cms.hhs.gov.utilities.loggerUtilities.MyLogger;

import java.io.BufferedReader;
import java.io.InputStreamReader;


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

    // Helper method to get the current Git branch name
    public static String getGitBranch() {
        try {
            Process process = Runtime.getRuntime().exec("git rev-parse --abbrev-ref HEAD");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return reader.readLine().trim();
        } catch (Exception e) {
            MyLogger.endTestCase(ExceptionUtils.printStackTrace(e));
            return "Unknown Branch";
        }
    }

}
