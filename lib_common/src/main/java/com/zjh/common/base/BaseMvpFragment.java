package com.zjh.common.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zjh.common.mvp.IDelegateCallback;
import com.zjh.common.mvp.presenter.IBasePresenter;
import com.zjh.common.mvp.view.IBaseView;

public abstract class BaseMvpFragment<V extends IBaseView, P extends IBasePresenter<V>>
        extends Fragment implements IBaseView, IDelegateCallback<V, P> {

    protected P presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
        presenter.attachView((V)this);
    }

    @NonNull
    @Override
    public abstract P createPresenter();

    @Override
    public P getPresenter(){
        return presenter;
    }

    @Override
    public void setPresenter(P presenter) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null){
            presenter.deleteView(false);
        }
    }

}
