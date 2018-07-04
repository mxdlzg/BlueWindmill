package com.longmai.security.plugin;

import com.longmai.security.plugin.base.BaseConfig;
import com.longmai.security.plugin.util.LogUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class PluginConfig implements BaseConfig {
    private static final String TAG = PluginConfig.class.getName();
    private Properties pro = new Properties();

    public PluginConfig(String file) throws IOException {
        InputStream input = PluginConfig.class.getResourceAsStream(file);
        if (input == null) {
            throw new IOException("The " + file + " config file not found");
        }
        this.pro.load(input);
    }

    public void loadConfig(String file) throws IOException {
        LogUtil.m10d(TAG, "loadConfig() - file: " + file);
        InputStream input = PluginConfig.class.getResourceAsStream(file);
        if (input == null) {
            throw new IOException("The " + file + " config file not found");
        }
        this.pro.load(input);
    }

    public void saveConfig() {
    }

    public List getList() {
        return null;
    }

    public void put(String key, String value) {
        this.pro.put(key, value);
    }

    public String get(String key) {
        return (String) this.pro.get(key);
    }

    public Map getDriverConfigInfo() {
        return null;
    }
}
