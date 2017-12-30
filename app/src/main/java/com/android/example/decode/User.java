package com.android.example.decode;

public class User {

    String name;
    String dpUrl;

    public User() {
    }

    public User(String name, String dpUrl) {
        this.name = name;
        this.dpUrl = dpUrl;
    }

    public String getName() {
        return name;
    }

    public String getDpUrl() {
        return dpUrl;
    }
}
