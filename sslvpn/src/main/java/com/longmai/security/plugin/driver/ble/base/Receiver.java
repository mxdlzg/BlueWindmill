package com.longmai.security.plugin.driver.ble.base;

import java.io.IOException;

public interface Receiver {
    void onReceive(byte[] bArr) throws IOException;

    void onReceive(byte[] bArr, int i, int i2) throws IOException;
}
