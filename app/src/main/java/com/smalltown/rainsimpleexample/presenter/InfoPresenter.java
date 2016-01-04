package com.smalltown.rainsimpleexample.presenter;

import com.smalltown.rainsimpleexample.mode.UserInfo;
import com.smalltown.rainsimpleexample.mode.impl.IUserInfoImpl;
import com.smalltown.rainsimpleexample.mode.inter.IUserInfo;
import com.smalltown.rainsimpleexample.ui.inter.IInfoView;

/**
 * 表现者 用于控制模型和视图
 * Created by Diagrams on 2016/1/4 11:00
 */
public class InfoPresenter  {
    //控制界面和模型交互
    private IUserInfo infoMode;
    private IInfoView infoView;

    public InfoPresenter(IInfoView infoView) {
        this.infoMode = new IUserInfoImpl();
        this.infoView = infoView;
    }

    public void getInfo(){
        infoView.setInfo(infoMode.getInfo());
    }

    public void saveInfo(UserInfo info){
        infoMode.setInfo(info);
    }
}
