package com.longmai.security.plugin.base;

import com.longmai.security.plugin.util.Int2Bytes;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EnvlopedKeyBlob extends Blob {
    private ECCCipherBlob ECCCipherBlob;
    private ECCPublicKeyBlob PubKey;
    private byte[] cbEncryptedPriKey;
    private int ulBits;
    private int ulSymmAlgID;
    private int version = 1;

    public EnvlopedKeyBlob(int ulSymmAlgID, int ulBits, byte[] cbEncryptedPriKey, ECCPublicKeyBlob PubKey, ECCCipherBlob ECCCipherBlob) {
        this.ulSymmAlgID = ulSymmAlgID;
        this.ulBits = ulBits;
        this.cbEncryptedPriKey = cbEncryptedPriKey;
        this.PubKey = PubKey;
        this.ECCCipherBlob = ECCCipherBlob;
    }

    public byte[] getEncode() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(Int2Bytes.int2byte(this.version, 4, false));
        bos.write(Int2Bytes.int2byte(this.ulSymmAlgID, 4, false));
        bos.write(Int2Bytes.int2byte(this.ulBits, 4, false));
        bos.write(new byte[(64 - this.cbEncryptedPriKey.length)]);
        bos.write(this.cbEncryptedPriKey);
        bos.write(this.PubKey.getEncode());
        bos.write(this.ECCCipherBlob.getEncode());
        return bos.toByteArray();
    }
}
