package com.topsec.sslvpn.datadef;

public enum eOperateType {
    OPERATION_GET_RESOURCE(1),
    OPERATION_GET_CAPTCHA(2),
    OPERATION_GET_SERVERCFG(3),
    OPERATION_AUTH_LOGININFO(4),
    OPERATION_UPLOAD_FEATURECODE(5),
    OPERATION_LOGIN_SYSTEM(6),
    OPERATION_START_PFPROXY(7),
    OPERATION_STOP_PFPROXY(8),
    OPERATION_LOGOUT_SYSTEM(9),
    OPERATION_GET_KEEPSTATUS(10),
    OPERATION_START_SERVICE(11),
    OPERATION_STOP_SERVICE(12),
    OPERATION_CLOSE_SERVICE(13),
    OPERATION_CHECK_NETSTATUS(14),
    OPERATION_PREPARE_NETACCESS(15),
    OPERATION_START_NETACCESS(16),
    OPERATION_STOP_NETACCESS(17),
    OPERATION_NETCONFIG_NETACCESS(18),
    OPERATION_TRYFIX_VPNTUNNEL(19),
    OPERATION_MODIFY_PASSWORD(20),
    OPERATION_MODIFY_TELPHONE(21),
    OPERATION_MODIFY_EMAIL(22),
    OPERATION_FORGET_PASSWD(23);
    
    private int m_iValue;

    private eOperateType(int iValue) {
        this.m_iValue = 0;
        this.m_iValue = iValue;
    }

    public static eOperateType valueOf(int iValue) {
        switch (iValue) {
            case 1:
                return OPERATION_GET_RESOURCE;
            case 2:
                return OPERATION_GET_CAPTCHA;
            case 3:
                return OPERATION_GET_SERVERCFG;
            case 4:
                return OPERATION_AUTH_LOGININFO;
            case 5:
                return OPERATION_UPLOAD_FEATURECODE;
            case 6:
                return OPERATION_LOGIN_SYSTEM;
            case 7:
                return OPERATION_START_PFPROXY;
            case 8:
                return OPERATION_STOP_PFPROXY;
            case 9:
                return OPERATION_LOGOUT_SYSTEM;
            case 10:
                return OPERATION_GET_KEEPSTATUS;
            case 11:
                return OPERATION_START_SERVICE;
            case 12:
                return OPERATION_STOP_SERVICE;
            case 13:
                return OPERATION_CLOSE_SERVICE;
            case 14:
                return OPERATION_CHECK_NETSTATUS;
            case 15:
                return OPERATION_PREPARE_NETACCESS;
            case 16:
                return OPERATION_START_NETACCESS;
            case 17:
                return OPERATION_STOP_NETACCESS;
            case 18:
                return OPERATION_NETCONFIG_NETACCESS;
            case 19:
                return OPERATION_TRYFIX_VPNTUNNEL;
            case 20:
                return OPERATION_MODIFY_PASSWORD;
            case 21:
                return OPERATION_MODIFY_TELPHONE;
            case 22:
                return OPERATION_MODIFY_EMAIL;
            case 23:
                return OPERATION_FORGET_PASSWD;
            default:
                return null;
        }
    }

    public int value() {
        return this.m_iValue;
    }
}
