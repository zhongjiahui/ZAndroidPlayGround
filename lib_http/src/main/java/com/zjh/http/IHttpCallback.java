package com.zjh.http;

public interface IHttpCallback<T> {

    void onSuccess(T data);

    void onFiler();
}
