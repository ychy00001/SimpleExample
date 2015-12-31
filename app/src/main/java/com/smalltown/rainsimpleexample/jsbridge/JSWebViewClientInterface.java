package com.smalltown.rainsimpleexample.jsbridge;

import android.webkit.WebView;

/**
 * Created by Diagrams on 2015/12/28 11:01
 */
public interface JSWebViewClientInterface {
    boolean shouldOverrideUrlLoading(WebView view, String url);
}
