package com.example.joshh.android_lambdamesageboard_services;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewMessagesActivity extends AppCompatActivity {
    ArrayList<Message> messages = new ArrayList<>();
    Context context;
    LinearLayout messagesLinearLayout;

    LinearLayout postLayout;
    EditText senderText, contentText;
    Button postButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_messages);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        messagesLinearLayout = findViewById(R.id.messages_linear_layout);
        context = this;
        postLayout = findViewById(R.id.post_layout);
        senderText = findViewById(R.id.sender_edit_text);
        contentText = findViewById(R.id.content_edit_text);
        postButton  = findViewById(R.id.post_button);

        final MessageBoard messageBoard = getIntent().getParcelableExtra(ScrollingActivity.MESSAGE_BOARD_KEY);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postLayout.setVisibility(View.VISIBLE);
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sender = senderText.getText().toString();
                String content = contentText.getText().toString();
                final Message message = new Message(sender, content);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MessageBoardDao.newMessage(messageBoard.getIdentifier(), message);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView tv = new TextView(context);
                                tv.setText(message.getSender() + ": " + message.getText());
                                tv.setTextSize(20);
                                tv.setTextColor(getResources().getColor(R.color.textColor));
                                messagesLinearLayout.addView(tv);
                            }
                        });
                    }
                }).start();

                postLayout.setVisibility(View.GONE);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                messages = messageBoard.getMessages(messageBoard.getIdentifier());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(Message m : messages){
                            TextView tv = new TextView(context);
                            tv.setText(m.getSender() + ": " +m.getText());
                            tv.setTextSize(20);
                            tv.setTextColor(getResources().getColor(R.color.textColor));
                            messagesLinearLayout.addView(tv);
                        }
                    }
                });
            }
        }).start();


    }
}

