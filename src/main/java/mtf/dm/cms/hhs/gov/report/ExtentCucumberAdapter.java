package mtf.dm.cms.hhs.gov.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.*;
import com.aventstack.extentreports.Status;

import java.util.HashMap;
import java.util.Map;

public class ExtentCucumberAdapter implements EventListener {
    private ExtentReports extent = ExtentReportManager.getInstance();
    private ExtentTest currentScenario;
    private Map<String, ExtentTest> scenarioMap = new HashMap<>();

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestStepStarted.class, this::handleStepStarted);
        publisher.registerHandlerFor(TestStepFinished.class, this::handleStepFinished);
        publisher.registerHandlerFor(TestCaseStarted.class, this::handleScenarioStarted);
        publisher.registerHandlerFor(TestCaseFinished.class, this::handleScenarioFinished);
    }

    private void handleScenarioStarted(TestCaseStarted event) {
        currentScenario =  ExtentReportManager.createTest(event.getTestCase().getName());
        scenarioMap.put(event.getTestCase().getName(), currentScenario);
    }

    private void handleStepStarted(TestStepStarted event) {
        // Log the step name
        if (event.getTestStep() instanceof PickleStepTestStep) {
            String stepName = ((PickleStepTestStep) event.getTestStep()).getStep().getText();
        }
    }

    private void handleStepFinished(TestStepFinished event) {
        if (event.getTestStep() instanceof PickleStepTestStep) {
            String stepName = ((PickleStepTestStep) event.getTestStep()).getStep().getText();
            io.cucumber.plugin.event.Status stepStatus = event.getResult().getStatus();

            if (stepStatus.equals(io.cucumber.plugin.event.Status.PASSED)) {
                currentScenario.log(Status.PASS, "Step passed: " + stepName);
            } else if (stepStatus.equals(io.cucumber.plugin.event.Status.FAILED)) {
                currentScenario.log(Status.FAIL, "Step failed: " + stepName);
                Throwable error = event.getResult().getError();
                if (error != null) {
                    currentScenario.log(Status.FAIL, error.getMessage());
                }
            } else {
                currentScenario.log(Status.SKIP, "Step skipped: " + stepName);
            }
        }
    }

    private void handleScenarioFinished(TestCaseFinished event) {
        ExtentReportManager.flush();
    }
}