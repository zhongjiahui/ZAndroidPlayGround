package com.zjh.common.mvp.presenter;

import com.zjh.common.mvp.view.IBaseView;

public interface IBasePresenter<V extends IBaseView> {

    void attachView(V view);

    void deleteView(boolean retainInstance);

}
