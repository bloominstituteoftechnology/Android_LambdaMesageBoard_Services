package com.vivekvishwanath.android_lambdamesageboard_services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MessageBoardDao {

    private static final String BASE_URL = "https://lambda-message-board.firebaseio.com/";
    private static final String URL_ENDING = ".json";
    private static final String MESSAGES_URL = "messages";

    private static final String PUT_MESSAGE_URL = BASE_URL + "%s/" + MESSAGES_URL + URL_ENDING;



    public static ArrayList<MessageBoard> getMessageBoards() {
        ArrayList<MessageBoard> messageBoards = new ArrayList<>();
        String result = NetworkAdapter.httpGETRequest(BASE_URL + URL_ENDING);

        try {
            // topLevel unwraps first curl
            JSONObject topLevel = new JSONObject(result);
            JSONArray messageBoardKeys = topLevel.names();
            for (int i = 0; i < messageBoardKeys.length(); i++) {
                JSONObject messageBoardJSONObject = topLevel.getJSONObject(messageBoardKeys.getString(i));
                messageBoards.add(new MessageBoard(messageBoardJSONObject, messageBoardKeys.getString(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return messageBoards;
    }

    public static void postMessage(String messageBoardKey, Message message) {
        String url = String.format(PUT_MESSAGE_URL, messageBoardKey);
        JSONObject messageJSON = new JSONObject();
        try {
            messageJSON.put("sender", message.getSender());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            messageJSON.put("text", message.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            messageJSON.put("timestamp", System.currentTimeMillis() / 1000);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String result = NetworkAdapter.httpRequest(url, NetworkAdapter.POST, messageJSON);
    }
}
