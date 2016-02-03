package com.smalltown.rainsimpleexample.presenter;

import com.smalltown.rainsimpleexample.mode.RxMode;
import com.smalltown.rainsimpleexample.ui.inter.RxView;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by yangchunyu
 * 2016/2/3 10:32
 */
public class RxPresenter {
    private RxView mView;
    private RxMode mMode;
    private Subscription intervalSubscribe;
    private Subscription timerSubscribe;

    public RxPresenter(RxView rxView) {
        this.mView = rxView;
        mMode = new RxMode();
    }

    /**
     * 执行Just标签
     */
    public void executeJust(){
        mMode.getJustMode().subscribe(new MySubscribe<String>());
    }

    /**
     * 执行From标签
     */
    public void executeForm() {
        mMode.getFormMode().subscribe(new MySubscribe<String>());
    }

    /**
     * 执行repet标签
     */
    public void executeRepeat() {
        mMode.getRepeatMode().subscribe(new MySubscribe<String>());
    }
    /**
     * 执行interval标签
     */
    public void executeInterval() {
        if(intervalSubscribe != null && !intervalSubscribe.isUnsubscribed()){
            intervalSubscribe.unsubscribe();
        }else{
            intervalSubscribe = mMode.getIntervalMode().subscribe(new MySubscribe<Long>());
        }
    }

    /**
     * 执行timer标签
     */
    public void executeTimer() {
        if(timerSubscribe != null && !timerSubscribe.isUnsubscribed()){
            timerSubscribe.unsubscribe();
        }else{
            timerSubscribe = mMode.getTimerMode().subscribe(new MySubscribe<Long>());
        }
    }

    public void executeFilter() {
        mMode.getFilterMode().subscribe(new MySubscribe<String>());
    }

    public void executeDistinct() {
        mMode.getDistinctMode().subscribe(new MySubscribe<String>());
    }

    public void executeMap() {
        mMode.getMapMode().subscribe(new MySubscribe<String>());
    }

    public void executeFlatMap() {
        mMode.getFlatMap().subscribe(new MySubscribe<String>());
    }


    /**
     * 所有的订阅都在次返回
     * @param <T>
     */
    class MySubscribe<T> extends Subscriber<T>{

        @Override
        public void onCompleted() {
            System.out.println("onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            System.out.println("onError");
        }

        @Override
        public void onNext(T o) {
            if(o instanceof  String){
                mView.setText((String) o);
                System.out.println("onNext:" + o);
            }else if(o instanceof Long){
                System.out.println("onNext:" + o);
            }
        }
    }
}
