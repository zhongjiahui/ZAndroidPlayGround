package com.zjh.playground;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.zjh.common.router.NewsRouter;
import com.zjh.common.router.UserRouter;
import com.zjh.common.util.DensityUtil;
import com.zjh.common.util.RecycleViewDivider;
import com.zjh.router.api.Router;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IMainNavigation {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL,
                (int) DensityUtil.dp2px(this, 1), getResources().getColor(com.zjh.common.R.color.common_divider_color)));
        MAdapter adapter = new MAdapter(getListData());
        adapter.setListener((view, position) -> {
            switch (position){
                case 0:
                    navigationToHome();
                    break;
                case 1:
                    navigationToNews();
                    break;
                case 2:
                    navigationToUser();
                    break;
                default:
                    break;
            }
        });
        recyclerView.setAdapter(adapter);
    }

    public ArrayList<String> getListData(){
        ArrayList<String> data = new ArrayList<>();
        data.add("Home");
        data.add("News");
        data.add("User");
        return data;
    }

    @Override
    public void navigationToHome() {

    }

    @Override
    public void navigationToNews() {
        ARouter.getInstance().build(NewsRouter.NEWS).navigation();
    }

    @Override
    public void navigationToUser() {
        Router.getInstance().build(UserRouter.USER).navigation(this);
    }
}