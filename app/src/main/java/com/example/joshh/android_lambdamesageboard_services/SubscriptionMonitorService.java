package com.example.joshh.android_lambdamesageboard_services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;

public class SubscriptionMonitorService extends Service {
    public static final int CHECK_PERIOD = 3000;
    private Long lastCheckTime;
    private String subscription;
    private String removeSubscription;
    private Context context;
    private ArrayList<String> subscriptionsList;

    public SubscriptionMonitorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.i("SubscriptionMonitorServ", "onBind started");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("SubscriptionMonitorServ", "onCreate started");
        lastCheckTime = System.currentTimeMillis() / 1000;
        subscription = "";
        removeSubscription = "";
        context = this;
        subscriptionsList = new ArrayList<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("SubscriptionMonitorServ", "onStartCommand started");
        subscription = intent.getStringExtra(ScrollingActivity.BOARD_TO_SUBSCRIBE_KEY);
        removeSubscription = intent.getStringExtra(ScrollingActivity.REMOVE_FROM_SUBSCRIBED_KEY);
        if(subscription != null){
            subscriptionsList.add(subscription);
        }
        if(removeSubscription != null){
            subscriptionsList.remove(removeSubscription);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(subscriptionsList.size() != 0){
                    ArrayList<String> subscriptionsListCopy = new ArrayList<>(subscriptionsList);
                    ArrayList<MessageBoard> messageBoards = MessageBoardDao.getMessageBoards();
                    for(MessageBoard m : messageBoards){
                        for(String s : subscriptionsListCopy){
                            if(m.getIdentifier().equals(s)){
                                Message message = m.getLastMessage(s);
                                if(message.getTimestamp() > lastCheckTime){
                                    final NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                                    final String channelId = getPackageName() + ".channel";
                                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                                        NotificationChannel channel = new NotificationChannel(
                                                channelId,
                                                "New Message Channel",
                                                NotificationManager.IMPORTANCE_HIGH);
                                        notificationManager.createNotificationChannel(channel);
                                    }
                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                                            .setContentTitle("New Message in Subscribed Board")
                                            .setSmallIcon(android.R.drawable.ic_dialog_alert)
                                            .setAutoCancel(true);
                                    notificationManager.notify(0, builder.build());
                                }
                            }
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
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i("SubscriptionMonitorServ", "onDestroy started");
        super.onDestroy();
    }
}
