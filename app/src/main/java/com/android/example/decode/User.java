package com.android.example.decode;

public class User {

    String uid;
    String name;
    String dpUrl;
    String deviceToken;

    public User() {
    }

    public User(String uid, String name, String dpUrl, String deviceToken) {
        this.uid = uid;
        this.name = name;
        this.dpUrl = dpUrl;
        this.deviceToken = deviceToken;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getDpUrl() {
        return dpUrl;
    }

    public String getDeviceToken() {
        return deviceToken;
    }
}
