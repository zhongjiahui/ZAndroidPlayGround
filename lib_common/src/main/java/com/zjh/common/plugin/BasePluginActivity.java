package com.zjh.common.plugin;

import android.app.Activity;
import android.content.res.Resources;

public class BasePluginActivity extends Activity {

    @Override
    public Resources getResources() {
        if (getApplication() != null && getApplication().getResources() != null){
            return getApplication().getResources();
        }
        return super.getResources();
    }
}
