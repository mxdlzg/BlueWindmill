package com.topsec.sslvpn.datadef;

public enum eProtocolType {
    PROTOCOL_TYPE_NONE(1),
    PROTOCOL_TYPE_CLOSE(2),
    PROTOCOL_TYPE_INTERN(4);
    
    private int m_iValue;

    private eProtocolType(int iValue) {
        this.m_iValue = 1;
        this.m_iValue = iValue;
    }

    public static eProtocolType valueOf(int iValue) {
        switch (iValue) {
            case 1:
                return PROTOCOL_TYPE_NONE;
            case 2:
                return PROTOCOL_TYPE_CLOSE;
            case 4:
                return PROTOCOL_TYPE_INTERN;
            default:
                return null;
        }
    }

    public int value() {
        return this.m_iValue;
    }
}
