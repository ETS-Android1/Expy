package com.xdev.expy.data.source.remote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.Contract;

import static com.xdev.expy.data.source.remote.StatusResponse.EMPTY;
import static com.xdev.expy.data.source.remote.StatusResponse.ERROR;
import static com.xdev.expy.data.source.remote.StatusResponse.LOADING;
import static com.xdev.expy.data.source.remote.StatusResponse.SUCCESS;

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
    @Contract(value = "_ -> new", pure = true)
    public static <T> ApiResponse<T> loading(String msg) {
        return new ApiResponse<>(LOADING, null, msg);
    }

    @NonNull
    @Contract(value = "_ -> new", pure = true)
    public static <T> ApiResponse<T> success(@Nullable T body) {
        return new ApiResponse<>(SUCCESS, body, null);
    }

    @NonNull
    @Contract(value = "_, _ -> new", pure = true)
    public static <T> ApiResponse<T> empty(String msg, @Nullable T body) {
        return new ApiResponse<>(EMPTY, body, msg);
    }

    @NonNull
    @Contract(value = "_, _ -> new", pure = true)
    public static <T> ApiResponse<T> error(String msg, @Nullable T body) {
        return new ApiResponse<>(ERROR, body, msg);
    }
}