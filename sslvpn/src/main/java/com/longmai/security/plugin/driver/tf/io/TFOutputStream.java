package com.longmai.security.plugin.driver.tf.io;

import com.longmai.security.plugin.device.Device;
import com.longmai.security.plugin.driver.tf.base.TF;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TFOutputStream extends OutputStream {
    private ByteArrayOutputStream buff = new ByteArrayOutputStream();
    private Device device;
    private TF tf;

    public TFOutputStream(Device device, TF tf) {
        this.device = device;
        this.tf = tf;
    }

    public void write(int oneByte) throws IOException {
        this.buff.write(oneByte);
    }

    public void close() throws IOException {
        this.buff.reset();
        this.buff.close();
    }

    public void flush() throws IOException {
        byte[] data = this.buff.toByteArray();
        this.buff.reset();
        if (this.tf.write(this.device.getId(), data, data.length) != 0) {
            throw new IOException();
        }
    }

    public void write(byte[] buffer, int offset, int count) throws IOException {
        this.buff.write(buffer, offset, count);
    }

    public void write(byte[] buffer) throws IOException {
        this.buff.write(buffer);
    }
}
