package com.example.jacob.android_lambdamessages;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MessageBoard implements Parcelable {
    private String title, identifier;
    private ArrayList<Message> messages;
//    JSONObject json;

    MessageBoard(String title, String identifier) {
        this.title = title;
        this.identifier = identifier;
        this.messages = MessageBoardDao.getMessages(identifier);
    }

    MessageBoard(String identifier) {
        this.identifier = identifier;
        MessageBoard board = MessageBoardDao.getBoard(identifier);
        this.title = board.title;
        this.messages = board.messages;
    }

    MessageBoard(String title, String identifier, ArrayList<Message> messages) {
        this.title = title;
        this.identifier = identifier;
        this.messages = messages;
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

    private MessageBoard(Parcel parcel) {
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

    static double getBoardLastMessageTimestamp(String identifier) {
        double messageTimestamp;
        ArrayList<Message> messages = MessageBoardDao.getMessages(identifier);
        messageTimestamp = messages.get(0).getTimestamp();
        return messageTimestamp;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
