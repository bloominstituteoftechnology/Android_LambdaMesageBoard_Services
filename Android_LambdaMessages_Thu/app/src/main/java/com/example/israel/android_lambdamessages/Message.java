package com.example.israel.android_lambdamessages;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;

import org.json.JSONException;
import org.json.JSONObject;

public class Message implements Parcelable {

    public Message(String displayName, String text, String senderID) {
        this.displayName = displayName;
        this.text = text;
        this.senderID = senderID;
        this.timestamp = System.currentTimeMillis() / 1000.0;
    }

    public Message(JSONObject jsonObject) {
        try {
            this.displayName = jsonObject.getString("displayName");
            this.text = jsonObject.getString("text");
            this.senderID = jsonObject.getString("senderID");
            this.timestamp = jsonObject.getDouble("timestamp");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Message(Parcel parcel) {
        displayName = parcel.readString();
        text = parcel.readString();
        senderID = parcel.readString();
        timestamp = parcel.readDouble();
    }

    private String displayName;
    private String text;
    private String senderID;
    private double timestamp;

    public String getDisplayName() {
        return displayName;
    }

    public String getText() {
        return text;
    }

    public String getSenderID() {
        return senderID;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public JSONObject toJson() {
        JSONObject outJson = new JSONObject();
        try {
            outJson.put("displayName", displayName);
            outJson.put("text", text);
            outJson.put("senderID", senderID);
            outJson.put("timestamp", timestamp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return outJson;
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
        dest.writeString(displayName);
        dest.writeString(text);
        dest.writeString(senderID);
        dest.writeDouble(timestamp);
    }
}
