package com.vivekvishwanath.android_lambdamesageboard_services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;

public class SubscriptionMonitorService extends Service {

    private static long lastCheckTime;
    String subscription;
    private Context context;
    private static final int NOTIFICATION_ID = 11;
    private static final int CHECK_PERIOD = 5000;
    ArrayList<String> subscriptions;

    public SubscriptionMonitorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        Log.i("Service", "Entered onCreate");
        lastCheckTime = System.currentTimeMillis() / 1000;
        subscription = "";
        context = this;
        subscriptions = new ArrayList<>();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Service", "Entered onStartCommand");
        subscription = intent.getStringExtra("add_subscription");
        subscriptions.add(subscription);
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean flag = false;
                while (subscription != "") {
                    ArrayList<MessageBoard> messageBoards = MessageBoardDao.getMessageBoards();
                    ArrayList<String> subscriptionsCopy = new ArrayList<>(subscriptions);
                    for (int i = 0; i < messageBoards.size(); i++) {
                        for (int j = 0; j < subscriptionsCopy.size(); j++) {
                            if (subscriptionsCopy.get(j).equals(messageBoards.get(i).getIdentifier())) {
                                int numMessages = messageBoards.get(i).getMessages().size();
                                double lastMessageTime = messageBoards.get(i).getMessages().get(numMessages - 1).getTimestamp();
                                if (lastMessageTime > lastCheckTime) {
                                    flag = true;
                                }
                            }
                        }
                    }

                    lastCheckTime = System.currentTimeMillis() / 1000;
                    if (flag) {
                        NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            CharSequence name = getPackageName();
                            String description = "MessageBoard Subscription Channel";
                            int importance = NotificationManager.IMPORTANCE_HIGH;
                            NotificationChannel channel = new NotificationChannel(getPackageName(), description, importance);

                            manager.createNotificationChannel(channel);
                        }
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, getPackageName())
                                .setContentTitle("New Message!")
                                .setContentText("New message in your message board")
                                .setSmallIcon(R.drawable.ic_launcher_foreground)
                                .setAutoCancel(true);
                        manager.notify(NOTIFICATION_ID, builder.build());
                    }
                    try {
                        Thread.sleep(5000);
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
        Log.i("Service", "Entered onDestroy");
        super.onDestroy();
    }
}
