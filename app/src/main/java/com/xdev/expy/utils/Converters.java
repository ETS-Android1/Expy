package com.xdev.expy.utils;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

import com.google.firebase.Timestamp;

import org.jetbrains.annotations.Contract;

import java.util.Date;

public class Converters {

    @NonNull
    @TypeConverter
    public static Long fromTimestamp(Timestamp timestamp) {
        return timestamp == null ? 0 : timestamp.toDate().getTime();
    }

    @NonNull
    @Contract("_ -> new")
    @TypeConverter
    public static Timestamp toTimestamp(Long value) {
        return value == null ? new Timestamp(0, 0) : new Timestamp(new Date(value));
    }
}
