package com.vivekvishwanath.android_lambdamessages;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    LinearLayout messageBoardsLayout;
    ArrayList<MessageBoard> messageBoards;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        messageBoardsLayout=findViewById(R.id.message_boards_layout);

        new Thread(new Runnable() {
            @Override
            public void run() {
                messageBoards = MessageBoardDao.getMessageBoards();
                for (final MessageBoard messageBoard : messageBoards) {
                    final TextView view = new TextView(context);
                    view.setText(messageBoard.getTitle());
                    view.setTextSize(20);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ViewMessagesActivity.class);
                            intent.putExtra("Message Board", messageBoard);
                            startActivity(intent);
                        }
                    });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            messageBoardsLayout.addView(view);
                        }
                    });
                }
            }
        }).start();
    }
}
