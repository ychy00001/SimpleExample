package com.smalltown.rainsimpleexample.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.smalltown.rainsimpleexample.R;
import com.smalltown.rainsimpleexample.mode.SplashMode;
import com.smalltown.rainsimpleexample.presenter.SplashPresenter;
import com.smalltown.rainsimpleexample.ui.base.BaseActivity;
import com.smalltown.rainsimpleexample.ui.inter.ISplashView;

/**
 * Created by Diagrams on 2016/1/4 14:47
 */
public class SplashActivity extends BaseActivity implements ISplashView{

    private CircleProgressBar mProgressBar;
    private SplashPresenter mSplashPresenter;


    @Override
    protected int initView() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assignViews();
        mSplashPresenter = new SplashPresenter(this);
        mSplashPresenter.executeHttp();
    }

    private void assignViews() {
        mProgressBar = (CircleProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setColorSchemeResources(android.R.color.holo_green_light);
    }

    /**************implements ISplashView********************/

    @Override
    public void setSplashMode(SplashMode mode) {
        mProgressBar.setProgress(mode.progress);
        if(mode.progress == 100){
            Intent intent = new Intent(SplashActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /***********************生命周期处理****************************/

    @Override
    protected void onStart() {
        super.onStart();
        mSplashPresenter.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSplashPresenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSplashPresenter.onDestroy();
    }
}
