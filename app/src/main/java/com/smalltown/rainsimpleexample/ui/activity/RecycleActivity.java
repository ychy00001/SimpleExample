package com.smalltown.rainsimpleexample.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import butterknife.Bind;
import com.smalltown.rainsimpleexample.R;
import com.smalltown.rainsimpleexample.ui.adapter.RecycleAdapter;
import com.smalltown.rainsimpleexample.ui.base.BaseActivity;
import com.smalltown.rainsimpleexample.ui.view.DividerItemDecoration;
import com.smalltown.rainsimpleexample.widget.LoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diagrams on 2015/12/22 17:40
 */
public class RecycleActivity extends BaseActivity {

    @Bind(R.id.rcv_recycleView)
    LoadMoreRecyclerView mRecyclerView;
    private List<String> mDatas;
    private RecycleAdapter mAdapter;

    @Override
    protected int setContentLayout() {
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
        mRecyclerView.setAutoLoadMoreEnable(true);
        //添加分割线
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, OrientationHelper.VERTICAL));

        //设置自动加载更多
        mRecyclerView.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                //执行加载更多 请求网络数据
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for(int i=0;i<5;i++){
                            mDatas.add("加载更多啦啦");
                        }
                        mAdapter.setLoadMoreData(mDatas);
                        if(mDatas.size() > 120){
                            mRecyclerView.notifyMoreFinishWithNoMoreData(false);
                        }else{
                            mRecyclerView.notifyMoreFinish(true);
                        }

                    }
                },2000);
            }
        });
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
