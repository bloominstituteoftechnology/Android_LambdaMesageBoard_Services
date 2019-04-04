package com.lambdaschool.android_lambdamessages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<MessageBoard> messageBoardArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                MessageBoardDao messageBoardDao = new MessageBoardDao();
                messageBoardArrayList = messageBoardDao.getMessageBoards();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LinearLayout linearLayout = findViewById(R.id.linear_layout_main);

                        for (final MessageBoard eachMB : messageBoardArrayList) {
                            TextView textView = new TextView(getApplicationContext());
                            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            textView.setPadding(5, 30, 5, 0);
                            textView.setText(eachMB.getTitle() + " ... " + eachMB.getIdentifier());

                            textView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(), SolitaryActivity.class);
                                    intent.putParcelableArrayListExtra("messageboard", eachMB.getMessages());
                                    startActivity(intent);
                                }
                            });
                            linearLayout.addView(textView);
                        }
                    }
                });
            }
        }).start();
    }
}
