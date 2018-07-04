package com.topsec.sslvpn.lib;

import com.topsec.sslvpn.ITrafficStatistic;
import com.topsec.sslvpn.TrafficStatisticHelper;

public class TrafficStatistic extends TrafficStatisticHelper implements ITrafficStatistic {
    private static TrafficStatistic m_tspSingleInstance = null;

    private TrafficStatistic() {
    }

    public static synchronized TrafficStatistic getInstance() {
        TrafficStatistic trafficStatistic;
        synchronized (TrafficStatistic.class) {
            m_tspSingleInstance = new TrafficStatistic();
            trafficStatistic = m_tspSingleInstance;
        }
        return trafficStatistic;
    }

    public synchronized int syncTrafficStatisticData() {
        return super.SyncTrafficStatisticData();
    }

    public int getSendPacketCount() {
        return super.GetSendPacketCount();
    }

    public int getRecvPacketCount() {
        return super.GetRecvPacketCount();
    }

    public long getSendBytes() {
        return super.GetSendBytes();
    }

    public long getRecvBytes() {
        return super.GetRecvBytes();
    }

    public float getSendSpeed() {
        return super.GetSendSpeed();
    }

    public float getRecvSpeed() {
        return super.GetRecvSpeed();
    }

    public long getTunnelActiveTime() {
        return GetTunnelActiveTime();
    }

    public int getTunnelState() {
        return GetTunnelState();
    }

    public int getTunnelType() {
        return GetTunnelType();
    }
}
