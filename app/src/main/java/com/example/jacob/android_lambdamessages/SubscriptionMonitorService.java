package com.example.jacob.android_lambdamessages;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class SubscriptionMonitorService extends Service {
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
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Subscription", "onStartCommand:  Method entered");
        String boardId = intent.getStringExtra(Constants.SERVICE_KEY);
        Log.i("Subscription to  board id: ", boardId);
        //TODO remove line below once we are doing something useful.
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Subscription", "onDestroy: Method entered");
    }
}
