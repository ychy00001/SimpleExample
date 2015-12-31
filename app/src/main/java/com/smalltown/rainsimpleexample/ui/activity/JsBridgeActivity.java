package com.smalltown.rainsimpleexample.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.*;
import com.smalltown.rainsimpleexample.R;
import com.smalltown.rainsimpleexample.jsbridge.JSBridge;
import com.smalltown.rainsimpleexample.ui.base.BaseActivity;
import com.smalltown.rainsimpleexample.util.ToastUtil;

/**
 * Created by Diagrams on 2015/12/24 15:24
 */
public class JsBridgeActivity extends BaseActivity {
    public static final String url = "http://a2.xcar.com.cn/touch_make/jsbridge/index.html";
    public static final String testUrl = "http://a.xcar.com.cn/huodong/201510/buying/";
    private WebView mWebView;
    private WebSettings settings;
    private JSBridge jsBridge;

    @Override
    protected int initView() {
        return R.layout.activity_jsbridge;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWebView = (WebView) findViewById(R.id.wv_webview);
        initData();
    }

    protected void initData() {
        jsBridge = new JSBridge(mWebView);
        settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(false);
        //noinspection deprecation
        settings.setTextSize(WebSettings.TextSize.NORMAL);
        settings.setBlockNetworkImage(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDomStorageEnabled(true);//
        mWebView.setBackgroundResource(R.mipmap.bg_transparent);
        mWebView.setBackgroundColor(0);
        mWebView.setWebViewClient(new WebClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.loadUrl(testUrl);
    }

    private class WebClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //拦截url事件 true 表示消费
            if(jsBridge!=null && jsBridge.shouldOverrideUrlLoading(view,url)){
                return true;
            }
            //默认链接
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            //开始加载
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            settings.setBlockNetworkImage(false);
            //结束加载
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            //显示自定义错误页面
            ToastUtil.showToast("加载页面错误");
        }
    }
}
