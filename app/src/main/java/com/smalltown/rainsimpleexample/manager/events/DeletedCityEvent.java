package com.smalltown.rainsimpleexample.manager.events;

/**
 * Created by yangchunyu
 * 2016/2/1 14:02
 */
public class DeletedCityEvent {
    private String status;

    public DeletedCityEvent(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
