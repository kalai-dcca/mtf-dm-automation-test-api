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
    public static void logAssertionError(Runnable assertion, String customMessage){
        try{
            assertion.run();
        } catch (AssertionError e){
            LoggerUtil.logger.error(customMessage);
            // Throw assertion error
            throw new AssertionError(customMessage, e);
        }
    }
}
