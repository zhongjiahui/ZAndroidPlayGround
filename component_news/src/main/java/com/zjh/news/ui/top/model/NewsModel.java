package com.zjh.news.ui.top.model;

import com.zjh.common.util.ZLog;
import com.zjh.http.IHttpCallback;
import com.zjh.news.entity.NewsEntity;
import com.zjh.news.service.NewsService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsModel implements INewsModel {

    @Override
    public void requestData(IHttpCallback<NewsEntity> callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://v.juhe.cn/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewsService newService = retrofit.create(NewsService.class);
        Call<NewsEntity> call = newService.getNewList("", 1, 30, 0);
        call.enqueue(new Callback<NewsEntity>() {
            @Override
            public void onResponse(Call<NewsEntity> call, Response<NewsEntity> response) {
                ZLog.d("onResponse: response = " + response);
                if (response.isSuccessful()){
                    NewsEntity body = response.body();
                    callback.onSuccess(body);
                }else {
                    callback.onFiler();
                }
            }

            @Override
            public void onFailure(Call<NewsEntity> call, Throwable t) {
                ZLog.e("onFailure: error = " + t.getMessage());
                t.printStackTrace();
                callback.onFiler();
            }
        });

    }

}
