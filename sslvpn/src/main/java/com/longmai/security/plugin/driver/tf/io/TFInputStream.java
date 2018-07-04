package com.longmai.security.plugin.driver.tf.io;

import com.longmai.security.plugin.device.Device;
import com.longmai.security.plugin.driver.tf.base.TF;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class TFInputStream extends InputStream {
    private ByteArrayOutputStream buff = new ByteArrayOutputStream();
    private Device device;
    private TF tf;

    public TFInputStream(Device device, TF tf) {
        this.device = device;
        this.tf = tf;
    }

    public int read() throws IOException {
        byte[] data = new byte[2048];
        int[] length = new int[1];
        if (this.tf.read(this.device.getId(), data, length) != 0) {
            throw new IOException();
        }
        this.buff.write(data, 0, length[0]);
        data = this.buff.toByteArray();
        this.buff.write(data, 1, data.length - 1);
        return data[0];
    }

    public int available() throws IOException {
        return super.available();
    }

    public void close() throws IOException {
        this.buff.reset();
        this.buff.close();
    }

    public void mark(int readlimit) {
    }

    public boolean markSupported() {
        return false;
    }

    public int read(byte[] buffer, int off, int len) throws IOException {
        byte[] data = new byte[2048];
        int[] length = new int[1];
        if (this.tf.read(this.device.getId(), data, length) != 0) {
            throw new IOException();
        }
        this.buff.write(data, 0, length[0]);
        if (buffer.length <= 0) {
            return 0;
        }
        if (buffer.length < len) {
            len = buffer.length;
        }
        if (data.length <= 0) {
            return -1;
        }
        data = this.buff.toByteArray();
        if (data.length >= len) {
            System.arraycopy(data, 0, buffer, 0, len);
            this.buff.reset();
            this.buff.write(data, len, data.length - len);
            return len;
        }
        System.arraycopy(data, 0, buffer, 0, data.length);
        this.buff.reset();
        return data.length;
    }

    public int read(byte[] buffer) throws IOException {
        byte[] data = new byte[2048];
        int[] length = new int[1];
        if (this.tf.read(this.device.getId(), data, length) != 0) {
            throw new IOException();
        }
        this.buff.write(data, 0, length[0]);
        if (buffer.length <= 0) {
            return 0;
        }
        if (data.length <= 0) {
            return -1;
        }
        data = this.buff.toByteArray();
        if (data.length >= buffer.length) {
            System.arraycopy(data, 0, buffer, 0, buffer.length);
            this.buff.reset();
            this.buff.write(data, buffer.length, data.length - buffer.length);
            return buffer.length;
        }
        System.arraycopy(data, 0, buffer, 0, data.length);
        this.buff.reset();
        return data.length;
    }

    public synchronized void reset() throws IOException {
        this.buff.reset();
    }

    public long skip(long byteCount) throws IOException {
        return super.skip(byteCount);
    }
}
