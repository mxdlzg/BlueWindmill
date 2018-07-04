package com.topsec.sslvpn.util;

public class IPHelper {
    public static int getMaskLen(int iMask) {
        int iCount = 0;
        if (-1 == iMask) {
            return 32;
        }
        while (iMask > 0) {
            iMask &= iMask - 1;
            iCount++;
        }
        return iCount;
    }

    public static String getIPFromInt(int iIP) {
        return String.format("%d.%d.%d.%d", new Object[]{Integer.valueOf(iIP & 255), Long.valueOf(((long) (iIP >> 8)) & 255), Long.valueOf(((long) (iIP >> 16)) & 255), Integer.valueOf((iIP >> 24) & 255)});
    }

    public static int getIpFromString(String strIP) {
        String[] strArrIP = strIP.split("\\.");
        int iIP = 0;
        if (4 != strArrIP.length) {
            return 0;
        }
        for (int ii = 0; ii < strArrIP.length; ii++) {
            iIP += (Integer.parseInt(strArrIP[ii]) & 255) << (8 * ii);
        }
        return iIP;
    }

    public static int getMaskLen(String strMask) {
        int i = 32;
        if (strMask == null || 1 > strMask.length()) {
            return i;
        }
        if (strMask.contains(".")) {
            return getMaskLen(getIpFromString(strMask));
        }
        try {
            return Integer.parseInt(strMask);
        } catch (NumberFormatException e) {
            return i;
        }
    }
}
