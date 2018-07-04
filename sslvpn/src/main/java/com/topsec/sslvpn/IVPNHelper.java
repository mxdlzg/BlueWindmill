package com.topsec.sslvpn;

import android.app.Activity;
import android.webkit.WebView;
import com.topsec.sslvpn.datadef.BaseAccountInfo;
import com.topsec.sslvpn.datadef.BaseConfigInfo;
import com.topsec.sslvpn.datadef.eExtraCodeType;
import com.topsec.sslvpn.lib.TrafficStatistic;

public interface IVPNHelper {
    int addProxyItem(WebView webView);

    int closeService();

    int continueToLoginWithExtraCode(String str, eExtraCodeType com_topsec_sslvpn_datadef_eExtraCodeType);

    String getCertificateContentInSyncMode();

    String getErrorInfoByCode(int i);

    String getExchangeDataFromMode(int i);

    TrafficStatistic getTrafficStatisticInstance();

    int getVPNRunningStateInSyncMode();

    int kickTheCurrentUserOffImmediately(String str);

    int loginVOne(BaseAccountInfo baseAccountInfo);

    int logoutVOne();

    int modifyEmail(String str);

    int modifyEmailInSyncMode(String str);

    int modifyMobilePhone(String str);

    int modifyMobilePhoneInSyncMode(String str);

    int modifyPassword(String str, String str2);

    int modifyPasswordInSyncMode(String str, String str2);

    int queryVPNRunningState();

    void recycle();

    int removeProxyItem(WebView webView);

    int requestCaptcha();

    int requestVPNResInfo();

    int retrievePassword(String str, String str2);

    int retrievePasswordInSyncMode(String str, String str2);

    int setConfigInfo(BaseConfigInfo baseConfigInfo);

    void setOnAcceptResultListener(OnAcceptResultListener onAcceptResultListener);

    void setOnAcceptSysLogListener(OnAcceptSysLogListener onAcceptSysLogListener);

    int startService();

    int startService(Activity activity, Class<?> cls);

    int stopService();

    void toGrantStartVpnService(int i);
}
