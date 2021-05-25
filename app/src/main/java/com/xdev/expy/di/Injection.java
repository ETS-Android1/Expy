package com.xdev.expy.di;

import android.app.Application;

import com.xdev.expy.data.AuthRepository;
import com.xdev.expy.data.MainRepository;
import com.xdev.expy.data.source.remote.RemoteDataSource;

public class Injection {

    public static AuthRepository provideRepository(Application application){
        return AuthRepository.getInstance(application);
    }

    public static MainRepository provideRepository(){
        return MainRepository.getInstance(RemoteDataSource.getInstance());
    }
}
