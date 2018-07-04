package com.topsec.topsap.manager;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.Serializable;

public abstract class VpnProfile implements Parcelable, Serializable {
    public static final Creator<VpnProfile> CREATOR = new C03921();
    private static final long serialVersionUID = 1;
    private String mAuthType = "0";
    private String mId;
    private boolean mIsCustomized;
    private String mName;
    private String mProtocolType = "0";
    private String mServerName;
    private String mServerPort = "443";

    static class C03921 implements Creator<VpnProfile> {
        C03921() {
        }

        public VpnProfile createFromParcel(Parcel in) {
            VpnProfile p = new VpnManager(null).createVpnProfile((VpnType) Enum.valueOf(VpnType.class, in.readString()), in.readInt() > 0);
            if (p == null) {
                return null;
            }
            p.readFromParcel(in);
            return p;
        }

        public VpnProfile[] newArray(int size) {
            return new VpnProfile[size];
        }
    }

    public abstract VpnType getType();

    public void setName(String name) {
        this.mName = name;
    }

    public String getName() {
        return this.mName;
    }

    public void setAuthType(String type) {
        this.mAuthType = type;
    }

    public String getAuthType() {
        return this.mAuthType;
    }

    public void setProtocolType(String ProtocolType) {
        this.mProtocolType = ProtocolType;
    }

    public String getProtocolType() {
        return this.mProtocolType;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getId() {
        return this.mId;
    }

    public void setServerName(String name) {
        this.mServerName = name;
    }

    public String getServerName() {
        return this.mServerName;
    }

    public String getServerPort() {
        return this.mServerPort;
    }

    public void setServerPort(String port) {
        this.mServerPort = port;
    }

    protected void readFromParcel(Parcel in) {
        this.mName = in.readString();
        this.mId = in.readString();
        this.mServerName = in.readString();
        this.mServerPort = in.readString();
        this.mAuthType = in.readString();
        this.mProtocolType = in.readString();
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(getType().toString());
        parcel.writeInt(this.mIsCustomized ? 1 : 0);
        parcel.writeString(this.mName);
        parcel.writeString(this.mId);
        parcel.writeString(this.mServerName);
        parcel.writeString(this.mServerPort);
        parcel.writeString(this.mAuthType);
        parcel.writeString(this.mProtocolType);
    }

    public int describeContents() {
        return 0;
    }
}
