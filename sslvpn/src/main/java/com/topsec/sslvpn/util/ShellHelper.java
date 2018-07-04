package com.topsec.sslvpn.util;

import android.util.Log;
import java.io.DataOutputStream;
import java.io.IOException;

public class ShellHelper {
    public static void RunCmd(String strCmd) {
        IOException e;
        Throwable th;
        DataOutputStream ostream = null;
        try {
            Runtime runtime = Runtime.getRuntime();
            if (false) {
                DataOutputStream ostream2 = new DataOutputStream(runtime.exec("su").getOutputStream());
                if (ostream2 != null) {
                    try {
                        ostream2.writeBytes(strCmd);
                        ostream2.writeBytes("exit\n");
                        ostream2.flush();
                        ostream2.close();
                        ostream = ostream2;
                    } catch (IOException e2) {
                        e = e2;
                        ostream = ostream2;
                        try {
                            Log.e("ShellHelper", e.getMessage());
                            if (ostream != null) {
                                try {
                                    ostream.close();
                                } catch (Exception e3) {
                                    Log.e("ShellHelper", "closing DataOutputStream", e3);
                                    return;
                                }
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            if (ostream != null) {
                                try {
                                    ostream.close();
                                } catch (Exception e32) {
                                    Log.e("ShellHelper", "closing DataOutputStream", e32);
                                }
                            }
                            //throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        ostream = ostream2;
                        if (ostream != null) {
                            ostream.close();
                        }
                        //throw th;
                    }
                }
                ostream = ostream2;
            } else {
                runtime.exec(strCmd);
            }
            if (ostream != null) {
                try {
                    ostream.close();
                } catch (Exception e322) {
                    Log.e("ShellHelper", "closing DataOutputStream", e322);
                }
            }
        } catch (IOException e4) {
            e = e4;
            Log.e("ShellHelper", e.getMessage());
            if (ostream != null) {
                try {
                    ostream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
