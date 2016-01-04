package com.smalltown.rainsimpleexample.mode.impl;

import com.smalltown.rainsimpleexample.mode.UserInfo;
import com.smalltown.rainsimpleexample.mode.inter.IUserInfo;

/**
 * Created by Diagrams on 2016/1/4 10:55
 */
public class IUserInfoImpl implements IUserInfo {
    private UserInfo info;

    @Override
    public UserInfo getInfo() {
        return info;
    }

    @Override
    public void setInfo(UserInfo userInfo) {
        info = userInfo;
    }
}
