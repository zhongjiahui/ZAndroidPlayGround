package com.zjh.news.ui.top.model;

import com.zjh.common.mvp.model.IBaseModel;
import com.zjh.http.IHttpCallback;
import com.zjh.news.entity.NewsEntity;

public interface INewsModel extends IBaseModel {

    void requestData(IHttpCallback<NewsEntity>  callback);
}
