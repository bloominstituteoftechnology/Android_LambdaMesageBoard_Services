package com.example.lambda_messages;

import android.widget.BaseExpandableListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MessageBoardDao {
    private static String BASE_URL = "https://lambda-message-board.firebaseio.com";
    private static String URL_ENDING = "/.json";
    private static String READ_ALL_MESSAGES = BASE_URL + "/%s" + URL_ENDING;
    private static String READ_ALL_MESSAGE_BOARDS = BASE_URL + URL_ENDING;

    public static ArrayList<MessageBoard> getMessageBoards() {

        final ArrayList<MessageBoard> resultList = new ArrayList<>();

        final String result = NetworkAdapter.httpRequest(READ_ALL_MESSAGE_BOARDS);
        try {
            JSONObject jsonObject = new JSONObject(result);
            for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
                String key = it.next();
                try {
                    final JSONObject jsonEntry = jsonObject.getJSONObject(key);
                    String title = jsonEntry.getString("title");
                    String id = key;



                    resultList.add(new MessageBoard(title, id, messageJsonObject));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultList;
    }


    public static ArrayList<Message> getMessages(String identifier) {

        final ArrayList<Message> resultList = new ArrayList<>();

        final String result = NetworkAdapter.httpRequest(String.format(READ_ALL_MESSAGES, identifier));
        try {
            JSONObject jsonObject = new JSONObject(result);
            for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
                String key = it.next();
                try {
                    final JSONObject jsonEntry = jsonObject.getJSONObject(key);
                    String senderId = null;
                    String displayName = null;
                    String sender = null;
                    String text = jsonEntry.getString("text");
                    double timestamp = jsonEntry.getDouble("timestamp");
                    String keyID = key;
                    if (jsonEntry.getString("sender") == null) {
                         senderId = jsonEntry.getString("senderID");
                        displayName = jsonEntry.getString("displayName");
                        resultList.add(new Message(displayName, senderId,text,timestamp,keyID));
                    } else{sender = jsonEntry.getString("sender");}

                    resultList.add(new Message(sender, text, timestamp, keyID));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultList;
    }
}


