package com.example.jacob.android_lambdamessages;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MessageBoard implements Parcelable {
    String title, identifier;
    ArrayList<Message> messages;
//    JSONObject json;

    public MessageBoard(String title, String identifier) {
        this.title = title;
        this.identifier = identifier;
        this.messages = MessageBoardDao.getMessages(identifier);
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
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(title);
        parcel.writeString(identifier);
        parcel.writeArray(messages.toArray());
    }

    public MessageBoard(Parcel parcel) {
        this.title = parcel.readString();
        this.identifier = parcel.readString();
        Object[] objects = parcel.readArray(Message.class.getClassLoader());
        ArrayList<Message> messageList = new ArrayList<>();
        Message message;
        for (Object object : objects) {
            message = (Message) object;
            messageList.add(message);
        }
        this.messages = messageList;
    }
}
