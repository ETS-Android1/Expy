package com.xdev.expy.core.data;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.xdev.expy.core.data.source.remote.ApiResponse;
import com.xdev.expy.core.data.source.remote.RemoteDataSource;

public class ProfileRepository implements ProfileDataSource {

    private volatile static ProfileRepository INSTANCE = null;
    private final RemoteDataSource remoteDataSource;

    private ProfileRepository(@NonNull RemoteDataSource remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
    }

    public static ProfileRepository getInstance(RemoteDataSource remoteData) {
        if (INSTANCE == null) {
            synchronized (ProfileRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ProfileRepository(remoteData);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public LiveData<ApiResponse<String>> uploadImage(Context context, Uri uriPath, String storagePath, String fileName) {
        return remoteDataSource.uploadImage(context, uriPath, storagePath, fileName);
    }
}