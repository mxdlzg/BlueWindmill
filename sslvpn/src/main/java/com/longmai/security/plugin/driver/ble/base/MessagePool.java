package com.longmai.security.plugin.driver.ble.base;

import com.longmai.security.plugin.base.PluginException;
import java.io.IOException;

public abstract class MessagePool implements Receiver, Transmitter {
    public abstract void destroy() throws PluginException;

    public abstract int getTimeOut();

    public abstract void init() throws PluginException;

    public abstract byte[] read() throws IOException, PluginException;

    public abstract void setTimeOut(int i);

    public abstract int write(byte[] bArr) throws IOException, PluginException;

    public abstract int write(byte[] bArr, int i, int i2, int i3) throws IOException, PluginException;
}
