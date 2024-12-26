package mtf.dm.cms.hhs.gov.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.cucumber.core.exception.ExceptionUtils;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ExtentReportListener implements ConcurrentEventListener {

    private static ExtentReports extent;
    private static Map<String, ExtentTest> scenarioTestMap = new HashMap<>();

    static {
        ExtentSparkReporter htmlReporter = new ExtentSparkReporter("target/cucumber-reports/extent-report.html");
        htmlReporter.config().setTheme(Theme.DARK);
        htmlReporter.config().setDocumentTitle("Enhanced Cucumber Extent Report");
        htmlReporter.config().setReportName("Detailed Test Execution Report");
        htmlReporter.config().setEncoding("UTF-8");

        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);

        // Dynamically fetch the tester's name
        String testerName = System.getProperty("tester.name", System.getProperty("user.name", "Unknown Tester"));
        extent.setSystemInfo("Tester", testerName);

        // Fetch Git branch name
        extent.setSystemInfo("Branch", getGitBranch());

        // Additional system info
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("Execution Time", java.time.LocalDateTime.now().toString());
    }

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseStarted.class, this::onTestCaseStarted);
        publisher.registerHandlerFor(TestStepFinished.class, this::onTestStepFinished);
        publisher.registerHandlerFor(TestCaseFinished.class, this::onTestCaseFinished);
    }

    private void onTestCaseStarted(TestCaseStarted event) {
        ExtentTest test = extent.createTest(event.getTestCase().getName())
                .assignAuthor(System.getProperty("tester.name", System.getProperty("user.name", "Unknown Tester")));
        test.info("Test Started: " + event.getTestCase().getName());
        scenarioTestMap.put(event.getTestCase().getId().toString(), test);
    }

    private void onTestStepFinished(TestStepFinished event) {
        ExtentTest test = scenarioTestMap.get(event.getTestCase().getId().toString());
        if (event.getTestStep() instanceof PickleStepTestStep) {
            String stepName = ((PickleStepTestStep) event.getTestStep()).getStep().getText();
            if (event.getResult().getStatus() == Status.PASSED) {
                test.pass("Step Passed: " + stepName);
            } else if (event.getResult().getStatus() == Status.FAILED) {
                test.fail("Step Failed: " + stepName + "\nError: " + event.getResult().getError().getMessage());
            } else if (event.getResult().getStatus() == Status.SKIPPED) {
                test.skip("Step Skipped: " + stepName);
            }
        }
    }

    private void onTestCaseFinished(TestCaseFinished event) {
        ExtentTest test = scenarioTestMap.get(event.getTestCase().getId().toString());
        if (event.getResult().getStatus() == Status.PASSED) {
            test.pass("Test Passed");
        } else if (event.getResult().getStatus() == Status.FAILED) {
            test.fail("Test Failed: " + event.getResult().getError().getMessage());
        }
        extent.flush();
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
