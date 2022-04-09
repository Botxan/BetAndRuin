package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Locale;

public class Dates {


    public static int getMonthNumber(String monthName) {

        DateTimeFormatter parser = DateTimeFormatter.ofPattern("MMMM")
                .withLocale(Locale.forLanguageTag("es-ES"));
        TemporalAccessor accessor = parser.parse(monthName);
        return accessor.get(ChronoField.MONTH_OF_YEAR);

        // return Month.valueOf(monthName.toUpperCase()).getValue();
    }

    public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static Date convertToDate(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    public static Date toDate(int year, int month) {
        //default time zone
        ZoneId defaultZoneId = ZoneId.systemDefault();

        LocalDate localDate = LocalDate.of(year, month, 1);
        return Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());

    }

    public static YearMonth getYearMonth(String date) {
        DateTimeFormatter fmt = new DateTimeFormatterBuilder()
                // case insensitive
                .parseCaseInsensitive()
                // pattern with full month name (MMMM)
                .appendPattern("MMMM yyyy")
                // set locale
                .toFormatter(Locale.getDefault());

        return YearMonth.parse(date, fmt);
    }

    /**
     * Returns whether a given day, month and year form a valid date or not.
     * @param year the day
     * @param month the month number
     * @param day the year
     * @return true if the formed date is valid. Otherwise, false.
     */
    public static boolean isValidDate(String year, String month, String day) {
        SimpleDateFormat sfm = new SimpleDateFormat("yyyy-MM-dd");
        sfm.setLenient(false);
        try {
            Date date = sfm.parse(year + "-" + month + "-" + day);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
