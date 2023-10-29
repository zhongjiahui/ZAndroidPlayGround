package com.zjh.playground;

import android.app.Application;
import android.content.res.Resources;

import com.alibaba.android.arouter.launcher.ARouter;
import com.zjh.common.plugin.LoadResourcesHelper;

public class AppApplication extends Application {

    private Resources resources;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {   // These two lines must be written before init, otherwise these configurations will be invalid in the init process
            ARouter.openLog();     // Print log
            ARouter.openDebug();   // Turn on debugging mode (If you are running in InstantRun mode, you must turn on debug mode! Online version needs to be closed, otherwise there is a security risk)
        }
        ARouter.init(this); // As early as possible, it is recommended to initialize in the Application

        resources = LoadResourcesHelper.loadResource(this, "");
    }

    @Override
    public Resources getResources() {
        return resources == null ? super.getResources() : resources;
    }
}
