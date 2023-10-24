package com.zjh.common.base;

import android.app.Application;
import android.content.Context;

public class BaseApplication extends Application {

    private static Application mApplication;
    private static Context mApplicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        mApplicationContext = this.getApplicationContext();
    }

    public static Application getApplication(){
        return mApplication;
    }

    public static Context getAppContext(){
        return mApplicationContext;
    }
}
