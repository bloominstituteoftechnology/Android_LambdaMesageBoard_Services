package com.example.israel.android_lambdamessages;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class SubscriptionMonitorService extends Service {
    public static final String TAG = "MyDebug";

    public static final String ACTION_NEW_MESSAGE = "com.example.israel.android_lambdamessages_subscription_new_message";

    private Long lastCheckTime;
    private ArrayList<String> subscriptions = new ArrayList<>();
    private final Object subscriptionsSync = new Object();

    private AtomicBoolean isRunning = new AtomicBoolean(false);
    private Thread thread = new Thread() {
        @Override
        public void run() {
            super.run();

            if (!isRunning.get()) { // initial check
                return;
            }

            do {
                String[] subscriptionsCopy;
                synchronized (subscriptionsSync) {
                    // copy subscriptions so we never bogged down the main thread
                    subscriptionsCopy = subscriptions.toArray(new String[subscriptions.size()]);
                }

                if (subscriptionsCopy.length != 0) { // at least one subscription
                    ArrayList<MessageBoard> messageBoards = MessageBoardNetworkDAO.getMessageBoards();

                    for (String subscription : subscriptionsCopy) {
                        for (MessageBoard messageBoard : messageBoards) {
                            if (messageBoard.getIdentifier().equals(subscription)) {
                                for (Message message : messageBoard.getMessages()) {
                                    if (message.getTimestamp() > lastCheckTime) { // there's a new message since we last checked
                                        broadcastNewMessagePosted(messageBoard);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

                lastCheckTime = System.currentTimeMillis() / 1000;

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } while (isRunning.get());
        }
    };

    public SubscriptionMonitorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "SubscriptionMonitorService.onBind");

        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "SubscriptionMonitorService.onCreate");
        super.onCreate();

        lastCheckTime = System.currentTimeMillis() / 1000;
        isRunning.set(true);
        thread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "SubscriptionMonitorService.onStartCommand");

        MessageBoard messageBoard = intent.getParcelableExtra(MessageBoardsAdapter.KEY_EXTRA_MESSAGE_BOARD);
        String subscription = messageBoard.getIdentifier();

        if (intent.getBooleanExtra(MessageBoardsAdapter.KEY_EXTRA_IS_SUBSCRIBING, false)) {
            // add subscription
            synchronized (subscriptionsSync) {
                subscriptions.add(subscription);
            }
        } else {
            // remove subscription
            synchronized (subscriptionsSync) {
                subscriptions.remove(subscription);
            }
        }


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "SubscriptionMonitorService.onDestroy");
        isRunning.set(false);
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        super.onDestroy();
    }

    private void broadcastNewMessagePosted(MessageBoard messageBoard) {
        Intent intent = new Intent(ACTION_NEW_MESSAGE);
        intent.putExtra(MessageBoardsAdapter.KEY_EXTRA_MESSAGE_BOARD, messageBoard);
        LocalBroadcastManager.getInstance(SubscriptionMonitorService.this).sendBroadcast(intent);

    }
}
