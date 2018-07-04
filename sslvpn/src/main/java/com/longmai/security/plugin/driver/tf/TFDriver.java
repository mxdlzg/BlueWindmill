package com.longmai.security.plugin.driver.tf;

import android.content.Context;
import com.longmai.security.plugin.base.PluginException;
import com.longmai.security.plugin.device.DeviceManager;
import com.longmai.security.plugin.driver.Driver;
import com.longmai.security.plugin.driver.DriverManager;
import com.longmai.security.plugin.util.LogUtil;

public class TFDriver implements Driver {
    private static final String TAG = TFDriver.class.getName();
    private Context context;
    private DeviceManager deviceManager;

    static {
        try {
            DriverManager.registerDriver(new TFDriver());
            LogUtil.m10d(TAG, "register driver");
        } catch (Exception e) {
            throw new RuntimeException("Can't register driver!");
        }
    }

    public void init() throws PluginException {
        this.context = null;
    }

    public void init(Object obj) throws PluginException {
        LogUtil.m10d(TAG, "TFDriver init()");
        if (obj instanceof Context) {
            this.context = (Context) obj;
            return;
        }
        throw new PluginException("Driver failed to initialize");
    }

    public void uninit() throws PluginException {
        LogUtil.m10d(TAG, "TFDriver uninit()");
        this.context = null;
        this.deviceManager = null;
        DriverManager.deregisterDriver(this);
    }

    public DeviceManager getDeviceManager() throws PluginException {
        LogUtil.m10d(TAG, "TFDriver getDeviceManager()");
        if (this.deviceManager == null) {
            if (this.context == null) {
                this.deviceManager = new DeviceManagerImple();
            } else {
                this.deviceManager = new DeviceManagerImple(this.context);
            }
        }
        return this.deviceManager;
    }
}
