package com.longmai.security.plugin.driver.otg;

import android.content.Context;
import android.os.Build.VERSION;
import com.longmai.security.plugin.base.PluginException;
import com.longmai.security.plugin.device.DeviceManager;
import com.longmai.security.plugin.driver.Driver;
import com.longmai.security.plugin.driver.DriverManager;
import com.longmai.security.plugin.util.LogUtil;

public class OTGDriver implements Driver {
    private static final String TAG = OTGDriver.class.getName();
    private static final int currentapiVersion = VERSION.SDK_INT;
    private Context context;
    private DeviceManager deviceManager;

    static {
        try {
            DriverManager.registerDriver(new OTGDriver());
            LogUtil.m10d(TAG, "register driver");
        } catch (Exception e) {
            throw new RuntimeException("Can't register driver!");
        }
    }

    public void init() throws PluginException {
        LogUtil.m10d(TAG, "init()");
        throw new PluginException("Driver failed to initialize");
    }

    public void init(Object context) throws PluginException {
        LogUtil.m10d(TAG, "init()");
        if (currentapiVersion < 14) {
            throw new PluginException(5);
        } else if (context instanceof Context) {
            this.context = (Context) context;
        } else {
            throw new PluginException("Driver failed to initialize");
        }
    }

    public DeviceManager getDeviceManager() throws PluginException {
        LogUtil.m10d(TAG, "getDeviceManager()");
        if (this.deviceManager == null) {
            this.deviceManager = new DeviceManagerImple(this.context);
        }
        return this.deviceManager;
    }

    public void uninit() throws PluginException {
        LogUtil.m10d(TAG, "uninit()");
        this.context = null;
        this.deviceManager = null;
        DriverManager.deregisterDriver(this);
    }
}
