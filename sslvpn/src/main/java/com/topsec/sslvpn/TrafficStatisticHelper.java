package com.topsec.sslvpn;

public abstract class TrafficStatisticHelper {
    protected native long GetRecvBytes();

    protected native int GetRecvPacketCount();

    protected native float GetRecvSpeed();

    protected native long GetSendBytes();

    protected native int GetSendPacketCount();

    protected native float GetSendSpeed();

    protected native long GetTunnelActiveTime();

    protected native int GetTunnelState();

    protected native int GetTunnelType();

    protected native int SyncTrafficStatisticData();
}
