package configuration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class UtilDate {

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

	public static Date newDate(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("CET"));
		calendar.set(year, month, day, 0, 0, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		System.out.println("newDate: " + calendar.getTime());
		return calendar.getTime();
	}

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
	 * Returns todays date in Date format.
	 * @return Todays date.
	 */
	public static Date getTodaysDate(){
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}
	
	/**
	 * Given a birthdate returns whether it has more than 18 years or not.
	 * @param birthdate Birthdate of the user.
	 * @return true if the user is undrage.
	 */
	public static boolean isUnderage(Date birthdate)
	{
		LocalDate birthdateL = birthdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate todaysDateL = getTodaysDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		Duration diff = Duration.between(todaysDateL, birthdateL);
		return diff.toDays() < 365 * 18;
	}
}