package com.jakeesveld.android_lambdamessages;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class MessageBoard implements Parcelable {
    private String title, identifier;
    private ArrayList<Message> messagesList;
    public static final String TOP_LEVEL_KEY = "-Lb_2nzrahrdW2G38H5u";


    public MessageBoard(String title, String identifier, JSONObject message) {
        this.title = title;
        this.identifier = identifier;
        JSONObject messages = null;
        messagesList = new ArrayList<>();
        try {
            messages = message.getJSONObject("messages");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (Iterator<String> it = messages.keys(); it.hasNext(); ) {
            String key = it.next();

                try {
                    Integer timeStamp = messages.getJSONObject(key).getInt("timestamp");
                    messagesList.add(new Message(messages.getJSONObject(key).getString("sender"), messages.getJSONObject(key).getString("text"), key, timeStamp));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

        }
    }

    public MessageBoard(JSONObject json, String identifier){
        try {
            this.identifier = json.getJSONObject(TOP_LEVEL_KEY).getString("identifier");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.title = json.getJSONObject(TOP_LEVEL_KEY).getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONObject messages = json.getJSONObject(TOP_LEVEL_KEY).getJSONObject("messages");
            for (Iterator<String> it = messages.keys(); it.hasNext(); ) {
                String key = it.next();
                JSONObject messageJson = json.getJSONObject(key);
                Message newMessage = new Message(messageJson.getString("sender"), messageJson.getString("text"));
                messagesList.add(newMessage);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getTitle() {
        return title;
    }

    public String getIdentifier() {
        return identifier;
    }

    public ArrayList<Message> getMessagesList() {
        return this.messagesList;
    }

    protected MessageBoard(Parcel in) {
        title = in.readString();
        identifier = in.readString();
        Message[] messageArray = (Message[]) in.readArray(Message.class.getClassLoader());
        for(Message message: messageArray){
            messagesList.add(message);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(identifier);
        dest.writeArray(messagesList.toArray());
    }
}

/* "-LQuzp_LDuYHLe3MDLva": {
         "messages": {
         "-LQv-QhtT6JHsI1r_XwP": {
         "sender": "Chance",
         "text": "Inaugural Android Message",
         "timestamp": 1541809486
         },
         "-LQv-VoXq9fnkka-jLQe": {
         "sender": "Chance",
         "text": "First",
         "timestamp": 1541809507
         }
         },
         "title": "Android Thread"
         }*/
