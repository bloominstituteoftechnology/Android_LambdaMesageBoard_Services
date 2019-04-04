package com.example.israel.android_lambdamessages;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MessageBoardNetworkDAO {

    public static final String BASE_URL = "https://lambda-http-requests.firebaseio.com/";

    @WorkerThread
    @NonNull
    static ArrayList<MessageBoard> getMessageBoards() {
        String messageBoardsJsonStr = NetworkAdapter.httpRequestGET(BASE_URL + ".json");
        ArrayList<MessageBoard> messageBoards = new ArrayList<>();
        if (messageBoardsJsonStr == null) {
            return messageBoards;
        }

        try {
            JSONObject messageBoardsJson = new JSONObject(messageBoardsJsonStr);
            JSONArray messageBoardKeyArr = messageBoardsJson.names();
            if (messageBoardKeyArr != null) {
                messageBoards.ensureCapacity(messageBoardKeyArr.length()); // pre allocate
                for (int i = 0; i < messageBoardKeyArr.length(); ++i) {
                    String messageBoardKey = messageBoardKeyArr.getString(i);
                    JSONObject messageBoardJson = messageBoardsJson.getJSONObject(messageBoardKey);
                    messageBoards.add(new MessageBoard(messageBoardJson));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return messageBoards;
    }


}
