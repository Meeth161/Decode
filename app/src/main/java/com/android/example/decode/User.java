package com.android.example.decode;

public class User {

    String uid;
    String name;
    String dpUrl;

    public User() {
    }

    public User(String uid, String name, String dpUrl) {
        this.uid = uid;
        this.name = name;
        this.dpUrl = dpUrl;
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
}
