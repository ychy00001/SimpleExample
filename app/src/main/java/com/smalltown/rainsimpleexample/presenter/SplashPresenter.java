package com.smalltown.rainsimpleexample.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.smalltown.rainsimpleexample.api.Apis;
import com.smalltown.rainsimpleexample.cache.DiskCache;
import com.smalltown.rainsimpleexample.mode.SplashMode;
import com.smalltown.rainsimpleexample.mode.impl.ISplashModeImpl;
import com.smalltown.rainsimpleexample.mode.inter.ISplashMode;
import com.smalltown.rainsimpleexample.presenter.base.BasePresenter;
import com.smalltown.rainsimpleexample.ui.inter.ISplashView;
import com.smalltown.rainsimpleexample.util.ToastUtil;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Splash页面逻辑控制
 * Created by Diagrams on 2016/1/4 15:45
 */
public class SplashPresenter extends BasePresenter{
    private static SplashPresenter splashPresenter;

    private ISplashMode splashMode;
    private ISplashView splashView;
    private DiskCache mDiskCache;
    private Context mContext;
    private Handler handler = new Handler();
    private Thread thread;
    private Request mSplashImgRequest;
    private boolean isStop = false;//用于停止线程


    public SplashPresenter(Context context,ISplashView splashView) {
        this.mContext = context;
        this.splashView = splashView;
        this.splashMode = new ISplashModeImpl();
        isStop = false;
    }

    /**
     * 更新进度条
     */
    public void updateProgress(){
       splashView.updateProgress(splashMode.getMode().progress);
    }

    /**
     * 更新启动图
     */
    public void updateStartImage(){
        if(null == mRequestQueue){
            return;
        }
        splashView.updateStartView(splashMode.getMode(), mRequestQueue);
    }

    /**
     * 设置模型
     * @param progress 进度条
     */
    public void setProgress(int progress){
        SplashMode mode = splashMode.getMode();
        mode.progress = progress;
        splashMode.setMode(mode);
    }

    public void setStartImage(String imgUrl,String text){
        SplashMode mode = splashMode.getMode();
        mode.startImgUrl = imgUrl;
        mode.text = text;
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
                if(!isStop){
                    while(total < 100){
                        SystemClock.sleep(100);
                        total++;
                        setProgress(total);//设置进度条
                        executeRunnable(mRunnable);
                    }
                }
            }
        };
        thread.start();
        /*********************Volley网络请求****************************/
        mDiskCache = new DiskCache(mContext);
        if(Apis.ZH_SPLASH_IMG_URL.endsWith(".jpg")
                ||Apis.ZH_SPLASH_IMG_URL.endsWith(".png")
                ||Apis.ZH_SPLASH_IMG_URL.endsWith(".jpeg")){
            setStartImage(Apis.ZH_SPLASH_IMG_URL,null);
            updateStartImage();
        }else{
            mSplashImgRequest = new JsonObjectRequest(Request.Method.GET, Apis.ZH_SPLASH_IMG_URL, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        mDiskCache.addCache(response.toString(), Apis.ZH_SPLASH_IMG_URL);
                        //使用volly框架直接加载图片
                        String imgUrl = response.getString("img");
                        String text = response.getString("text");
                        setStartImage(imgUrl,text);
                        executeRunnable(finishRequestRunnable);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //请求错误
                    if(error instanceof NoConnectionError){
                        ToastUtil.showToast("连接错误,请检查网络");
                        return;
                    }
                    ToastUtil.showToast("请求错误");
                }
            });
            executeRequest(mSplashImgRequest, this);
        }
    }

    /**
     * 执行handler的post方法
     * @param runnable 具体线程执行的方法
     */
    private void executeRunnable(Runnable runnable) {
        if(handler != null){
            handler.post(runnable);
        }
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

    private Runnable finishRequestRunnable = new Runnable() {
        @Override
        public void run() {
            //请求图片链接完成
            if(mDiskCache!=null){
                mDiskCache.close();
            }
            updateStartImage();
        }
    };

    public void onStart(){
        isStop = false;
    }

    public void onStop(){
        isStop = true;
    }

    public void onDestroy(){
        super.onDestroy();
        isStop = true;

        //要求先线程停止 而后handler移除所有消息  反之会出现nullPointException
        thread.interrupt();
        thread = null;

        handler.removeCallbacks(mRunnable);
        handler = null;

    }

}
