package com.smalltown.rainsimpleexample.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.smalltown.rainsimpleexample.R;
import com.smalltown.rainsimpleexample.mode.UserInfo;
import com.smalltown.rainsimpleexample.presenter.InfoPresenter;
import com.smalltown.rainsimpleexample.ui.base.BaseActivity;
import com.smalltown.rainsimpleexample.ui.inter.IInfoView;

/**
 * 界面 通过接口 与模型进行交互
 * Created by Diagrams on 2016/1/4 10:09
 */
public class MvpSimpleActivity extends BaseActivity implements IInfoView,View.OnClickListener {

    private EditText mEtIdcard;
    private EditText mEtName;
    private EditText mEtAddress;
    private Button mBtnSave;
    private TextView mTvInfo;
    private Button mBtnGetinfo;
    private InfoPresenter mInfoPresenter;

    @Override
    protected int initView() {
        return R.layout.activity_mvp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assignViews();
        mInfoPresenter = new InfoPresenter(this);
    }

    private void assignViews() {
        mEtIdcard = (EditText) findViewById(R.id.et_idcard);
        mEtName = (EditText) findViewById(R.id.et_name);
        mEtAddress = (EditText) findViewById(R.id.et_address);
        mBtnSave = (Button) findViewById(R.id.btn_save);
        mTvInfo = (TextView) findViewById(R.id.tv_info);
        mBtnGetinfo = (Button) findViewById(R.id.btn_getinfo);
        mBtnSave.setOnClickListener(this);
        mBtnGetinfo.setOnClickListener(this);
    }

    @Override
    public UserInfo getInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.address = mEtAddress.getText().toString().trim();
        userInfo.idCard = mEtIdcard.getText().toString().trim();
        userInfo.name = mEtName.getText().toString().trim();
        return userInfo;
    }

    @Override
    public void setInfo(UserInfo info) {
        mTvInfo.setText(info.toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                mInfoPresenter.saveInfo(getInfo());
                break;
            case R.id.btn_getinfo:
                mInfoPresenter.getInfo();
                break;
        }
    }
}
