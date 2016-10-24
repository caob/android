package com.ergo.poc.data.api;

/*
 * Created by mariano on 10/21/16.
 */
public interface ResultListener<T> {
    void loading();
    void finish(T result);
    void error(Throwable error);
}

