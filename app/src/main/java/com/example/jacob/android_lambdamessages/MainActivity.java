package com.example.jacob.android_lambdamessages;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.facebook.stetho.Stetho;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int LAYOUT_SPAN_COUNT = 2;
    public static SharedPreferences preferences;

    private Context context;
    private Activity activity;
    private GridLayoutManager layoutManager;
    private RecyclerView listView;
    private MessageBoardListAdapter listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Stetho.initializeWithDefaults(this);

        context = this;
        activity = this;
        preferences = this.getPreferences(Context.MODE_PRIVATE);

        listView = findViewById(R.id.messageboard_recycler_view);
        layoutManager = new GridLayoutManager(context, LAYOUT_SPAN_COUNT);
        listView.setLayoutManager(layoutManager);
        ArrayList<MessageBoard> dummy = new ArrayList<>();
        listAdapter = new MessageBoardListAdapter(dummy, activity);
        listView.setAdapter(listAdapter);
        new offloadTask().execute();
    }


    public class offloadTask extends AsyncTask<Void, Integer, ArrayList<MessageBoard>> {

        @Override
        protected void onPostExecute(ArrayList<MessageBoard> messageBoards) {
            super.onPostExecute(messageBoards);
            if (messageBoards != null) {
                listAdapter = new MessageBoardListAdapter(messageBoards, activity);
                listView.setAdapter(listAdapter);
            }
        }

        @Override
        protected ArrayList<MessageBoard> doInBackground(Void... voids) {
            return MessageBoardDao.getAllMessageBoards();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new offloadTask().execute();
    }
}
