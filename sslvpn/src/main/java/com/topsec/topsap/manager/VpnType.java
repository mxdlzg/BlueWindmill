package com.topsec.topsap.manager;

import com.topsec.topsap.C0319R;

public enum VpnType {
    TopConnect("TopConnect", C0319R.string.ssl_vpn_description, SSLProfile.class);
    
    private Class<? extends VpnProfile> mClass;
    private int mDescriptionId;
    private String mDisplayName;

    private VpnType(String displayName, int descriptionId, Class<? extends VpnProfile> klass) {
        this.mDisplayName = displayName;
        this.mDescriptionId = descriptionId;
        this.mClass = klass;
    }

    public String getDisplayName() {
        return this.mDisplayName;
    }

    public int getDescriptionId() {
        return this.mDescriptionId;
    }

    public Class<? extends VpnProfile> getProfileClass() {
        return this.mClass;
    }
}
