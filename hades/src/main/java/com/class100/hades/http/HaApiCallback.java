package com.class100.hades.http;

public interface HaApiCallback<T> {
    void onError(int code, String message);

    void onSuccess(T content);
}