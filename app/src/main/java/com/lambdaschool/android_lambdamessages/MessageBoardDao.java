package com.lambdaschool.android_lambdamessages;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MessageBoardDao {

    private static final String URL_BASE = "https://lambda-message-board.firebaseio.com/";
    private static final String URL_MIDDLE = "-Lb_2nzrahrdW2G38H5u";
    private static final String URL_ENDING = ".json";
    private static final String URL_READ_ALL = URL_BASE + URL_ENDING;

    public ArrayList<MessageBoard> getMessageBoards() {
        ArrayList<MessageBoard> messageBoardArrayList = new ArrayList<>();
        String returnedJsonAsString = NetworkAdapter.httpRequest(URL_READ_ALL);

        try {
            JSONObject fullJson = new JSONObject(returnedJsonAsString);

            for (Iterator<String> it = fullJson.keys(); it.hasNext(); ) {
                String identifier = it.next();

                try {
                    JSONObject jsonJSONObject = fullJson.getJSONObject(identifier);

                    messageBoardArrayList.add(new MessageBoard(jsonJSONObject, identifier));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return messageBoardArrayList;
    }

    public static Message newMessage(String identifier, Message message) {
        String url = URL_BASE + identifier + "/" + URL_ENDING;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sender", message.getSender());
            jsonObject.put("text", message.getText());
            jsonObject.put("timestamp", (System.currentTimeMillis() / 1000));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String returnedJsonAsString = NetworkAdapter.httpRequest(url, NetworkAdapter.REQUEST_POST, jsonObject, null);
        try {
            jsonObject = new JSONObject(returnedJsonAsString);
            message.setId(jsonObject.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return message;
    }
}
