package com.xdev.expy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.xdev.expy.data.source.AuthRepository;
import com.xdev.expy.di.Injection;
import com.xdev.expy.ui.auth.AuthViewModel;
import com.xdev.expy.ui.main.MainViewModel;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private static volatile ViewModelFactory INSTANCE;

    private final Application application;
    private final AuthRepository authRepository;

    private ViewModelFactory(Application application, AuthRepository authRepository){
        this.application = application;
        this.authRepository = authRepository;
    }

    public static ViewModelFactory getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(application, Injection.provideRepository(application));
                }
            }
        }
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AuthViewModel.class)) {
            return (T) new AuthViewModel(application, authRepository);
        } else if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(application, authRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
