package com.example.lambda_messages;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MessageBoard{

String title, identifier;
ArrayList<Message> messages = new ArrayList<>();

    public MessageBoard(String title, String identifier, JSONObject jsonObject) {
        this.title = title;
        this.identifier = identifier;
        createMessageArr(jsonObject);
    }

    private void createMessageArr(JSONObject jsonObject) {
        String sender, text, messageKey;
        double timestamp;
            for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
                String key = it.next();
                try {
                    JSONObject jsonMessage = jsonObject.getJSONObject(key);
                    sender = jsonMessage.getString("sender");
                    text = jsonMessage.getString("text");
                    timestamp = jsonMessage.getDouble("timestamp");
                    messageKey = key;
                    messages.add(new Message(sender, text, timestamp,messageKey));
                }catch(JSONException e){e.printStackTrace();}


            }
    }

    public String getTitle() {
        return title;
    }

    public String getIdentifier() {
        return identifier;
    }
}
