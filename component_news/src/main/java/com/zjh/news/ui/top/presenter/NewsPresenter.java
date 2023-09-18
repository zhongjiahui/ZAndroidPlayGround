package com.zjh.news.ui.top.presenter;

import com.zjh.common.mvp.presenter.BasePresenter;
import com.zjh.http.IHttpCallback;
import com.zjh.news.entity.NewsEntity;
import com.zjh.news.ui.top.model.NewsModel;
import com.zjh.news.ui.top.view.INewsView;

public class NewsPresenter extends BasePresenter<INewsView> {

    private final NewsModel newModel;

    public NewsPresenter() {
        this.newModel = new NewsModel();
    }

    public void requestData() {
        getView().showLoading();
        newModel.requestData(new IHttpCallback<NewsEntity>() {
            @Override
            public void onSuccess(NewsEntity data) {
                getView().hideLoading();
                getView().onNewsRequestSuccess(data);
            }

            @Override
            public void onFiler() {
                getView().hideLoading();
                getView().onNewsRequestFiler();
            }
        });
    }


}
