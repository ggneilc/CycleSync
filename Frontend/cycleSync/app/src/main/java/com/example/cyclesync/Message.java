package com.example.cyclesync;

public class Message {
    private final String bodyText;
    private final boolean isMyMessage;

    public Message(String bodyText, boolean isMyMessage) {
        this.bodyText = bodyText;
        this.isMyMessage = isMyMessage;
    }

    public boolean isMyMessage() {
        return isMyMessage;
    }

    public String getBodyText() {
        return bodyText;
    }
}