package com.xdev.expy.data.source.local.room;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.xdev.expy.data.source.local.entity.ProductEntity;
import com.xdev.expy.data.source.local.entity.ProductWithReminders;
import com.xdev.expy.data.source.local.entity.ReminderEntity;

import java.util.List;

@Dao
public abstract class ProductDao {

    @Transaction
    @Query("SELECT * FROM productEntities WHERE expiryDate > :currentDate ORDER BY expiryDate ASC")
    public abstract DataSource.Factory<Integer, ProductWithReminders> getMonitoredProducts(String currentDate);

    @Transaction
    @Query("SELECT * FROM productEntities WHERE expiryDate <= :currentDate ORDER BY expiryDate DESC")
    public abstract DataSource.Factory<Integer, ProductWithReminders> getExpiredProducts(String currentDate);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertProducts(List<ProductEntity> productList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertReminders(List<ReminderEntity> reminderList);

    @Query("DELETE FROM productEntities")
    abstract void deleteProducts();

    @Transaction
    public void insertProductsAndReminders(List<ProductEntity> productList, List<ReminderEntity> reminderList){
        deleteProducts();
        insertProducts(productList);
        insertReminders(reminderList);
    }
}
