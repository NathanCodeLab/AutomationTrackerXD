package com.framework.support;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Util {

	private Util() {
	}

	public static String getFileSeparator() {
		return System.getProperty("file.separator");
	}

	public static Date getCurrentTime() {
		final Calendar calendar = Calendar.getInstance();
		return calendar.getTime();
	}

	public static String getCurrentFormattedTime(final String dateFormatString) {
		final DateFormat dateFormat = new SimpleDateFormat(dateFormatString);
		final Calendar calendar = Calendar.getInstance();
		return dateFormat.format(calendar.getTime());
	}

	public static String getFormattedTime(final Date time, final String dateFormatString) {
		final DateFormat dateFormat = new SimpleDateFormat(dateFormatString);
		return dateFormat.format(time);
	}

	public static String getTimeDifference(final Date startTime, final Date endTime) {
		long timeDiff = endTime.getTime() - startTime.getTime();
		timeDiff /= 1000L;
		final String timeDiffDetail = String.valueOf(Long.toString(timeDiff / 60L)) + "minute(s), "
				+ Long.toString(timeDiff % 60L) + " secounds ";
		return timeDiffDetail;

	}
}
