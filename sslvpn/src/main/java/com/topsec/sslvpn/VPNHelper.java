package com.topsec.sslvpn;

import android.webkit.WebView;
import com.topsec.sslvpn.datadef.BaseAccountInfo;
import com.topsec.sslvpn.datadef.BaseConfigInfo;

public abstract class VPNHelper {
    protected native int AddProxyItem(WebView webView);

    protected native int CloseService();

    protected native int ContinueToLoginWithExtraCode(String str, int i);

    protected native String GetCertificateContentInSyncMode();

    protected native String GetExchangeDataFromMode(int i);

    protected native String GetLastErrInfo(int i, int i2);

    protected native int GetVPNRunningStateInSyncMode();

    protected native int KickTheCurrentUserOffImmediately(String str);

    protected native int LoginVOne(BaseAccountInfo baseAccountInfo);

    protected native int LogoutVOne();

    protected native int ModifyEmail(String str);

    protected native int ModifyEmailInSyncMode(String str);

    protected native int ModifyMobilePhone(String str);

    protected native int ModifyMobilePhoneInSyncMode(String str);

    protected native int ModifyPassword(String str, String str2);

    protected native int ModifyPasswordInSyncMode(String str, String str2);

    protected native int NetworkSwitched(int i, String str);

    protected native int QueryVPNRunningState();

    protected native void Recycle();

    protected native int RemoveProxyItem(WebView webView);

    protected native int RequestCaptcha();

    protected native int RequestVPNResInfo();

    protected native int RetrievePassword(String str, String str2);

    protected native int RetrievePasswordInSyncMode(String str, String str2);

    protected native int SetConfigInfo(BaseConfigInfo baseConfigInfo);

    protected native int SetLocalFDForNetAccess(int i);

    protected native int StartService();

    protected native int StopService();

    protected abstract int pushExecuteResult(int i, int i2, Object obj, Object obj2);

    protected abstract void pushSysLogInfo(int i, String str, String str2);
}
