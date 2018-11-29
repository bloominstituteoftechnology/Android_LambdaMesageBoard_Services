package com.example.joshh.android_lambdamessages;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MessageBoard implements Parcelable{
    String title, identifier;
    ArrayList<Message> messages;

    public MessageBoard(JSONObject jsonObject, String identifier) {
        //this.title = title;
        this.identifier = identifier;
        try {
            this.title = jsonObject.getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected MessageBoard(Parcel in) {
        title = in.readString();
        identifier = in.readString();
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIdentifier() {
        return identifier;
    }

    public ArrayList<Message> getMessages(String id) {
        final String result = NetworkAdapter.httpRequest(String.format(MessageBoardDao.MESSAGE_BOARD_URL, id), NetworkAdapter.GET);
        messages = new ArrayList<>();
        try {
            JSONObject topLevel = new JSONObject(result);
            JSONArray messageIds = topLevel.names();
            for(int i = 0; i < messageIds.length(); i++){
                messages.add(new Message(topLevel.getJSONObject(messageIds.getString(i)), messageIds.getString(i)));
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    return messages;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(identifier);
    }
}
