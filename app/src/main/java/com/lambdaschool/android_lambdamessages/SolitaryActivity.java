package com.lambdaschool.android_lambdamessages;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class SolitaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solitary);

        ArrayList<Message> messageArrayList = getIntent().getParcelableArrayListExtra("messageboard");

        final LinearLayout linearLayout = findViewById(R.id.linear_layout_solitary);

        for (final Message eachM : messageArrayList) {
            final TextView textView = new TextView(getApplicationContext());
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setPadding(5, 30, 5, 0);
            String formattedMessageBoard = "Sender: %s | Time: %d | id: %s | Text: %s";
            textView.setText(String.format(Locale.US, formattedMessageBoard, eachM.getSender(), (int) eachM.getTimestamp(), eachM.getId(), eachM.getText()));
            linearLayout.addView(textView);
        }

        Button buttonPost = findViewById(R.id.button_post);
        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String sender = ((EditText) findViewById(R.id.edit_text_sender)).getText().toString();
                final String text = ((EditText) findViewById(R.id.edit_text_text)).getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Message message = MessageBoardDao.newMessage("-Lb_2nzrahrdW2G38H5u/messages", new Message(sender, text));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView textView = new TextView(getApplicationContext());
                                textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                textView.setPadding(5, 30, 5, 0);
                                String formattedMessageBoard = "Sender: %s | Time: %d | id: %s | Text: %s";
                                textView.setText(String.format(Locale.US, formattedMessageBoard, message.getSender(), (int) message.getTimestamp(), message.getId(), message.getText()));
                                linearLayout.addView(textView);
                            }
                        });
                    }
                }).start();

            }
        });
    }
}
