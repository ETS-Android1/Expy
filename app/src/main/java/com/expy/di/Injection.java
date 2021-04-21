package com.expy.di;

import android.app.Application;

import com.expy.data.source.AuthRepository;

public class Injection {
    public static AuthRepository provideRepository(Application application){
        return AuthRepository.getInstance(application);
    }
}
