package com.smalltown.rainsimpleexample.manager;

import android.content.Context;
import android.util.Log;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.smalltown.rainsimpleexample.util.BuildConfigUtil;

/**
 * 该类用于在Application中初始化网络请求(Volley)
 * Created by Diagrams on 2016/1/5 17:36
 */
public class RequestManager {
    public static final String TAG = "RequestManager";
    private static RequestQueue mRequestQueue;

    private RequestManager(){}

    /**
     * 初始化请求队列
     */
    public static void init(Context context){
        mRequestQueue = Volley.newRequestQueue(context);
    }

    /**
     * 获取到当前请求队列
     *
     * @return 请求队列
     */
    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        }else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    /**
     * 添加并开启新的请求
     *
     * @param request 添加请求
     * @param tag 添加标志位 用于中断
     */
    public static void addRequest(Request<?> request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
        if(BuildConfigUtil.DEBUG) {
            if (request != null) {
                Log.v(TAG,request.getUrl() + "\n" );
            }
        }
        mRequestQueue.add(request);
    }

    /**
     * 终端所有对应tag的请求
     * @param tag
     */
    public static void cancelAll(Object tag){
        mRequestQueue.cancelAll(tag);
    }

    /**
     * 执行Request
     */
    public static void executeRequest(Request<?> request,Object tag){
        //可以设置超时后的处理
        request.setRetryPolicy(new DefaultRetryPolicy(15000,0,0));
        addRequest(request,tag);
    }

}
