package com.example.israel.android_lambdamessages;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MessagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_messages);
        recyclerView.setHasFixedSize(true); // this relates to the width and height. optimization

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        final MessageBoard messageBoard = intent.getParcelableExtra(MessageBoardsAdapter.KEY_EXTRA_MESSAGE_BOARD);
        TextView titleTextView = findViewById(R.id.text_view_messages_message_board_title);
        titleTextView.setText(messageBoard.getTitle());

        final MessagesAdapter messagesAdapter = new MessagesAdapter(messageBoard.getMessages());
        recyclerView.setAdapter(messagesAdapter);

        Button sendMessageButton = findViewById(R.id.button_send_message);
        final EditText textEditText = findViewById(R.id.edit_text_text);
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textStr = textEditText.getText().toString();
                if (textStr.length() != 0) {
                    Message newMessage = new Message("name", textStr, "1");
                    new SendMessageAsyncTask(messageBoard, newMessage).execute();
                    messagesAdapter.addMessage(newMessage);
                    textEditText.setText("");
                }
            }
        });
    }

    private class SendMessageAsyncTask extends AsyncTask<Void, Void, String> {

        public SendMessageAsyncTask(MessageBoard messageBoard, Message message) {
            this.messageBoard = messageBoard;
            this.message = message;
        }

        private MessageBoard messageBoard;
        private Message message;

        @Override
        protected String doInBackground(Void... voids) {

            return MessageNetworkDAO.sendMessage(messageBoard, message);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }
    }
}
