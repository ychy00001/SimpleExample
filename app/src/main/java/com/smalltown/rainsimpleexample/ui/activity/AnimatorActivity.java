package com.smalltown.rainsimpleexample.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Bind;
import com.smalltown.rainsimpleexample.R;
import com.smalltown.rainsimpleexample.ui.adapter.AnimatorAdapter;
import com.smalltown.rainsimpleexample.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diagrams on 2015/12/30 17:58
 */
public class AnimatorActivity extends BaseActivity {

    @Bind(R.id.rlv_animators) RecyclerView mRecycleView;

    private List<String> datas;

    @Override
    protected int initView() {
        return R.layout.activity_animator;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        AnimatorAdapter adapter = new AnimatorAdapter(this, datas);
        adapter.setOnItemClickLitener(new MyClickListener());
        //设置布局管理器
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setAdapter(adapter);
    }

    private void initData() {
        datas = new ArrayList<>();
        datas.add("飞行动画");
    }

    private class MyClickListener implements AnimatorAdapter.OnItemClickLitener{

        @Override
        public void onItemClick(View view, int position) {
            switch (position){
                case 0:
                    clickFlyItem();
                    break;
            }
        }
    }

    private void clickFlyItem() {
        Intent intent = new Intent(AnimatorActivity.this,FlyActivity.class);
        startActivity(intent);
    }
}
