package com.smalltown.rainsimpleexample.manager;

import android.app.Activity;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

/**
 * Created by yangchunyu
 * 2016/2/1 11:10
 */
abstract public class SimpleBackgroundTask<T> extends AsyncTask<Void,Void,T> {
    WeakReference<Activity> weakReference;

    public SimpleBackgroundTask(Activity activity) {
        this.weakReference = new WeakReference<Activity>(activity);
    }

    @Override
    protected T doInBackground(Void... params) {
        return onRun();
    }

    private boolean canContinue(){
        Activity activity = weakReference.get();
        return activity != null && !activity.isFinishing();
    }

    @Override
    protected void onPostExecute(T t) {
        if(canContinue()){
            onSuccess(t);
        }
    }

    abstract protected  T onRun();
    abstract protected  void onSuccess(T result);
}
