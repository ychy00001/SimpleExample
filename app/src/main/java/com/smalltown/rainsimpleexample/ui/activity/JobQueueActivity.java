package com.smalltown.rainsimpleexample.ui.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.path.android.jobqueue.JobManager;
import com.smalltown.rainsimpleexample.R;
import com.smalltown.rainsimpleexample.global.RainApplication;
import com.smalltown.rainsimpleexample.manager.PostTweetJob;
import com.smalltown.rainsimpleexample.manager.SimpleBackgroundTask;
import com.smalltown.rainsimpleexample.manager.events.DeletedCityEvent;
import com.smalltown.rainsimpleexample.manager.events.PostedCityEvent;
import com.smalltown.rainsimpleexample.manager.events.PostingCityEvent;
import com.smalltown.rainsimpleexample.ui.base.BaseActivity;
import de.greenrobot.event.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by yangchunyu
 * 2016/2/1 9:54
 */
public class JobQueueActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "JobQueueActivity";
    private Button mBtnRequest;
    private Button mBtnLocal;

    private JobManager jobManager;

    @Override
    protected int setContentLayout() {
        return R.layout.activity_jobqueue;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBtnRequest = (Button)findViewById(R.id.btn_request);
        mBtnLocal = (Button)findViewById(R.id.btn_local);
        mBtnRequest.setOnClickListener(this);
        mBtnLocal.setOnClickListener(this);
        jobManager = RainApplication.getInstance().getJobManager();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_request:
                requestDaa();
                break;
            case R.id.btn_local:
                doLocalTask();
                break;
        }
    }

    private void doLocalTask() {
        new SimpleBackgroundTask<String>(this){

            @Override
            protected String onRun() {
                Log.e(TAG,"SimpleBackgroundTask sleep 8S");
                SystemClock.sleep(8000);
                return null;
            }

            @Override
            protected void onSuccess(String result) {
                Log.e(TAG, "SimpleBackgroundTask execute success");
            }
        }.execute();
    }

    protected void requestDaa() {
        jobManager.addJobInBackground(1,new PostTweetJob("SKY"));
    }

    /**
     * EventBus回调
     * @param ignored
     */
    public void onEventMainThread(PostingCityEvent ignored){
        Log.i(TAG, "PostingCityEvent status=" + ignored.getStatus());
    }

    public void onEventMainThread(PostedCityEvent ignored){
        Log.i(TAG, "PostingCityEvent status=" + ignored.getStatus());
        Date data = new Date(1453973270000L);
        String str = getDateString(data,"yyyy年MM月");
    }

    public void onEventMainThread(DeletedCityEvent ignored){
        Log.i(TAG, "PostingCityEvent status=" + ignored.getStatus());
    }

    public static String getDateString(Date date, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.getDefault());
        return df.format(date.getTime());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
