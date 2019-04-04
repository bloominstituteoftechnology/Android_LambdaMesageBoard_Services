package com.jakeesveld.android_lambdamessages;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Message implements Parcelable {
    private String sender, text, id;
    private double timestamp;

    public Message(String sender, String text, String id, double timestamp) {
        this.sender = sender;
        this.text = text;
        this.id = id;
        this.timestamp = timestamp;
    }

    public Message(String sender, String text) {
        this.sender = sender;
        this.text = text;
        this.id = null;
        this.timestamp = System.currentTimeMillis();
    }

    public Message(JSONObject json){
        try {
            this.sender = json.getString("sender");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.text = json.getString("text");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.id = json.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.timestamp = json.getDouble("timestamp");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected Message(Parcel in) {
        sender = in.readString();
        text = in.readString();
        id = in.readString();
        timestamp = in.readDouble();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public String getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public String getId() {
        return id;
    }

    public double getTimestamp() {
        return timestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sender);
        dest.writeString(text);
        dest.writeString(id);
        dest.writeDouble(timestamp);
    }

    public String toJson(){
        JSONObject json = new JSONObject();
        try {
            json.put("sender", sender);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            json.put("text", text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            json.put("timestamp", timestamp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json.toString();

    }
}
