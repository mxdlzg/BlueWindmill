package com.topsec.sslvpn.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@SuppressLint({"SimpleDateFormat", "SdCardPath"})
public class CrashHandler implements UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";
    private static CrashHandler m_schMain = null;
    private Context m_cContext;
    private DateFormat m_dfFormatter = new SimpleDateFormat("yyyyMMddhhmmssSSS");
    private Map<String, String> m_mpInfos = new HashMap();
    private String m_strLogDir = null;
    private UncaughtExceptionHandler m_uehDefaultHandler;

    class C03161 extends Thread {
        C03161() {
        }

        public void run() {
            Looper.prepare();
            Toast.makeText(CrashHandler.this.m_cContext, "很抱歉,程序出现异常,即将退出.", 1).show();
            Looper.loop();
        }
    }

    private CrashHandler() {
    }

    public static CrashHandler Initialize(Context cAppContext) {
        if (m_schMain == null) {
            if (cAppContext == null) {
                return m_schMain;
            }
            String strLogDir = Loger.GetDefaultLogDir(cAppContext);
            if (strLogDir == null || 1 > strLogDir.trim().length()) {
                strLogDir = new StringBuilder(String.valueOf(cAppContext.getFilesDir().getAbsolutePath())).append("/").toString();
            }
            File dir = new File(strLogDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            m_schMain = new CrashHandler();
            m_schMain.m_cContext = cAppContext;
            m_schMain.m_strLogDir = strLogDir;
            m_schMain.m_uehDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(m_schMain);
        }
        return m_schMain;
    }

    public void uncaughtException(Thread thread, Throwable ex) {
        if (handleException(ex) || this.m_uehDefaultHandler == null) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Log.e(TAG, "error : ", e);
            }
            Process.killProcess(Process.myPid());
            System.exit(1);
            return;
        }
        this.m_uehDefaultHandler.uncaughtException(thread, ex);
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        new C03161().start();
        collectDeviceInfo(this.m_cContext);
        saveCrashInfo2File(ex);
        return true;
    }

    public void collectDeviceInfo(Context ctx) {
        try {
            PackageInfo pi = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 1);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = new StringBuilder(String.valueOf(pi.versionCode)).toString();
                this.m_mpInfos.put("versionName", versionName);
                this.m_mpInfos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        for (Field field : Build.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                this.m_mpInfos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e2) {
                Log.e(TAG, "an error occured when collect crash info", e2);
            }
        }
    }

    private String saveCrashInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Entry<String, String> entry : this.m_mpInfos.entrySet()) {
            String value = (String) entry.getValue();
            sb.append(new StringBuilder(String.valueOf((String) entry.getKey())).append("=").append(value).append("\n").toString());
        }
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        for (Throwable cause = ex.getCause(); cause != null; cause = cause.getCause()) {
            cause.printStackTrace(printWriter);
        }
        printWriter.close();
        sb.append(writer.toString());
        try {
            String fileName = "crash-" + this.m_dfFormatter.format(new Date()) + "-" + System.currentTimeMillis() + ".log";
            if (!Environment.getExternalStorageState().equals("mounted")) {
                return fileName;
            }
            File fTmp = new File(this.m_strLogDir + fileName);
            if (!fTmp.exists()) {
                fTmp.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(this.m_strLogDir + fileName);
            fos.write(sb.toString().getBytes());
            fos.close();
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
            return null;
        }
    }
}
