package com.lambdaschool.android_lambdamessages;

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
        this.timestamp = (double) System.currentTimeMillis() / 1000;
    }

    public Message(JSONObject jsonObject) {
        try {
            this.sender = jsonObject.getString("sender");
        } catch (JSONException e) {
            e.printStackTrace();
            this.sender = null;
        }
        try {
            this.text = jsonObject.getString("text");
        } catch (JSONException e) {
            e.printStackTrace();
            this.text = null;
        }
        try {
            this.id = jsonObject.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
            this.id = null;
        }
        try {
            this.timestamp = jsonObject.getDouble("timestamp");
        } catch (JSONException e) {
            e.printStackTrace();
            this.timestamp = (double) System.currentTimeMillis() / 1000;
        }
    }

    protected Message(Parcel in) {
        this.sender = in.readString();
        this.text = in.readString();
        id = in.readString();
        this.timestamp = in.readDouble();
    }

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

    public void setId(String id) {
        this.id = id;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sender);
        dest.writeString(this.text);
        dest.writeString(this.id);
        dest.writeDouble(this.timestamp);
    }
}
