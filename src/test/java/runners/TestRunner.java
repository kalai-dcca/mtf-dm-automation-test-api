package runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "/feature",
        glue = "mtf.dm.cms.hhs.gov.cucumber",
        plugin = {"pretty", "html:target/cucumber-reports.html",
                "mtf.dm.cms.hhs.gov.utilities.ExtentReportListener"},
        tags = "@smoke"
)

public class TestRunner {
}
