package com.xdev.expy.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import static com.xdev.expy.reminder.ReminderHelper.showNotification;

public class ReminderReceiver extends BroadcastReceiver {

    private final String TAG = getClass().getSimpleName();

    private static final String EXTRA_NOTIFICATION_ID = "extra_notification_id";
    private static final String EXTRA_TITLE = "extra_title";
    private static final String EXTRA_MESSAGE = "extra_message";

    @Override
    public void onReceive(Context context, Intent intent) {
        String channelId = "channel_reminder";
        String channelName= "Reminder";
        int notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, 0);
        String title = intent.getStringExtra(EXTRA_TITLE);
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        showNotification(context, channelId, channelName, notificationId, title, message);
    }

    public void setReminder(Context context, int notificationId, String title, String message, Date date) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra(EXTRA_NOTIFICATION_ID, notificationId);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_MESSAGE, message);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        if (alarmManager != null){
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Log.d(TAG, "Reminder set up: " + notificationId + " on " +
                    calendar.get(Calendar.YEAR) + "/" +
                    (calendar.get(Calendar.MONTH)+1) + "/" +
                    calendar.get(Calendar.DAY_OF_MONTH) + " " +
                    calendar.get(Calendar.HOUR_OF_DAY) + ":" +
                    calendar.get(Calendar.MINUTE) + ":" +
                    calendar.get(Calendar.SECOND));
        }
    }

    public void cancelReminder(Context context, int notificationId){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent, 0);
        pendingIntent.cancel();
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            Log.d(TAG, "Reminder canceled: " + notificationId);
        }
    }
}