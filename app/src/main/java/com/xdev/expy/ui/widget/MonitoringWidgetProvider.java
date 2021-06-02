package com.xdev.expy.ui.widget;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.xdev.expy.R;
import com.xdev.expy.ui.main.MainActivity;

public class MonitoringWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Intent intent = new Intent(context, MonitoringWidgetRemoteViewsService.class);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_monitoring);
        views.setRemoteAdapter(R.id.list_view, intent);

        // Click event on single views
        Intent titleIntent = new Intent(context, MainActivity.class);
        titleIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent titlePendingIntent = PendingIntent.getActivity(context, 0, titleIntent, 0);
        views.setOnClickPendingIntent(R.id.tv_title, titlePendingIntent);

        // Click events on ListView items
        Intent clickIntentTemplate = new Intent(context, MainActivity.class);
        PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(clickIntentTemplate)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.list_view, clickPendingIntentTemplate);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        final String action = intent.getAction();
        if (action != null){
            if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
                AppWidgetManager manager = AppWidgetManager.getInstance(context);
                ComponentName cn = new ComponentName(context, MonitoringWidgetProvider.class);
                manager.notifyAppWidgetViewDataChanged(manager.getAppWidgetIds(cn), R.id.list_view);
            }
        }
    }

    // Updating the widget manually
    public static void sendRefreshBroadcast(Context context) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, MonitoringWidgetProvider.class));
        context.sendBroadcast(intent);
    }
}