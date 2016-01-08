package com.smalltown.rainsimpleexample.presenter.base;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.smalltown.rainsimpleexample.manager.RequestManager;
import com.smalltown.rainsimpleexample.util.ToastUtil;

/**
 * 主导器的基类 MVP中的P
 * Created by Diagrams on 2016/1/5 17:30
 */
public class BasePresenter {

    protected RequestQueue mRequestQueue;

    public BasePresenter() {
        this.mRequestQueue = RequestManager.getRequestQueue();
    }

    /**
     * 执行request
     *
     * @param request 要执行的request
     * @param tag     执行request时绑定的tag，用于取消request
     */
    public void executeRequest(Request<?> request, Object tag) {
        if (tag == null)
            tag = this;
        RequestManager.executeRequest(request, tag);
    }

    protected void onDestroy(){
        RequestManager.cancelAll(this);
        System.gc();
    }
}
