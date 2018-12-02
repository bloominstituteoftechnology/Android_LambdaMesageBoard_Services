package com.example.jacob.android_lambdamessages;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class NewMessageActivity extends AppCompatActivity {
    public static final String NEW_MESSAGE_KEY = "new_message";
    EditText sender, content;
    Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        sender = findViewById(R.id.edit_text_nm_sender);
        content = findViewById(R.id.edit_text_nm_text);

        final String boardId = getIntent().getStringExtra(Constants.MESSAGE_BOARD_KEY);

        findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String senderName = sender.getText().toString();
                if (senderName.equals("")) {
                    senderName = "Anonymous";
                }
                message = new Message("", "", "-1", -1);
                message.setSender(senderName);
                message.setText(content.getText().toString());
                message.setTimestamp(System.currentTimeMillis() / 1000);
                Intent intent = new Intent();
                intent.putExtra(NEW_MESSAGE_KEY, message);
                intent.putExtra(Constants.MESSAGE_BOARD_KEY, boardId);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

}