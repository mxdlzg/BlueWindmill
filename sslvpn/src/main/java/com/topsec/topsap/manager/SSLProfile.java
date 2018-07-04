package com.topsec.topsap.manager;

import android.os.Parcel;

public class SSLProfile extends VpnProfile {
    private static final long serialVersionUID = 1;
    private String mCertFilename;
    private String mCertPasswordString;
    private boolean mLog = false;
    private boolean mSaveUserPassword = true;
    private String mUserPasswordString;
    private String mUsername;

    public VpnType getType() {
        return VpnType.TopConnect;
    }

    public void setUsername(String name) {
        this.mUsername = name;
    }

    public String getUsername() {
        return this.mUsername;
    }

    public void setUserSavePasswordEnabled(boolean enabled) {
        this.mSaveUserPassword = enabled;
    }

    public boolean isSavePasswordEnabled() {
        return this.mSaveUserPassword;
    }

    public void setUserPasswordString(String secret) {
        this.mUserPasswordString = secret;
    }

    public String getUserPasswordString() {
        return this.mUserPasswordString;
    }

    public void setCertFilename(String filename) {
        this.mCertFilename = filename;
    }

    public String getCertFilename() {
        return this.mCertFilename;
    }

    public void setCertPasswordString(String secret) {
        this.mCertPasswordString = secret;
    }

    public String getCertPasswordString() {
        return this.mCertPasswordString;
    }

    public void setLogEnabled(boolean enabled) {
        this.mLog = enabled;
    }

    public boolean isLogEnabled() {
        return this.mLog;
    }

    protected void readFromParcel(Parcel in) {
        boolean z = true;
        super.readFromParcel(in);
        this.mUsername = in.readString();
        this.mSaveUserPassword = in.readInt() > 0;
        this.mUserPasswordString = in.readString();
        this.mCertFilename = in.readString();
        this.mCertPasswordString = in.readString();
        if (in.readInt() <= 0) {
            z = false;
        }
        this.mLog = z;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        int i = 1;
        super.writeToParcel(parcel, flags);
        parcel.writeString(this.mUsername);
        parcel.writeInt(this.mSaveUserPassword ? 1 : 0);
        parcel.writeString(this.mUserPasswordString);
        parcel.writeString(this.mCertFilename);
        parcel.writeString(this.mCertPasswordString);
        if (!this.mLog) {
            i = 0;
        }
        parcel.writeInt(i);
    }
}
