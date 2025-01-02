package mtf.dm.cms.hhs.gov.utilities;

import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
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
            ExtentCucumberAdapter.addTestStepLog("<pre>" + customMessage + "</pre>");
        } catch (AssertionError e){
            MyLogger.error("-- failure -- \n" + customMessage,e);
            // Throw assertion error
            throw new SuppressedStackTraceException("-- failure -- \n" + customMessage);
        }
    }

    /**
     * Reusable method to handle soft assert failures and log them
     *
     * @param soft          AssertJ SoftAssertions object
     */
    public static void handleSoftAssertFailures(SoftAssertions soft) throws SuppressedStackTraceException {
        // Collect errors from soft assertions
        List<Throwable> failures = soft.errorsCollected();

        // Extract and format failure messages
        String failureMessages = failures.stream()
                .map(Throwable::getMessage) // Get the message of the failure
                .map(msg -> msg.split("at AssertionUtils")[0]) // Remove anything from "at AssertionUtils" onwards
                .collect(Collectors.joining("-- failure --"));

        // Verify all soft assertions
        logAssertionError(soft::assertAll, "-- failure --" + failureMessages);
    }
}
