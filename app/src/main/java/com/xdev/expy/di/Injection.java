package com.xdev.expy.di;

import android.app.Application;
import android.content.Context;

import com.xdev.expy.data.AuthRepository;
import com.xdev.expy.data.MainRepository;
import com.xdev.expy.data.source.local.LocalDataSource;
import com.xdev.expy.data.source.local.room.ProductDatabase;
import com.xdev.expy.data.source.remote.RemoteDataSource;
import com.xdev.expy.utils.AppExecutors;

public class Injection {

    public static AuthRepository provideRepository(Application application){
        return AuthRepository.getInstance(application);
    }

    public static MainRepository provideRepository(Context context){
        ProductDatabase database = ProductDatabase.getInstance(context);
        RemoteDataSource remoteDataSource = RemoteDataSource.getInstance();
        LocalDataSource localDataSource = LocalDataSource.getInstance(database.productDao());
        AppExecutors appExecutors = new AppExecutors();
        return MainRepository.getInstance(remoteDataSource, localDataSource, appExecutors);
    }
}
