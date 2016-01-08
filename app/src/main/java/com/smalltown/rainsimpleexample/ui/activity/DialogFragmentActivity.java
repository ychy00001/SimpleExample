package com.smalltown.rainsimpleexample.ui.activity;

import android.os.Bundle;
import android.widget.FrameLayout;
import butterknife.Bind;
import butterknife.OnClick;
import com.smalltown.rainsimpleexample.R;
import com.smalltown.rainsimpleexample.ui.base.BaseActivity;
import com.smalltown.rainsimpleexample.ui.fragment.MyDialogFragment;

/**
 * Created by Diagrams on 2015/12/31 11:04
 */
public class DialogFragmentActivity extends BaseActivity{

    @Bind(R.id.fl_continer)FrameLayout mContiner;
    @Override
    protected int setContentLayout() {
        return R.layout.activity_dialogfragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @OnClick(R.id.btn_fragment_dialog)
    void clickShowFragmentDialog(){
        MyDialogFragment myDialogFragment = new MyDialogFragment();
        myDialogFragment.show(getSupportFragmentManager(),"MyDialogFragment");
    }


}
