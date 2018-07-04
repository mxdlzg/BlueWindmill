package com.longmai.security.plugin.skf.ndk;

import com.longmai.security.plugin.skf.SKF_CMDPacker;

public class SKF_CMDPackerImple implements SKF_CMDPacker {
    public static native int SM3DigestFinal(byte[] bArr, int i, byte[] bArr2, int[] iArr);

    public static native int SM3DigestInit();

    public static native int SM3DigestUpdate(byte[] bArr, int i);

    public static native int SM3_GetZ(byte[] bArr, byte[] bArr2);

    public native int SKF_ChangePIN_Request(int i, int i2, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4);

    public native int SKF_ChangePIN_Response(byte[] bArr, int[] iArr, int[] iArr2);

    public native int SKF_ClearSecureState_Request(int i, byte[] bArr);

    public native int SKF_ClearSecureState_Response(byte[] bArr, int[] iArr);

    public native int SKF_CloseContainer_Request(int i, int i2, byte[] bArr);

    public native int SKF_CloseContainer_Response(byte[] bArr, int[] iArr);

    public native int SKF_CreateApp_Request(byte[] bArr, byte[] bArr2, int i, byte[] bArr3, int i2, int i3, byte[] bArr4);

    public native int SKF_CreateApp_Response(byte[] bArr, int[] iArr);

    public native int SKF_CreateContainer_Request(int i, byte[] bArr, byte[] bArr2);

    public native int SKF_CreateContainer_Response(byte[] bArr, int[] iArr, int[] iArr2);

    public native int SKF_CreaterFile_Request(int i, byte[] bArr, int i2, int i3, int i4, byte[] bArr2);

    public native int SKF_CreaterFile_Response(byte[] bArr, int[] iArr);

    public native int SKF_DecryptFinal_Request(int i, int i2, int i3, byte[] bArr, int i4, byte[] bArr2);

    public native int SKF_DecryptFinal_Response(byte[] bArr, int[] iArr, byte[] bArr2, int[] iArr2);

    public native int SKF_DecryptInit_Request(int i, int i2, int i3, byte[] bArr, int i4, int i5, byte[] bArr2);

    public native int SKF_DecryptInit_Response(byte[] bArr, int[] iArr);

    public native int SKF_DecryptUpdate_Request(int i, int i2, int i3, byte[] bArr, int i4, byte[] bArr2);

    public native int SKF_DecryptUpdate_Response(byte[] bArr, int[] iArr, byte[] bArr2, int[] iArr2);

    public native int SKF_DeleteApp_Request(byte[] bArr, byte[] bArr2);

    public native int SKF_DeleteApp_Response(byte[] bArr, int[] iArr);

    public native int SKF_DeleteCertificate_Request(int i, int i2, int i3, byte[] bArr);

    public native int SKF_DeleteCertificate_Response(byte[] bArr, int[] iArr);

    public native int SKF_DeleteContainer_Request(int i, byte[] bArr, byte[] bArr2);

    public native int SKF_DeleteContainer_Response(byte[] bArr, int[] iArr);

    public native int SKF_DeleteFile_Request(int i, byte[] bArr, byte[] bArr2);

    public native int SKF_DeleteFile_Response(byte[] bArr, int[] iArr);

    public native int SKF_DeleteKeyPair_Request(int i, int i2, int i3, byte[] bArr);

    public native int SKF_DeleteKeyPair_Response(byte[] bArr, int[] iArr);

    public native int SKF_DeriveCommSessionKey_Request(byte[] bArr, int i, byte[] bArr2);

    public native int SKF_DeriveCommSessionKey_Response(byte[] bArr, int[] iArr);

    public native int SKF_DestroySymtricKey_Request(int i, int i2, int i3, byte[] bArr);

    public native int SKF_DestroySymtricKey_Response(byte[] bArr, int[] iArr);

    public native int SKF_DeviceAuth_Request(byte[] bArr, byte[] bArr2, byte[] bArr3);

    public native int SKF_DeviceAuth_Response(byte[] bArr, int[] iArr);

    public native int SKF_DeviceInternalAuth_Request(byte[] bArr);

    public native int SKF_DeviceInternalAuth_Response(byte[] bArr, int[] iArr);

