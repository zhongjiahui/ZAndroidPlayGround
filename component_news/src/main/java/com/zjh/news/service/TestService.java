package com.zjh.news.service;

import com.zjh.retrofit.annotation.Field;
import com.zjh.retrofit.annotation.GET;
import com.zjh.retrofit.annotation.POST;
import com.zjh.retrofit.annotation.Query;

import okhttp3.Call;

/**
 * @author Administrator
 * @date 2023/9/22
 * @description
 **/
public interface TestService {

    @POST("/v3/weather/weatherInfo")
    Call postWeather(@Field("city") String city, @Field("key") String key);

    @GET("/v3/weather/weatherInfo")
    Call getWeather(@Query("city") String city, @Query("key") String key);


}
