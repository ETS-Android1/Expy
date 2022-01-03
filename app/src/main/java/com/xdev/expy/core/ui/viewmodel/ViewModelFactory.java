package com.xdev.expy.core.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.xdev.expy.core.data.AuthRepository;
import com.xdev.expy.core.data.ProductRepository;
import com.xdev.expy.core.di.Injection;
import com.xdev.expy.auth.AuthViewModel;
import com.xdev.expy.main.MainViewModel;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private static volatile ViewModelFactory INSTANCE;

    private final Application application;
    private final AuthRepository authRepository;
    private final ProductRepository productRepository;

    private ViewModelFactory(Application application, AuthRepository authRepository, ProductRepository productRepository) {
        this.application = application;
        this.authRepository = authRepository;
        this.productRepository = productRepository;
    }

    public static ViewModelFactory getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(application,
                            Injection.provideRepository(application),
                            Injection.provideRepository(application.getApplicationContext())
                    );
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
            return (T) new MainViewModel(application, authRepository, productRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
