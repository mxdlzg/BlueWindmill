package com.longmai.security.plugin.util;

import android.content.Context;
import android.os.Environment;
import android.os.Process;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogSaveFIle {
    private static final String fileName = "hardware.log";

    public static void writeLog(String tag, String msg) {
        Exception e;
        Throwable th;
        OutputStream out = null;
        if (Environment.getExternalStorageState().equals("mounted")) {
            try {
                OutputStream out2 = new FileOutputStream(new File(Environment.getExternalStorageDirectory(), fileName), true);
                try {
                    out2.write((getSysTime() + " : " + Process.myPid() + "    " + tag + "    " + msg + "\n").getBytes());
                    if (out2 != null) {
                        try {
                            out2.close();
                            out = out2;
                            return;
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                    out = out2;
                } catch (Exception e3) {
                    e = e3;
                    out = out2;
                    try {
                        e.printStackTrace();
                        if (out != null) {
                            try {
                                out.close();
                            } catch (IOException e22) {
                                e22.printStackTrace();
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (out != null) {
                            try {
                                out.close();
                            } catch (IOException e222) {
                                e222.printStackTrace();
                            }
                        }
                        //throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    out = out2;
                    if (out != null) {
                        out.close();
                    }
                    //throw th;
                }
            } catch (Exception e4) {
                e = e4;
                e.printStackTrace();
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

    public static void clearLog(Context ctx) {
        if (Environment.getExternalStorageState().equals("mounted")) {
            File file = new File(Environment.getExternalStorageDirectory() + "ecxSys.log");
            try {
                if (file.exists()) {
                    boolean flag = file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getSysTime() {
        return new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ").format(new Date(System.currentTimeMillis()));
    }
}