    public native int SKF_DigestFinal_Request(byte[] bArr, int i, byte[] bArr2);

    public native int SKF_DigestFinal_Response(byte[] bArr, int[] iArr, byte[] bArr2, int[] iArr2);

    public native int SKF_DigestInit_Display_Request(int i, byte[] bArr);

    public native int SKF_DigestInit_Display_Response(byte[] bArr, int[] iArr);

    public native int SKF_DigestInit_Request(int i, byte[] bArr);

    public native int SKF_DigestInit_Response(byte[] bArr, int[] iArr);

    public native int SKF_DigestUpdate_Display_Request(byte[] bArr, int i, byte[] bArr2);

    public native int SKF_DigestUpdate_Display_Response(byte[] bArr, int[] iArr);

    public native int SKF_DigestUpdate_Request(byte[] bArr, int i, byte[] bArr2);

    public native int SKF_DigestUpdate_Response(byte[] bArr, int[] iArr);

    public native int SKF_ECCExportPublicKey_Request(int i, int i2, int i3, byte[] bArr);

    public native int SKF_ECCExportPublicKey_Response(byte[] bArr, int[] iArr, byte[] bArr2, int[] iArr2);

    public native int SKF_ECCPrivateDecrypt_Request(int i, int i2, int i3, byte[] bArr, int i4, byte[] bArr2);

    public native int SKF_ECCPrivateDecrypt_Response(byte[] bArr, int[] iArr, byte[] bArr2, int[] iArr2);

    public native int SKF_ECCSignDataInteractiveCancel_Request(int i, int i2, int i3, int i4, byte[] bArr);

    public native int SKF_ECCSignDataInteractiveCancel_Response(byte[] bArr, int[] iArr);

    public native int SKF_ECCSignDataInteractive_Request(int i, int i2, int i3, int i4, int i5, byte[] bArr, int i6, byte[] bArr2, int i7, byte[] bArr3);

    public native int SKF_ECCSignDataInteractive_Response(byte[] bArr, int[] iArr, byte[] bArr2, int[] iArr2);

    public native int SKF_ECCSignData_Request(int i, int i2, int i3, int i4, byte[] bArr, int i5, byte[] bArr2, int i6, byte[] bArr3);

    public native int SKF_ECCSignData_Response(byte[] bArr, int[] iArr, byte[] bArr2, int[] iArr2);

    public native int SKF_ECCVerify_Request(byte[] bArr, byte[] bArr2, int i, byte[] bArr3, int i2, byte[] bArr4);

    public native int SKF_ECCVerify_Response(byte[] bArr, int[] iArr);

    public native int SKF_EncryptFinal_Request(int i, int i2, int i3, byte[] bArr, int i4, byte[] bArr2);

    public native int SKF_EncryptFinal_Response(byte[] bArr, int[] iArr, byte[] bArr2, int[] iArr2);

    public native int SKF_EncryptInit_Request(int i, int i2, int i3, byte[] bArr, int i4, int i5, byte[] bArr2);

    public native int SKF_EncryptInit_Response(byte[] bArr, int[] iArr);

    public native int SKF_EncryptUpdate_Request(int i, int i2, int i3, byte[] bArr, int i4, byte[] bArr2);

    public native int SKF_EncryptUpdate_Response(byte[] bArr, int[] iArr, byte[] bArr2, int[] iArr2);

    public native int SKF_EnumApplication_Request(byte[] bArr);

    public native int SKF_EnumApplication_Response(byte[] bArr, int[] iArr, byte[] bArr2, int[] iArr2);

    public native int SKF_EnumApps_Request(byte[] bArr);

    public native int SKF_EnumApps_Response(byte[] bArr, int[] iArr, byte[] bArr2, int[] iArr2);

    public native int SKF_EnumContainer_Request(int i, byte[] bArr);

    public native int SKF_EnumContainer_Response(byte[] bArr, int[] iArr, byte[] bArr2, int[] iArr2);

    public native int SKF_EnumFiles_Request(int i, byte[] bArr);

    public native int SKF_EnumFiles_Response(byte[] bArr, int[] iArr, byte[] bArr2, int[] iArr2);

    public native int SKF_ExportCertificate_Request(int i, int i2, int i3, byte[] bArr);

