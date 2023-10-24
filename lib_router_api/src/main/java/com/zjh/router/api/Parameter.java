package com.zjh.router.api;

import android.app.Activity;
import android.util.LruCache;

public class Parameter {

    private static Parameter instance;

    private LruCache<String, IParameter> cache;
    private final static String FILE_SUFFIX_NAME = "$$Parameter";

    private Parameter(){
        cache = new LruCache<>(200);
    }

    public static Parameter getInstance() {
        if (instance == null){
            synchronized (Parameter.class){
                if (instance == null){
                    instance = new Parameter();
                }
            }
        }
        return instance;
    }

    public void loadParameter(Activity activity){
        String className = activity.getClass().getName();

        IParameter iParameter = cache.get(className);
        if (null == iParameter){
            try {
                Class<?> aClass = Class.forName(className + FILE_SUFFIX_NAME);
                iParameter = (IParameter) aClass.newInstance();
                cache.put(className, iParameter);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        iParameter.getParameter(activity);
    }
}
