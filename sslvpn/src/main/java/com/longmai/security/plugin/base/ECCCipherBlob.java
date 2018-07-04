package com.longmai.security.plugin.base;

import com.longmai.security.plugin.util.Int2Bytes;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ECCCipherBlob extends Blob {
    private byte[] Cipher;
    private int CipherLen;
    private byte[] HASH;
    private byte[] XCoordinate;
    private byte[] YCoordinate;

    public ECCCipherBlob(byte[] XCoordinate, byte[] YCoordinate, byte[] HASH, int CipherLen, byte[] Cipher) {
        this.XCoordinate = XCoordinate;
        this.YCoordinate = YCoordinate;
        this.HASH = HASH;
        this.CipherLen = CipherLen;
        this.Cipher = Cipher;
    }

    public byte[] getEncode() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(new byte[(64 - this.XCoordinate.length)]);
        bos.write(this.XCoordinate);
        bos.write(new byte[(64 - this.YCoordinate.length)]);
        bos.write(this.YCoordinate);
        bos.write(this.HASH);
        bos.write(Int2Bytes.int2byte(this.CipherLen, 4, false));
        bos.write(this.Cipher, 0, this.CipherLen);
        return bos.toByteArray();
    }
}
