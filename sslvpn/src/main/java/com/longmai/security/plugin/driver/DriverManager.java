package com.longmai.security.plugin.driver;

import com.longmai.security.plugin.base.PluginException;
import com.longmai.security.plugin.util.LogUtil;
import java.util.Enumeration;
import java.util.Vector;

public class DriverManager {
    private static final String TAG = DriverManager.class.getName();
    private static Vector<DriverInfo> drivers = new Vector();
    private static boolean initialized = false;

    public static synchronized void registerDriver(Driver driver) throws PluginException {
        synchronized (DriverManager.class) {
            if (!initialized) {
                initialize();
            }
            DriverInfo di = new DriverInfo();
            di.driver = driver;
            di.driverClass = driver.getClass();
            di.driverClassName = driver.getClass().getSimpleName();
            drivers.addElement(di);
        }
    }

    public static synchronized void deregisterDriver(Driver driver) {
        synchronized (DriverManager.class) {
            Enumeration<DriverInfo> e = drivers.elements();
            while (e.hasMoreElements()) {
                DriverInfo di = (DriverInfo) e.nextElement();
                if (di.driver.equals(driver)) {
                    drivers.remove(di);
                }
            }
        }
    }

    public static Enumeration<DriverInfo> getDrivers() {
        if (drivers.size() <= 0) {
            return null;
        }
        return drivers.elements();
    }

    public static Driver getDriver() throws PluginException {
        if (drivers.size() > 0) {
            return ((DriverInfo) drivers.get(0)).driver;
        }
        throw new PluginException(9);
    }

    public static Driver getDriver(String url) throws PluginException {
        if (drivers.size() <= 0) {
            throw new PluginException(9);
        }
        Enumeration<DriverInfo> e = drivers.elements();
        while (e.hasMoreElements()) {
            DriverInfo di = (DriverInfo) e.nextElement();
            if (di.driverClass.getName().equals(url)) {
                return di.driver;
            }
        }
        throw new PluginException(9);
    }

    private static void initialize() {
        if (!initialized) {
            initialized = true;
            LogUtil.m14i(TAG, "Plugin DriverManager initialized");
        }
    }
}
