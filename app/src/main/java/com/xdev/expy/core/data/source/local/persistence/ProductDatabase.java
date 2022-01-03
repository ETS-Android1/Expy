package com.xdev.expy.core.data.source.local.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.xdev.expy.core.data.source.local.entity.ProductEntity;
import com.xdev.expy.core.data.source.local.entity.ReminderEntity;
import com.xdev.expy.core.util.Converters;

@Database(entities = {ProductEntity.class, ReminderEntity.class},
        version = 1,
        exportSchema = false)
@TypeConverters(Converters.class)
public abstract class ProductDatabase extends RoomDatabase {

    public abstract ProductDao productDao();

    private static volatile ProductDatabase INSTANCE;

    public static ProductDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ProductDatabase.class) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        ProductDatabase.class, "Products.db")
                        .build();
            }
        }
        return INSTANCE;
    }
}
