package com.topsec.sslvpn.lib;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public final class BaseMonitor extends Service {
    public IBinder onBind(Intent intent) {
        Log.i("sd", "onBind");
        return null;
    }

    public void onCreate() {
        Log.i("sd", "onCreate");
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("sd", "onStartCommand");
        return 3;
    }

    public void onDestroy() {
        Log.i("sd", "onDestroy");
        super.onDestroy();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        Log.i("sd", "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }

    @SuppressLint({"NewApi"})
    public void onTrimMemory(int level) {
        Log.i("sd", "onTrimMemory");
        super.onTrimMemory(level);
    }

    protected void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        Log.i("sd", "dump");
        super.dump(fd, writer, args);
    }
}
