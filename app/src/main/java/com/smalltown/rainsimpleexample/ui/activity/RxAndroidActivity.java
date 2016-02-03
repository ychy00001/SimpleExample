package com.smalltown.rainsimpleexample.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.smalltown.rainsimpleexample.R;
import com.smalltown.rainsimpleexample.presenter.RxPresenter;
import com.smalltown.rainsimpleexample.ui.base.BaseActivity;
import com.smalltown.rainsimpleexample.ui.inter.RxView;

/**
 * RxAndroid的基本使用
 * Created by yangchunyu
 * 2016/2/3 10:12
 */
public class RxAndroidActivity extends BaseActivity implements RxView {

    private RxPresenter mPresent;
    private TextView textView;

    @Override
    protected int setContentLayout() {
        return R.layout.activity_rxandroid;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresent = new RxPresenter(this);
        textView = (TextView)findViewById(R.id.textView);
    }

    //测试Just标签
    public void toJust(View view) {
        mPresent.executeJust();
    }

    //测试Form标签
    public void toForm(View view) {
        mPresent.executeForm();
    }

    //测试Repeat标签
    public void toRepeat(View view) {
        mPresent.executeRepeat();
    }
    //测试Interval标签
    public void toInterval(View view) {
        mPresent.executeInterval();
    }
    //测试Timer
    public void toTimer(View view) {
        mPresent.executeTimer();
    }
    //测试Filter
    public void toFilter(View view) {
        mPresent.executeFilter();
    }
    //测试distinct（独特）
    public void toDistinct(View view) {
        mPresent.executeDistinct();
    }

    //测试Map转换标签
    public void toMap(View view) {
        mPresent.executeMap();
    }

    //测试flatMap转换标签
    public void toFlatMap(View view) {
        mPresent.executeFlatMap();
    }


    @Override
    public void setText(String s) {
        textView.setText(s);
    }

}
