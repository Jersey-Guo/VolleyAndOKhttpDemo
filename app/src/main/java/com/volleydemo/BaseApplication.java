package com.volleydemo;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by guojiadong
 * on 2016/12/14.
 */

public class BaseApplication extends Application {
    public static RequestQueue requestQueue;
    public BaseApplication app;
    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        requestQueue = Volley.newRequestQueue(this);
    }


}
