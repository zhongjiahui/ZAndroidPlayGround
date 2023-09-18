package com.zjh.news.ui.top;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.zjh.common.base.BaseMvpFragment;
import com.zjh.common.dialog.LoadingDialog;
import com.zjh.common.util.DensityUtil;
import com.zjh.common.util.RecycleViewDivider;
import com.zjh.news.databinding.FragmentNewsTopBinding;
import com.zjh.news.entity.NewsEntity;
import com.zjh.news.ui.top.presenter.NewsPresenter;
import com.zjh.news.ui.top.view.INewsView;

public class TopNewsFragment extends BaseMvpFragment<INewsView, NewsPresenter> implements INewsView {

    private FragmentNewsTopBinding binding;
    private TopNewsAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public NewsPresenter createPresenter() {
        return new NewsPresenter();
    }

    @Override
    public void setPresenter(NewsPresenter presenter) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNewsTopBinding.inflate(inflater, container, false);
        binding.newsList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.newsList.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL,
                (int) DensityUtil.dp2px(getContext(), 1), getResources().getColor(com.zjh.common.R.color.common_divider_color)));
        adapter = new TopNewsAdapter();
        adapter.setListener((view, position) -> {

        });
        binding.newsList.setAdapter(adapter);
        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.requestData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void showLoading() {
        LoadingDialog.getInstance().startLoading(getContext(), binding.loading);
    }

    @Override
    public void hideLoading() {
        LoadingDialog.getInstance().stopLoading(binding.loading);
    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void onNewsRequestSuccess(NewsEntity data) {
        adapter.setData(data.getResult().getData());
    }

    @Override
    public void onNewsRequestFiler() {

    }
}
