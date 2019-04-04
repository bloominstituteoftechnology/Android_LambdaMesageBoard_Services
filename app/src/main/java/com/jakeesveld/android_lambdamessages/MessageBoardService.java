package com.jakeesveld.android_lambdamessages;

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

public class MessageBoardService extends Service {
    public MessageBoardService() {
    }

    public static long lastCheckTime;
    String subscription;
    Context context;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        subscription = intent.getStringExtra("AddService");
        Log.i("Service", "onStartCommand - " + subscription);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!subscription.equals("")){
                    ArrayList<MessageBoard> messageBoards = MessageBoardsDAO.getMessageBoards();
                    for(MessageBoard board: messageBoards){
                        if(board.getTitle().equals(subscription)){
                            ArrayList<Message> messages = board.getMessagesList();
                            for(Message message: messages){
                                if(message.getTimestamp() > lastCheckTime){
                                    final NotificationManager notificationManager =(NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                                    String channelID = getPackageName() + "button";
                                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                                        CharSequence name = "Notifications";
                                        String description = "These are the notifications our app will send you";
                                        int importance = NotificationManager.IMPORTANCE_HIGH;

                                        NotificationChannel channel = new NotificationChannel(channelID, name, importance);
                                        channel.setDescription(description);

                                        notificationManager.createNotificationChannel(channel);
                                    }

                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelID)
                                            .setContentTitle("Lambda Message Boards")
                                            .setContentText("A new message was posted to the message board")
                                            .setSmallIcon(android.R.drawable.gallery_thumb)
                                            .setPriority(NotificationManager.IMPORTANCE_HIGH)
                                            .setColor(getResources().getColor(R.color.colorPrimary))
                                            .setDefaults(Notification.DEFAULT_ALL);

                                    notificationManager.notify(1, builder.build());
                                    lastCheckTime = System.currentTimeMillis() / 1000;
                                    try {
                                        Thread.sleep(100000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        }
                    }


                }

                stopSelf();
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        lastCheckTime = System.currentTimeMillis() / 1000;
        subscription = "";
        context = this;
        Log.i("Service", "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Service", "onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
}
