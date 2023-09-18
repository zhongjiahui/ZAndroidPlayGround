package com.zjh.common.dialog;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.content.res.AppCompatResources;

import com.zjh.common.R;

public class LoadingDialog {

    protected AnimatedVectorDrawable loadingDrawable;
    private LoadingDialog() {

    }

    private static final class AppInstanceHolder {
        static final LoadingDialog mInstance = new LoadingDialog();
    }

    public static LoadingDialog getInstance() {
        return AppInstanceHolder.mInstance;
    }

    public void startLoading(Context context, ImageView imageView) {
        if (loadingDrawable == null && context!= null) {
            loadingDrawable = (AnimatedVectorDrawable) AppCompatResources.getDrawable(context, R.drawable.ic_animated_loading_blue);
            imageView.setImageDrawable(loadingDrawable);
        }
        imageView.setVisibility(View.VISIBLE);
        loadingDrawable.start();
    }

    public void stopLoading(ImageView imageView) {
        if (loadingDrawable != null && loadingDrawable.isRunning()) {
            loadingDrawable.stop();
        }
        imageView.setVisibility(View.GONE);
    }

}
