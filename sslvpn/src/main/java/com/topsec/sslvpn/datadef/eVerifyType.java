package com.topsec.sslvpn.datadef;

public enum eVerifyType {
    VERIFY_TYPE_NONE(1),
    VERIFY_TYPE_STATIC_CODEWORD(1),
    VERIFY_TYPE_SOFTCERT(2),
    VERIFY_TYPE_HARDCERT(4),
    VERIFY_TYPE_TW(6),
    VERIFY_TYPE_DYNAMIC_CODEWORD(8),
    VERIFY_TYPE_TOKEN(16);
    
    private int m_iValue;

    private eVerifyType(int iValue) {
        this.m_iValue = 1;
        this.m_iValue = iValue;
    }

    public static eVerifyType valueOf(int iValue) {
        switch (iValue) {
            case 1:
                return VERIFY_TYPE_NONE;
            case 2:
                return VERIFY_TYPE_SOFTCERT;
            case 4:
                return VERIFY_TYPE_HARDCERT;
            case 6:
                return VERIFY_TYPE_TW;
            case 8:
                return VERIFY_TYPE_DYNAMIC_CODEWORD;
            case 16:
                return VERIFY_TYPE_TOKEN;
            default:
                return null;
        }
    }

    public int value() {
        return this.m_iValue;
    }
}
