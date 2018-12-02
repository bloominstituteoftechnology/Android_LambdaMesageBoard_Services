package com.example.jacob.android_lambdamessages;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class MessageBoardDao {
    private static final String BASE_URL = "https://lambda-message-board.firebaseio.com/";
    private static final String MESSAGE = "messages/";
    private static final String URL_ENDING = ".json";

    private static final String BOARDS_URL = BASE_URL + URL_ENDING;
    private static final String BOARD_URL = BASE_URL + "%s/" + URL_ENDING;
    private static final String MESSAGE_URL = BASE_URL + "%s/" + MESSAGE + URL_ENDING;


    public static ArrayList<MessageBoard> getAllMessageBoards() {
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
        MessageBoard board;
        board = getBoard(identifier);
        if (board != null) {
            messages = board.getMessages();
        }
        return messages;
    }

    public static MessageBoard getBoard(String identifier) {
        MessageBoard board = null;
        String title;
        ArrayList<Message> messages = new ArrayList<>();
        final String result = NetworkAdapter.httpRequest(String.format(BOARD_URL, identifier), NetworkAdapter.GET);

        try {
            JSONObject topLevel = new JSONObject(result);
            title = topLevel.getString("title");
            JSONObject jsonObject = topLevel.getJSONObject("messages");
            JSONArray boardNames = jsonObject.names();
            for (int i = 0; i < boardNames.length(); ++i) {
                final String id = boardNames.getString(i);
                JSONObject json = jsonObject.getJSONObject(id);
                messages.add(new Message(json, id));
            }
            Collections.reverse(messages);
            board = new MessageBoard(title, identifier, messages);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return board;
    }

    public static void newMessage(String boardId, Message message) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sender", message.getSender());
            jsonObject.put("text", message.getText());
            jsonObject.put("timestamp", message.getTimestamp());
            NetworkAdapter.httpRequest(String.format(MESSAGE_URL, boardId), NetworkAdapter.POST, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getMessageBoardIds() {
        ArrayList<String> ids = new ArrayList<>();
        ArrayList<MessageBoard> boards = getAllMessageBoards();
        for (MessageBoard board : boards) {
            ids.add(board.getIdentifier());
        }
        return ids;
    }

}
