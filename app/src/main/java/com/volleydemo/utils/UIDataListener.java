package com.volleydemo.utils;


/**
 * Created by guojiadong
 * on 2016/12/14.
 */

public interface UIDataListener<T>  {
    void onDataChanged(T result);

    void onErrorHappend(String errorMsg);
}
