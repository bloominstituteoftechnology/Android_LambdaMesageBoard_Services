package com.lambdaschool.android_lambdamessages;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class SubscriptionMonitorService extends Service {
    private static final String TAG = "SubscriptionMonSvc in ";
    public static final String SUBSCRIPTION_ADD = "subscription_add";


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
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, Thread.currentThread().getStackTrace()[2].getMethodName());
        String retrievedValue = intent.getStringExtra(SUBSCRIPTION_ADD);
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, Thread.currentThread().getStackTrace()[2].getMethodName());
    }
}
