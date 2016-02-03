package com.smalltown.rainsimpleexample.mode;

import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by yangchunyu
 * 2016/2/3 10:36
 */
public class RxMode {
    public Observable<String> getJustMode() {
        return Observable.just("aa", "bb", "cc");
    }

    public Observable<String> getFormMode() {
        List<String> list = new ArrayList<>();
        list.add("gaga");
        list.add("lili");
        list.add("mama");
        String[] str = new String[]{"gaga", "lili", "mama"};
        return Observable.from(list);
    }

    public Observable<String> getRepeatMode() {
        String[] str = new String[]{"rere", "pepe", "tete"};
        return Observable.from(str).repeat(2);
    }

    //defer 用于在定语时候才创建Observable
    public Observable<String> getDeferMode() {
        final String[] str = new String[]{"rere", "pepe", "tete"};
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                return Observable.from(str);
            }
        });
    }


    //泛型必须为long 从0开始 每隔3秒递增一个数 0 1 2
    public Observable<Long> getIntervalMode() {
        return Observable.interval(3, TimeUnit.SECONDS);
    }

    //timer标签 用于延迟x秒后执行onNext
    public Observable<Long> getTimerMode() {
        return Observable.timer(5, TimeUnit.SECONDS);
    }

    //过滤标签 用于过滤结果中的数据
    public Observable<String> getFilterMode() {
        final String[] str = new String[]{"filter", "ttt", "rrr"};
        return Observable.from(str).filter(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                return s.contains("t");
            }
        });
    }

    //去掉重复数据
    public Observable<String> getDistinctMode() {
        final String[] str = new String[]{"distinct", "dis", "nct"};
        return Observable.from(str).repeat(2).distinct();
    }

    //Map标签 用于转换
    public Observable<String> getMapMode(){
        Integer[] ints = {1,2,3,4};
        return Observable.from(ints).map(new Func1<Integer,String>() {
            @Override
            public String call(Integer integer) {
                String str = "";
                switch (integer) {
                    case 1:
                        str = "aaa";
                        break;
                    case 2:
                        str = "bbb";
                        break;
                    case 3:
                        str = "ccc";
                        break;
                }
                return str;
            }
        });
    }

    //flatMap 用于返回一个Observable也就是被观察者 根据已获取的数据 分发不同的Observable
    public Observable<String> getFlatMap(){
        Integer[] ints = {1,2,3,4};
        return Observable.from(ints).flatMap(new Func1<Integer, Observable<String>>() {
            @Override
            public Observable<String> call(Integer integer) {
                Observable<String> observable = getJustMode();
                switch (integer) {
                    case 1:
                        observable = getFormMode();
                        break;
                    case 2:
                        observable = getFilterMode();
                        break;
                }
                return observable;
            }
        });
    }
}
