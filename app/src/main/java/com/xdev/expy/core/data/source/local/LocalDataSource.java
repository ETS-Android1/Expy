package com.xdev.expy.core.data.source.local;

import androidx.paging.DataSource;

import com.xdev.expy.core.util.DateUtils;
import com.xdev.expy.core.data.source.local.entity.ProductEntity;
import com.xdev.expy.core.data.source.local.entity.ProductWithReminders;
import com.xdev.expy.core.data.source.local.entity.ReminderEntity;
import com.xdev.expy.core.data.source.local.persistence.ProductDao;

import java.util.List;

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
        if (isExpired) return productDao.getExpiredProducts(DateUtils.getCurrentDate());
        else return productDao.getMonitoredProducts(DateUtils.getCurrentDate());
    }

    public void insertProductsAndReminders(List<ProductEntity> productList, List<ReminderEntity> reminderList) {
        productDao.insertProductsAndReminders(productList, reminderList);
    }

    public void clearDatabase() {
        productDao.deleteProducts();
    }
}
