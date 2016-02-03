package com.smalltown.rainsimpleexample.manager.events;

/**
 * Created by yangchunyu
 * 2016/2/1 14:01
 */
public class PostedCityEvent {
    private String status;

    public PostedCityEvent(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}

