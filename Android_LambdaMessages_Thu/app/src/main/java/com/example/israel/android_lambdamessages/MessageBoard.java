package com.example.israel.android_lambdamessages;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class MessageBoard implements Parcelable {

    public MessageBoard(JSONObject messageBoardJson) {
        messages = new ArrayList<>();
        try {
            this.title = messageBoardJson.getString("title");
            this.identifier = messageBoardJson.getString("identifier");

            JSONObject messagesJson = messageBoardJson.getJSONObject("messages");
            JSONArray messageKeyArr = messagesJson.names();
            if (messageKeyArr != null) {
                for (int i = 0; i < messagesJson.length(); ++i) {
                    String messageKey = messageKeyArr.getString(i);
                    if (messageKey.equals("-placeholder")) {
                        continue; // this is a placeholder for messages
                    }
                    JSONObject messageJson = messagesJson.getJSONObject(messageKey);
                    messages.add(new Message(messageJson));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public MessageBoard(Parcel parcel) {
        this.title = parcel.readString();
        this.identifier = parcel.readString();
        Object[] messageObjs = parcel.readArray(Message.class.getClassLoader());
        if (messageObjs == null) {
            this.messages = new ArrayList<>();
            return;
        }
        this.messages = new ArrayList<>(messageObjs.length);
        for (Object messageObj : messageObjs) {
            this.messages.add((Message) messageObj);
        }
    }

    private String title;
    private String identifier;
    private ArrayList<Message> messages;

    public String getTitle() {
        return title;
    }

    public String getIdentifier() {
        return identifier;
    }

    public ArrayList<Message> getMessages() {
        return messages;
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
        dest.writeArray(messages.toArray());
    }
}
