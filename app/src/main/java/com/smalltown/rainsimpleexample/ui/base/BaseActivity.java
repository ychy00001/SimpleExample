package com.smalltown.rainsimpleexample.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import butterknife.ButterKnife;

/**
 * Created by Diagrams on 2015/12/22 17:59
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initView());
        ButterKnife.bind(this);//绑定ButterKnife
    }

    /**
     * 初始化UI
     * @return 需要返回加载界面的id
     */
    protected abstract int initView();

}
