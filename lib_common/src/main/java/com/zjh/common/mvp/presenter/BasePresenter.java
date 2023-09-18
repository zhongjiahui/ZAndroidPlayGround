package com.zjh.common.mvp.presenter;

import com.zjh.common.mvp.view.IBaseView;

import java.lang.ref.WeakReference;

public class BasePresenter<V extends IBaseView> implements IBasePresenter<V> {

    private WeakReference<V> viewRef;

    public BasePresenter() {
    }

    @Override
    public void attachView(V view) {
        this.viewRef = new WeakReference<>(view);
    }

    @Override
    public void deleteView(boolean retainInstance) {
        if (this.viewRef.get() != null){
            viewRef.clear();
            viewRef = null;
        }
    }

    protected V getView(){
        return viewRef != null ? viewRef.get() : null;
    }
}
