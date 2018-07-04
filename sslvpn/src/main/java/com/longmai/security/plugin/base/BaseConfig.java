package com.longmai.security.plugin.base;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface BaseConfig {
    String get(String str);

    Map getDriverConfigInfo();

    List getList();

    void loadConfig(String str) throws IOException;

    void put(String str, String str2);

    void saveConfig() throws IOException;
}
