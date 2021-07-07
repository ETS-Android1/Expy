package com.xdev.expy.utils;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static final String DATE_FORMAT = "yyyy/MM/dd";

    @NonNull
    public static String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getFormattedDate(String simpleFormattedDate, boolean shortMonth) {
        try {
            int dateStyle = shortMonth ? DateFormat.MEDIUM : DateFormat.LONG;
            DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            Date date = dateFormat.parse(simpleFormattedDate);

            if (date != null)
                return DateFormat.getDateInstance(dateStyle, Locale.getDefault()).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return simpleFormattedDate;
    }

    @NonNull
    public static String addDay(String oldDate, int numberOfDays) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            Date date = dateFormat.parse(oldDate);
            if (date != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_MONTH, numberOfDays);
                return dateFormat.format(calendar.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "N/A";
    }

    public static long differenceOfDates(String newerDate, String olderDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            Date finalDate = dateFormat.parse(newerDate);
            Date currentDate = dateFormat.parse(olderDate);
            if (finalDate != null && currentDate != null) {
                double difference = finalDate.getTime() - currentDate.getTime();
                return (long) (difference / (24 * 60 * 60 * 1000));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
