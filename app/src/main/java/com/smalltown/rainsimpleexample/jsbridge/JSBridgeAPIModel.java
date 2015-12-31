package com.smalltown.rainsimpleexample.jsbridge;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Diagrams on 2015/12/28 13:52
 */
public class JSBridgeAPIModel implements JSDataBuildInterface {
    /** 注入支持的API列表 */
    public static final String ACTION = "jsApiList";
    /** 上传图片 */
    public static final String ACTION_UPLOAD_IMAGE = "simpleUploader";
    /** 分享 */
    public static final String ACTION_SHARE = "share";
    /** 登录 */
    public static final String ACTION_LOGIN = "login";
    /** 请求登录信息 */
    public static final String ACTION_REQUEST_INFO = "requestInfo";
    /** 个人中心 */
    public static final String ACTION_USER_INFO = "userInfo";
    /** 接收分享用数据 */
    public static final String ACTION_SHARE_INFO = "shareInfo";
    @SerializedName("action")
    private final String action = ACTION;
    @SerializedName("data")
    private final String[] API_LIST = {
            ACTION_UPLOAD_IMAGE,//上传图片
            ACTION_SHARE,//分享
            ACTION_LOGIN,//登录
            ACTION_REQUEST_INFO,//登录信息同步
            ACTION_USER_INFO,
            ACTION_SHARE_INFO
    };

    @SerializedName("unique")
    private final String unique;

    public JSBridgeAPIModel(String unique) {
        this.unique = unique;
    }

    @Override
    public String build() {
        return new Gson().toJson(this);
    }
}
