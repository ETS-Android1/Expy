package com.xdev.expy.ui.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class MonitoringWidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MonitoringRemoteViewsFactory(this.getApplicationContext());
    }
}
