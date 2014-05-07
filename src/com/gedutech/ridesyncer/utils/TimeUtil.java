package com.gedutech.ridesyncer.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

	public static final String TIME_FORMAT = "HH:mm:ss";
	public static final String RFC3339_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

	public static SimpleDateFormat getFormatter(String format) {
		return new SimpleDateFormat(format);
	}

	public static String format(Date date, String format) {
		return getFormatter(format).format(date);
	}

	public static Date parseTime(String time) throws ParseException {
		return getFormatter(TIME_FORMAT).parse(time);
	}

	public static String formatTime12(Date date) {
		return getFormatter("hh:mma").format(date);
	}

	public static String formatTime(Date time) {
		return getFormatter(TIME_FORMAT).format(time);
	}

	public static Date parseRFC3339(String dateString) throws ParseException {
		return getFormatter(RFC3339_FORMAT).parse(dateString);
	}

	public static String shortTime(Date date) {
		return getFormatter("hh:mma").format(date);
	}

	public static String formatRFC3339(Date date) {
		return getFormatter(RFC3339_FORMAT).format(date);
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
