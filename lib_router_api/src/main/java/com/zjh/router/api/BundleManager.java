package com.zjh.router.api;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import java.io.Serializable;

public class BundleManager {

    private Bundle bundle = new Bundle();
    private Call call;

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public Call getCall() {
        return call;
    }

    public void setCall(Call call) {
        this.call = call;
    }

    public BundleManager withString(String key, String value){
        bundle.putString(key, value);
        return this;
    }

    public BundleManager withInt(String key, int value){
        bundle.putInt(key, value);
        return this;
    }

    public BundleManager withBoolean(String key, boolean value){
        bundle.putBoolean(key, value);
        return this;
    }

    public BundleManager withSerializable(String key, Serializable value){
        bundle.putSerializable(key, value);
        return this;
    }


    public Object navigation(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            return Router.getInstance().navigation(context, this);
        }
        return null;
    }

}
