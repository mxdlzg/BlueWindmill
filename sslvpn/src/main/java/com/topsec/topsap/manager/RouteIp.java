package com.topsec.topsap.manager;

import java.util.Locale;

public class RouteIp {
    int len;
    String mIp;

    public RouteIp(String ip, String mask) {
        long netmask;
        this.mIp = ip;
        if (mask.equals("")) {
            netmask = (long) Integer.parseInt("128");
        } else {
            netmask = getInt(mask);
        }
        netmask += 4294967296L;
        int lenZeros = 0;
        while ((1 & netmask) == 0) {
            lenZeros++;
            netmask >>= 1;
        }
        if (netmask != (8589934591L >> lenZeros)) {
            this.len = 32;
        } else {
            this.len = 32 - lenZeros;
        }
    }

    public int getLen() {
        return this.len;
    }

    public RouteIp(String address, int prefix_length) {
        this.len = prefix_length;
        this.mIp = address;
    }

    public boolean equals(RouteIp rt) {
        if (this.mIp.equalsIgnoreCase(rt.mIp) && this.len == rt.len) {
            return true;
        }
        return false;
    }

    public String toString() {
        return String.format(Locale.ENGLISH, "%s/%d", new Object[]{this.mIp, Integer.valueOf(this.len)});
    }

    public boolean normalise() {
        long ip = getInt(this.mIp);
        if ((ip & (4294967295L << (32 - this.len))) == ip) {
            return false;
        }
        this.mIp = String.format("%d.%d.%d.%d", new Object[]{Long.valueOf((-16777216 & (ip & (4294967295L << (32 - this.len)))) >> 24), Long.valueOf((16711680 & (ip & (4294967295L << (32 - this.len)))) >> 16), Long.valueOf((65280 & (ip & (4294967295L << (32 - this.len)))) >> 8), Long.valueOf(255 & (ip & (4294967295L << (32 - this.len))))});
        return true;
    }

    static long getInt(String ipaddr) {
        String[] ipt = ipaddr.split("\\.");
        return (((0 + (Long.parseLong(ipt[0]) << 24)) + ((long) (Integer.parseInt(ipt[1]) << 16))) + ((long) (Integer.parseInt(ipt[2]) << 8))) + ((long) Integer.parseInt(ipt[3]));
    }

    public long getInt() {
        return getInt(this.mIp);
    }
}
