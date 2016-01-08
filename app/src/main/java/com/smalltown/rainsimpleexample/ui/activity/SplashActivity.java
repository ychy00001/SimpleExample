package com.smalltown.rainsimpleexample.ui.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.smalltown.rainsimpleexample.R;
import com.smalltown.rainsimpleexample.api.Apis;
import com.smalltown.rainsimpleexample.cache.BitmapCache;
import com.smalltown.rainsimpleexample.cache.DiskCache;
import com.smalltown.rainsimpleexample.mode.SplashMode;
import com.smalltown.rainsimpleexample.presenter.SplashPresenter;
import com.smalltown.rainsimpleexample.ui.base.BaseActivity;
import com.smalltown.rainsimpleexample.ui.inter.ISplashView;
import com.smalltown.rainsimpleexample.util.JsonUtil;

import java.util.HashMap;

/**
 * Created by Diagrams on 2016/1/4 14:47
 */
public class SplashActivity extends BaseActivity implements ISplashView, Animation.AnimationListener {
    public static final String TAG = "SplashActivity";

    private CircleProgressBar mProgressBar;
    private SplashPresenter mSplashPresenter;
    private NetworkImageView mImgStart;//背景图
    private Context mContext;
    private DiskCache diskCache;

    private TextView mTvSplsahText;


    @Override
    protected int setContentLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        assignViews();
        loadCache();
        //开始动画
        initImageAnim();
        loadData();
    }

    /**
     * 加载数据
     */
    private void loadData() {
        mSplashPresenter = new SplashPresenter(mContext, this);
        mSplashPresenter.executeHttp();
    }

    /**
     * 加载缓存
     */
    private void loadCache() {
        diskCache = new DiskCache(mContext);
        String json = diskCache.getJsonCache(Apis.ZH_SPLASH_IMG_URL);
        if(!TextUtils.isEmpty(json)){
            HashMap<String, Object> map = JsonUtil.parseJsonToMap(json);
            String imgStr = (String)map.get("img");
            String text = (String)map.get("text");
            if(!TextUtils.isEmpty(imgStr)){
                BitmapCache cache = BitmapCache.getInstance();
                cache.initilize(mContext);
                mImgStart.setImageUrl(imgStr, new ImageLoader(mRequestQueue,cache));
            }
            if(!TextUtils.isEmpty(text)){
                mTvSplsahText.setText(text);
            }
        }
        diskCache.close();
    }

    /**
     * 初始化View
     */
    private void assignViews() {
        mImgStart = (NetworkImageView) findViewById(R.id.img_start);
        mProgressBar = (CircleProgressBar) findViewById(R.id.progressBar);
        mTvSplsahText = (TextView) findViewById(R.id.tv_splsah_text);
        mProgressBar.setColorSchemeResources(android.R.color.holo_green_light);
        mProgressBar.setShowArrow(true);

        //设置默认和错误图片
        mImgStart.setDefaultImageResId(R.mipmap.splash);
        mImgStart.setErrorImageResId(R.mipmap.splash);
    }

    /**
     * 初始化启动图动画
     */
    private void initImageAnim() {// 图片动画
        Animation animation = new ScaleAnimation(1.0f, 1.08f, 1.0f, 1.08f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f); // 将图片放大1.2倍，从中心开始缩放
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setRepeatMode(Animation.REVERSE);
        animation.setDuration(3000); // 动画持续时间
        animation.setFillAfter(true); // 动画结束后停留在结束的位置
        animation.setAnimationListener(SplashActivity.this); // 添加动画监听
        mImgStart.startAnimation(animation); // 启动动画
    }

    /************** implements ISplashView ********************/

    @Override
    public void updateProgress(int progress) {
        if (progress >= 0) {
            mProgressBar.setProgress(progress);
            if (progress == 100) {
                toMainActivity(TAG);
            }
        }
    }

    /**
     * 更新开始界面的背景图
     *
     * @param mode 启动界面
     * @param queue 请求队列
     */
    @Override
    public void updateStartView(SplashMode mode, RequestQueue queue) {
        //初始化缓存
        BitmapCache cache = BitmapCache.getInstance();
        cache.initilize(mContext);
        mImgStart.setImageUrl(mode.startImgUrl, new ImageLoader(queue, cache));
        if(!TextUtils.isEmpty(mode.text)){
            mTvSplsahText.setText(mode.text);
        }
    }

    /*********************** 生命周期处理 ****************************/

    @Override
    protected void onStart() {
        super.onStart();
        mSplashPresenter.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSplashPresenter.onStop();
    }

    @Override
    protected void onDestroy() {
        mImgStart.destroyDrawingCache();
        mImgStart.setImageBitmap(null);

        if(Build.VERSION.SDK_INT >= 21){
            mProgressBar.stopNestedScroll();
        }

        mProgressBar.destroyDrawingCache();
        mSplashPresenter.onDestroy();
        mSplashPresenter = null;

        System.gc();
        super.onDestroy();
    }


    /****************动画监听回调*************************/

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        toMainActivity(TAG);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }



}
