package mtf.dm.cms.hhs.gov.utilities;

import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.assertj.core.api.SoftAssertions;

import java.util.List;
import java.util.stream.Collectors;

public class AssertionHandler {

    public enum AssertType{
        HARD, SOFT
    }

    /**
     * Adds an assertion error to the log report.
     *
     * @param assertion          The assertion call
     * @param customMessage          The customized error message to be logged.
     */
    public static void logAssertionError(Runnable assertion, String customMessage) throws SuppressedStackTraceException {
        try{
            assertion.run();
            // Add custom message to Extent report
            ExtentCucumberAdapter.getCurrentStep().info(MarkupHelper.createLabel("RESULT", ExtentColor.GREEN));
            ExtentCucumberAdapter.addTestStepLog("<pre>" + customMessage + "</pre>");
        } catch (AssertionError e){
            ExtentCucumberAdapter.getCurrentStep().info(MarkupHelper.createLabel("RESULT", ExtentColor.RED));
            MyLogger.error(customMessage,e);
            // Throw assertion error
            throw new SuppressedStackTraceException(customMessage);
        }
    }

    /**
     * Reusable method to handle soft assert failures and log them
     *
     * @param soft           AssertJ SoftAssertions object
     * @param customMessages
     */
    public static void handleSoftAssertFailures(SoftAssertions soft, List<String> customMessages) throws SuppressedStackTraceException {
        // Collect errors from soft assertions
        List<Throwable> failures = soft.errorsCollected();

        // Verify all soft assertions
        if (!failures.isEmpty()){
            // Extract and format failure messages
            String failureMessages = failures.stream()
                    .map(Throwable::getMessage) // Get the message of the failure
                    .map(msg -> msg.split("at AssertionUtils")[0]) // Remove anything from "at AssertionUtils" onwards
                    .collect(Collectors.joining("\n" + "-- failure --"));

            logAssertionError(soft::assertAll, "-- failure --" + failureMessages);
        }
        else{ // return all custom messages if all tests passes
           String successMessages = String.join("\n", customMessages);

            logAssertionError(soft::assertAll, successMessages);
        }

    }
}
