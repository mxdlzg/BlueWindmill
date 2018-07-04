package com.topsec.sslvpn.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InvalidObjectException;
import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class Loger {
    private static boolean m_bEnableLogcat = false;
    private static File m_fLogFile = null;
    private static FileOutputStream m_fosFileStream = null;
    private static int m_iSaveLevel = 0;
    private static long m_lCurLogFileLen = 0;
    private static long m_lLogFileLength = 0;
    private static StringBuilder m_sbBuffer = null;
    private static SimpleDateFormat m_sdfFormater = null;

    static class C03171 implements FilenameFilter {
        C03171() {
        }

        public boolean accept(File fDir, String strName) {
            return strName.endsWith(".log") && Loger.m_fLogFile.getName().equals(strName);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @android.annotation.SuppressLint({"SimpleDateFormat"})
    public static void Initialize(Context paramContext, String paramString1, int paramInt1, int paramInt2, int paramInt3, String paramString2, boolean paramBoolean)
            throws InvalidParameterException
    {
        String str = null;
        if (paramString1 != null) {
            str = paramString1;
        }
        try
        {
            if ("".equals(paramString1))
            {
                paramString1 = paramContext.getPackageName();
                str = GetDefaultLogDir(paramContext) + paramString1 + ".log";
            }
            if ((m_fosFileStream != null) || (!OpenFile(str))) {
                ;
            }
            if ((2 > paramInt1) && (6 < paramInt1)) {
                throw new InvalidParameterException("the iSaveMinLevel value must >=Log.VERBOSE or <= Log.ERROR");
            }
        }
        finally {}
        int i = paramInt3;
        if (1048576 > paramInt3) {
            i = 1048576;
        }
        long l = i;
        m_lLogFileLength = l;
        paramInt3 = paramInt2;
        if (1024 > paramInt2) {
            paramInt3 = 1024;
        }
        m_sbBuffer = new StringBuilder(paramInt3);
        m_sdfFormater = new SimpleDateFormat(paramString2);
        m_iSaveLevel = paramInt1;
        m_bEnableLogcat = paramBoolean;
    }

    public static String GetDefaultLogDir(Context cAppContex) {
        String strTmp = cAppContex.getPackageName();
        String strFilePath = "";
        int iPos = strTmp.lastIndexOf(46);
        if (-1 < iPos) {
            strTmp = strTmp.substring(iPos + 1);
        }
        strFilePath = getExternalSdCardPath();
        if (strFilePath != null) {
            File fTmp = new File(strFilePath);
            if (!fTmp.exists()) {
                fTmp.mkdirs();
            }
            strFilePath = new StringBuilder(String.valueOf(strFilePath)).append("/").append(strTmp).append("/log/").toString();
        } else {
            strFilePath = new StringBuilder(String.valueOf(cAppContex.getFilesDir().getAbsolutePath())).append("/").append(strTmp).append("/log/").toString();
        }
        File fLogDir = new File(strFilePath);
        if (!fLogDir.exists()) {
            fLogDir.mkdirs();
        }
        return strFilePath;
    }

    public static synchronized void UnInitialize() {
        synchronized (Loger.class) {
            Flush();
            CloseFile();
        }
    }

    public static void m28i(String strTagName, String strContent) throws InvalidObjectException {
        WriteLog(4, strTagName, strContent);
    }

    public static void m30i(String strTagName, Throwable taSrc) throws InvalidObjectException {
        WriteLog(4, strTagName, taSrc);
    }

    public static void m29i(String strTagName, String strContent, Throwable taSrc) throws InvalidObjectException {
        WriteLog(4, strTagName, strContent, taSrc);
    }

    public static void m31w(String strTagName, String strContent) throws InvalidObjectException {
        WriteLog(5, strTagName, strContent);
    }

    public static void m33w(String strTagName, Throwable taSrc) throws InvalidObjectException {
        WriteLog(5, strTagName, taSrc);
    }

    public static void m32w(String strTagName, String strContent, Throwable taSrc) throws InvalidObjectException {
        WriteLog(5, strTagName, strContent, taSrc);
    }

    public static void m22d(String strTagName, String strContent) throws InvalidObjectException {
        WriteLog(3, strTagName, strContent);
    }

    public static void m24d(String strTagName, Throwable taSrc) throws InvalidObjectException {
        WriteLog(3, strTagName, taSrc);
    }

    public static void m23d(String strTagName, String strContent, Throwable taSrc) throws InvalidObjectException {
        WriteLog(3, strTagName, strContent, taSrc);
    }

    public static void m25e(String strTagName, String strContent) throws InvalidObjectException {
        WriteLog(6, strTagName, strContent);
    }

    public static void m27e(String strTagName, Throwable taSrc) throws InvalidObjectException {
        WriteLog(6, strTagName, taSrc);
    }

    public static void m26e(String strTagName, String strContent, Throwable taSrc) throws InvalidObjectException {
        WriteLog(6, strTagName, strContent, taSrc);
    }

    private static boolean OpenFile(String strFilePath) {
        try {
            m_fLogFile = new File(strFilePath);
            if (m_fLogFile.exists()) {
                m_lCurLogFileLen = m_fLogFile.length();
                m_fosFileStream = new FileOutputStream(m_fLogFile, true);
            } else {
                m_fLogFile.createNewFile();
                m_fosFileStream = new FileOutputStream(m_fLogFile);
            }
            Log.i("Loger", "Log file: " + strFilePath);
            return true;
        } catch (FileNotFoundException e) {
            Log.e("Loger", e.toString());
            return false;
        } catch (IOException e2) {
            Log.e("Loger", e2.toString());
            return false;
        }
    }

    private static boolean CloseFile() {
        try {
            m_fosFileStream.close();
            m_fosFileStream = null;
            m_sdfFormater = null;
            m_sbBuffer = null;
            m_fLogFile = null;
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @SuppressLint({"SimpleDateFormat"})
    public static void Flush() {
        try {
            if (m_sbBuffer != null) {
                byte[] bTmp = m_sbBuffer.toString().getBytes();
                m_fosFileStream.write(bTmp);
                m_fosFileStream.flush();
                m_lCurLogFileLen += (long) bTmp.length;
                m_sbBuffer.setLength(0);
                if (m_lCurLogFileLen >= m_lLogFileLength) {
                    m_fosFileStream.close();
                    String strOld = m_fLogFile.getName();
                    int iPos = strOld.indexOf(46);
                    m_fLogFile.renameTo(new File(new StringBuilder(String.valueOf(m_fLogFile.getParent())).append("/").append(new SimpleDateFormat("MMddHHmmss").format(new Date())).append(".log").toString()));
                    if (-1 < iPos) {
                        strOld = strOld.substring(0, iPos);
                    }
                    ZipLogFile(strOld, 5);
                    m_fLogFile.createNewFile();
                    OpenFile(m_fLogFile.getAbsolutePath());
                }
            }
        } catch (IOException e) {
        }
    }

    private static synchronized void ZipLogFile(String strFileName, int iLimits) {
        synchronized (Loger.class) {
            File[] flst = new File(m_fLogFile.getParent()).listFiles(new C03171());
            if (flst != null && flst.length > iLimits) {
                strFileName = new StringBuilder(String.valueOf(m_fLogFile.getParent())).append("/").append(strFileName).append("_logs.zip").toString();
                File fTmp = new File(strFileName);
                if (fTmp.length() > 67108864) {
                    fTmp.delete();
                }
                try {
                    ZipHelper.ZipFile(flst, strFileName, true);
                    for (File fCur : flst) {
                        fCur.delete();
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
    }

    public static void WriteLog(int iLogLevel, String strTagName, String strContent) throws InvalidObjectException {
        WriteLog(iLogLevel, strTagName, strContent, null);
    }

    public static void WriteLog(int iLogLevel, String strTagName, Throwable taSrc) throws InvalidObjectException {
        WriteLog(iLogLevel, strTagName, "", taSrc);
    }

    public static synchronized void WriteLog(int iLogLevel, String strTagName, String strContent, Throwable taSrc) throws InvalidObjectException {
        synchronized (Loger.class) {
            if (m_fosFileStream == null) {
                throw new InvalidObjectException("we do not found a invalid instance!");
            }
            String strLogLevel = WriteLogToLogCat(iLogLevel, strTagName, strContent);
            if (m_fosFileStream != null && m_iSaveLevel <= iLogLevel) {
                String strTmp = GetString(strLogLevel, strTagName, strContent) + GetThrowableInfo(taSrc) + "\n";
                if (m_sbBuffer.capacity() == strTmp.length() + m_sbBuffer.length()) {
                    m_sbBuffer.append(strTmp);
                    Flush();
                } else if (m_sbBuffer.capacity() < strTmp.length() + m_sbBuffer.length()) {
                    Flush();
                    m_sbBuffer.append(strTmp);
                } else {
                    m_sbBuffer.append(strTmp);
                }
            }
        }
    }

    private static String GetString(String strLogLevel, String strTagName, String strContent) {
        return String.format("%s: [%s]-[%s] [%s]\n", new Object[]{m_sdfFormater.format(new Date()), strLogLevel, strTagName, strContent});
    }

    private static String WriteLogToLogCat(int iLogLevel, String strTagName, String strContent) {
        String strLogLevel = "";
        switch (iLogLevel) {
            case 1:
                strLogLevel = "DEBUG";
                if (m_bEnableLogcat) {
                    Log.d(strTagName, strContent);
                    break;
                }
                break;
            case 2:
                strLogLevel = "INFO";
                if (m_bEnableLogcat) {
                    Log.i(strTagName, strContent);
                    break;
                }
                break;
            case 4:
                strLogLevel = "WARN";
                if (m_bEnableLogcat) {
                    Log.w(strTagName, strContent);
                    break;
                }
                break;
            case 8:
                strLogLevel = "ERROR";
                if (m_bEnableLogcat) {
                    Log.e(strTagName, strContent);
                    break;
                }
                break;
            default:
                strLogLevel = "UNKNOWN";
                if (m_bEnableLogcat) {
                    Log.e(strTagName, strContent);
                    break;
                }
                break;
        }
        return strLogLevel;
    }

    private static String GetThrowableInfo(Throwable taSrc) {
        if (taSrc == null) {
            return "";
        }
        return String.format("%s(%s:%d)$%s", new Object[]{taSrc.getStackTrace()[1].getMethodName(), taSrc.getStackTrace()[1].getFileName(), Integer.valueOf(taSrc.getStackTrace()[1].getLineNumber()), taSrc.getMessage()});
    }

    @SuppressLint({"SimpleDateFormat"})
    protected static String getExternalSdCardPath() {
        if (isMounted()) {
            return new File(Environment.getExternalStorageDirectory().getAbsolutePath()).getAbsolutePath();
        }
        String path = null;
        Iterator it = getDevMountList().iterator();
        while (it.hasNext()) {
            String devMount = (String) it.next();
            if (devMount.indexOf("storage") >= 0) {
                File file = new File(devMount);
                if (file.isDirectory() && file.canWrite()) {
                    path = file.getAbsolutePath();
                    File testWritable = new File(path, "test_" + new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date()));
                    if (testWritable.mkdirs()) {
                        testWritable.delete();
                    } else {
                        path = null;
                    }
                }
            }
        }
        if (path != null) {
            return new File(path).getAbsolutePath();
        }
        return null;
    }

    private static boolean isMounted() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    private static ArrayList<String> getDevMountList() {
        IOException e;
        Throwable th;
        ArrayList<String> out = new ArrayList();
        BufferedReader bfTmp = null;
        try {
            Process pCur = Runtime.getRuntime().exec("cat /proc/mounts");
            if (pCur != null) {
                BufferedReader bfTmp2 = new BufferedReader(new InputStreamReader(pCur.getInputStream()));
                while (true) {
                    try {
                        String strTmp = bfTmp2.readLine();
                        if (strTmp == null) {
                            break;
                        }
                        String[] toSearch = strTmp.split(" ");
                        if (new File(toSearch[1]).exists()) {
                            out.add(toSearch[1]);
                        }
                    } catch (IOException e2) {
                        e = e2;
                        bfTmp = bfTmp2;
                    } catch (Throwable th2) {
                        th = th2;
                        bfTmp = bfTmp2;
                    }
                }
                if (bfTmp2 != null) {
                    try {
                        bfTmp2.close();
                        bfTmp = bfTmp2;
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
                bfTmp = bfTmp2;
            } else if (bfTmp != null) {
                try {
                    bfTmp.close();
                } catch (IOException e32) {
                    e32.printStackTrace();
                }
            }
        } catch (IOException e4) {
            IOException e32 = e4;
            try {
                e32.printStackTrace();
                if (bfTmp != null) {
                    try {
                        bfTmp.close();
                    } catch (IOException e322) {
                        e322.printStackTrace();
                    }
                }
                return out;
            } catch (Throwable th3) {
                th = th3;
                if (bfTmp != null) {
                    try {
                        bfTmp.close();
                    } catch (IOException e3222) {
                        e3222.printStackTrace();
                    }
                }
                //throw th;
            }
        }
        return out;
    }
}
