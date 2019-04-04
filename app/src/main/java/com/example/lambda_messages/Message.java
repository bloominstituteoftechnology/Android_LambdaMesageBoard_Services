package com.example.lambda_messages;

import org.json.JSONObject;

public class Message {

    String sender, text, senderId, key;
    double timestamp;


    public Message(String sender, String text, double timestamp, String key) {
        this.sender = sender;
        this.text = text;
        this.key = key;
        this.senderId = null;
        this.timestamp = System.currentTimeMillis() / 1000;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }

    public Message(String sender, String text, String senderID, double timestamp, String key) {
        this.sender = sender;
        this.text = text;
        this.key = key;
        this.senderId = senderID;
        this.timestamp = System.currentTimeMillis() / 1000;
    }


}
