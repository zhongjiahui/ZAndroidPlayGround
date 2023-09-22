package com.zjh.retrofit;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * @author Administrator
 * @date 2023/9/22
 * @description
 **/
public class Retrofit {

    public final Map<Method, ServiceMethod> serviceMethodMap = new ConcurrentHashMap<>();

    public HttpUrl baseUrl;

    public Call.Factory callFactory;

    public Retrofit(HttpUrl baseUrl, Call.Factory callFactory) {
        this.baseUrl = baseUrl;
        this.callFactory = callFactory;
    }

    public <T> T create(Class<T> service){
        return (T)Proxy.newProxyInstance(service.getClassLoader(), new Class[]{service}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                ServiceMethod serviceMethod = loadServiceMethod(method);
                return serviceMethod.invoke(args);
            }
        });
    }

    private ServiceMethod loadServiceMethod(Method method) {
        ServiceMethod serviceMethod = serviceMethodMap.get(method);
        if(serviceMethod != null){
            return serviceMethod;
        }
        synchronized (serviceMethodMap){
            serviceMethod = serviceMethodMap.get(method);
             if (serviceMethod == null){
                 serviceMethod = new ServiceMethod.Builder(this, method).build();
                 serviceMethodMap.put(method, serviceMethod);
             }
        }
        return serviceMethod;
    }

    public static final class Builder{

        private HttpUrl baseUrl;

        private Call.Factory callFactory;

        public Builder callFactory(Call.Factory callFactory){
            this.callFactory = callFactory;
            return this;
        }

        public Builder baseUrl(String baseUrl){
            this.baseUrl = HttpUrl.get(baseUrl);
            return this;
        }

        public Retrofit build(){
            if(baseUrl == null){
                throw new IllegalStateException("Base URL required.");
            }
            Call.Factory factory = this.callFactory;
            if(factory == null){
                factory = new OkHttpClient();
            }
            return new Retrofit(baseUrl, factory);
        }
    }
}
