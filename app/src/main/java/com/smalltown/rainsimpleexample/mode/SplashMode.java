package com.smalltown.rainsimpleexample.mode;

/**
 * Created by Diagrams on 2016/1/4 15:19
 */
public class SplashMode {
    public boolean isOnLine;
    public int progress;
    public String text;
    public String startImgUrl;

    public SplashMode() {}

    public SplashMode(boolean isOnLine, int progress,String startImgUrl) {
        this.isOnLine = isOnLine;
        this.progress = progress;
    }
}
