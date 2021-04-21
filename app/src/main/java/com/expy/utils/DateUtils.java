package com.expy.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static final String DATE_FORMAT = "yyyy/MM/dd";

    public static String getCurrentDate(){
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getFormattedDate(String defaultDate, boolean shortMonth){
        try {
            int dateStyle = shortMonth ? DateFormat.MEDIUM : DateFormat.LONG;
            DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            Date date = dateFormat.parse(defaultDate);

            if (date != null)
                return DateFormat.getDateInstance(dateStyle, Locale.getDefault()).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return defaultDate;
    }
}
