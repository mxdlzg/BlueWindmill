package com.longmai.security.plugin.driver.ble.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BLEOutputStream extends OutputStream {
    private ByteArrayOutputStream buff = new ByteArrayOutputStream();

    public void write(int oneByte) throws IOException {
        this.buff.write(oneByte);
    }

    public void close() throws IOException {
        this.buff.reset();
        this.buff.close();
    }

    public void write(byte[] buffer, int offset, int count) throws IOException {
        this.buff.write(buffer, offset, count);
    }

    public void write(byte[] buffer) throws IOException {
        this.buff.write(buffer);
    }

    public synchronized void flush() throws IOException {
        this.buff.reset();
    }
}
