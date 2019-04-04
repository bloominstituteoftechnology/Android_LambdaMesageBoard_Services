package com.vivekvishwanath.android_lambdamessages;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Message implements Parcelable {
    private String sender, text, id;
    private double timestamp;

    public Message (String sender, String text, String id, double timestamp) {
        this.sender = sender;
        this.text = text;
        this.id = id;
        this.timestamp = timestamp;
    }

    public Message (String sender, String text) {
        this.sender = sender;
        this.text = text;
        this.id = null;
        this.timestamp = System.currentTimeMillis() / 1000;
    }

    public Message (JSONObject messageJSON) {
        try {
            this.sender = messageJSON.getString("sender");
        } catch (JSONException e) {
            e.printStackTrace();
            this.sender = null;
        }
        try {
            this.text = messageJSON.getString("text");
        } catch (JSONException e) {
            e.printStackTrace();
            this.text = null;
        }
        try {
            this.id = messageJSON.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
            this.id = null;
        }
        try {
            this.timestamp = messageJSON.getDouble("timestamp");
        } catch (JSONException e) {
            e.printStackTrace();
            this.timestamp = System.currentTimeMillis() / 1000;
        }
    }

    public Message(Parcel parcel) {
        this.sender = parcel.readString();
        this.text = parcel.readString();
        this.id = parcel.readString();
        this.timestamp = parcel.readDouble();
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
