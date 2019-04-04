package com.jakeesveld.android_lambdamessages;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MessageBoardsDAO {
    private static final String URL_PREFIX = "https://lambda-message-board.firebaseio.com/";
    private static final String URL_SUFFIX = ".json";
    private static final String GET_ALL_URL = URL_PREFIX + URL_SUFFIX;
    private static String POST_NEW_MESSAGE = URL_PREFIX + "%s/" + "messages" + URL_SUFFIX;

    public MessageBoardsDAO(){

    }

    public static ArrayList<MessageBoard> getMessageBoards(){
        String result = NetworkAdapter.httpRequest(GET_ALL_URL);
        ArrayList<MessageBoard> messageBoards = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(result);
            for (Iterator<String> it = json.keys(); it.hasNext(); ) {
                String key = it.next();
                if(key.equals("-Lb_2nzrahrdW2G38H5u")) {
                    MessageBoard board = new MessageBoard(json.getJSONObject(key).getString("title"),
                            key,
                            json.getJSONObject(key));

                    messageBoards.add(board);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return messageBoards;
    }

    public void createNewMessage(final String jsonData){
        new Thread(new Runnable() {
            @Override
            public void run() {

        try {
            String result = NetworkAdapter.httpRequest(URL_PREFIX + MessageBoard.TOP_LEVEL_KEY + "/" + "messages" + URL_SUFFIX,
                    "POST",
                    new JSONObject(jsonData));

        } catch (JSONException e) {
            e.printStackTrace();
        }

            }
        }).start();
    }

}
