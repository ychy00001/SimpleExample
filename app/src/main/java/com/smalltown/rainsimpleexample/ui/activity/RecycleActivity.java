package com.smalltown.rainsimpleexample.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import butterknife.Bind;
import com.smalltown.rainsimpleexample.R;
import com.smalltown.rainsimpleexample.ui.adapter.RecycleAdapter;
import com.smalltown.rainsimpleexample.ui.base.BaseActivity;
import com.smalltown.rainsimpleexample.ui.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diagrams on 2015/12/22 17:40
 */
public class RecycleActivity extends BaseActivity {

    @Bind(R.id.rcv_recycleView)
    RecyclerView mRecyclerView;
    private List<String> mDatas;
    private RecycleAdapter mAdapter;

    @Override
    protected int initView() {
        return R.layout.activity_recycle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initRecycleView();
    }

    private void initRecycleView() {
        //设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置adapter
        mAdapter = new RecycleAdapter(this,mDatas);
        //        //设置Item增加、移除动画
        //        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        System.out.println("adapter:"+mAdapter);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, OrientationHelper.VERTICAL));
    }

    /**
     * 初始化使用数据
     */
    public void initData() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            mDatas.add("条目" + i);
        }
    }
}
