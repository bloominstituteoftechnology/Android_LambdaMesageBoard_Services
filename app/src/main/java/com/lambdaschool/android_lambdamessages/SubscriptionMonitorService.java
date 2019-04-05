package com.lambdaschool.android_lambdamessages;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;

public class SubscriptionMonitorService extends Service {
    private static final String TAG = "SubscriptionMonSvc in ";
    public static final String SUBSCRIPTION_ADD = "subscription_add";
    public static final String SUBSCRIPTION_REMOVE = "subscription_remove";
    ArrayList<String> subscriptionArrayList;
    long lastCheckTime;
    String subscription;
    Context context;

    public SubscriptionMonitorService() {
        Log.i(TAG, "constructor " + Thread.currentThread().getStackTrace()[2].getMethodName());
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, Thread.currentThread().getStackTrace()[2].getMethodName());
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, Thread.currentThread().getStackTrace()[2].getMethodName());
        lastCheckTime = System.currentTimeMillis() / 1000;
        subscriptionArrayList = new ArrayList<>();
        subscription = "";
        context = this;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.i(TAG, Thread.currentThread().getStackTrace()[2].getMethodName());
        subscription = intent.getStringExtra(SUBSCRIPTION_ADD);
        String subscriptionRemove = intent.getStringExtra(SUBSCRIPTION_REMOVE);
        if (subscriptionRemove != null) {
            subscriptionArrayList.remove(subscriptionRemove);
        } else if (subscription != null) {
            subscriptionArrayList.add(subscription);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (subscriptionArrayList.size() > 0) {
                        MessageBoardDao messageBoardDao = new MessageBoardDao();
                        ArrayList<MessageBoard> messageBoardArrayList = messageBoardDao.getMessageBoards();
                        ArrayList<String> subscripCopy = (new ArrayList<>(subscriptionArrayList));
                        boolean newMessage = false;
                        for (MessageBoard mb : messageBoardArrayList) {
                            for (String subscrip : subscripCopy) {
                                if (mb.getIdentifier().equals(subscrip)) {
                                    for (Message m : mb.getMessages()) {
                                        if (m.getTimestamp() > lastCheckTime) {
                                            newMessage = true;

                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                Intent intent2 = new Intent(context, MainActivity.class);
                                                intent2.putExtra("notification", "You are subscribed");
                                                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent2, PendingIntent.FLAG_ONE_SHOT);

                                                NotificationManager notifMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                                NotificationChannel notifChannel = new NotificationChannel(getPackageName() + ".boards", "Basil's Channel", NotificationManager.IMPORTANCE_HIGH);
                                                notifChannel.setDescription("Message Board Subscription");
                                                notifMgr.createNotificationChannel(notifChannel);
                                                NotificationCompat.Builder notifCompatBuilder = new NotificationCompat.Builder(context, getPackageName() + ".boards");
                                                notifCompatBuilder.setPriority(NotificationManager.IMPORTANCE_HIGH);
                                                notifCompatBuilder.setContentIntent(pendingIntent);
                                                notifCompatBuilder.setContentTitle("Basil's Channel");
                                                notifCompatBuilder.setContentText("There is a new message!");
                                                notifCompatBuilder.setSmallIcon(R.drawable.ic_launcher_background);
                                                notifCompatBuilder.setColor(Color.BLUE);
                                                notifCompatBuilder.setDefaults(Notification.DEFAULT_SOUND);
                                                notifMgr.notify(7896575, notifCompatBuilder.build());
                                            }
                                            lastCheckTime = System.currentTimeMillis() / 1000;
                                            break;
                                        }
                                    }
                                }
                            }

                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    stopSelf();

                }

            }).start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, Thread.currentThread().getStackTrace()[2].getMethodName());
    }
}
