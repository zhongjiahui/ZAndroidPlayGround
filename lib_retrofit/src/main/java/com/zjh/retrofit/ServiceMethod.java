package com.zjh.retrofit;

import com.zjh.retrofit.annotation.Field;
import com.zjh.retrofit.annotation.GET;
import com.zjh.retrofit.annotation.POST;
import com.zjh.retrofit.annotation.Query;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * @author Administrator
 * @date 2023/9/22
 * @description
 **/
public class ServiceMethod {

    private final Call.Factory factory;
    private final HttpUrl baseUrl;
    private final ParameterHandler[] parameterHandler;
    private final String httpMethod;
    private final String relativeUrl;
    private boolean hasBody;
    private FormBody.Builder formBuild;
    private HttpUrl.Builder urlBuilder;

    public ServiceMethod(Builder builder) {
        this.factory = builder.retrofit.callFactory;
        this.baseUrl = builder.retrofit.baseUrl;

        this.parameterHandler = builder.parameterHandler;
        this.httpMethod = builder.httpMethod;
        this.relativeUrl = builder.relativeUrl;
        this.hasBody = builder.hasBody;

        if (hasBody){
            this.formBuild = new FormBody.Builder();
        }
    }

    public Object invoke(Object[] args) {
        for(int i = 0; i < parameterHandler.length; i++){
            ParameterHandler handler = parameterHandler[i];
            handler.apply(this, args[i].toString());
        }

        HttpUrl url = null;
        if (urlBuilder == null){
            urlBuilder = baseUrl.newBuilder(relativeUrl);
        }
        url = urlBuilder.build();

        FormBody formBody = null;
        if(formBuild != null){
            formBody = formBuild.build();
        }

        Request request = new Request.Builder().url(url).method(httpMethod, formBody).build();
        return factory.newCall(request);
    }

    public void addQueryParameter(String key, String value) {
        if (urlBuilder == null){
            urlBuilder = new HttpUrl.Builder();
        }
        urlBuilder.addQueryParameter(key, value);
    }

    public void addFieldParameter(String key, String value) {
        formBuild.add(key, value);
    }

    public static class Builder {

        private final Retrofit retrofit;
        private Annotation[] annotations;
        private Annotation[][] parameterAnnotations;
        private ParameterHandler[] parameterHandler;
        private String httpMethod;
        private String relativeUrl;
        private boolean hasBody;


        public Builder(Retrofit retrofit, Method method) {
            this.retrofit = retrofit;

            annotations = method.getAnnotations();
            parameterAnnotations = method.getParameterAnnotations();
        }

        public ServiceMethod build() {
            for(Annotation annotation : annotations){
                if (annotation instanceof POST){
                    httpMethod = "POST";
                    relativeUrl =((POST) annotation).value();
                    hasBody = true;
                } else if (annotation instanceof GET){
                    httpMethod = "GET";
                    relativeUrl = ((GET) annotation).value();
                    hasBody = false;
                }
            }

            int length = parameterAnnotations.length;
            parameterHandler = new ParameterHandler[length];
            for(int i = 0; i < length; i++){
                Annotation[] annotations1 = parameterAnnotations[i];
                for (Annotation annotation : annotations1){
                    if(annotation instanceof Field){
                        String value = ((Field) annotation).value();
                        parameterHandler[i] = new ParameterHandler.FieldParameterHandler(value);
                    } else if (annotation instanceof Query){
                        String value = ((Query) annotation).value();
                        parameterHandler[i] = new ParameterHandler.QueryParameterHandler(value);
                    }
                }
            }

            return new ServiceMethod(this);
        }
    }
}
