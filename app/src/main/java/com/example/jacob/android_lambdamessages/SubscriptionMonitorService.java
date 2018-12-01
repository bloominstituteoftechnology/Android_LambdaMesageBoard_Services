package com.example.jacob.android_lambdamessages;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

public class SubscriptionMonitorService extends Service {

    public static final int CHECK_PERIOD = 60000;
    private Long lastCheckTime;
    String subscription;

    public SubscriptionMonitorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Subscription", "onCreate: Method entered");
        lastCheckTime = System.currentTimeMillis() / 1000;
        subscription = "";
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Subscription", "onStartCommand:  Method entered");
        subscription = intent.getStringExtra(Constants.SERVICE_KEY);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!subscription.equals("")) {
                    ArrayList<MessageBoard> boards = MessageBoardDao.getMessageBoards();
                    if(boards.indexOf(subscription) != -1) {
                        Double lastMessageTime = MessageBoard.getBoardLastMessageTimestamp(subscription);
                        if(lastMessageTime > lastCheckTime) {
                        //TODO Implement a notification here that a new message has come in. Part 3 step 20 and beyond.
                        }
                    }
                    lastCheckTime = System.currentTimeMillis() / 1000;
                    try {
                        Thread.sleep(CHECK_PERIOD);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                stopSelf();
            }
        }).start();



        Log.i("Subscription to  board id: ", subscription);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Subscription", "onDestroy: Method entered");
    }
}
