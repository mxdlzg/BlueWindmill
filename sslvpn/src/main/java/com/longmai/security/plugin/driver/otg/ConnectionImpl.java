package com.longmai.security.plugin.driver.otg;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import com.longmai.security.plugin.base.PluginException;
import com.longmai.security.plugin.driver.conn.Connection;
import com.longmai.security.plugin.driver.otg.io.OTGInputStream;
import com.longmai.security.plugin.driver.otg.io.OTGOutputStream;
import com.longmai.security.plugin.driver.otg.io.stack.MessagePool;
import com.longmai.security.plugin.driver.otg.io.stack.MessagePoolImpl;
import com.longmai.security.plugin.util.LogUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeoutException;

public class ConnectionImpl implements Connection {
    private static final String TAG = ConnectionImpl.class.getName();
    private UsbDeviceConnection connection;
    private MessagePool messagePool;

    public ConnectionImpl(UsbManager usbManager, UsbDevice usbDevice) throws PluginException {
        if (usbManager.hasPermission(usbDevice)) {
            this.connection = usbManager.openDevice(usbDevice);
            this.messagePool = new MessagePoolImpl(this.connection, usbDevice);
            this.messagePool.init();
            this.messagePool.setTimeOut(3000);
            return;
        }
        throw new PluginException(17);
    }

    public ConnectionImpl(UsbDeviceContext context) throws PluginException {
        if (context.hasPermission()) {
            this.connection = context.openDevice();
            this.messagePool = new MessagePoolImpl(context);
            this.messagePool.init();
            this.messagePool.setTimeOut(3000);
            return;
        }
        throw new PluginException(17);
    }

    public InputStream getInputStream() throws PluginException {
        LogUtil.m10d(TAG, "getInputStream()");
        return new OTGInputStream(this.messagePool);
    }

    public OutputStream getOutputStream() throws PluginException {
        LogUtil.m10d(TAG, "getOutputStream()");
        return new OTGOutputStream(this.messagePool);
    }

    public boolean isValid() {
        LogUtil.m10d(TAG, "isValid()");
        return true;
    }

    public void close() throws PluginException {
        LogUtil.m10d(TAG, "close()");
        if (this.messagePool != null) {
            this.messagePool.destroy();
            this.messagePool = null;
        }
        if (this.connection != null) {
            this.connection.close();
            this.connection = null;
        }
    }

    public int deviceio(byte[] input, int input_len, byte[] output, int[] output_len) throws PluginException {
        LogUtil.m10d(TAG, "deviceio()");
        int i = 0;
        while (i < 3) {
            try {
                this.messagePool.write(input, 0, input_len);
                try {
                    byte[] apdu = this.messagePool.read();
                    if (apdu == null) {
                        return 1;
                    }
                    if (output.length < apdu.length) {
                        throw new PluginException(2);
                    }
                    System.arraycopy(apdu, 0, output, 0, apdu.length);
                    output_len[0] = apdu.length;
                    return 0;
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new PluginException(15);
                } catch (TimeoutException e2) {
                    e2.printStackTrace();
                    i++;
                }
            } catch (IOException e3) {
                e3.printStackTrace();
                throw new PluginException(14);
            }
        }
        throw new PluginException(15);
    }

    public void setValue(int type, Object value) throws PluginException {
        LogUtil.m10d(TAG, "setValue()");
    }

    public Object getValue(int type) throws PluginException {
        LogUtil.m10d(TAG, "getValue()");
        return null;
    }

    public void setTimeOut(int timeOut) {
        LogUtil.m10d(TAG, "setTimeOut()");
        this.messagePool.setTimeOut(timeOut);
    }

    public int getTimeOut() {
        LogUtil.m10d(TAG, "getTimeOut()");
        return this.messagePool.getTimeOut();
    }
}
