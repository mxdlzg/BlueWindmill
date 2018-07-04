package com.longmai.security.plugin.util;

import com.longmai.security.plugin.SOF_DeviceLib;
import com.longmai.security.plugin.skf.ndk.SKF_CMDPackerImple;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestUtil {
    public static final String MD5 = "MD5";
    public static final String SHA1 = "SHA1";
    public static final String SHA256 = "SHA-256";
    public static final String SHA384 = "SHA-384";
    public static final String SHA512 = "SHA-512";
    private int algoId;
    private MessageDigest md;

    public int SoftDigestInit(int algoId, byte[] eccPubKey) throws NoSuchAlgorithmException {
        this.algoId = algoId;
        switch (algoId) {
            case 1:
                SKF_CMDPackerImple.SM3DigestInit();
                if (eccPubKey != null) {
                    byte[] Z = new byte[32];
                    SKF_CMDPackerImple.SM3_GetZ(eccPubKey, Z);
                    SKF_CMDPackerImple.SM3DigestUpdate(Z, 32);
                    break;
                }
                break;
            case 2:
                this.md = MessageDigest.getInstance(SHA1);
                break;
            case 4:
                this.md = MessageDigest.getInstance(SHA256);
                break;
            case SOF_DeviceLib.SGD_MD5 /*129*/:
                this.md = MessageDigest.getInstance(MD5);
                break;
            case 130:
                this.md = MessageDigest.getInstance(SHA384);
                break;
            case SOF_DeviceLib.SGD_SHA512 /*131*/:
                this.md = MessageDigest.getInstance(SHA512);
                break;
        }
        return 0;
    }

    public int SoftDigestUpdate(byte[] input, int inputLen) {
        switch (this.algoId) {
            case 1:
                SKF_CMDPackerImple.SM3DigestUpdate(input, inputLen);
                break;
            case 2:
            case 4:
            case SOF_DeviceLib.SGD_MD5 /*129*/:
            case 130:
            case SOF_DeviceLib.SGD_SHA512 /*131*/:
                this.md.update(input, 0, inputLen);
                break;
        }
        return 0;
    }

    public int SoftDigestFinal(byte[] input, int inputLen, byte[] output, int[] outputLen) {
        switch (this.algoId) {
            case 1:
                SKF_CMDPackerImple.SM3DigestFinal(input, inputLen, output, outputLen);
                break;
            case 2:
            case 4:
            case SOF_DeviceLib.SGD_MD5 /*129*/:
            case 130:
            case SOF_DeviceLib.SGD_SHA512 /*131*/:
                this.md.update(input, 0, inputLen);
                byte[] d = this.md.digest();
                System.arraycopy(d, 0, output, 0, d.length);
                outputLen[0] = d.length;
                break;
        }
        return 0;
    }

    public int SoftDigestFinal(byte[] output, int[] outputLen) {
        switch (this.algoId) {
            case 1:
                SKF_CMDPackerImple.SM3DigestFinal(new byte[0], 0, output, outputLen);
                break;
            case 2:
            case 4:
            case SOF_DeviceLib.SGD_MD5 /*129*/:
            case 130:
            case SOF_DeviceLib.SGD_SHA512 /*131*/:
                byte[] d = this.md.digest();
                System.arraycopy(d, 0, output, 0, d.length);
                outputLen[0] = d.length;
                break;
        }
        return 0;
    }
}
