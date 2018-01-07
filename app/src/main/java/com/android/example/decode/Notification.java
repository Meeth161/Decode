package com.android.example.decode;


public class Notification {

    String fromId;
    String type;

    public Notification(String fromId, String type) {
        this.fromId = fromId;
        this.type = type;
    }

    public String getFromId() {
        return fromId;
    }

    public String getType() {
        return type;
    }
}
