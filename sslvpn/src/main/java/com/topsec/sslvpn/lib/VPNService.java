package com.topsec.sslvpn.lib;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy.Builder;
import android.text.format.Formatter;
import android.util.Log;
import android.webkit.WebView;
import com.topsec.sslvpn.IVPNHelper;
import com.topsec.sslvpn.OnAcceptResultListener;
import com.topsec.sslvpn.OnAcceptSysLogListener;
import com.topsec.sslvpn.VPNHelper;
import com.topsec.sslvpn.datadef.BaseAccountInfo;
import com.topsec.sslvpn.datadef.BaseConfigInfo;
import com.topsec.sslvpn.datadef.eExtraCodeType;
import com.topsec.sslvpn.datadef.eOperateType;
import com.topsec.sslvpn.lib.na.NaVpnController;
import com.topsec.sslvpn.util.FileFinger;
import com.topsec.sslvpn.util.ShellHelper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class VPNService extends VPNHelper implements IVPNHelper {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$topsec$sslvpn$datadef$eOperateType;
    private static Context m_acContext = null;
    private static List<WebView> m_lstwvWebViewList = null;
    private static String m_strDisplayLan = null;
    protected static IVPNHelper m_vsMain = null;
    private OnAcceptSysLogListener m_asllHandler = null;
    private NetChangeReceiver m_ccrNetChangeReceiver = null;
    @SuppressLint({"HandlerLeak"})
    private Handler m_hCurrent = new C03151();
    private NaVpnController m_nsNaController = null;
    private OnAcceptResultListener m_oarlHandler = null;

    class C03151 extends Handler {
        C03151() {
        }

        public void handleMessage(Message msg) {
            if (VPNService.this.m_oarlHandler != null) {
                Object[] objArr = (Object[])msg.obj;
                VPNService.this.m_oarlHandler.onAcceptExecResultListener(msg.what, msg.arg1, objArr[0], objArr[1]);
            }
        }
    }

    final class NetChangeReceiver extends BroadcastReceiver {
        NetChangeReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(0);
            NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(1);
            try {
                if (wifiNetInfo.isConnected()) {
                    VPNService.this.NetworkSwitched(1, wifiNetInfo.getExtraInfo());
                } else if (mobNetInfo.isConnected()) {
                    VPNService.this.NetworkSwitched(0, null);
                } else {
                    VPNService.this.NetworkSwitched(-1, null);
                }
            } catch (Exception ex) {
                Log.e("VPNSDK", ex.toString());
            }
        }
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$topsec$sslvpn$datadef$eOperateType() {
        int[] iArr = $SWITCH_TABLE$com$topsec$sslvpn$datadef$eOperateType;
        if (iArr == null) {
            iArr = new int[eOperateType.values().length];
            try {
                iArr[eOperateType.OPERATION_AUTH_LOGININFO.ordinal()] = 4;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[eOperateType.OPERATION_CHECK_NETSTATUS.ordinal()] = 14;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[eOperateType.OPERATION_CLOSE_SERVICE.ordinal()] = 13;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[eOperateType.OPERATION_FORGET_PASSWD.ordinal()] = 23;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[eOperateType.OPERATION_GET_CAPTCHA.ordinal()] = 2;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[eOperateType.OPERATION_GET_KEEPSTATUS.ordinal()] = 10;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[eOperateType.OPERATION_GET_RESOURCE.ordinal()] = 1;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[eOperateType.OPERATION_GET_SERVERCFG.ordinal()] = 3;
            } catch (NoSuchFieldError e8) {
            }
            try {
                iArr[eOperateType.OPERATION_LOGIN_SYSTEM.ordinal()] = 6;
            } catch (NoSuchFieldError e9) {
            }
            try {
                iArr[eOperateType.OPERATION_LOGOUT_SYSTEM.ordinal()] = 9;
            } catch (NoSuchFieldError e10) {
            }
            try {
                iArr[eOperateType.OPERATION_MODIFY_EMAIL.ordinal()] = 22;
            } catch (NoSuchFieldError e11) {
            }
            try {
                iArr[eOperateType.OPERATION_MODIFY_PASSWORD.ordinal()] = 20;
            } catch (NoSuchFieldError e12) {
            }
            try {
                iArr[eOperateType.OPERATION_MODIFY_TELPHONE.ordinal()] = 21;
            } catch (NoSuchFieldError e13) {
            }
            try {
                iArr[eOperateType.OPERATION_NETCONFIG_NETACCESS.ordinal()] = 18;
            } catch (NoSuchFieldError e14) {
            }
            try {
                iArr[eOperateType.OPERATION_PREPARE_NETACCESS.ordinal()] = 15;
            } catch (NoSuchFieldError e15) {
            }
            try {
                iArr[eOperateType.OPERATION_START_NETACCESS.ordinal()] = 16;
            } catch (NoSuchFieldError e16) {
            }
            try {
                iArr[eOperateType.OPERATION_START_PFPROXY.ordinal()] = 7;
            } catch (NoSuchFieldError e17) {
            }
            try {
                iArr[eOperateType.OPERATION_START_SERVICE.ordinal()] = 11;
            } catch (NoSuchFieldError e18) {
            }
            try {
                iArr[eOperateType.OPERATION_STOP_NETACCESS.ordinal()] = 17;
            } catch (NoSuchFieldError e19) {
            }
            try {
                iArr[eOperateType.OPERATION_STOP_PFPROXY.ordinal()] = 8;
            } catch (NoSuchFieldError e20) {
            }
            try {
                iArr[eOperateType.OPERATION_STOP_SERVICE.ordinal()] = 12;
            } catch (NoSuchFieldError e21) {
            }
            try {
                iArr[eOperateType.OPERATION_TRYFIX_VPNTUNNEL.ordinal()] = 19;
            } catch (NoSuchFieldError e22) {
            }
            try {
                iArr[eOperateType.OPERATION_UPLOAD_FEATURECODE.ordinal()] = 5;
            } catch (NoSuchFieldError e23) {
            }
            $SWITCH_TABLE$com$topsec$sslvpn$datadef$eOperateType = iArr;
        }
        return iArr;
    }

    protected VPNService() {
        try {
            registerReceiver();
        } catch (Exception ex) {
            Log.e("VPNSDK", "err:" + ex.toString());
        }
    }

    protected void finalize() throws Throwable {
        try {
            unRegisterReceiver();
        } catch (Exception ex) {
            Log.e("VPNSDK", "err:" + ex.toString());
        }
        super.finalize();
    }

    private static void SendMessage(Handler hDst, int iOperation, int iRet, Object objData, Object objReserved) {
        Message msg = new Message();
        msg.what = iOperation;
        msg.arg1 = iRet;
        msg.obj = new Object[]{objData, objReserved};
        msg.setTarget(hDst);
        msg.sendToTarget();
    }

    @SuppressLint({"UseSparseArrays"})
    public static synchronized IVPNHelper getVPNInstance(Context cAppContex) {
        IVPNHelper iVPNHelper = null;
        synchronized (VPNService.class) {
            if (m_vsMain != null || cAppContex == null) {
                iVPNHelper = m_vsMain;
            } else {
                m_acContext = cAppContex;
                if (IsReadyToStart(m_acContext)) {
                    try {
                        System.loadLibrary("VPNImpact");
                        enableStripMode();
                        m_vsMain = new VPNService();
                        m_lstwvWebViewList = new ArrayList();
                        m_strDisplayLan = Locale.getDefault().getLanguage();
                        String strData = "";
                        String strCurAppFileDir = m_acContext.getFilesDir().getAbsolutePath();
                        writeFile(m_acContext, new StringBuilder(String.valueOf(strCurAppFileDir)).append("/tmprsader.key").toString(), getFromAssets(m_acContext, "tmprsader.key"));
                        writeFile(m_acContext, new StringBuilder(String.valueOf(strCurAppFileDir)).append("/tmprsapem.key").toString(), getFromAssets(m_acContext, "tmprsapem.key"));
                        writeFile(m_acContext, new StringBuilder(String.valueOf(strCurAppFileDir)).append("/tmpsm2pem.key").toString(), getFromAssets(m_acContext, "tmpsm2pem.key"));
                        iVPNHelper = m_vsMain;
                    } catch (Throwable ex) {
                        Log.e(VPNService.class.getName(), "Init library failed & error:!" + ex.toString());
                    }
                } else {
                    Log.e(VPNService.class.getName(), "获取实例失败，APP权限不够，请检查（INTERNET、ACCESS_WIFI_STATE、ACCESS_NETWORK_STATE、WRITE_EXTERNAL_STORAGE 和 READ_PHONE_STATE）!");
                }
            }
        }
        return iVPNHelper;
    }

    protected static boolean IsReadyToStart(Context cAppContex) {
        if (cAppContex != null && cAppContex.checkCallingOrSelfPermission("android.permission.INTERNET") == PackageManager.PERMISSION_GRANTED && cAppContex.checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE") == PackageManager.PERMISSION_GRANTED && cAppContex.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == PackageManager.PERMISSION_GRANTED && cAppContex.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED && cAppContex.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    public static void writeFile(Context context, String fileName, String writestr) throws IOException {
        try {
            File dir = new File(fileName);
            if (!dir.exists()) {
                try {
                    dir.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("123321", "Exception1");
                }
                FileOutputStream fos = new FileOutputStream(dir);
                fos.write(writestr.getBytes());
                fos.close();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.e("123321", "Exception2 = " + e2.getMessage());
        }
    }

    public static Context getContext() {
        return m_acContext;
    }

    public static List<WebView> getProxyWebVIew() {
        return m_lstwvWebViewList;
    }

    public boolean checkNetwork() {
        NetworkInfo net = ((ConnectivityManager) m_acContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (net == null || !net.isConnected()) {
            return false;
        }
        return true;
    }

    private void ReleaseLibs() {
        String strDstFilePath = new StringBuilder(String.valueOf(m_acContext.getFilesDir().getAbsolutePath())).append("/TopsecVPN.so").toString();
        if (releaseAssetsFile(m_acContext, "libTopSecVPN.bin", strDstFilePath, false)) {
            Log.e(VPNService.class.getName(), "Release file successfully:" + strDstFilePath);
            ShellHelper.RunCmd("chmod 777 " + strDstFilePath);
            strDstFilePath = new StringBuilder(String.valueOf(m_acContext.getFilesDir().getAbsolutePath())).append("/TopsecImpact.so").toString();
            if (releaseAssetsFile(m_acContext, "libVPNImpact.bin", strDstFilePath, false)) {
                Log.e(VPNService.class.getName(), "Release file successfully:" + strDstFilePath);
                ShellHelper.RunCmd("chmod 777 " + strDstFilePath);
                try {
                    System.load(strDstFilePath);
                    return;
                } catch (Throwable ex) {
                    Log.e(VPNService.class.getName(), "Init library failed & error:!" + ex.toString());
                    return;
                }
            }
            Log.e(VPNService.class.getName(), "Release file Error!");
            return;
        }
        Log.e(VPNService.class.getName(), "Release file Error!");
    }

    @SuppressLint({"NewApi"})
    private static void enableStripMode() {
        if (VERSION.SDK_INT > 9) {
            StrictMode.setThreadPolicy(new Builder().permitAll().build());
        }
    }

    private static boolean releaseAssetsFile(Context context, String strSrcFile, String strDstFile, boolean bReplace) {
        try {
            FileFinger ffTmp = new FileFinger();
            InputStream in = context.getAssets().open(strSrcFile);
            File fFile = new File(strDstFile);
            if (in.available() == 0) {
                return false;
            }
            if (fFile.exists()) {
                if (bReplace || !ffTmp.getFileMD5String(fFile).equalsIgnoreCase(ffTmp.getFileMD5String(in))) {
                    fFile.delete();
                } else {
                    Log.i("VPNService", "file " + strSrcFile + " is exists.");
                    return true;
                }
            }
            in = context.getAssets().open(strSrcFile);
            FileOutputStream out = new FileOutputStream(strDstFile);
            byte[] buffer = new byte[4096];
            while (true) {
                int read = in.read(buffer);
                if (read <= 0) {
                    out.close();
                    in.close();
                    return true;
                }
                out.write(buffer, 0, read);
            }
        } catch (IOException e) {
            Log.e(strSrcFile, e.getMessage());
            return false;
        }
    }

    private void registerReceiver() {
        if (m_acContext != null) {
            IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
            this.m_ccrNetChangeReceiver = new NetChangeReceiver();
            m_acContext.registerReceiver(this.m_ccrNetChangeReceiver, filter);
        }
    }

    private void unRegisterReceiver() {
        if (m_acContext != null) {
            m_acContext.unregisterReceiver(this.m_ccrNetChangeReceiver);
        }
    }

    private String getGateWayIP() {
        if (m_acContext == null) {
            return "";
        }
        WifiManager wifi_service = (WifiManager) m_acContext.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcpInfo = wifi_service.getDhcpInfo();
        WifiInfo wifiinfo = wifi_service.getConnectionInfo();
        return Formatter.formatIpAddress(dhcpInfo.ipAddress);
    }

    public String getErrorInfoByCode(int iErrorCode) {
        if (m_strDisplayLan == null || m_strDisplayLan.toLowerCase().contains("zh")) {
            return super.GetLastErrInfo(1, iErrorCode);
        }
        return super.GetLastErrInfo(0, iErrorCode);
    }

    public synchronized int loginVOne(BaseAccountInfo baiActInfo) {
        return LoginVOne(baiActInfo);
    }

    public synchronized int logoutVOne() {
        return LogoutVOne();
    }

    public synchronized int setConfigInfo(BaseConfigInfo bcicfgInfo) {
        int i;
        if (bcicfgInfo == null) {
            i = -2;
        } else {
            switch (bcicfgInfo.m_iEnableModule) {
                case 2:
                    this.m_nsNaController = NaVpnController.getNAControllerInstance();
                    break;
            }
            i = SetConfigInfo(bcicfgInfo);
        }
        return i;
    }

    public void setOnAcceptResultListener(OnAcceptResultListener oarlSrc) {
        this.m_oarlHandler = oarlSrc;
    }

    public void setOnAcceptSysLogListener(OnAcceptSysLogListener oaslSrc) {
        this.m_asllHandler = oaslSrc;
    }

    public synchronized int startService() {
        return StartService();
    }

    public synchronized int stopService() {
        return StopService();
    }

    public int requestVPNResInfo() {
        return RequestVPNResInfo();
    }

    public synchronized int addProxyItem(WebView wvSrc) {
        int i = -1;
        synchronized (this) {
            if (wvSrc != null) {
                if (m_lstwvWebViewList.contains(wvSrc)) {
                    i = -32;
                } else {
                    for (int ii = m_lstwvWebViewList.size() - 1; ii > -1; ii--) {
                        WebView wvTmp = (WebView) m_lstwvWebViewList.get(ii);
                        if (wvTmp == null) {
                            m_lstwvWebViewList.remove(ii);
                        } else if (wvTmp.getId() == wvSrc.getId()) {
                            m_lstwvWebViewList.remove(ii);
                        }
                    }
                    m_lstwvWebViewList.add(wvSrc);
                    i = AddProxyItem(wvSrc);
                }
            }
        }
        return i;
    }

    public synchronized int removeProxyItem(WebView wvSrc) {
        int RemoveProxyItem;
        if (m_lstwvWebViewList.contains(wvSrc)) {
            m_lstwvWebViewList.remove(wvSrc);
            RemoveProxyItem = RemoveProxyItem(wvSrc);
        } else {
            RemoveProxyItem = -4;
        }
        return RemoveProxyItem;
    }

    protected int pushExecuteResult(int iOperationID, int iRetValue, Object objExtraValue, Object objReserved) {
        switch ($SWITCH_TABLE$com$topsec$sslvpn$datadef$eOperateType()[eOperateType.valueOf(iOperationID).ordinal()]) {
            case 15:
                if (this.m_nsNaController != null) {
                    return this.m_nsNaController.PreareNAService();
                }
                return -30;
            case 18:
                if (4 == iRetValue) {
                    SendMessage(this.m_hCurrent, iOperationID, iRetValue, objExtraValue, objReserved);
                }
                return this.m_nsNaController.ProcessExecResult(iRetValue, objExtraValue, objReserved);
            default:
                SendMessage(this.m_hCurrent, iOperationID, iRetValue, objExtraValue, objReserved);
                return 0;
        }
    }

    protected void pushSysLogInfo(int iLogLevel, String strLogTag, String strLogInfo) {
        if (this.m_asllHandler != null) {
            this.m_asllHandler.onAcceptSysLogInfo(iLogLevel, strLogTag, strLogInfo);
        }
    }

    public int requestCaptcha() {
        return RequestCaptcha();
    }

    public synchronized int closeService() {
        if (this.m_nsNaController != null) {
            this.m_nsNaController.SetManualCloseMark();
        }
        return CloseService();
    }

    public int queryVPNRunningState() {
        return QueryVPNRunningState();
    }

    public int getVPNRunningStateInSyncMode() {
        return GetVPNRunningStateInSyncMode();
    }

    public static String getFromAssets(Context context, String fileName) {
        try {
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(context.getAssets().open(fileName)));
            String line = "";
            String Result = "";
            while (true) {
                line = bufReader.readLine();
                if (line == null) {
                    return Result;
                }
                Result = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(Result)).append(line).toString())).append("\r\n").toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getExchangeDataFromMode(int iWorkModel) {
        return super.GetExchangeDataFromMode(iWorkModel);
    }

    public synchronized void toGrantStartVpnService(int resultCode) {
        if (this.m_nsNaController != null) {
            this.m_nsNaController.toGrantStartVpnService(resultCode);
        }
    }

    public int startService(Activity aMainActivity, Class<?> clsCfgActivityCls) {
        if (aMainActivity == null) {
            throw new NullPointerException("Invalid or activity");
        }
        if (this.m_nsNaController != null) {
            this.m_nsNaController.SetSetupInfo(aMainActivity, clsCfgActivityCls);
        }
        return startService();
    }

    public int modifyPasswordInSyncMode(String strNewPasswd, String strOldPasswd) {
        return ModifyPasswordInSyncMode(strNewPasswd, strOldPasswd);
    }

    public int modifyPassword(String strNewPasswd, String strOldPasswd) {
        return ModifyPassword(strNewPasswd, strOldPasswd);
    }

    public String getCertificateContentInSyncMode() {
        return GetCertificateContentInSyncMode();
    }

    public int kickTheCurrentUserOffImmediately(String strCapatcha) {
        return KickTheCurrentUserOffImmediately(strCapatcha);
    }

    public int continueToLoginWithExtraCode(String strExtraCode, eExtraCodeType ectExtraCodeType) {
        return ContinueToLoginWithExtraCode(strExtraCode, ectExtraCodeType.value());
    }

    public int modifyMobilePhoneInSyncMode(String strNewMobilePhone) {
        return ModifyMobilePhoneInSyncMode(strNewMobilePhone);
    }

    public int modifyMobilePhone(String strNewMobilePhone) {
        return ModifyMobilePhone(strNewMobilePhone);
    }

    public int modifyEmailInSyncMode(String strNewEmail) {
        return ModifyEmailInSyncMode(strNewEmail);
    }

    public int modifyEmail(String strNewEmail) {
        return ModifyEmail(strNewEmail);
    }

    public TrafficStatistic getTrafficStatisticInstance() {
        return TrafficStatistic.getInstance();
    }

    public void recycle() {
        Recycle();
    }

    public int retrievePassword(String strUserName, String strCapatcha) {
        return RetrievePassword(strUserName, strCapatcha);
    }

    public int retrievePasswordInSyncMode(String strUserName, String strCapatcha) {
        return RetrievePasswordInSyncMode(strUserName, strCapatcha);
    }
}
