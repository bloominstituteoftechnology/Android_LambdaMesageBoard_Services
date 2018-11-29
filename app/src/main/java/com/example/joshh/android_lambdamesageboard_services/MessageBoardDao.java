package com.example.joshh.android_lambdamesageboard_services;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MessageBoardDao {
    private static final String BASE_URL = "https://lambda-message-board.firebaseio.com/";
    private static final String URL_ENDING = ".json";
    private static final String MESSAGES = "messages/";
    private static final String URL = BASE_URL + URL_ENDING;

    public static final String MESSAGE_BOARD_URL = BASE_URL + "%s/" + MESSAGES + URL_ENDING;

    public static ArrayList<MessageBoard> getMessageBoards(){
        ArrayList<MessageBoard> messageBoards = new ArrayList<>();
        final String result = NetworkAdapter.httpRequest(URL, NetworkAdapter.GET);
        try {
            JSONObject topLevel = new JSONObject(result);
            JSONArray messageBoardIds = topLevel.names();
            for(int i = 0; i < messageBoardIds.length(); i++){
                messageBoards.add(new MessageBoard(topLevel.getJSONObject(messageBoardIds.getString(i)), messageBoardIds.getString(i)));
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return messageBoards;
    }

    public static void newMessage(String messageBoardId, Message message){
        String messageBoardUrl = String.format(MESSAGE_BOARD_URL, messageBoardId);
        JSONObject json = new JSONObject();
        try {
            json.put("text", message.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            json.put("sender", message.getSender());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            json.put("timestamp", System.currentTimeMillis()/1000);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonString = json.toString();
        final String result = NetworkAdapter.httpRequest(messageBoardUrl, NetworkAdapter.POST, jsonString);
        try {
            JSONObject jsonObject = new JSONObject(result);
            message.setId(jsonObject.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

