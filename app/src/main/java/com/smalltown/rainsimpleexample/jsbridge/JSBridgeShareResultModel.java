package com.smalltown.rainsimpleexample.jsbridge;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Diagrams on 2015/12/28 14:20
 */
public class JSBridgeShareResultModel implements JSDataBuildInterface {
    public static final String ACTION = JSBridgeAPIModel.ACTION_SHARE;
    @SerializedName("action")
    private final String action = ACTION;
    @SerializedName("unique")
    private final String unique;
    @SerializedName("data")
    private final Data data;


    public JSBridgeShareResultModel(String unique, int shareType, boolean result) {
        this.unique = unique;
        data = new Data(shareType, result);
    }

    @Override
    public String build() {
        return new Gson().toJson(this);
    }

    public static class Data {
        @SerializedName("result")
        private final boolean result;
        @SerializedName("shareType")
        private int shareType;

        public Data(int shareType, boolean result) {
            this.result = result;
            this.shareType = shareType;
        }
    }
}
