package com.topsec.sslvpn.lib.na;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.VpnService;
import android.net.VpnService.Builder;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.system.OsConstants;
import android.util.Log;
import com.topsec.sslvpn.IVPNHelper;
import com.topsec.sslvpn.datadef.na.BaseACLInfo;
import com.topsec.sslvpn.datadef.na.NetCardConfigInfo;
import com.topsec.sslvpn.lib.VPNService;
import com.topsec.sslvpn.util.IPHelper;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

@TargetApi(14)
public class NaVpnService extends VpnService {
    private static final String TAG = "NaVpnService";
    private static BaseACLInfo[] m_baiACLPool = null;
    private static NetCardConfigInfo m_nccVirtualNetCardInfo = null;
    private Builder m_bdBuilder;
    private ParcelFileDescriptor m_pfdMainSocketFD;

    public NaVpnService() {
        this.m_bdBuilder = null;
        this.m_pfdMainSocketFD = null;
        this.m_bdBuilder = new Builder();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (intent != null) {
            Class<?> clsTmp = null;
            try {
                if (NaVpnController.getNAControllerInstance() != null) {
                    clsTmp = NaVpnController.getNAControllerInstance().GetPendingCls();
                }
                SetupVPNService(VPNService.getContext(), clsTmp);
                NaVpnController.SetNAVPNInstance(this, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "no permission to start");
            NaVpnController.SetNAVPNInstance(this, false);
        }
        return Service.START_NOT_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "The vpn service is ready to close!");
        this.m_bdBuilder = null;
        m_nccVirtualNetCardInfo = null;
        m_baiACLPool = null;
        NaVpnController.SetNAVPNInstance(null, true);
        Log.i(TAG, "close system vpn tunnel succeed!");
    }

    public void forceToClose() {
        Log.i(TAG, "Force to close service!");
        closeVPNSocket();
        stopSelf();
    }

