package com.xdev.expy.data.source.local;

import androidx.paging.DataSource;

import com.xdev.expy.data.source.local.entity.ProductEntity;
import com.xdev.expy.data.source.local.entity.ProductWithReminders;
import com.xdev.expy.data.source.local.entity.ReminderEntity;
import com.xdev.expy.data.source.local.room.ProductDao;

import java.util.List;

import static com.xdev.expy.utils.DateUtils.getCurrentDate;

public class LocalDataSource {

    private static LocalDataSource INSTANCE;
    private final ProductDao productDao;

    private LocalDataSource(ProductDao productDao) {
        this.productDao = productDao;
    }

    public static LocalDataSource getInstance(ProductDao productDao) {
        if (INSTANCE == null) {
            INSTANCE = new LocalDataSource(productDao);
        }
        return INSTANCE;
    }

    public DataSource.Factory<Integer, ProductWithReminders> getProducts(boolean isExpired) {
        if (isExpired) return productDao.getExpiredProducts(getCurrentDate());
        else return productDao.getMonitoredProducts(getCurrentDate());
    }

    public void insertProductsAndReminders(List<ProductEntity> productList, List<ReminderEntity> reminderList) {
        productDao.insertProductsAndReminders(productList, reminderList);
    }
}
