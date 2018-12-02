package com.example.jacob.android_lambdamessages;

import android.app.Notification;
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
import android.widget.Toast;

import java.util.ArrayList;

public class SubscriptionMonitorService extends Service {

    public static final int CHECK_PERIOD = 5000;
    private Long lastCheckTime;
    String subscription;
    Context context;
    NotificationManager notificationManager;
    int notificationImportance = NotificationManager.IMPORTANCE_DEFAULT;
    private static final int NOTIFICATION_ID_INSTANT = 354;

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
        context = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Subscription", "onStartCommand:  Method entered");
        subscription = intent.getStringExtra(Constants.SERVICE_KEY);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!subscription.equals("")) {
                    Log.i("Subscription", "Checking for new messages...");
                    ArrayList<String> boardIds = MessageBoardDao.getMessageBoardIds();
                    if(boardIds.indexOf(subscription) != -1) {
                        Double lastMessageTime = MessageBoard.getBoardLastMessageTimestamp(subscription);
                        if(lastMessageTime > lastCheckTime) {
                            String channelId = getPackageName() + ".subscription";
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                CharSequence name = "New Message Channel";
                                String description = "Notifications triggered by subscription monitor service";
                                NotificationChannel channel = new NotificationChannel(channelId, name, notificationImportance);
                                channel.setDescription(description);
                                notificationManager.createNotificationChannel(channel);
                            }

                            Intent intent = new Intent(context, MainActivity.class);
                            MessageBoard board = new MessageBoard(subscription);
                            intent.putExtra(MessageViewActivity.VIEW_BOARD_KEY,board);
                            PendingIntent notifyPendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_ONE_SHOT);

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                                    .setPriority(notificationImportance)
                                    .setContentTitle("Message")
                                    .setContentText("NewMessage")
                                    .setColor(context.getColor(R.color.colorPrimary))
                                    .addAction(R.drawable.ic_launcher_foreground,"View message board",notifyPendingIntent)
                                    .setAutoCancel(true)
                                    .setSmallIcon(android.R.drawable.ic_dialog_alert)
                                    .setDefaults(Notification.DEFAULT_ALL);
                            notificationManager.notify(NOTIFICATION_ID_INSTANT, builder.build());
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
