package com.zjh.news.ui.top.view;

import com.zjh.common.mvp.view.IBaseView;
import com.zjh.news.entity.NewsEntity;

public interface INewsView extends IBaseView {

    void onNewsRequestSuccess(NewsEntity data);

    void onNewsRequestFiler();


}