    public native int SKF_ExportCertificate_Response(byte[] bArr, int i, int[] iArr, byte[] bArr2, int[] iArr2);

    public native int SKF_ExtECCEncrypt_Request(byte[] bArr, byte[] bArr2, int i, byte[] bArr3);

    public native int SKF_ExtECCEncrypt_Response(byte[] bArr, int[] iArr, byte[] bArr2, int[] iArr2);

    public native int SKF_ExtRSAEncrypt_Request(byte[] bArr, byte[] bArr2, int i, byte[] bArr3);

    public native int SKF_ExtRSAEncrypt_Response(byte[] bArr, int[] iArr, byte[] bArr2, int[] iArr2);

    public native int SKF_ExtRSAPubKeyOperation_Request(byte[] bArr, byte[] bArr2, int i, byte[] bArr3);

    public native int SKF_ExtRSAPubKeyOperation_Response(byte[] bArr, int[] iArr, byte[] bArr2, int[] iArr2);

    public native int SKF_ExtRSAVerify_Request(byte[] bArr, int i, byte[] bArr2, int i2, byte[] bArr3, int i3, byte[] bArr4);

    public native int SKF_ExtRSAVerify_Response(byte[] bArr, int[] iArr);

    public native int SKF_GenECCKeyPair_Request(int i, int i2, int i3, int i4, byte[] bArr);

    public native int SKF_GenECCKeyPair_Response(byte[] bArr, int[] iArr, byte[] bArr2, int[] iArr2);

    public native int SKF_GenRSAKeyPair_Request(int i, int i2, int i3, int i4, byte[] bArr);

    public native int SKF_GenRSAKeyPair_Response(byte[] bArr, int[] iArr, byte[] bArr2, int[] iArr2);

    public native int SKF_GenRandom_Request(int i, byte[] bArr);

    public native int SKF_GenRandom_Response(byte[] bArr, int[] iArr, byte[] bArr2, int[] iArr2);

    public native int SKF_GetApduMode();

    public native int SKF_GetContainerType_Request(int i, byte[] bArr, byte[] bArr2);

    public native int SKF_GetContainerType_Response(byte[] bArr, int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4, int[] iArr5, int[] iArr6);

    public native int SKF_GetDevInfoEx_Request(byte[] bArr);

    public native int SKF_GetDevInfoEx_Response(byte[] bArr, int[] iArr, byte[] bArr2, byte[] bArr3, int[] iArr2, int[] iArr3, byte[] bArr4, int[] iArr4);

    public native int SKF_GetDevInfo_Request(byte[] bArr);

    public native int SKF_GetDevInfo_Response(byte[] bArr, int[] iArr, byte[] bArr2, byte[] bArr3, int[] iArr2);

    public native int SKF_GetFileInfo_Request(int i, byte[] bArr, byte[] bArr2);

    public native int SKF_GetFileInfo_Response(byte[] bArr, int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4);

    public native int SKF_GetPINInfo_Request(int i, int i2, byte[] bArr);

    public native int SKF_GetPINInfo_Response(byte[] bArr, int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4);

    public native int SKF_ImportCertificate_Request(int i, int i2, int i3, int i4, byte[] bArr, int i5, byte[] bArr2);

    public native int SKF_ImportCertificate_Response(byte[] bArr, int[] iArr);

    public native int SKF_ImportECCKeyPair_Request(int i, int i2, byte[] bArr, int i3, byte[] bArr2);

    public native int SKF_ImportECCKeyPair_Response(byte[] bArr, int[] iArr);

    public native int SKF_ImportExtRSAKeyPair_Request(int i, int i2, int i3, int i4, byte[] bArr, int i5, byte[] bArr2);

    public native int SKF_ImportExtRSAKeyPair_Response(byte[] bArr, int[] iArr);

    public native int SKF_ImportRSAKeyPair_Request(int i, int i2, int i3, int i4, int i5, byte[] bArr, int i6, byte[] bArr2, int i7, byte[] bArr3);

    public native int SKF_ImportRSAKeyPair_Response(byte[] bArr, int[] iArr);

    public native int SKF_Init();

    public native int SKF_LedControl_Request(int i, int i2, byte[] bArr);

    public native int SKF_LedControl_Response(byte[] bArr, int[] iArr);

