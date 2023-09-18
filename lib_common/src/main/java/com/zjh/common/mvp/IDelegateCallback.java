package com.zjh.common.mvp;

import androidx.annotation.NonNull;

import com.zjh.common.mvp.presenter.IBasePresenter;
import com.zjh.common.mvp.view.IBaseView;

public interface IDelegateCallback <V extends IBaseView, P extends IBasePresenter<V>>{

    @NonNull
    P createPresenter();

    P getPresenter();

    void setPresenter(P presenter);

}
