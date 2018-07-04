package com.topsec.topsap.application;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.Character.UnicodeBlock;
import org.apache.http.protocol.HTTP;

public class Common {
    private static final String TAG = "Common";
    public static boolean isOutputLogCat = true;

    public static boolean isFileExist(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            return true;
        }
        if (fileName.endsWith(File.separator)) {
            return false;
        }
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            return false;
        }
        try {
            if (file.createNewFile()) {
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getSDKVersionNumber() {
        try {
            return Integer.valueOf(VERSION.SDK).intValue();
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static String gbToUtf8(String str) throws UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            String s = str.substring(i, i + 1);
            if (s.charAt(0) > '') {
                byte[] bytes = s.getBytes("Unicode");
                String binaryStr = "";
                for (int j = 2; j < bytes.length; j += 2) {
                    binaryStr = new StringBuilder(String.valueOf(binaryStr)).append(getBinaryString(Integer.valueOf(getHexString(bytes[j + 1]), 16).intValue())).toString();
                    binaryStr = new StringBuilder(String.valueOf(binaryStr)).append(getBinaryString(Integer.valueOf(getHexString(bytes[j]), 16).intValue())).toString();
                }
                String s1 = "1110" + binaryStr.substring(0, 4);
                String s2 = "10" + binaryStr.substring(4, 10);
                String s3 = "10" + binaryStr.substring(10, 16);
                sb.append(new String(new byte[]{Integer.valueOf(s1, 2).byteValue(), Integer.valueOf(s2, 2).byteValue(), Integer.valueOf(s3, 2).byteValue()}, HTTP.UTF_8));
            } else {
                sb.append(s);
            }
        }
        return sb.toString();
    }

    private static String getHexString(byte b) {
        String hexStr = Integer.toHexString(b);
        int m = hexStr.length();
        if (m < 2) {
            return "0" + hexStr;
        }
        return hexStr.substring(m - 2);
    }

    private static String getBinaryString(int i) {
        String binaryStr = Integer.toBinaryString(i);
        int length = binaryStr.length();
        for (int l = 0; l < 8 - length; l++) {
            binaryStr = "0" + binaryStr;
        }
        return binaryStr;
    }

    public static boolean checkIP(String checkStr) {
        try {
            if (Integer.parseInt(checkStr.substring(0, checkStr.indexOf(46))) > 255) {
                return false;
            }
            checkStr = checkStr.substring(checkStr.indexOf(46) + 1);
            if (Integer.parseInt(checkStr.substring(0, checkStr.indexOf(46))) > 255) {
                return false;
            }
            checkStr = checkStr.substring(checkStr.indexOf(46) + 1);
            if (Integer.parseInt(checkStr.substring(0, checkStr.indexOf(46))) > 255 || Integer.parseInt(checkStr.substring(checkStr.indexOf(46) + 1)) > 255) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isChinese(char c) {
        UnicodeBlock ub = UnicodeBlock.of(c);
        if (ub == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == UnicodeBlock.GENERAL_PUNCTUATION || ub == UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    public static boolean isConnect(Context context) {
        NetworkInfo networkinfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }
        return true;
    }

    public String readToString(File file, String encoding) {
        byte[] filecontent = new byte[Long.valueOf(file.length()).intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e3) {
            System.err.println("The OS does not support " + encoding);
            e3.printStackTrace();
            return null;
        }
    }

    public static String getSocketAndString(BufferedReader in) throws IOException {
        String strReturn = "";
        Log.i(TAG, "br=" + in.toString());
        char[] buffer = new char[20480];
        int haveRead = in.read(buffer);
        Log.d(TAG, "收到的字符数：" + haveRead);
        if (haveRead == -1) {
            return "NoContent";
        }
        String firstStrReturn = new String(buffer, 0, haveRead);
        int firstStrReturnLength = firstStrReturn.getBytes("gb2312").length;
        Log.d(TAG, "收到的字节数：" + firstStrReturnLength);
        Log.d(TAG, "返回的报文是" + firstStrReturn);
        int contentLength = Integer.parseInt(getField(firstStrReturn, "Content-Length: ", "\r\n", 15));
        Log.d(TAG, "contentLength=" + contentLength);
        int index = firstStrReturn.indexOf("\r\n\r\n");
        Log.d(TAG, "index=" + index);
        if ((index + 4) + contentLength == firstStrReturnLength) {
            return firstStrReturn;
        }
        String contentHaveRead = firstStrReturn.substring(index + 4);
        Log.d(TAG, "最初读的=" + contentHaveRead);
        int contentHaveReadLength = contentHaveRead.getBytes("gb2312").length;
        Log.d(TAG, "最初读到的长度=" + contentHaveReadLength);
        int readyToRead = contentLength - contentHaveReadLength;
        Log.d(TAG, "readyToRead=" + readyToRead);
        int laterRead = 0;
        int laterReadCount = 0;
        while (readyToRead != laterRead) {
            laterReadCount += in.read(buffer, laterReadCount + haveRead, 5000);
            String allHaveRead = new String(buffer, haveRead, laterReadCount);
            laterRead = allHaveRead.getBytes("gb2312").length;
            Log.d(TAG, "laterRead=" + laterRead);
            Log.d(TAG, "laterReadCount=" + laterReadCount);
            Log.d(TAG, "allHaveRead.trim=" + allHaveRead.trim());
        }
        return String.valueOf(buffer);
    }

    public static byte[] getMessage(InputStream in) throws IOException {
        byte[] exception = "NoContent".getBytes();
        byte[] buffer = new byte[204800];
        int haveRead = in.read(buffer);
        Log.d(TAG, "收到的字节数：" + haveRead);
        if (haveRead == -1) {
            return exception;
        }
        String firstStrReturn = new String(buffer, 0, haveRead);
        Log.d(TAG, "返回的报文是" + firstStrReturn);
        int contentLength = Integer.parseInt(getField(firstStrReturn, "Content-Length: ", "\r\n", 15));
        Log.d(TAG, "contentLength=" + contentLength);
        if (contentLength == 0) {
            return buffer;
        }
        int index = firstStrReturn.indexOf("\r\n\r\n");
        Log.d(TAG, "index=" + index);
        if ((index + 4) + contentLength == haveRead) {
            return buffer;
        }
        Log.d(TAG, "最初读的=" + firstStrReturn.substring(index + 4));
        int contentHaveReadLength = (haveRead - index) - 4;
        Log.d(TAG, "最初读到的长度=" + contentHaveReadLength);
        int readyToRead = contentLength - contentHaveReadLength;
        Log.d(TAG, "readyToRead=" + readyToRead);
        int laterReadCount = 0;
        while (readyToRead != laterReadCount) {
            laterReadCount += in.read(buffer, laterReadCount + haveRead, 5000);
            Log.d(TAG, "laterReadCount=" + laterReadCount);
        }
        return buffer;
    }

    public static String getField(String str, String strStart, String strEnd, int i) {
        int iCookieLengthPointStart = str.indexOf(strStart);
        Log.i(TAG, "start=" + iCookieLengthPointStart);
        int iCookieLengthPointEnd = str.indexOf(strEnd, iCookieLengthPointStart);
        if (iCookieLengthPointStart == -1 || iCookieLengthPointEnd == -1) {
            return "NotContain";
        }
        Log.i(TAG, "end=" + iCookieLengthPointEnd);
        return str.substring(iCookieLengthPointStart + i, iCookieLengthPointEnd).trim();
    }
}
