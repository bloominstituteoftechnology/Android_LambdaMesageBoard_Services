package com.example.joshh.android_lambdamesageboard_services;

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
        Log.i("SubscriptionMonitorServ", "onBind started");
        return null;
    }

    @Override
    public void onCreate() {
        Log.i("SubscriptionMonitorServ", "onCreate started");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("SubscriptionMonitorServ", "onStartCommand started");
        String boardId = intent.getStringExtra("board_to_subscribe");
        Log.i("boardIdFromIntent", boardId);
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i("SubscriptionMonitorServ", "onDestroy started");
        super.onDestroy();
    }
}
