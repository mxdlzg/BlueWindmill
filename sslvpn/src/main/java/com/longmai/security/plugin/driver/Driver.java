package com.longmai.security.plugin.driver;

import com.longmai.security.plugin.base.PluginException;
import com.longmai.security.plugin.device.DeviceManager;

public interface Driver {
    DeviceManager getDeviceManager() throws PluginException;

    void init() throws PluginException;

    void init(Object obj) throws PluginException;

    void uninit() throws PluginException;
}
