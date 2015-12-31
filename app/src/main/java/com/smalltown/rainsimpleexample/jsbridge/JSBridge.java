package com.smalltown.rainsimpleexample.jsbridge;

import android.os.SystemClock;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.smalltown.rainsimpleexample.mode.NameValuePair;
import com.smalltown.rainsimpleexample.util.CommonUtil;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Diagrams on 2015/12/28 11:01
 */
public class JSBridge implements JSWebViewClientInterface {
    private static final String PARAMETER_SEPARATOR = "&";
    private static final String NAME_VALUE_SEPARATOR = "=";
    private static final String DEFAULT_CONTENT_CHARSET = "ISO-8859-1";

    private static final String SCHEMA_PRE = "schema";
    private static final String KEY_ACTION = "action";
    private static final String KEY_UNIQUE = "unique";
    private static final String KEY_PARAM = "param";
    private static final String USER_AGENT = "appxcar";


    private JSBridgeInteractor mInteractor;
    private WebView mWebView;
    public JSBridge(WebView webView) {
        mWebView = webView;
        mInteractor = new JSBridgeInteractor(webView);
        WebSettings settings = webView.getSettings();
        String userAgentString = settings.getUserAgentString();
        settings.setUserAgentString(userAgentString+USER_AGENT);//添加个人代理
        // return window.navigator.userAgent.indexOf('appxcar') > -1;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith(SCHEMA_PRE)) {
          System.out.println("SCHEMA 过滤");
            final List<NameValuePair> pair = decodeParamsFromRequest(url, null);
            if(get(pair,"action").equals("jsApiList")){
                //加载页面
                mInteractor.injectAPIList(get(pair,"unique"));
            }
            if(get(pair,"action").equals("share")){
                //分享 在此调用本地方法执行分享
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(2000);
                        //分享完成回调
                        CommonUtil.runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                mInteractor.onShareComplete(get(pair, "unique"), 1, true);
                            }
                        });
                    }
                }).start();
            }
            return true;
        }
        return false;
    }

    /**
     * 获取nameValuePairs中的值
     * @param nameValuePairs 键值对
     * @param key 键
     * @return 值
     */
    private String get(List<NameValuePair> nameValuePairs, String key) {
        if (nameValuePairs != null && nameValuePairs.size() > 0) {
            for (NameValuePair pair : nameValuePairs) {
                if (pair != null && pair.name != null
                        && pair.name.equalsIgnoreCase(key)) {
                    return pair.value;
                }
            }
        }
        return null;
    }
    /**
     * 根据编码准备解析URL
     * @param requestUrl 链接
     * @param charset 编码
     * @return 键值对
     */
    private List<NameValuePair> decodeParamsFromRequest(String requestUrl,String charset) {
        try {
           return parsePair(new URI(requestUrl), charset == null ? "utf-8" : charset);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("URL链接错误");
        }
    }

    /**
     * 解析数据前的准备 获取uri的Query
     * @param uri 链接
     * @param encoding 编码
     * @return
     */
    private List<NameValuePair> parsePair(final URI uri, final String encoding){
        List<NameValuePair> result = Collections.emptyList();
        final String query = uri.getRawQuery();
        System.out.println("query:"+query);
        if (query != null && query.length() > 0) {
            result = new ArrayList<>();
            parse(result, new Scanner(query), encoding);
        }
        return result;
    }

    /**
     * 真正的解析键值对
     * @param parameters 键值对列表
     * @param scanner 扫描类
     * @param encoding 编码方式
     */
    public static void parse(final List<NameValuePair> parameters, final Scanner scanner, final String encoding) {
        scanner.useDelimiter(PARAMETER_SEPARATOR);
        while (scanner.hasNext()) {
            final String[] nameValue = scanner.next().split(NAME_VALUE_SEPARATOR);
            if (nameValue.length == 0 || nameValue.length > 2)
                throw new IllegalArgumentException("bad parameter");
            final String name = decode(nameValue[0], encoding);
            String value = null;
            if (nameValue.length == 2)
                value = decode(nameValue[1], encoding);
            parameters.add(new NameValuePair(name, value));
        }
    }

    /**
     * URL编码的解码 因为服务器传来的数据是URL编码的 所以要解码
     * @param content 内容
     * @param encoding 编码
     * @return 内容
     */
    private static String decode(final String content, final String encoding) {
        try {
            return URLDecoder.decode(content,
                    encoding != null ? encoding : DEFAULT_CONTENT_CHARSET);
        } catch (UnsupportedEncodingException problem) {
            throw new IllegalArgumentException(problem);
        }
    }

}