    protected void closeVPNSocket() {
        try {
            if (this.m_pfdMainSocketFD != null) {
                this.m_pfdMainSocketFD.close();
                this.m_pfdMainSocketFD = null;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.i(TAG, "close system vpn tunnel failed!");
        }
    }

    public NetCardConfigInfo getVirtualNetCardInfo() {
        return m_nccVirtualNetCardInfo;
    }

    public static void setVirtualNetCardInfo(NetCardConfigInfo nccSrcVirtualNetCardInfo) {
        m_nccVirtualNetCardInfo = nccSrcVirtualNetCardInfo;
    }

    public BaseACLInfo[] getACLPool() {
        return m_baiACLPool;
    }

    public int getMainSocketFD() {
        if (this.m_pfdMainSocketFD == null) {
            try {
                this.m_pfdMainSocketFD = this.m_bdBuilder.establish();
                if (this.m_pfdMainSocketFD == null) {
                    stopSelf();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                if (this.m_pfdMainSocketFD == null) {
                    stopSelf();
                }
            } catch (Throwable th) {
                if (this.m_pfdMainSocketFD == null) {
                    stopSelf();
                }
            }
            Log.i(TAG, "SetupVPNService " + (this.m_pfdMainSocketFD != null ? "succeed" : "failed"));
        }
        if (this.m_pfdMainSocketFD != null) {
            return this.m_pfdMainSocketFD.getFd();
        }
        return -1;
    }

    public static void setACLPool(BaseACLInfo[] baiSrcACLPool) {
        m_baiACLPool = baiSrcACLPool;
    }

    public int setProtectedFD(int iSocket) {
        if (protect(iSocket)) {
            return 0;
        }
        stopSelf();
        return -1;
    }

    public void AddRouteRecord(String strIP, String strMask) {
        int iMaskLen = 0;
        if (strMask != null && strMask.length() > 0) {
            if (strMask.contains(".")) {
                iMaskLen = IPHelper.getMaskLen(strMask);
            } else {
                try {
                    iMaskLen = Integer.parseInt(strMask);
                } catch (NumberFormatException e) {
                    iMaskLen = -1;
                }
            }
        }
        AddRouteRecord(strIP, iMaskLen);
    }

    public void AddRouteRecord(String strIP, int iMaskLen) {
        if (strIP != null && 7 <= strIP.length() && iMaskLen >= 0) {
            try {
                this.m_bdBuilder.addRoute(strIP, iMaskLen);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            Log.i(TAG, String.format("%s/%d", new Object[]{strIP, Integer.valueOf(iMaskLen)}));
        }
    }

    public void AddRouteRecord(int iIP, int iMask) {
        AddRouteRecord(IPHelper.getIPFromInt(iIP), IPHelper.getMaskLen(iMask));
    }

    public void AddDnsServer(String strDns) {
        if (strDns != null && 7 <= strDns.length()) {
            this.m_bdBuilder.addDnsServer(strDns);
        }
    }

    public void AddDnsServer(int iDns) {
        if (iDns > 0) {
            AddDnsServer(IPHelper.getIPFromInt(iDns));
        }
    }

    protected void AddAddress(String strIP, int iMaskLen) throws UnknownHostException {
        if (strIP != null && 7 <= strIP.length() && 1 <= iMaskLen) {
            this.m_bdBuilder.addAddress(InetAddress.getByName(strIP), iMaskLen);
            this.m_bdBuilder.setSession(new StringBuilder(String.valueOf(strIP)).append("/").append(iMaskLen).toString());
        }
    }

    protected void AddAddress(int iIP, int iMask) throws UnknownHostException {
        AddAddress(IPHelper.getIPFromInt(iIP), IPHelper.getMaskLen(iMask));
    }

    PendingIntent getLogPendingIntent(Context cApp, Class<?> clsPending) {
        Intent intent = new Intent(cApp, clsPending);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        PendingIntent startLW = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return startLW;
    }

    public boolean protect(int socket) {
        return super.protect(socket);
    }

    public boolean protect(Socket socket) {
        return super.protect(socket);
    }

    public boolean protect(DatagramSocket socket) {
        return super.protect(socket);
    }

    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    public void onRevoke() {
        IVPNHelper vpsTmp = VPNService.getVPNInstance(null);
        if (vpsTmp != null) {
            vpsTmp.closeService();
        }
        NaVpnController nvpController = NaVpnController.getNAControllerInstance();
        if (nvpController != null) {
            nvpController.ResetManualCloseMark();
        }
    }

    public int getIPLen(int iIP) {
        return 32;
    }

    @SuppressLint({"InlinedApi"})
    protected int SetupVPNService(Context cApp, Class<?> clsPendingCls) throws Exception {
        if (m_nccVirtualNetCardInfo == null) {
            throw new InvalidObjectException("Invalid Virtual NetCardInfo instance!");
        } else if (this.m_bdBuilder == null) {
            throw new InvalidObjectException("Invalid Builder instance!");
        } else {
            Log.i(TAG, "Ready to start up system vpn service");
            this.m_pfdMainSocketFD = null;
            if (m_nccVirtualNetCardInfo.m_bIsSupportIPV6) {
                if (20 < VERSION.SDK_INT) {
                    this.m_bdBuilder.allowFamily(OsConstants.AF_INET);
                    this.m_bdBuilder.allowFamily(OsConstants.AF_INET6);
                }
                AddAddress(m_nccVirtualNetCardInfo.m_strVirtualIPv6, Integer.parseInt(m_nccVirtualNetCardInfo.m_strPrefixv6));
                AddDnsServer(m_nccVirtualNetCardInfo.m_strDNS1v6);
                AddDnsServer(m_nccVirtualNetCardInfo.m_strDNS2v6);
            }
            AddAddress(m_nccVirtualNetCardInfo.m_uiVirtualIP, m_nccVirtualNetCardInfo.m_uiMask);
            AddDnsServer(m_nccVirtualNetCardInfo.m_uiDNS1);
            AddDnsServer(m_nccVirtualNetCardInfo.m_uiDNS2);
            if (m_baiACLPool != null) {
                for (BaseACLInfo baiTmp : m_baiACLPool) {
                    AddRouteRecord(baiTmp.m_uiDestIP, baiTmp.m_uiDestIPMask);
                }
            }
            this.m_bdBuilder.setMtu(1500);
            if (clsPendingCls != null) {
                this.m_bdBuilder.setConfigureIntent(getLogPendingIntent(cApp, clsPendingCls));
            }
            return 0;
        }
    }
}
