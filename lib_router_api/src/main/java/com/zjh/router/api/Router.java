package com.zjh.router.api;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;

import androidx.annotation.RequiresApi;

import com.zjh.router.annotation.bean.RouterBean;

public class Router {

    private static Router instance;

    private String path;
    private String group;

    private LruCache<String, RouterGroup> groupLruCache;
    private LruCache<String, RouterPath> pathLruCache;

    private final static String FILE_GROUP_NAME = "Router$$Group$$";

    private Router(){
        groupLruCache = new LruCache<>(200);
        pathLruCache = new LruCache<>(200);
    }

    public static Router getInstance() {
        if (instance == null){
            synchronized (Router.class){
                if (instance == null){
                    instance = new Router();
                }
            }
        }
        return instance;
    }

    public BundleManager build(String path){
        if (TextUtils.isEmpty(path) || !path.startsWith("/")){
            throw new IllegalArgumentException("path 格式不对");
        }

        if (path.lastIndexOf("/") == 0){
            throw new IllegalArgumentException("path 格式不对");
        }

        String finalGroup = path.substring(1, path.indexOf("/", 1));
        if (TextUtils.isEmpty(finalGroup)){
            throw new IllegalArgumentException("path 格式不对");
        }

        this.path = path;
        this.group = finalGroup;

        return new BundleManager();
    }

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
    public Object navigation(Context context, BundleManager bundleManager){
        //TODO
        //String groupClassName = context.getPackageName() + "." + FILE_GROUP_NAME + group;
        String groupClassName = "com.zjh.router.routes." + FILE_GROUP_NAME + "component_" + group;
        Log.e("Router", "navigation: groupName = " + groupClassName);

        try {
            RouterGroup routerGroup = groupLruCache.get(group);
            if (null == routerGroup){
                Class<?> aClass = Class.forName(groupClassName);
                routerGroup = (RouterGroup) aClass.newInstance();
                groupLruCache.put(group, routerGroup);
            }

            if (routerGroup.getGroupMap().isEmpty()){
                throw new RuntimeException("路由表 group 异常");
            }

            RouterPath routerPath = pathLruCache.get(path);
            if (null == routerPath){
                Class<? extends RouterPath> aClass = routerGroup.getGroupMap().get(group);
                routerPath = aClass.newInstance();
                pathLruCache.put(path, routerPath);
            }

            if (routerPath != null){
                if (routerPath.getPathMap().isEmpty()){
                    throw new RuntimeException("路由表 path 异常");
                }

                RouterBean routerBean = routerPath.getPathMap().get(path);
                if (routerBean != null){
                    switch (routerBean.getTypeEnum()){
                        case ACTIVITY:
                            Intent intent = new Intent(context, routerBean.getmClass());
                            intent.putExtras(bundleManager.getBundle());
                            context.startActivity(intent);
                            break;
                        case CALL:
                            Class<?> aClass = routerBean.getmClass();
                            Call call = (Call) aClass.newInstance();
                            bundleManager.setCall(call);
                            return bundleManager.getCall();
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
