package com.smalltown.rainsimpleexample.jsbridge;

import android.webkit.WebView;

/**
 * Created by Diagrams on 2015/12/28 13:57
 */
public class JSBridgeInteractor {
    private static final String JAVASCRIPT_MASK = "javascript:window.xcarjsapi.jsCallBack(%1$s)";
    private WebView mWebView;

    public JSBridgeInteractor(WebView webView) {
        this.mWebView = webView;
    }

    /** 初始化可用方法列表 */
    public void injectAPIList(String unique) {
        if (mWebView != null) {
            System.out.println("injectAPIList:"+String.format(JAVASCRIPT_MASK, new JSBridgeAPIModel(unique).build()));
            mWebView.loadUrl(String.format(JAVASCRIPT_MASK, new JSBridgeAPIModel(unique).build()));
        }
    }

    /** 分享完成 */
    public void onShareComplete(String unique, int shareType, boolean result) {
        if (mWebView != null) {
            System.out.println("onShareComplete:"+build(new JSBridgeShareResultModel(unique, shareType, result)));
            mWebView.loadUrl(build(new JSBridgeShareResultModel(unique, shareType, result)));
        }
    }
//
//    /** 上传图片完成 */
//    public void uploadComplete(String unique, String data) {
//        if (mWebView != null) {
//            mWebView.loadUrl(build(new JSBridgeUploadResult(unique, data)));
//        }
//    }
//
//    /**
//     * 注入登录信息
//     */
//    public void injectSession(String unique) {
//        LoginUtil util = LoginUtil.getInstance(MyApplication.getContext());
//        if (mWebView != null) {
//            JSBridgeSessionModel data;
//            if (util.checkLogin()) {
//                data = new JSBridgeSessionModel(unique, util.getUid(), util.getUname(), util.getBbsAuth());
//            } else {
//                data = new JSBridgeSessionModel(unique, "", "", "");
//            }
//            mWebView.loadUrl(build(data));
//        }
//
//    }

    private String build(JSDataBuildInterface builder) {
        System.out.println(String.format(JAVASCRIPT_MASK, builder.build()));
        return String.format(JAVASCRIPT_MASK, builder.build());
    }
}
