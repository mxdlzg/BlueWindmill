package com.topsec.sslvpn.datadef;

public enum eLoginType {
    LOGIN_TYPE_NONE(1),
    LOGIN_TYPE_CODEWORD(2),
    LOGIN_TYPE_CERT(4),
    LOGIN_TYPE_DOUBLEFACTOR(8),
    LOGIN_TYPE_EXTERANAL(16);
    
    private int m_iValue;

    private eLoginType(int iValue) {
        this.m_iValue = 1;
        this.m_iValue = iValue;
    }

    public static eLoginType valueOf(int iValue) {
        switch (iValue) {
            case 1:
                return LOGIN_TYPE_NONE;
            case 2:
                return LOGIN_TYPE_CODEWORD;
            case 4:
                return LOGIN_TYPE_CERT;
            case 8:
                return LOGIN_TYPE_DOUBLEFACTOR;
            case 16:
                return LOGIN_TYPE_EXTERANAL;
            default:
                return null;
        }
    }

    public int value() {
        return this.m_iValue;
    }
}
