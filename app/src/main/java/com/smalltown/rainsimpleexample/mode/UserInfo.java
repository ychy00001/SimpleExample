package com.smalltown.rainsimpleexample.mode;

/**
 * Created by Diagrams on 2016/1/4 10:53
 */
public class UserInfo {
    public String idCard;
    public String name;
    public String address;

    @Override
    public String toString() {
        return "UserInfo{" +"\n"+
                "idCard='" + idCard + '\'' +"\n"+
                ", name='" + name + '\'' +"\n"+
                ", address='" + address + '\'' +"\n"+
                '}';
    }
}
