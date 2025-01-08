package mtf.dm.cms.hhs.gov.utilities.fileHandlerUtilities;

public class ExcelColumnSpec {
    private String type;
    private String format;
    private String pattern;
    private String errorMessage;

    // Constructor
    public ExcelColumnSpec(String type, String format, String pattern, String errorMessage) {
        this.type = type;
        this.format = format;
        this.pattern = pattern;
        this.errorMessage = errorMessage;
    }

    // Getters
    public String getType() {
        return type;
    }

    public String getFormat() {
        return format;
    }

    public String getPattern() {
        return pattern;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return String.format("Type: %s, Format: %s, Pattern: %s, ErrorMessage: %s", type, format, pattern, errorMessage);
    }

    public boolean cellMatchesPattern(String value) {
        return value.matches(this.pattern);
    }
}
