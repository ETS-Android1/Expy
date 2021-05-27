package com.xdev.expy.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    public static String addDay(String oldDate, int numberOfDays){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            Date date = dateFormat.parse(oldDate);
            if (date != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_MONTH, numberOfDays);
                return dateFormat.format(calendar.getTime());
            }
        } catch (ParseException e){
            e.printStackTrace();
        }
        return "-1";
    }

    public static int differenceOfDates(String newerDate, String olderDate){
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            Date finalDate = dateFormat.parse(newerDate);
            Date currentDate = dateFormat.parse(olderDate);
            if (finalDate != null && currentDate != null) {
                double difference = (double) ((finalDate.getTime()-currentDate.getTime())/100000);
                return (int) ((difference / (24*60*60*1000))*100000);
            }
        }catch (ParseException e){
            e.printStackTrace();
        }
        return -1;
    }

    public static int[] getArrayDate(String date){
        try {
            String[] stringArrayDate = date.split("/");
            int[] integerArrayDate = new int[3];
            for (int i = 0; i < 3; i++) integerArrayDate[i] = Integer.parseInt(stringArrayDate[i]);
            // Karena bulan di mulai dari 0, jadi dikurangi 1
            return new int[] {integerArrayDate[0], integerArrayDate[1]-1, integerArrayDate[2]};
        } catch (Exception e){
            e.printStackTrace();
        }
        return new int[] {1, 0, 1970};
    }
}
