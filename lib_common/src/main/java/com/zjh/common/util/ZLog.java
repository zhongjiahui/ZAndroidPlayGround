package com.zjh.common.util;

import android.util.Log;

public class ZLog {

    public static final String TAG = "ZLog";

    public static void d (String msg){
        Log.d(TAG, msg);
    }

    public static void e (String msg){
        Log.e(TAG, msg);
    }

    public static void i (String msg){
        Log.i(TAG, msg);
    }

    public static void w (String msg){
        Log.w(TAG, msg);
    }
}
