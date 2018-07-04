package com.longmai.security.plugin.driver.conn;

import com.longmai.security.plugin.base.PluginException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Connection {
    public static final int STATE_CONNECTED = 2;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_DISCONNECT = 0;
    public static final int UART_OP_DESTROYSESSION = 242;
    public static final int UART_OP_GENERIC_APDU = 0;
    public static final int UART_OP_INTERNALAUTH = 241;
    public static final int UART_OP_KEYEXCHG = 240;
    public static final int UART_OP_SECURITY_APDU = 128;
    public static final int UART_OP_SETPPS = 255;

    void close() throws PluginException;

    int deviceio(byte[] bArr, int i, byte[] bArr2, int[] iArr) throws PluginException;

    InputStream getInputStream() throws PluginException;

    OutputStream getOutputStream() throws PluginException;

    int getTimeOut();

    Object getValue(int i) throws PluginException;

    boolean isValid();

    void setTimeOut(int i);

    void setValue(int i, Object obj) throws PluginException;
}
