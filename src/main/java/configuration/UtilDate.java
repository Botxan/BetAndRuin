package configuration;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * This class implements the date utility of the application 
 * @author Josefinators team
 * @version first iteration
 *
 */
public class UtilDate {

	/**
	 * It trims the date 
	 * @param date an instance of date 
	 * @return trimmed date 
	 */
	public static Date trim(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("CET"));
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		return calendar.getTime();
	}

	/**
	 * It sets a new date 
	 * @param year an instance of year 
	 * @param month an instance of month 
	 * @param day an instance of day 
	 * @return a new date 
	 */
	public static Date newDate(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("CET"));
		calendar.set(year, month, day, 0, 0, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * It sets the first day of the month 
	 * @param date an instance of date 
	 * @return first day of the month 
	 */
	public static Date firstDayMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.setTimeZone(TimeZone.getTimeZone("CET"));
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		return calendar.getTime();
	}

	/**
	 * It sets the last day of the month 
	 * @param date an instance of date 
	 * @return last day of the month 
	 */
	public static Date lastDayMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.setTimeZone(TimeZone.getTimeZone("CET"));
		//int month=calendar.get(Calendar.MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		return calendar.getTime();
	}
	
	/**
	 * It returns the last day of the month.
	 * @param month an instance of month 
	 * @param year an instance of year 
	 * @return last day of the month
	 */
	public static int lastDayMonth(int month, int year)
	{
		YearMonth yearMonthObject = YearMonth.of(year, month);
		return yearMonthObject.lengthOfMonth();
	}
	
	/**
	 * It returns today's date in appropriate format
	 * @return today's date
	 */
	public static Date getTodaysDate(){
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}
	
	/**
	 * It returns whether a given user is 18 years old or not 
	 * @param birthdate an instance of the birthdate of the user 
	 * @return true if the user is underage 
	 */
	public static boolean isUnderage(Date birthdate)
	{
		LocalDate birthdateL = birthdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate todaysDateL = getTodaysDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		//Duration diff = Duration.between(birthdate, birthdate);
        int age = (int) ChronoUnit.YEARS.between(birthdateL, todaysDateL);
		return age < 18;
	}
}