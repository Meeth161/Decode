package com.android.example.decode;

public class Request {

    String senderUid;
    String receiverUid;
    String status;
    String key;

    public Request() {
    }

    public Request(String senderUid, String receiverUid, String status, String key) {
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
        this.status = status;
        this.key = key;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public String getStatus() {
        return status;
    }

    public String getKey() {
        return key;
    }
}
