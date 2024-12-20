package mtf.dm.cms.hhs.gov.utilities;

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
        } catch (AssertionError e){
            MyLogger.error(customMessage,e);
            // Throw assertion error
            throw new SuppressedStackTraceException(customMessage);
        }
    }
}
