package com.smalltown.rainsimpleexample.ui.activity;

import android.os.Bundle;
import butterknife.Bind;
import butterknife.OnClick;
import com.smalltown.rainsimpleexample.R;
import com.smalltown.rainsimpleexample.ui.base.BaseActivity;
import com.smalltown.rainsimpleexample.ui.view.FlyView;

/**
 * Created by Diagrams on 2015/12/30 18:11
 */
public class FlyActivity extends BaseActivity{

    @Bind(R.id.flyView)FlyView mFlyView;
    @Override
    protected int setContentLayout() {
        return R.layout.activity_fly;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.btn_add)
    void clickAdd(){
        mFlyView.addFlakes(5);
    }

    @OnClick(R.id.btn_delete)
    void clickDelete(){
        mFlyView.subtractFlakes(5);
    }
}
