package com.android.example.decode;

/**
 * Created by Meeth on 27-Feb-18.
 */

public class Message {

    long timestamp;
    String message;
    String from;
    boolean seen;

    public Message() {
    }

    public Message(String message, String from) {
        this.message = message;
        this.from = from;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getFrom() {
        return from;
    }
}
