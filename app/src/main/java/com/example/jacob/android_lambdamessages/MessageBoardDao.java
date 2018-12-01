package com.example.jacob.android_lambdamessages;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class MessageBoardDao {
    private static final String BASE_URL = "https://lambda-message-board.firebaseio.com/";
    private static final String MESSAGE = "messages/";
    private static final String URL_ENDING = ".json";

    private static final String BOARDS_URL = BASE_URL + URL_ENDING;
    private static final String MESSAGE_URL = BASE_URL + "%s/" + MESSAGE + URL_ENDING;


    public static ArrayList<MessageBoard> getMessageBoards() {
        ArrayList<MessageBoard> boards = new ArrayList<>();
        final String result = NetworkAdapter.httpRequest(BOARDS_URL, NetworkAdapter.GET);

        try {
            JSONObject topLevel = new JSONObject(result);
            JSONArray boardNames = topLevel.names();
            for (int i = 0; i < boardNames.length(); ++i) {
                final String id = boardNames.getString(i);
                final String title = topLevel.getJSONObject(id).getString("title");
                boards.add(new MessageBoard(title, id));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return boards;
    }

    public static ArrayList<Message> getMessages(String identifier) {
        ArrayList<Message> messages = new ArrayList<>();
        final String result = NetworkAdapter.httpRequest(String.format(MESSAGE_URL, identifier), NetworkAdapter.GET);

        try {
            JSONObject topLevel = new JSONObject(result);
            JSONArray boardNames = topLevel.names();
            for (int i = 0; i < boardNames.length(); ++i) {
                final String id = boardNames.getString(i);
                JSONObject json = topLevel.getJSONObject(id);
                messages.add(new Message(json, id));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public static void newMessage(String boardId, Message message) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sender", message.getSender());
            jsonObject.put("text", message.getText());
            jsonObject.put("timestamp", message.getTimestamp());
            NetworkAdapter.httpRequest(String.format(MESSAGE_URL, boardId), NetworkAdapter.POST,jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
