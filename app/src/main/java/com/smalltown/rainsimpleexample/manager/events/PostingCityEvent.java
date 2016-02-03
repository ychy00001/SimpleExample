package com.smalltown.rainsimpleexample.manager.events;

/**
 * Created by yangchunyu
 * 2016/2/1 14:00
 */
public class PostingCityEvent {
    private String status;
    public PostingCityEvent(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }
}

