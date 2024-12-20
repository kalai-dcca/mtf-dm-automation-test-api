package mtf.dm.cms.hhs.gov.utilities;
/*
Custom exception class for suppressing the stack trace in the end-user report.
 */
public class SuppressedStackTraceException extends Exception {
    private String userMessage;

    // Constructor to initialize with a custom message
    public SuppressedStackTraceException(String message) {
        super(message);
        this.userMessage = message;

        // Set the stack trace to be empty (i.e., suppress the default stack trace)
        setStackTrace(new StackTraceElement[0]);
    }

    // Constructor to initialize with a cause (another throwable)
    public SuppressedStackTraceException(String message, Throwable cause) {
        super(message, cause);
        this.userMessage = message;

        // Set the stack trace to be empty (i.e., suppress the default stack trace)
        setStackTrace(new StackTraceElement[0]);
    }

    // Override the toString method to provide a user-friendly message (suppress stack trace)
    @Override
    public String toString() {
        return userMessage;
    }
}
