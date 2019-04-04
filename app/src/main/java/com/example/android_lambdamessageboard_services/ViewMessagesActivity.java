package com.example.android_lambdamessageboard_services;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android_lambdamessages.R;

import java.util.ArrayList;

public class ViewMessagesActivity extends AppCompatActivity {
    ArrayList<Message> messages = new ArrayList<>();
    Context context;
    LinearLayout messageLayout;

    LinearLayout postLayout;
    EditText senderEditText, messageEditText;
    Button addNewMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_messages);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        messageLayout = findViewById(R.id.messages_linear_layout);
        context = this;
        postLayout = findViewById(R.id.post_layout);
        senderEditText = findViewById(R.id.sender_edit_text);
        messageEditText = findViewById(R.id.content_edit_text);
        addNewMessage = findViewById(R.id.add_new_message);

        final MessageBoard messageBoard = getIntent().getParcelableExtra("message_board_key");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postLayout.setVisibility(View.VISIBLE);
            }
        });
        // here is where you can send a message
        addNewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sender = senderEditText.getText().toString();
                String content = messageEditText.getText().toString();
                final Message message = new Message(sender, content);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MessageBoardDao.newMessage(messageBoard.getIdentifier(), message);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView textView = new TextView(context);
                                textView.setText(message.getSender() + ": " + message.getText());
                                textView.setTextSize(20);
                                messageLayout.addView(textView);
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
                messages = messageBoard.getMessages(messageBoard.identifier);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(Message message : messages){
                            TextView textView = new TextView(context);
                            textView.setText(message.getSender() + ": " +message.getText());
                            textView.setTextSize(20);
                            messageLayout.addView(textView);
                        }
                    }
                });
            }
        }).start();


    }
}