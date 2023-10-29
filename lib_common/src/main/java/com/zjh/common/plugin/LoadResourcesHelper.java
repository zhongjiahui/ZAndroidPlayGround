package com.zjh.common.plugin;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.lang.reflect.Method;

public class LoadResourcesHelper {

    public static Resources loadResource(Context context, String apkPath){
        try {
            Class<AssetManager> assetManagerClass = AssetManager.class;
            AssetManager assetManager = assetManagerClass.newInstance();
            Method addAssetPath = assetManagerClass.getDeclaredMethod("addAssetPath", String.class);
            addAssetPath.setAccessible(true);
            addAssetPath.invoke(assetManager, apkPath);
            Resources resources = context.getResources();
            return new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