    public native int SKF_OpenApplication_Request(byte[] bArr, byte[] bArr2);

    public native int SKF_OpenApplication_Response(byte[] bArr, int[] iArr, int[] iArr2);

    public native int SKF_OpenContainer_Request(int i, byte[] bArr, byte[] bArr2);

    public native int SKF_OpenContainer_Response(byte[] bArr, int[] iArr, int[] iArr2);

    public native int SKF_RSADecrypt_Request(int i, int i2, int i3, byte[] bArr, int i4, byte[] bArr2);

    public native int SKF_RSADecrypt_Response(byte[] bArr, int[] iArr, byte[] bArr2, int[] iArr2);

    public native int SKF_RSAEncrypt_Request(int i, int i2, int i3, byte[] bArr, int i4, byte[] bArr2);

    public native int SKF_RSAEncrypt_Response(byte[] bArr, int[] iArr, byte[] bArr2, int[] iArr2);

    public native int SKF_RSAExportPublicKey_Request(int i, int i2, int i3, byte[] bArr);

    public native int SKF_RSAExportPublicKey_Response(byte[] bArr, int[] iArr, byte[] bArr2, int[] iArr2);

    public native int SKF_RSAPrivate_Request(int i, int i2, int i3, byte[] bArr, int i4, byte[] bArr2);

    public native int SKF_RSAPrivate_Response(byte[] bArr, int[] iArr, byte[] bArr2, int[] iArr2);

    public native int SKF_RSAPublic_Request(int i, int i2, int i3, byte[] bArr, int i4, byte[] bArr2);

    public native int SKF_RSAPublic_Response(byte[] bArr, int[] iArr, byte[] bArr2, int[] iArr2);

    public native int SKF_RSASignDataInteractiveCancel_Request(int i, int i2, int i3, int i4, byte[] bArr);

    public native int SKF_RSASignDataInteractiveCancel_Response(byte[] bArr, int[] iArr);

    public native int SKF_RSASignDataInteractive_Request(int i, int i2, int i3, int i4, int i5, byte[] bArr, int i6, byte[] bArr2);

    public native int SKF_RSASignDataInteractive_Response(byte[] bArr, int[] iArr, byte[] bArr2, int[] iArr2);

    public native int SKF_RSASignData_Request(int i, int i2, int i3, int i4, byte[] bArr, int i5, byte[] bArr2);

    public native int SKF_RSASignData_Response(byte[] bArr, int[] iArr, byte[] bArr2, int[] iArr2);

    public native int SKF_RSAVerify_Request(int i, int i2, int i3, int i4, byte[] bArr, int i5, byte[] bArr2, int i6, byte[] bArr3);

    public native int SKF_RSAVerify_Response(byte[] bArr, int[] iArr);

    public native int SKF_ReadFile_Request(int i, byte[] bArr, int i2, int i3, byte[] bArr2);

    public native int SKF_ReadFile_Response(byte[] bArr, int[] iArr, byte[] bArr2, int[] iArr2);

    public native int SKF_SM3Digest(byte[] bArr, int i, byte[] bArr2, int[] iArr);

    public native void SKF_SetApduMode(int i);

    public native int SKF_SetLabel_Request(byte[] bArr, byte[] bArr2);

    public native int SKF_SetLabel_Response(byte[] bArr, int[] iArr);

    public native int SKF_SetSymtricKey_Request(int i, int i2, int i3, byte[] bArr, int i4, byte[] bArr2);

    public native int SKF_SetSymtricKey_Response(byte[] bArr, int[] iArr, int[] iArr2);

    public native int SKF_UnblockPIN_Request(int i, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4);

    public native int SKF_UnblockPIN_Response(byte[] bArr, int[] iArr, int[] iArr2);

    public native int SKF_VerifyPIN_Request(int i, int i2, byte[] bArr, byte[] bArr2, byte[] bArr3);

    public native int SKF_VerifyPIN_Response(byte[] bArr, int[] iArr, int[] iArr2);

    public native int SKF_WriteFile_Request(int i, byte[] bArr, int i2, byte[] bArr2, int i3, byte[] bArr3);

    public native int SKF_WriteFile_Response(byte[] bArr, int[] iArr);

    static {
        System.loadLibrary("LMSecurityPlugin");
    }
}
