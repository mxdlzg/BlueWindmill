package com.topsec.sslvpn;

public interface ITrafficStatistic {
    long getRecvBytes();

    int getRecvPacketCount();

    float getRecvSpeed();

    long getSendBytes();

    int getSendPacketCount();

    float getSendSpeed();

    long getTunnelActiveTime();

    int getTunnelState();

    int getTunnelType();

    int syncTrafficStatisticData();
}
