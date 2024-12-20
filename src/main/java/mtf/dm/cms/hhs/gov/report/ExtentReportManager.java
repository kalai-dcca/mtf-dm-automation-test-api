package mtf.dm.cms.hhs.gov.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import mtf.dm.cms.hhs.gov.utilities.JsonUtils;

public class ExtentReportManager {

    private static ExtentReports extent;
    public static ExtentTest test;

    public static ExtentReports getInstance() {
        if (extent == null) {
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter("test-output/ExtentReport.html");
            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
        }
        return extent;
    }

    public static ExtentTest createTest(String testName) {
        test = getInstance().createTest(testName);
        return test;
    }

    public static void flush() {
        if (extent != null) {
            extent.flush();
        }
    }

    public static void logJson(String json, String message) {
        String formattedJson = JsonUtils.formatJson(json);
        test.log(Status.INFO, message + "<pre>" + formattedJson + "</pre>");
    }

    public static ExtentTest getTest() {
        return test;
    }

}
