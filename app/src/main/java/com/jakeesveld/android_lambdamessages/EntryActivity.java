package com.jakeesveld.android_lambdamessages;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EntryActivity extends AppCompatActivity {

    EditText editTextSender;
    EditText editTextContent;
    Button buttonSubmit;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        editTextContent = findViewById(R.id.edit_text_content);
        editTextSender = findViewById(R.id.edit_text_sender);
        buttonSubmit = findViewById(R.id.button_submit);
        context = this;

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sender = editTextSender.getText().toString();
                String content = editTextContent.getText().toString();

                Message newMessage = new Message(sender, content);
                MessageBoardsDAO dao = new MessageBoardsDAO();
                dao.createNewMessage(newMessage.toJson());
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
