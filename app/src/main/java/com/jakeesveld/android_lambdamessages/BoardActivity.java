package com.jakeesveld.android_lambdamessages;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class BoardActivity extends AppCompatActivity {
    MessageListAdapter listAdapter;
    Context context;
    Button buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        context = this;
        buttonAdd = findViewById(R.id.button_add_new_message);
        Intent intent = getIntent();
        ArrayList<Message> messages = intent.getParcelableArrayListExtra("board");
        listAdapter = new MessageListAdapter(messages);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(listAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newMessageIntent = new Intent(context, EntryActivity.class);
                startActivity(newMessageIntent);
            }
        });

    }

}
