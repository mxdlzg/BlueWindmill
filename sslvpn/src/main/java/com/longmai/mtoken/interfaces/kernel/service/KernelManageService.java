package com.longmai.mtoken.interfaces.kernel.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import com.longmai.mtoken.interfaces.kernel.MinaServerThread;
import com.longmai.security.plugin.util.LogUtil;

public class KernelManageService extends Service {
    private static final String TAG = KernelManageService.class.getName();
    private static final String VERSION = "0.1.0";
    private final IBinder mBinder = new LocalBinder();
    private MinaServerThread server;

    public class LocalBinder extends Binder {
        public KernelManageService getService() {
            return KernelManageService.this;
        }
    }

    public void onCreate() {
        LogUtil.m10d(TAG, "onCreate()");
        super.onCreate();
        if (this.server != null) {
            this.server.cancel();
            this.server = null;
        }
        LogUtil.setDebug(true);
        LogUtil.setWriteFile(true);
        this.server = new MinaServerThread(this);
        this.server.start();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.m10d(TAG, "onStartCommand(intent - " + intent + ", flags - " + flags + ", startId - " + startId + ")");
        return super.onStartCommand(intent, Service.START_FLAG_REDELIVERY, startId);
    }

    public IBinder onBind(Intent intent) {
        LogUtil.m10d(TAG, "onBind()");
        return this.mBinder;
    }

    public String getVersion() {
        return VERSION;
    }

    public boolean onUnbind(Intent intent) {
        LogUtil.m10d(TAG, "onUnbind()");
        return super.onUnbind(intent);
    }

    public void onDestroy() {
        LogUtil.m10d(TAG, "onDestroy()");
        this.server.cancel();
        this.server = null;
        sendBroadcast(new Intent("com.longmai.mtoken.boot"));
        super.onDestroy();
    }
}
