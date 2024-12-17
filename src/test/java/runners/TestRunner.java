package runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "step",
        plugin = {"pretty", "html:target/cucumber-reports.html",
                "mtf.dm.cms.hhs.gov.utilities.ExtentReportListener"},
        tags = "@REG-API"
)

public class TestRunner {
}
