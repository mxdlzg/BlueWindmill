package com.topsec.sslvpn.datadef;

public enum eExtraCodeType {
    EXTRA_CODE_NONE(0),
    EXTRA_CODE_CAPTCHA(1),
    EXTRA_CODE_SMS(2);
    
    private int m_iValue;

    private eExtraCodeType(int iValue) {
        this.m_iValue = 1;
        this.m_iValue = iValue;
    }

    public static eExtraCodeType valueOf(int iValue) {
        switch (iValue) {
            case 0:
                return EXTRA_CODE_NONE;
            case 1:
                return EXTRA_CODE_CAPTCHA;
            case 36:
                return EXTRA_CODE_SMS;
            default:
                return null;
        }
    }

    public int value() {
        return this.m_iValue;
    }
}
