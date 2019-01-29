package gatech.edu.STIECR.JSON.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	private static final String defaultDateFormat = "dd-MM-yy";
	private static final String birthdayDateFormat = "yyyyMMdd";
	private static final String defaultDateTimeFormat = "yyyyMMdd";
	private static final String fhirDateTimeFormat = "MMM dd, yyyy h:m:s a";
	private static final SimpleDateFormat sdFormmater = new SimpleDateFormat(defaultDateFormat);
	private static final SimpleDateFormat birthdayFormmater = new SimpleDateFormat(birthdayDateFormat);
	private static final DateFormat sdDateTimeFormmater = DateFormat.getDateTimeInstance();
	private static final SimpleDateFormat fhirDateTimeFormmater = new SimpleDateFormat(fhirDateTimeFormat);
	
	public static Calendar stringToCalendar(String string) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdFormmater.parse(string));
		return cal;
	}
	
	public static Date stringToDate(String string) throws ParseException {
		return sdFormmater.parse(string);
	}
	
	public static String dateToStdString(Date date) {
		return sdFormmater.format(date);
	}
	
	public static Calendar birthdayStringToCalendar(String string) throws ParseException{
		Calendar cal = Calendar.getInstance();
		cal.setTime(birthdayFormmater.parse(string));
		return cal;
	}
	
	public static String DateTimeToStdString(Date dateTime) {
		return sdDateTimeFormmater.format(dateTime);
	}
	
	public static Date DateTimeStringToDateTime(String string) throws ParseException {
		return fhirDateTimeFormmater.parse(string);
	}
	
	public static String stringToDateTime(Date date){
		return fhirDateTimeFormmater.format(date);
	}
}
