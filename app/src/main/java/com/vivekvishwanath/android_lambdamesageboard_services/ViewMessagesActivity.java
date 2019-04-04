package com.vivekvishwanath.android_lambdamessages;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewMessagesActivity extends AppCompatActivity {

    ArrayList<Message> messages = new ArrayList<>();
    Context context;
    LinearLayout messageViewLayout;
    EditText senderEditText;
    EditText textEditText;
    Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_messages);
        context = this;

        messageViewLayout = findViewById(R.id.message_view_layout);
        senderEditText = findViewById(R.id.sender_edit_text);
        textEditText = findViewById(R.id.text_edit_text);
        sendButton = findViewById(R.id.send_button);

        final MessageBoard messageBoard = getIntent().getParcelableExtra("Message Board");
        final String identifier = messageBoard.getIdentifier();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Message newMessage = new Message(senderEditText.getText().toString(), textEditText.getText().toString());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MessageBoardDao.postMessage(identifier, newMessage);
                    }
                }).start();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                messages = messageBoard.getMessages();
                for (Message message : messages) {
                    final TextView view = new TextView(context);
                    view.setText(message.getText());
                    view.setTextSize(20);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            messageViewLayout.addView(view);
                        }
                    });
                }
            }
        }).start();
    }
}
