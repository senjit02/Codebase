package com.ishare.split.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	public static String getDateFormat(Date date,String format)
	{
		SimpleDateFormat format1 = new SimpleDateFormat(format);
		return format1.format(date);
	}
	
	public static boolean compareDate(Date date1, Date date2)
	{
		return date2.after(date1);
	}
	
	public static Date getDateFromString(String date, String format)
	{
		DateFormat sourceFormat = new SimpleDateFormat(format);
		Date formatedDate = null;
		try {
			formatedDate = sourceFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return formatedDate;
	}

}
