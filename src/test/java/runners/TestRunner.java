package runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test.demoApi/feature",
        glue = "mtf.dm.cms.hhs.gov.cucumber",
        plugin = {"pretty", "json:target/cucumber-reports/cucumber.json",
                "html:target/cucumber-reports/cucumberreport.html",
                "mtf.dm.cms.hhs.gov.report.ExtentCucumberAdapter"},
        tags = "@REG-API"
)

public class TestRunner {
}
