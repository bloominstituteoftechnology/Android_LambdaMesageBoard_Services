package com.jakeesveld.android_lambdamessages;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    LinearLayout layoutList;
    ArrayList<MessageBoard> boardOfBoards;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layoutList = findViewById(R.id.layout_list);
        context = this;
        boardOfBoards = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<MessageBoard> boards = MessageBoardsDAO.getMessageBoards();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        boardOfBoards = boards;
                        populateUI(boards);
                    }
                });
            }
        }).start();
    }

    public void populateUI(ArrayList<MessageBoard> boards){
        layoutList.removeAllViews();
        for(MessageBoard board: boards){
            layoutList.addView(createTextView(board));
        }
    }


    public TextView createTextView (final MessageBoard board){
        TextView view = new TextView(context);
        view.setText(board.getTitle());
        view.setTextSize(24);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BoardActivity.class);
                intent.putParcelableArrayListExtra("board", board.getMessagesList());
                startActivity(intent);
            }
        });
        view.setLongClickable(true);
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(context, MessageBoardService.class);
                intent.putExtra("AddService", board.getTitle());
                startService(intent);
                return false;
            }
        });
        return view;
    }
}
