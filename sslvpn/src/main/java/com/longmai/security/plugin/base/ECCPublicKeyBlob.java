package com.longmai.security.plugin.base;

import com.longmai.security.plugin.util.Int2Bytes;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ECCPublicKeyBlob extends Blob {
    private int BitLen;
    private byte[] XCoordinate;
    private byte[] YCoordinate;

    public ECCPublicKeyBlob(int BitLen, byte[] XCoordinate, byte[] YCoordinate) {
        this.BitLen = BitLen;
        this.XCoordinate = XCoordinate;
        this.YCoordinate = YCoordinate;
    }

    public byte[] getEncode() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(Int2Bytes.int2byte(this.BitLen, 4, false));
        bos.write(new byte[(64 - this.XCoordinate.length)]);
        bos.write(this.XCoordinate);
        bos.write(new byte[(64 - this.YCoordinate.length)]);
        bos.write(this.YCoordinate);
        return bos.toByteArray();
    }
}
