package com.topsec.sslvpn.util;

import com.longmai.security.plugin.driver.conn.Connection;
import com.longmai.security.plugin.util.DigestUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileFinger {
    protected char[] m_charrDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    protected MessageDigest m_mdDigest = null;

    public FileFinger() {
        try {
            this.m_mdDigest = MessageDigest.getInstance(DigestUtil.MD5);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String getFileMD5String(File file) throws IOException {
        InputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        while (true) {
            int numRead = fis.read(buffer);
            if (numRead <= 0) {
                fis.close();
                return bufferToHex(this.m_mdDigest.digest());
            }
            this.m_mdDigest.update(buffer, 0, numRead);
        }
    }

    public String getFileMD5String(InputStream in) throws IOException {
        byte[] buffer = new byte[1024];
        while (true) {
            int numRead = in.read(buffer);
            if (numRead <= 0) {
                in.close();
                return bufferToHex(this.m_mdDigest.digest());
            }
            this.m_mdDigest.update(buffer, 0, numRead);
        }
    }

    private String bufferToHex(byte[] bytes) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private String bufferToHex(byte[] bytes, int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(n * 2);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = this.m_charrDigits[(bt & Connection.UART_OP_KEYEXCHG) >> 4];
        char c1 = this.m_charrDigits[bt & 15];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }
}
