package com.example.joshh.android_lambdamesageboard_services;

import org.json.JSONException;
import org.json.JSONObject;


public class Message {
    String sender, text, id;
    double timestamp;

    public Message(String sender, String text, String id, double timestamp) {
        this.sender = sender;
        this.text = text;
        this.id = null;
        this.timestamp = timestamp;
    }

    public Message(String sender, String text) {
        this.sender = sender;
        this.text = text;
    }

    public Message(JSONObject jsonObject, String identifier) {
        this.id = identifier;
        try {
            this.sender = jsonObject.getString("sender");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.text = jsonObject.getString("text");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.timestamp = jsonObject.getLong("timestamp");
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }
}

