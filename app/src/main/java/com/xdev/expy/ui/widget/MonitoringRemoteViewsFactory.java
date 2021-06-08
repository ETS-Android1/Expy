package com.xdev.expy.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.xdev.expy.R;
import com.xdev.expy.data.source.local.entity.ProductEntity;
import com.xdev.expy.data.source.local.room.ProductDao;
import com.xdev.expy.data.source.local.room.ProductDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.xdev.expy.utils.DateUtils.differenceOfDates;
import static com.xdev.expy.utils.DateUtils.getCurrentDate;

public class MonitoringRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context context;
    private final ProductDao productDao;

    private List<ProductEntity> productList = new ArrayList<>();

    public MonitoringRemoteViewsFactory(Context context) {
        this.context = context;
        ProductDatabase db = ProductDatabase.getInstance(context);
        productDao = db.productDao();
    }

    @Override
    public void onCreate() {}

    @Override
    public void onDataSetChanged() {
        final long identityToken = Binder.clearCallingIdentity();

        productList = productDao.getSoonToExpireProducts(getCurrentDate());

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {}

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION ||
                productList == null || productList.get(position) == null) {
            return null;
        }

        ProductEntity product = productList.get(position);

        long dte = differenceOfDates(product.getExpiryDate(), getCurrentDate());
        if (dte < 0) dte = 0;

        int background;
        if (dte <= 3) background = R.drawable.bg_countdown_red;
        else if (dte <= 7) background = R.drawable.bg_countdown_orange;
        else if (dte <= 30) background = R.drawable.bg_countdown_yellow;
        else background = R.drawable.bg_countdown_green;

        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_monitoring_item);
        rv.setTextViewText(R.id.tv_name, product.getName());
        rv.setTextViewText(R.id.tv_countdown, context.getResources().getQuantityString(
                        R.plurals.number_of_days_remaining_countdown, (int) dte, dte));
        rv.setInt(R.id.layout_countdown, "setBackgroundResource", background);

        Intent fillInIntent = new Intent();
        fillInIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        rv.setOnClickFillInIntent(R.id.widget_item_container, fillInIntent);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
