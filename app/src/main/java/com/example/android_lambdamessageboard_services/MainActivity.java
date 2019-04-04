package com.example.android_lambdamessageboard_services;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android_lambdamessages.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<MessageBoard> messageBoards = new ArrayList<>();
    Context context;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        context = this;
        linearLayout = findViewById(R.id.message_board_linear_layout);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                messageBoards = MessageBoardDao.getAllBoards();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(final MessageBoard m : messageBoards){
                            final TextView tv = new TextView(context);
                            tv.setText(m.title);
                            if(m.title == null || m.title.equals("")){
                                tv.setText(m.identifier);
                            }
                            tv.setTextSize(20);
                            tv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                            tv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent viewMessagesIntent = new Intent(context, ViewMessagesActivity.class);
                                    viewMessagesIntent.putExtra("message_board_key", m);
                                    startActivity(viewMessagesIntent);
                                }
                            });
                            linearLayout.addView(tv);
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
