package mtf.dm.cms.hhs.gov.jsonSpecs;

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

    public void validateCell(String value, String columnName, int rowIndex) {
        if (!value.matches(this.pattern)) {
            throw new RuntimeException(String.format(
                    "Validation failed for column '%s' at row %d: \nValue: '%s' \nSpec: %s",
                    columnName,
                    rowIndex + 1, // +1 for Excel's 1-based row numbering
                    value,
                    this.toString()
            ));
        }
        System.out.println("Value matches for value: " + value);
    }

}
