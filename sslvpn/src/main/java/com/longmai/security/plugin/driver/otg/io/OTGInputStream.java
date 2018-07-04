package com.longmai.security.plugin.driver.otg.io;

import com.longmai.security.plugin.base.PluginException;
import com.longmai.security.plugin.driver.otg.io.stack.MessagePool;
import com.longmai.security.plugin.util.LogUtil;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeoutException;

public class OTGInputStream extends InputStream {
    private static final String TAG = OTGInputStream.class.getName();
    private ByteArrayInputStream buff;

    public OTGInputStream(MessagePool messagePool) throws PluginException {
        byte[] apdu = null;
        try {
            apdu = messagePool.read();
        } catch (IOException e) {
            e.printStackTrace();
            throw new PluginException(15);
        } catch (TimeoutException e2) {
            e2.printStackTrace();
        }
        this.buff = new ByteArrayInputStream(apdu);
    }

    public int read() throws IOException {
        LogUtil.m10d(TAG, "read()");
        return this.buff.read();
    }

    public int available() throws IOException {
        return this.buff.available();
    }

    public void close() throws IOException {
        LogUtil.m10d(TAG, "close()");
        this.buff.close();
    }

    public void mark(int readlimit) {
        this.buff.mark(readlimit);
    }

    public boolean markSupported() {
        return this.buff.markSupported();
    }

    public int read(byte[] buffer, int off, int len) throws IOException {
        LogUtil.m10d(TAG, "readEx()");
        return this.buff.read(buffer, off, len);
    }

    public int read(byte[] buffer) throws IOException {
        return this.buff.read(buffer);
    }

    public void reset() throws IOException {
        this.buff.reset();
    }

    public long skip(long byteCount) throws IOException {
        return this.buff.skip(byteCount);
    }
}
