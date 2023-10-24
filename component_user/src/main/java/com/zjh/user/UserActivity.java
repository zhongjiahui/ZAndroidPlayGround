package com.zjh.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.zjh.common.router.UserRouter;
import com.zjh.router.annotation.Parameter;
import com.zjh.router.annotation.Router;

@Router(path = UserRouter.USER)
public class UserActivity extends AppCompatActivity {

    @Parameter
    public String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
    }
}