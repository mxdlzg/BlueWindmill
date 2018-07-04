package com.topsec.topsap.view;

import android.app.Activity;
import android.app.Application;
import java.util.LinkedList;
import java.util.List;

public class SysApplication extends Application {
    private static SysApplication instance;
    private List<Activity> mList = new LinkedList();

    private SysApplication() {
    }

    public static synchronized SysApplication getInstance() {
        SysApplication sysApplication;
        synchronized (SysApplication.class) {
            if (instance == null) {
                instance = new SysApplication();
            }
            sysApplication = instance;
        }
        return sysApplication;
    }

    public void addActivity(Activity activity) {
        this.mList.add(activity);
    }

    public void exit() {
        try {
            for (Activity activity : this.mList) {
                if (activity != null) {
                    activity.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
}
