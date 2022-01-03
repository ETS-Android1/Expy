package com.xdev.expy.core.di;

import android.app.Application;
import android.content.Context;

import com.xdev.expy.core.util.AppExecutors;
import com.xdev.expy.core.data.AuthRepository;
import com.xdev.expy.core.data.ProductRepository;
import com.xdev.expy.core.data.source.local.LocalDataSource;
import com.xdev.expy.core.data.source.local.persistence.ProductDatabase;
import com.xdev.expy.core.data.source.remote.RemoteDataSource;

public class Injection {

    public static AuthRepository provideRepository(Application application) {
        return AuthRepository.getInstance(application);
    }

    public static ProductRepository provideRepository(Context context) {
        ProductDatabase database = ProductDatabase.getInstance(context);
        RemoteDataSource remoteDataSource = RemoteDataSource.getInstance();
        LocalDataSource localDataSource = LocalDataSource.getInstance(database.productDao());
        AppExecutors appExecutors = new AppExecutors();
        return ProductRepository.getInstance(remoteDataSource, localDataSource, appExecutors);
    }
}
