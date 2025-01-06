package mtf.dm.cms.hhs.gov.utilities;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.text.MessageFormat.format;

public class DateUtilities {

    public static final String DATE_PREFIX = "date.";
    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtilities.class);
    private static final String MILLISECONDS = "milliseconds";
    private static final String MILLISECONDS_DATE_FORMAT = "yyyy/MM/dd HH:mm:ss:SSS";

    private DateUtilities() {}

    public static final String DD_MMM_YY = "dd/MMM/yy";
    public static final String MMMM_DD_YYYY = "MMMM dd yyyy";

    public static String getToday(String dateFormat) {
        return new SimpleDateFormat(dateFormat).format(new Date());
    }

    public static String getDate(Date date, String dateFormat) {
        return new SimpleDateFormat(dateFormat).format(date);
    }

    public static String getDate(int days, String dateFormat) {
        return new SimpleDateFormat(dateFormat).format(DateUtils.addDays(new Date(), days));
    }

    public static String alterDaysToDateMilliseconds(String days, String format) {
        try {
            return String.valueOf(new SimpleDateFormat(format).parse(days).getTime());
        } catch (ParseException e) {
            String errorMessage = format("Could not parse {0} date with format {1}", days, format);
            LOGGER.error(errorMessage);
            throw new DateUtilitiesException(errorMessage);
        }
    }

    public static String getFormattedDateValue(String value) {
        String replaceValue;
        String[] dayFormatValues = value.split("\\.");
        String prefixString = dayFormatValues[0] + ".";
        String day = dayFormatValues[1];
        String dateFormat = dayFormatValues[2];
        if ("now".equalsIgnoreCase(day)) {
            replaceValue = MILLISECONDS.equalsIgnoreCase(dateFormat) ?
                    alterDaysToDateMilliseconds(getToday(MILLISECONDS_DATE_FORMAT), MILLISECONDS_DATE_FORMAT)
                    : getToday(dateFormat);
        } else if (NumberUtils.isParsable(day)) {
            replaceValue = MILLISECONDS.equalsIgnoreCase(dateFormat) ?
                    alterDaysToDateMilliseconds(getDate(Integer.parseInt(day), MILLISECONDS_DATE_FORMAT), MILLISECONDS_DATE_FORMAT)
                    : getDate(Integer.parseInt(day), dateFormat);
        } else {
            throw new DateUtilitiesException(format("Value \"{0}\" has to be \"now\" or any positive or negative digit number", day));
        }
        value = (prefixString.replace(DATE_PREFIX, "")).concat(replaceValue);
        return value;
    }

    private static class DateUtilitiesException extends RuntimeException {
        private DateUtilitiesException(String errorMessage) { super(errorMessage); }
    }
}
