package com.topsec.topsap.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Environment;

public class VpnManager {
    private static final String ACTION_VPN_CONNECTIVITY = "sslvpn.connectivity";
    private static final String ACTION_VPN_SERVICE = (new StringBuilder(String.valueOf(VpnManager.class.getPackage().getName())).append(".").toString() + "SERVICE");
    private static final String ACTION_VPN_SETTINGS = (new StringBuilder(String.valueOf(VpnManager.class.getPackage().getName())).append(".").toString() + "SETTINGS");
    public static final String BROADCAST_CONNECTION_STATE = "connection_state";
    public static final String BROADCAST_ERROR_CODE = "err";
    public static final String BROADCAST_PROFILE_NAME = "profile_name";
    private static final String PACKAGE_PREFIX = new StringBuilder(String.valueOf(VpnManager.class.getPackage().getName())).append(".").toString();
    public static final String PROFILES_PATH = "/sslvpn/profiles";
    public static final String TAG = VpnManager.class.getSimpleName();
    public static final int VPN_ERROR_AUTH = 51;
    public static final int VPN_ERROR_AUTH_NOT_SUPPLIED = 104;
    public static final int VPN_ERROR_CHALLENGE = 5;
    public static final int VPN_ERROR_CONNECTION_FAILED = 101;
    public static final int VPN_ERROR_CONNECTION_LOST = 103;
    public static final int VPN_ERROR_LARGEST = 200;
    public static final int VPN_ERROR_NO_ERROR = 0;
    public static final int VPN_ERROR_PPP_NEGOTIATION_FAILED = 42;
    public static final int VPN_ERROR_REMOTE_HUNG_UP = 7;
    public static final int VPN_ERROR_REMOTE_PPP_HUNG_UP = 48;
    public static final int VPN_ERROR_UNKNOWN_SERVER = 102;
    private Context mContext;

    public static String getProfilePath() {
        return new StringBuilder(String.valueOf(Environment.getDataDirectory().getPath())).append("/data/").append(VpnManager.class.getPackage().getName()).append(PROFILES_PATH).toString();
    }

    public static VpnType[] getSupportedVpnTypes() {
        return VpnType.values();
    }

    public VpnManager(Context c) {
        this.mContext = c;
    }

    public VpnProfile createVpnProfile(VpnType type) {
        return createVpnProfile(type, false);
    }

    public VpnProfile createVpnProfile(VpnType type, boolean customized) {
        try {
            return (VpnProfile) type.getProfileClass().newInstance();
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e2) {
            return null;
        }
    }

    public void startVpnService() {
        this.mContext.startService(new Intent(ACTION_VPN_SERVICE));
    }

    public void stopVpnService() {
        this.mContext.stopService(new Intent(ACTION_VPN_SERVICE));
    }

    public boolean bindVpnService(ServiceConnection c) {
        if (this.mContext.bindService(new Intent(ACTION_VPN_SERVICE), c, 0)) {
            return true;
        }
        return false;
    }

    public void broadcastConnectivity(String profileName, VpnState s) {
        broadcastConnectivity(profileName, s, 0);
    }

    public void broadcastConnectivity(String profileName, VpnState s, int error) {
        Intent intent = new Intent(ACTION_VPN_CONNECTIVITY);
        intent.putExtra(BROADCAST_PROFILE_NAME, profileName);
        intent.putExtra(BROADCAST_CONNECTION_STATE, s);
        if (error != 0) {
            intent.putExtra("err", error);
        }
        this.mContext.sendBroadcast(intent);
    }

    public void registerConnectivityReceiver(BroadcastReceiver r) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_VPN_CONNECTIVITY);
        this.mContext.registerReceiver(r, filter);
    }

    public void unregisterConnectivityReceiver(BroadcastReceiver r) {
        this.mContext.unregisterReceiver(r);
    }

    public void startSettingsActivity() {
        Intent intent = new Intent(ACTION_VPN_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.mContext.startActivity(intent);
    }

    public Intent createSettingsActivityIntent() {
        Intent intent = new Intent(ACTION_VPN_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }
}
