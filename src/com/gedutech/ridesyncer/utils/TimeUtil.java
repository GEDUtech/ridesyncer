package com.gedutech.ridesyncer.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

	public static SimpleDateFormat getFormatter(String format) {
		return new SimpleDateFormat(format);
	}

	public static Date parseRFC3339(String dateString) throws ParseException {
		return getFormatter("yyyy-MM-dd'T'h:m:ssXXX").parse(dateString);
	}

	public static String shortTime(Date date) {
		return getFormatter("hh:mma").format(date);
	}

	public static String formatRFC3339(Date date) {
		return getFormatter("yyyy-MM-dd'T'h:m:ssXXX").format(date);
	}

	public static String shortWeekday(int weekday) {
		return weekday(weekday, "EEE");
	}

	protected static String weekday(int weekday, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK, weekday);
		return getFormatter(format).format(calendar.getTime());
	}

}
