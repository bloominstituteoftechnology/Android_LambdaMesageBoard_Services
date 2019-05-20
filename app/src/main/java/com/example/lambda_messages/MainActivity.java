package com.example.lambda_messages;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<MessageBoard> arList;
        ArrayList<Message> msList;

        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<MessageBoard> arList = MessageBoardDao.getMessageBoards();
              //  ArrayList<Message> msList = MessageBoardDao.getMessages(arList.get(0).getIdentifier());
            }
        }).start();


        Log.i("test2", "blehh");
    }
}
