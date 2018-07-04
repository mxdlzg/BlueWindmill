package com.longmai.security.plugin.device;

import com.longmai.security.plugin.base.PluginException;
import com.longmai.security.plugin.driver.conn.Connection;
import java.util.List;

public interface DeviceManager {
    List<Device> find(int i, String... strArr) throws PluginException;

    List<Device> find(String... strArr) throws PluginException;

    Connection getConnection(Device device) throws PluginException;

    Connection getConnection(Device device, int i) throws PluginException;
}
