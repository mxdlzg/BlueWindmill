package com.longmai.security.plugin.util;

import android.util.Log;

public class LogUtil {
    public static volatile boolean isDebug = false;
    public static volatile boolean writeFile = false;

    public static boolean isDebug() {
        return isDebug;
    }

    public static void setDebug(boolean isDebug) {
        isDebug = isDebug;
    }

    public static boolean isWriteFile() {
        return writeFile;
    }

    public static void setWriteFile(boolean writeFile) {
        writeFile = writeFile;
    }

    public static void m16v(String tag, String msg) {
        if (isDebug) {
            Log.v(tag, msg);
        }
        if (writeFile) {
            LogSaveFIle.writeLog(tag, msg);
        }
    }

    public static void m17v(String tag, String msg, Throwable t) {
        if (isDebug) {
            Log.v(tag, msg, t);
        }
        if (writeFile) {
            LogSaveFIle.writeLog(tag, msg);
        }
    }

    public static void m10d(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
        if (writeFile) {
            LogSaveFIle.writeLog(tag, msg);
        }
    }

    public static void m11d(String tag, String msg, Throwable t) {
        if (isDebug) {
            Log.d(tag, msg, t);
        }
        if (writeFile) {
            LogSaveFIle.writeLog(tag, msg);
        }
    }

    public static void m14i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
        if (writeFile) {
            LogSaveFIle.writeLog(tag, msg);
        }
    }

    public static void m15i(String tag, String msg, Throwable t) {
        if (isDebug) {
            Log.i(tag, msg, t);
        }
        if (writeFile) {
            LogSaveFIle.writeLog(tag, msg);
        }
    }

    public static void m18w(String tag, String msg) {
        if (isDebug) {
            Log.w(tag, msg);
        }
        if (writeFile) {
            LogSaveFIle.writeLog(tag, msg);
        }
    }

    public static void m19w(String tag, String msg, Throwable t) {
        if (isDebug) {
            Log.w(tag, msg, t);
        }
        if (writeFile) {
            LogSaveFIle.writeLog(tag, msg);
        }
    }

    public static void m12e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
        if (writeFile) {
            LogSaveFIle.writeLog(tag, msg);
        }
    }

    public static void m13e(String tag, String msg, Throwable t) {
        if (isDebug) {
            Log.e(tag, msg, t);
        }
        if (writeFile) {
            LogSaveFIle.writeLog(tag, msg);
        }
    }
}
