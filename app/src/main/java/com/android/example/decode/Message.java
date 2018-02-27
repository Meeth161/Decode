package com.android.example.decode;

/**
 * Created by Meeth on 27-Feb-18.
 */

public class Message {

    String timestamp;
    String message;
    String from;

    public Message() {
    }

    public Message(String timestamp, String message, String from) {
        this.timestamp = timestamp;
        this.message = message;
        this.from = from;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getFrom() {
        return from;
    }
}
