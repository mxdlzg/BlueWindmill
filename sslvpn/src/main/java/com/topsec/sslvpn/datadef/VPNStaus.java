package com.topsec.sslvpn.datadef;

public class VPNStaus {
    public static final int RUNSTATUS_LOGGEDIN = 4;
    public static final int RUNSTATUS_LOGGINGIN = 2;
    public static final int RUNSTATUS_RUNNING = 16;
    public static final int RUNSTATUS_SHUTDOWN = 32;
    public static final int RUNSTATUS_UNLOGIN = 1;

    public static boolean IsUserLoggedin(int iStatusValue) {
        return 4 == (iStatusValue & 4);
    }

    public static boolean IsVPNServiceRunning(int iStatusValue) {
        return 16 == (iStatusValue & 16);
    }
}
