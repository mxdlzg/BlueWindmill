package com.topsec.sslvpn.lib.na;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.topsec.sslvpn.datadef.na.BaseACLInfo;
import com.topsec.sslvpn.datadef.na.NetCardConfigInfo;
import com.topsec.sslvpn.lib.VPNService;
import java.util.List;

public class NaVpnController {
    private static final int DNS_ADD_REQUEST = 2;
    private static final int DNS_DEL_REQUEST = 32;
    private static final int GET_ESTSOCKET_REQUEST = 128;
    public static final int NACORE_SETUP_REQUEST = 4;
    private static final int NACORE_UNSETUP_REQUEST = 64;
    private static final int ROUTE_ADD_REQUEST = 1;
    private static final int ROUTE_DEL_REQUEST = 16;
    private static final int SET_PROTECTEDFD_REQUEST = 8;
    private static boolean m_bSetupedService = false;
    private static NaVpnController m_nsNaController = null;
    private static NaVpnService m_nvsNAInstance = null;
    private Activity m_aMainActivity = null;
    private boolean m_bGranted = false;
    private boolean m_bIsManualOperation = false;
    private boolean m_bWaitingForChoice = false;
    private Class<?> m_clsPendingCls;

    NaVpnController() {
        m_nsNaController = this;
    }

    public Class<?> GetPendingCls() {
        return this.m_clsPendingCls;
    }

    public static NaVpnController getNAControllerInstance() {
        if (m_nsNaController != null) {
            return m_nsNaController;
        }
        return new NaVpnController();
    }

    public void ResetManualCloseMark() {
        this.m_bIsManualOperation = false;
    }

    public void SetManualCloseMark() {
        this.m_bIsManualOperation = true;
    }

    public boolean IsManualClose() {
        return this.m_bIsManualOperation;
    }

    public void SetSetupInfo(Activity aMainActivity, Class<?> cls) {
        this.m_aMainActivity = aMainActivity;
    }

    public static void SetNAVPNInstance(NaVpnService nvsNAInstance, boolean bIsSetuped) {
        m_nvsNAInstance = nvsNAInstance;
        m_bSetupedService = bIsSetuped;
    }

    @TargetApi(14)
    public int PreareNAService() {
        if (m_nvsNAInstance != null) {
            return -31;
        }
        Intent iTmp = NaVpnService.prepare(this.m_aMainActivity);
        if (iTmp != null) {
            this.m_bWaitingForChoice = true;
            this.m_aMainActivity.startActivityForResult(iTmp, 0);
            waitForGrant();
            if (isGranted()) {
                return 0;
            }
            return -41;
        }
        toGrantStartVpnService(-1);
        return 0;
    }

    public int StartNAService() throws Exception {
        if (!this.m_bGranted) {
            return -3;
        }
        ResetManualCloseMark();
        m_bSetupedService = false;
        ComponentName objName = VPNService.getContext().startService(new Intent(VPNService.getContext(), NaVpnService.class));
        waitForServiceRun();
        if (objName == null) {
            m_bSetupedService = false;
        }
        if (m_bSetupedService) {
            return 0;
        }
        m_nvsNAInstance.forceToClose();
        m_nvsNAInstance = null;
        return -2;
    }

    public void CloseService() {
        m_bSetupedService = false;
        if (m_nvsNAInstance != null) {
            m_nvsNAInstance.forceToClose();
        }
        waitForServiceclose();
    }

    public int getMainSocketFD() {
        return m_nvsNAInstance.getMainSocketFD();
    }

    public void toGrantStartVpnService(int resultCode) {
        boolean z = false;
        this.m_bWaitingForChoice = false;
        if (-1 == resultCode) {
            z = true;
        }
        this.m_bGranted = z;
    }

    protected void waitForServiceRun() {
        int iRetryCount = 0;
        while (m_nvsNAInstance == null && 100 >= iRetryCount) {
            iRetryCount++;
            try {
                Log.i(NaVpnController.class.getSimpleName(), "wait for vpnservice run with " + iRetryCount);
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void waitForServiceclose() {
        int iRetryCount = 0;
        while (m_nvsNAInstance != null) {
            iRetryCount++;
            if (40 < iRetryCount) {
                break;
            }
            try {
                Log.i(NaVpnController.class.getSimpleName(), "wait for vpnservice close with " + iRetryCount);
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.gc();
    }

    public boolean isServiceWork(Context mContext, Class<?> clsClassNeedToCheck) {
        List<RunningServiceInfo> lstServiceInfo = ((ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE);
        for (int i = 0; i < lstServiceInfo.size(); i++) {
            if (((RunningServiceInfo) lstServiceInfo.get(i)).service.getClassName().equals(clsClassNeedToCheck.getName())) {
                return true;
            }
        }
        return false;
    }

    protected void waitForGrant() {
        int iRetryCount = 0;
        while (this.m_bWaitingForChoice) {
            iRetryCount++;
            if (200 >= iRetryCount) {
                try {
                    Thread.sleep(250);
                    Log.i(NaVpnController.class.getSimpleName(), "wait for choice");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                return;
            }
        }
    }

    protected final boolean isGranted() {
        return this.m_bGranted;
    }

    protected int ConverObjToInt(Object objSrc) {
        try {
            return ((Integer) objSrc).intValue();
        } catch (Exception ex) {
            Log.e(NaVpnController.class.getSimpleName(), ex.toString());
            return -1;
        }
    }

    public int ProcessExecResult(int iRetValue, Object objExtraValue, Object objReserved) {
        if (4 == iRetValue) {
            if (m_nvsNAInstance != null) {
                CloseService();
            }
            NaVpnService.setVirtualNetCardInfo((NetCardConfigInfo) objExtraValue);
            NaVpnService.setACLPool((BaseACLInfo[]) objReserved);
            try {
                return StartNAService();
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        } else if (m_nvsNAInstance == null) {
            return -10;
        } else {
            switch (iRetValue) {
                case 1:
                    m_nvsNAInstance.AddRouteRecord((String) objExtraValue, (String) objReserved);
                    break;
                case 2:
                    m_nvsNAInstance.AddDnsServer((String) objExtraValue);
                    break;
                case 4:
                    NaVpnService.setVirtualNetCardInfo((NetCardConfigInfo) objExtraValue);
                    NaVpnService.setACLPool((BaseACLInfo[]) objReserved);
                    break;
                case 8:
                    return m_nvsNAInstance.setProtectedFD(ConverObjToInt(objExtraValue));
                case 64:
                    CloseService();
                    break;
                case 128:
                    return m_nvsNAInstance.getMainSocketFD();
            }
            return 0;
        }
    }
}
