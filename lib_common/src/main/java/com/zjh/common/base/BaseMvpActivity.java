package com.zjh.common.base;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zjh.common.mvp.IDelegateCallback;
import com.zjh.common.mvp.presenter.IBasePresenter;
import com.zjh.common.mvp.view.IBaseView;

public abstract class BaseMvpActivity<V extends IBaseView, P extends IBasePresenter<V>>
        extends AppCompatActivity implements IBaseView, IDelegateCallback<V, P> {

    protected P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
        presenter.attachView((V) this);
    }

    @NonNull
    @Override
    public abstract P createPresenter();

    @Override
    public P getPresenter(){
        return presenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null){
            presenter.deleteView(false);
        }
    }
}
