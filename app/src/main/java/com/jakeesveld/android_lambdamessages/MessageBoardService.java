package com.jakeesveld.android_lambdamessages;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MessageBoardService extends Service {
    public MessageBoardService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String receivedBoardTitle = intent.getStringExtra("AddService");
        Log.i("Service", "onStartCommand - " + receivedBoardTitle);




        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
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
