package com.longmai.security.plugin.driver.tf;

import com.longmai.security.plugin.base.PluginException;
import com.longmai.security.plugin.device.Device;
import com.longmai.security.plugin.driver.conn.Connection;
import com.longmai.security.plugin.driver.tf.base.TF;
import com.longmai.security.plugin.driver.tf.io.TFInputStream;
import com.longmai.security.plugin.driver.tf.io.TFOutputStream;
import com.longmai.security.plugin.util.LogUtil;
import java.io.InputStream;
import java.io.OutputStream;

public class ConnectionImpl implements Connection {
    private static final String TAG = ConnectionImpl.class.getName();
    private static HeartBeatThread heartBeat;
    private static final Object io_lock = new Object();
    private volatile int connStat = 0;
    private Device device;
    private TF tf;

    class HeartBeatThread extends Thread {
        boolean flag = true;

        HeartBeatThread() {
        }

        public void run() {
            while (this.flag) {
                try {
                    sleep(1000);
                    try {
                        if (ConnectionImpl.this.deviceio(new byte[1], 1, new byte[1024], new int[1]) != 0) {
                            ConnectionImpl.this.connStat = 0;
                            break;
                        }
                    } catch (PluginException e) {
                        e.printStackTrace();
                    }
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                    LogUtil.m10d(ConnectionImpl.TAG, "线程唤醒，跳出循环");
                }
            }
            LogUtil.m10d(ConnectionImpl.TAG, "Heart Beat end");
        }

        public void cancel() {
            LogUtil.m10d(ConnectionImpl.TAG, "Heart Beat cancel");
            this.flag = false;
            if (!isInterrupted()) {
                LogUtil.m10d(ConnectionImpl.TAG, "中断线程");
                interrupt();
            }
        }
    }

    public ConnectionImpl(Device device, TF tf) throws PluginException {
        LogUtil.m10d(TAG, "ConnectionImpl Create");
        this.device = device;
        this.tf = tf;
        if (tf.connect(device.getId()) != 0) {
            this.connStat = 0;
            throw new PluginException(13);
        }
        if (heartBeat != null) {
            heartBeat.cancel();
            heartBeat = null;
        }
        this.connStat = 2;
    }

    public InputStream getInputStream() throws PluginException {
        LogUtil.m10d(TAG, "getInputStream()");
        return new TFInputStream(this.device, this.tf);
    }

    public OutputStream getOutputStream() throws PluginException {
        LogUtil.m10d(TAG, "getOutputStream()");
        return new TFOutputStream(this.device, this.tf);
    }

    public boolean isValid() {
        LogUtil.m10d(TAG, "isValid() " + this.connStat);
        if (this.connStat == 0) {
            return false;
        }
        return true;
    }

    public void close() throws PluginException {
        LogUtil.m10d(TAG, "close()");
        if (heartBeat != null) {
            heartBeat.cancel();
            heartBeat = null;
        }
        if (this.connStat == 2) {
            this.connStat = 0;
            this.tf.disconnect(this.device.getId());
        }
    }

    public int deviceio(byte[] cmd, int cmd_len, byte[] response, int[] res_len) throws PluginException {
        int deviceio;
        LogUtil.m10d(TAG, "deviceio()");
        synchronized (io_lock) {
            deviceio = this.tf.deviceio(this.device.getId(), cmd, cmd_len, response, res_len);
        }
        return deviceio;
    }

    public void setValue(int type, Object value) throws PluginException {
    }

    public Object getValue(int type) throws PluginException {
        return null;
    }

    public void setTimeOut(int timeOut) {
    }

    public int getTimeOut() {
        return 0;
    }
}
