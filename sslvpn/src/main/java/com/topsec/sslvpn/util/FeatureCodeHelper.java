package com.topsec.sslvpn.util;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import com.longmai.security.plugin.util.DigestUtil;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FeatureCodeHelper {
    private static String TAG = "FeatureCodeHelper";

    public static String getSCode(Context appContext) {
        String Scode = getIMEICode(appContext);
        if (Scode == null) {
            Scode = getMacAddr(appContext);
            if (Scode == null) {
                Scode = "0000000000000000";
            }
        }
        String md5Sum = Md5Sum(new StringBuilder(String.valueOf(Scode)).append("hEcHaO").append("lUoYuAn").append("DeNgHuI").toString().getBytes());
        String tmp = "0000000000000000" + "hEcHaO" + "lUoYuAn" + "DeNgHuI";
        return md5Sum;
    }

    private static String getIMEICode(Context appContext) {
        return ((TelephonyManager) appContext.getSystemService("phone")).getDeviceId();
    }

    private static String getMacAddr(Context appContext) {
        return ((WifiManager) appContext.getSystemService("wifi")).getConnectionInfo().getMacAddress();
    }

    private static byte[] hexStr2ByteArr(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;
        byte[] arrOut = new byte[(iLen / 2)];
        for (int i = 0; i < iLen; i += 2) {
            arrOut[i / 2] = (byte) Integer.parseInt(new String(arrB, i, 2), 16);
        }
        return arrOut;
    }

    private static String Md5Sum(byte[] bytes) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance(DigestUtil.MD5);
            algorithm.reset();
            algorithm.update(bytes);
            return toHexString(algorithm.digest(), "");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException");
        }
    }

    private static String toHexString(byte[] bytes, String separator) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String stmp = Integer.toHexString(b & 255);
            if (stmp.length() == 1) {
                stmp = "0" + stmp;
            }
            hexString.append(stmp).append(separator);
        }
        return hexString.toString();
    }

    public static String getPhoneFeatureCode(Context appContext) {
        String scode = getSCode(appContext);
        Log.i(TAG, "scode===" + scode);
        String scodeb64 = "";
        try {
            scodeb64 = Base64.encodeToString(hexStr2ByteArr(scode), 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String stmp = scodeb64;
        while (true) {
            int index = stmp.indexOf("+");
            if (index <= 0) {
                return stmp;
            }
            String sleft = stmp.substring(0, index);
            stmp = new StringBuilder(String.valueOf(sleft)).append("%2B").append(stmp.substring(index + 1, stmp.length())).toString();
        }
    }
}
