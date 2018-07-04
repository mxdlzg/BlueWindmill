package com.topsec.topsap.application;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Handler;
import com.freerdp.freerdpcore.application.GlobalApp;

public class VPNApplication extends GlobalApp {
    private static final String VALUE = "Harvey";
    private static Context m_cxtContext = null;
    private Handler mHandler;
    private String value;

    public void onCreate() {
        super.onCreate();
        //ZXingLibrary.initDisplayOpinion(this);
        m_cxtContext = this;
        setValue(VALUE);
    }

    public static Context getContext() {
        return m_cxtContext;
    }

    public void ExitApp() {
//        if (7 < VERSION.SDK_INT) {
//            ((ActivityManager) getSystemService("activity")).killBackgroundProcesses(getPackageName());
//        } else {
//            ((ActivityManager) getSystemService("activity")).restartPackage(getPackageName());
//        }
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    public void setMainHandler(Handler handler) {
        this.mHandler = handler;
    }

    public Handler getMainHandler() {
        return this.mHandler;
    }
}
