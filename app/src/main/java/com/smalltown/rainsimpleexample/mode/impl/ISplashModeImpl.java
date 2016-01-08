package com.smalltown.rainsimpleexample.mode.impl;

import com.smalltown.rainsimpleexample.mode.SplashMode;
import com.smalltown.rainsimpleexample.mode.inter.ISplashMode;

/**
 * Created by Diagrams on 2016/1/4 15:21
 */
public class ISplashModeImpl implements ISplashMode {
    private SplashMode splashMode;

    @Override
    public void setMode(SplashMode splashMode) {
        this.splashMode = splashMode;
    }

    @Override
    public SplashMode getMode() {
        if(null == splashMode) {
            splashMode = new SplashMode();
        }
        return splashMode;
    }
}
