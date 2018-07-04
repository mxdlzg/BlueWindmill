package com.topsec.topsap.application;

import android.app.ProgressDialog;
import android.widget.Button;
import com.topsec.sslvpn.datadef.BaseCaptchaInfo;
import com.topsec.sslvpn.datadef.BaseResourceInfo;
import com.topsec.sslvpn.datadef.ServiceAuthCfg.eCaptchaType;
import com.topsec.sslvpn.datadef.na.BaseACLInfo;
import com.topsec.sslvpn.datadef.na.NetCardConfigInfo;
import com.topsec.sslvpn.datadef.pf.ResourceInfoForConnect;

public class DataVector {
    public static BaseACLInfo[] m_BaseACLInfo = null;
    public static NetCardConfigInfo m_NetCardConfigInfo = null;
    public static DataVector m_dv = null;
    private BaseCaptchaInfo m_BaseCaptchaInfo = null;
    private int m_CapIndex = 0;
    private boolean m_bAuth_Cert = false;
    private boolean m_bAuth_Passwd = true;
    private boolean m_bAuth_TwoFactor = false;
    private boolean m_bAuth_TwoFactor_Hide = false;
    private boolean m_bRefreshCap = false;
    private BaseResourceInfo[] m_briArrayResInfo = null;
    private Button m_cerBtnLogin = null;
    private Button m_doubleBtnLogin = null;
    private eCaptchaType m_gid;
    private int m_nNaResCount = 0;
    private int m_nPort = 0;
    private ProgressDialog m_pdLgnWaitDialog = null;
    private ProgressDialog m_pdResWaitDialog = null;
    private ProgressDialog m_pdWelWaitDialog = null;
    private Button m_pwdBtnLogin = null;
    private int m_riFCResCount = 0;
    private ResourceInfoForConnect[] m_rifcpArrayConnectResInfo = null;
    private String m_strIP = "";
    private String m_strPackageName = "";
    private String m_strPwd = null;
    private String m_strSessionID = null;

    public static synchronized DataVector GetDataVector() {
        DataVector dataVector;
        synchronized (DataVector.class) {
            if (m_dv == null) {
                m_dv = new DataVector();
            }
            dataVector = m_dv;
        }
        return dataVector;
    }

    public void SetIP(String ip) {
        this.m_strIP = ip;
    }

    public String GetIP() {
        return this.m_strIP;
    }

    public void SetPort(int port) {
        this.m_nPort = port;
    }

    public int GetPort() {
        return this.m_nPort;
    }

    public void SetPackageName(String str) {
        this.m_strPackageName = str;
    }

    public String GetPackageName() {
        return this.m_strPackageName;
    }

    public void SetAuthPwd(boolean b) {
        this.m_bAuth_Passwd = b;
    }

    public boolean GetAuthPwd() {
        return this.m_bAuth_Passwd;
    }

    public void SetAuthCert(boolean b) {
        this.m_bAuth_Cert = b;
    }

    public boolean GetAuthCert() {
        return this.m_bAuth_Cert;
    }

    public void SetAuthTwoFactor(boolean b) {
        this.m_bAuth_TwoFactor = b;
    }

    public boolean GetAuthTwoFactor() {
        return this.m_bAuth_TwoFactor;
    }

    public void SetAuthTwoFactor_Hide(boolean b) {
        this.m_bAuth_TwoFactor_Hide = b;
    }

    public boolean GetAuthTwoFactor_Hide() {
        return this.m_bAuth_TwoFactor_Hide;
    }

    public void SetGid(eCaptchaType i) {
        this.m_gid = i;
    }

    public eCaptchaType GetGid() {
        return this.m_gid;
    }

    public void SetCapIndex(int i) {
        this.m_CapIndex = i;
    }

    public int GetCapIndex() {
        return this.m_CapIndex;
    }

    public void SetWelcomeProgressDialog(ProgressDialog pd) {
        this.m_pdWelWaitDialog = pd;
    }

    public ProgressDialog GetWelcomeProgressDialog() {
        return this.m_pdWelWaitDialog;
    }

    public void SetLoginProgressDialog(ProgressDialog pd) {
        this.m_pdLgnWaitDialog = pd;
    }

    public ProgressDialog GetLoginProgressDialog() {
        return this.m_pdLgnWaitDialog;
    }

    public void SetResProgressDialog(ProgressDialog pd) {
        this.m_pdResWaitDialog = pd;
    }

    public ProgressDialog GetResProgressDialog() {
        return this.m_pdResWaitDialog;
    }

    public void SetBaseResourceInfo(BaseResourceInfo[] bri) {
        this.m_briArrayResInfo = bri;
    }

    public BaseResourceInfo[] GetBaseResourceInfo() {
        return this.m_briArrayResInfo;
    }

    public void SetResourceInfoForConnect(ResourceInfoForConnect[] bri) {
        this.m_rifcpArrayConnectResInfo = bri;
    }

    public ResourceInfoForConnect[] GetResourceInfoForConnect() {
        return this.m_rifcpArrayConnectResInfo;
    }

    public void SetNAResourceCount(int count) {
        this.m_nNaResCount = count;
    }

    public int GetNAResourceCount() {
        return this.m_nNaResCount;
    }

    public void SetFCResourceCount(int count) {
        this.m_riFCResCount = count;
    }

    public int GetFCResourceCount() {
        return this.m_riFCResCount;
    }

    public void SetNetCardConfigInfo(NetCardConfigInfo ncci) {
        m_NetCardConfigInfo = ncci;
    }

    public NetCardConfigInfo GetNetCardConfigInfo() {
        return m_NetCardConfigInfo;
    }

    public void SetBaseACLInfo(BaseACLInfo[] ncci) {
        m_BaseACLInfo = ncci;
    }

    public BaseACLInfo[] GetBaseACLInfo() {
        return m_BaseACLInfo;
    }

    public void SetBaseCaptchaInfo(BaseCaptchaInfo bci) {
        this.m_BaseCaptchaInfo = bci;
    }

    public BaseCaptchaInfo GetBaseCaptchaInfo() {
        return this.m_BaseCaptchaInfo;
    }

    public void SetRefreshCap(boolean b) {
        this.m_bRefreshCap = b;
    }

    public boolean GetRefreshCap() {
        return this.m_bRefreshCap;
    }

    public void SetPwd(String s) {
        this.m_strPwd = s;
    }

    public String GetPwd() {
        return this.m_strPwd;
    }

    public void SetSessionID(String s) {
        this.m_strSessionID = s;
    }

    public String GetSessionID() {
        return this.m_strSessionID;
    }
}
