package com.xdev.expy.core.data.source.remote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ApiResponse<T> {

    @NonNull
    public final StatusResponse status;

    @Nullable
    public final String message;

    @Nullable
    public final T body;

    public ApiResponse(@NonNull StatusResponse status, @Nullable T body, @Nullable String message) {
        this.status = status;
        this.body = body;
        this.message = message;
    }

    @NonNull
    public static <T> ApiResponse<T> loading(String msg) {
        return new ApiResponse<>(StatusResponse.LOADING, null, msg);
    }

    @NonNull
    public static <T> ApiResponse<T> success(@Nullable T body) {
        return new ApiResponse<>(StatusResponse.SUCCESS, body, null);
    }

    @NonNull
    public static <T> ApiResponse<T> error(String msg, @Nullable T body) {
        return new ApiResponse<>(StatusResponse.ERROR, body, msg);
    }
}