package com.smalltown.rainsimpleexample.manager;

import android.os.SystemClock;
import android.util.Log;
import com.path.android.jobqueue.BaseJob;
import com.smalltown.rainsimpleexample.manager.events.DeletedCityEvent;
import com.smalltown.rainsimpleexample.manager.events.PostedCityEvent;
import com.smalltown.rainsimpleexample.manager.events.PostingCityEvent;
import de.greenrobot.event.EventBus;

/**
 * Created by yangchunyu
 * 2016/2/1 13:40
 */
public class PostTweetJob extends BaseJob{


    private static final String TAG = "PostTweetJob";
    private final long localId;
    private String text;

    public PostTweetJob(String text) {
        super(true, true, "post_tweet");
        localId = -System.currentTimeMillis();
        this.text = text;
    }

    @Override
    public void onAdded() {
        Log.e(TAG,"onAdded");
        EventBus.getDefault().post(new PostingCityEvent("posting"));
    }

    @Override
    public void onRun() throws Throwable {
        Log.e(TAG,"onRun sleep 6s");
        SystemClock.sleep(6000);
        EventBus.getDefault().post(new PostedCityEvent("posted"));
    }

    @Override
    protected void onCancel() {
        Log.e(TAG, "onCancel");
        EventBus.getDefault().post(new DeletedCityEvent("delete"));
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        Log.e(TAG,"shouldReRunOnThrowable");
        return false;
    }
}
