package com.longmai.mtoken.interfaces.kernel.func.base;

import org.json.JSONException;

public interface Function {
    public static final String SOF_ChanegPassWd = "SOF_ChanegPassWd";
    public static final String SOF_Connect = "SOF_Connect";
    public static final String SOF_CreateContainer = "SOF_CreateContainer";
    public static final String SOF_CreaterFile = "SOF_CreaterFile";
    public static final String SOF_DecryptData = "SOF_DecryptData";
    public static final String SOF_DecryptFinal = "SOF_DecryptFinal";
    public static final String SOF_DecryptInit = "SOF_DecryptInit";
    public static final String SOF_DecryptUpdate = "SOF_DecryptUpdate";
    public static final String SOF_DeleteCertificate = "SOF_DeleteCertificate";
    public static final String SOF_DeleteContainer = "SOF_DeleteContainer";
    public static final String SOF_DeleteFile = "SOF_DeleteFile";
    public static final String SOF_DeleteKeyPair = "SOF_DeleteKeyPair";
    public static final String SOF_DestroySymtricKey = "SOF_DestroySymtricKey";
    public static final String SOF_DigestData = "SOF_DigestData";
    public static final String SOF_Disconnect = "SOF_Disconnect";
    public static final String SOF_EncryptData = "SOF_EncryptData";
    public static final String SOF_EncryptFinal = "SOF_EncryptFinal";
    public static final String SOF_EncryptInit = "SOF_EncryptInit";
    public static final String SOF_EncryptUpdate = "SOF_EncryptUpdate";
    public static final String SOF_EnumApplication = "SOF_EnumApplication";
    public static final String SOF_EnumContainers = "SOF_EnumContainers";
    public static final String SOF_EnumDeviceByName = "SOF_EnumDeviceByName";
    public static final String SOF_EnumDevices = "SOF_EnumDevices";
    public static final String SOF_EnumFiles = "SOF_EnumFiles";
    public static final String SOF_ExportPublicKeyBlob = "SOF_ExportPublicKeyBlob";
    public static final String SOF_ExportUserCert = "SOF_ExportUserCert";
    public static final String SOF_ExtPublicEncrypt = "SOF_ExtPublicEncrypt";
    public static final String SOF_ExtRSAPubKeyOperation = "SOF_ExtRSAPubKeyOperation";
    public static final String SOF_GenKeyPair = "SOF_GenKeyPair";
    public static final String SOF_GenRandom = "SOF_GenRandom";
    public static final String SOF_GetContainerInfo = "SOF_GetContainerInfo";
    public static final String SOF_GetDeviceInfo = "SOF_GetDeviceInfo";
    public static final String SOF_GetFileInfo = "SOF_GetFileInfo";
    public static final String SOF_GetInstance = "SOF_GetInstance";
    public static final String SOF_GetPINInfo = "SOF_GetPINInfo";
    public static final String SOF_ImportCertificate = "SOF_ImportCertificate";
    public static final String SOF_ImportECCKeyPair = "SOF_ImportECCKeyPair";
    public static final String SOF_ImportExtRSAKeyPair = "SOF_ImportExtRSAKeyPair";
    public static final String SOF_ImportRSAKeyPair = "SOF_ImportRSAKeyPair";
    public static final String SOF_LoadLibrary = "SOF_LoadLibrary";
    public static final String SOF_Login = "SOF_Login";
    public static final String SOF_Logout = "SOF_Logout";
    public static final String SOF_PrivateDecrypt = "SOF_PrivateDecrypt";
    public static final String SOF_PublicEncrypt = "SOF_PublicEncrypt";
    public static final String SOF_ReadFile = "SOF_ReadFile";
    public static final String SOF_SM3Digest = "SOF_SM3Digest";
    public static final String SOF_SetLabel = "SOF_SetLabel";
    public static final String SOF_SetSymtricKey = "SOF_SetSymtricKey";
    public static final String SOF_SignData = "SOF_SignData";
    public static final String SOF_UnblockPIN = "SOF_UnblockPIN";
    public static final String SOF_VerifySignedData = "SOF_VerifySignedData";
    public static final String SOF_WriteFile = "SOF_WriteFile";

    String exec() throws RuntimeException, JSONException;

    void init();
}
