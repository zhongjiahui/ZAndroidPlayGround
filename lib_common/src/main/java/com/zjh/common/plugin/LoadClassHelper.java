package com.zjh.common.plugin;

import android.content.Context;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class LoadClassHelper {

    public static void loadClass(Context context, String apkPath){
        try {
            // 获取 pathList 字段
            Class baseDexClassLoader = Class.forName("dalvik.system.BaseDexClassLoader");
            Field pathList = baseDexClassLoader.getDeclaredField("pathList");
            pathList.setAccessible(true);

            // 获取 dexElements 字段
            Class<?> dexPathListClass = Class.forName("dalvik.system.DexPathList");
            Field dexElements = dexPathListClass.getDeclaredField("dexElements");
            dexElements.setAccessible(true);

            //获取插件的 dexElements[]
            DexClassLoader dexClassLoader = new DexClassLoader(apkPath,
                    context.getCacheDir().getAbsolutePath(), null, context.getClassLoader());
            Object pluginPathList = pathList.get(dexClassLoader);
            Object[] pluginDexElements = (Object[]) dexElements.get(pluginPathList);

            //获取宿主的 dexElements[]
            PathClassLoader pathClassLoader = (PathClassLoader) context.getClassLoader();
            Object hostPathList= pathList.get(pathClassLoader);
            Object[] hostDexElements = (Object[]) dexElements.get(hostPathList);

            //将插件的 dexElements[] 和宿主的 dexElements[] 合并为一个新的 dexElements[]
            Object[] newDexElements = (Object[]) Array.newInstance(hostDexElements.getClass().getComponentType(),
                    pluginDexElements.length + hostDexElements.length);
            System.arraycopy(hostDexElements, 0, newDexElements, 0, hostDexElements.length);
            System.arraycopy(pluginDexElements, 0, newDexElements, hostDexElements.length, pluginDexElements.length);

            dexElements.set(hostPathList, newDexElements);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
