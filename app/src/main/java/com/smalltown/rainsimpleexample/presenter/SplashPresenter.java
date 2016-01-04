package com.smalltown.rainsimpleexample.presenter;

import android.os.Handler;
import android.os.SystemClock;
import com.smalltown.rainsimpleexample.mode.SplashMode;
import com.smalltown.rainsimpleexample.mode.impl.ISplashModeImpl;
import com.smalltown.rainsimpleexample.mode.inter.ISplashMode;
import com.smalltown.rainsimpleexample.ui.inter.ISplashView;

/**
 * Splash页面逻辑控制
 * Created by Diagrams on 2016/1/4 15:45
 */
public class SplashPresenter {
    private ISplashMode splashMode;
    private ISplashView splashView;
    private Handler handler = new Handler();
    private Thread thread;
    private boolean isStop = false;//用于停止线程

    public SplashPresenter(ISplashView splashView) {
        this.splashView = splashView;
        this.splashMode = new ISplashModeImpl();
    }

    /**
     * 更新进度条
     */
    public void updateProgress(){
       splashView.setSplashMode(splashMode.getMode());
    }

    /**
     * 设置模型
     * @param mode SplashMode
     */
    public void setSplashMode(SplashMode mode){
        splashMode.setMode(mode);
    }

    /**
     * 模拟请求网络
     */
    public void executeHttp() {
        thread =  new Thread(){
            @Override
            public void run() {
                super.run();
                int total = 0;
                while(total < 100){
                    if(!isStop){
                        System.out.println("进度"+total);
                        SystemClock.sleep(100);
                        total++;
                        setSplashMode(new SplashMode(false, total));
                        handler.post(mRunnable);
                    }
                }
            }
        };
        thread.start();
    }

    /**
     * handler发送消息至此
     */
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            updateProgress();
        }
    };

    public void onStart(){
        isStop = false;
    }

    public void onStop(){
        isStop = true;
    }

    public void onDestroy(){
        isStop = true;
        handler.removeCallbacks(mRunnable);
    }
}
