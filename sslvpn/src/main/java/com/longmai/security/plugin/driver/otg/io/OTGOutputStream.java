package com.longmai.security.plugin.driver.otg.io;

import com.longmai.security.plugin.driver.otg.io.stack.MessagePool;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class OTGOutputStream extends OutputStream {
    private ByteArrayOutputStream buff = new ByteArrayOutputStream();
    private MessagePool messagePool;

    public OTGOutputStream(MessagePool messagePool) {
        this.messagePool = messagePool;
    }

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
        byte[] apdu = this.buff.toByteArray();
        this.buff.reset();
        this.messagePool.write(apdu);
    }
}
