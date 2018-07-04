package com.longmai.security.plugin.util;

import com.longmai.security.plugin.skf.ndk.SKF_CMDPackerImple;

public class SMUtil {
    public static int SM2_GetX(byte[] eccPubKey, byte[] X) {
        return 0;
    }

    public static int SM2_GetY(byte[] eccPubKey, byte[] Y) {
        return 0;
    }

    public static int SM3_GetZ(byte[] eccPubKey, byte[] Z) {
        if (SKF_CMDPackerImple.SM3_GetZ(eccPubKey, Z) > 0) {
            return 0;
        }
        return 1;
    }
}
