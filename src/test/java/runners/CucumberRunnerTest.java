package runners;

import io.cucumber.core.options.Constants;
import org.junit.platform.suite.api.*;

import static io.cucumber.junit.platform.engine.Constants.*;

/**
 * Use junit-platform-suite to tell both your IDE and Gradle where to find tests. The "features" folder in
 * src/test/resources contains all .feature files of this project. Note that the SelectClasspathResource annotation
 * can be repeated, thus allowing us to specify multiple locations for feature files.
 *
 * The "cucumber" engine inclusion is not strictly necessary since that is the only engine available in this project.
 * However, if you are planning on running regular JUnit5 tests in the same project, make sure to specify the engine
 * here.
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("mtf.dm.cms.hhs.gov.cucumber")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME,value = "mtf.dm.cms.hhs.gov.cucumber")
@ConfigurationParameter(key = FEATURES_PROPERTY_NAME,value = "src/")
@ConfigurationParameter(key = EXECUTION_DRY_RUN_PROPERTY_NAME,value = "false")
@ConfigurationParameter(key = PLUGIN_PUBLISH_QUIET_PROPERTY_NAME,value = "true")
@ConfigurationParameter(key = PLUGIN_PUBLISH_ENABLED_PROPERTY_NAME,value = "false")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME,value = "pretty, html:target/cucumber-report/cucumber.html, json:target/cucumber-reports/cucumber.json")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME,value = "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:")


public class CucumberRunnerTest {


}
