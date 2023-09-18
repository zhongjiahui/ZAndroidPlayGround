package com.zjh.news.service;


import com.zjh.news.entity.NewsEntity;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


public interface NewsService {

    /**
     *
     * @param type  支持类型
     *              top(推荐,默认)
     *              guonei(国内)
     *              guoji(国际)
     *              yule(娱乐)
     *              tiyu(体育)
     *              junshi(军事)
     *              keji(科技)
     *              caijing(财经)
     *              youxi(游戏)
     *              qiche(汽车)
     *              jiankang(健康)
     * @param page  当前页数, 默认1, 最大50
     * @param page_size 每页返回条数, 默认30 , 最大30
     * @param is_filter 是否只返回有内容详情的新闻, 1:是, 默认0
     */
    @GET("toutiao/index?key=37fd1d08d3bb92e901f85330eb38c8cd")
    Call<NewsEntity> getNewList(@Query("type") String type, @Query("page") int page, @Query("page_size") int page_size, @Query("is_filter") int is_filter);

    @GET("toutiao/index?key=37fd1d08d3bb92e901f85330eb38c8cd")
    Call<NewsEntity> getNewList(@QueryMap Map<String, String> options);

    @POST("toutiao/index")
    Call<NewsEntity> postNewList(@Query("key") String key, @Query("type") String type, @Query("page") int page, @Query("page_size") int page_size, @Query("is_filter") int is_filter);

}


