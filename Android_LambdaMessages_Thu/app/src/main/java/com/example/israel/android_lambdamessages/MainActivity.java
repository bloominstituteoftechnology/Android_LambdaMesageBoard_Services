package com.example.israel.android_lambdamessages;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String NOTIFICATION_CHANNEL_ID = "id:1";
    public static final String NOTIFICATION_NAME = "name:1";
    private int notificationId;

    private MessageBoardsAdapter messageBoardsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_message_boards);
        recyclerView.setHasFixedSize(true); // this relates to the width and height. optimization

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        messageBoardsAdapter = new MessageBoardsAdapter();
        recyclerView.setAdapter(messageBoardsAdapter);

        final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action == null) {
                    return;
                }

                if (action.equals(SubscriptionMonitorService.ACTION_NEW_MESSAGE)) {
                    MessageBoard messageBoard = intent.getParcelableExtra(MessageBoardsAdapter.KEY_EXTRA_MESSAGE_BOARD);
                    postNewMessageNotification(messageBoard);
                }
            }

            // notification is ThreadSafe, this is just for the project
            private void postNewMessageNotification(MessageBoard messageBoard) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // channel
                    NotificationManager notificationManager = (NotificationManager) getSystemService(MainActivity.NOTIFICATION_SERVICE);
                    NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_NAME, NotificationManager.IMPORTANCE_HIGH);
                    channel.setDescription("desc: This is an example notification");
                    notificationManager.createNotificationChannel(channel);

                    // builder
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, NOTIFICATION_CHANNEL_ID)
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setContentTitle("Message received")
                            .setContentText(messageBoard.getTitle())
                            .setSmallIcon(R.drawable.ic_message_black_24dp)
                            .setColor(Color.argb(255, 255, 255, 0))
                            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

                    notificationManager.notify(notificationId++, builder.build());
                }
            }
        };

        // this can be done in the manifest too
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SubscriptionMonitorService.ACTION_NEW_MESSAGE);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);

        // display message boards
        retrieveMessageBoards();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        retrieveMessageBoards();
    }

    private void retrieveMessageBoards() {
        MessageBoardRequestAsyncTask messageBoardRequestAsyncTask = new MessageBoardRequestAsyncTask();
        messageBoardRequestAsyncTask.execute();
    }

    private void updateUi(ArrayList<MessageBoard> messageBoards) {
        messageBoardsAdapter.setMessageBoards(messageBoards);
    }

    private class MessageBoardRequestAsyncTask extends AsyncTask<Void, Void, ArrayList<MessageBoard>> {

        @Override
        protected ArrayList<MessageBoard> doInBackground(Void... voids) {
            return MessageBoardNetworkDAO.getMessageBoards();
        }

        @Override
        protected void onPostExecute(ArrayList<MessageBoard> messageBoards) {
            super.onPostExecute(messageBoards);

            updateUi(messageBoards);
        }
    }
}
