package com.smalltown.rainsimpleexample.ui.inter;

import com.android.volley.RequestQueue;
import com.smalltown.rainsimpleexample.mode.SplashMode;
import com.smalltown.rainsimpleexample.ui.base.BaseViewInter;

/**
 * Created by Diagrams on 2016/1/4 15:24
 */
public interface ISplashView extends BaseViewInter {
    void updateProgress(int progress);
    void updateStartView(SplashMode splashMode,RequestQueue queue);
}
