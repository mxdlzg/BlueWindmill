package com.longmai.security.plugin.driver.conn;

import com.longmai.security.plugin.base.PluginException;

public interface SecurityConnection extends Connection {
    byte[] deriveSecurityCommSession(byte[] bArr) throws PluginException;

    int deviceio(byte[] bArr, int i, byte[] bArr2, int[] iArr, int i2) throws PluginException;
}
