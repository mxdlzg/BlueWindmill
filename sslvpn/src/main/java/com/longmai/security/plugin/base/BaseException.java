package com.longmai.security.plugin.base;

public class BaseException extends Exception {
    public static final int Error_Auth = 16;
    public static final int Error_Connect = 13;
    public static final int Error_Connected = 7;
    public static final int Error_Data = 2;
    public static final int Error_IO = 11;
    public static final int Error_IO_Read = 15;
    public static final int Error_IO_Write = 14;
    public static final int Error_Init = 19;
    public static final int Error_No_Keypair = 3;
    public static final int Error_Not_Find_Config = 10;
    public static final int Error_Not_Find_Device = 12;
    public static final int Error_Not_Find_Driver = 9;
    public static final int Error_Not_Supported = 5;
    public static final int Error_Permission = 17;
    public static final int Error_Protocol = 18;
    public static final int Error_Request_Apdu = 8;
    public static final int Error_SM2_Digest = 20;
    public static final int Error_Sign_TimeOut = 21;
    public static final int Error_Unknow_Device = 6;
    public static final int Error_Unknown = 1;
    public static final int Error_Unknown_algo = 4;

    public BaseException(String error) {
        super(error);
    }
}
