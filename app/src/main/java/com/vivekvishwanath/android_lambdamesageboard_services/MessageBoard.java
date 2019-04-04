package com.vivekvishwanath.android_lambdamessages;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MessageBoard implements Parcelable {

    private String title, identifier;
    private ArrayList<Message> messages = new ArrayList<>();

    public MessageBoard(String title, String identifer) {
        this.title = title;
        this.identifier = identifer;
    }

    public MessageBoard(JSONObject messageBoardJSON, String identifier) {
        this.identifier = identifier;
        try {
            this.title = messageBoardJSON.getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONArray messageJSONArray = messageBoardJSON.getJSONObject("messages").names();
            for (int i = 0; i < messageJSONArray.length(); i++) {
                JSONObject messageJSONObject = messageBoardJSON.getJSONObject("messages").getJSONObject(messageJSONArray.getString(i));
                System.out.println(messageJSONObject);
                messages.add(new Message(messageJSONObject));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public MessageBoard(Parcel parcel) {
        this.title = parcel.readString();
        this.identifier = parcel.readString();
        Object[] parcelArray = parcel.readArray(Message.class.getClassLoader());
        messages = new ArrayList<>();
        for (int i = 0; i < parcelArray.length; i++) {
            messages.add((Message) parcelArray[i]);
        }
    }

    public static final Creator<MessageBoard> CREATOR = new Creator<MessageBoard>() {
        @Override
        public MessageBoard createFromParcel(Parcel in) {
            return new MessageBoard(in);
        }

        @Override
        public MessageBoard[] newArray(int size) {
            return new MessageBoard[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getIdentifier() {
        return identifier;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.identifier);
        dest.writeArray(this.messages.toArray());
    }
}
