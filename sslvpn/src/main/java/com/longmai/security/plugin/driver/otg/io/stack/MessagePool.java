package com.longmai.security.plugin.driver.otg.io.stack;

import com.longmai.security.plugin.base.PluginException;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface MessagePool {
    void destroy() throws PluginException;

    int getTimeOut();

    void init() throws PluginException;

    byte[] read() throws IOException, TimeoutException;

    void setTimeOut(int i);

    int write(byte[] bArr) throws IOException;

    int write(byte[] bArr, int i, int i2) throws IOException, IllegalArgumentException;
}
