package com.example.popularmovies.network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Resource<T> {
    @NonNull
    public final Status status;
    @Nullable
    public final T data;

    private Resource(@NonNull Status status, @Nullable T data) {
        this.status = status;
        this.data = data;
    }

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(Status.SUCCESS, data);
    }

    public static <T> Resource<T> error(@Nullable T data) {
        return new Resource<>(Status.ERROR, data);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(Status.LOADING, data);
    }

    public enum Status { SUCCESS, ERROR, LOADING }
}
