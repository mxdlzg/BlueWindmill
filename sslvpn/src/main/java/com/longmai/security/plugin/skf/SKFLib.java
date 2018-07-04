package com.longmai.security.plugin.skf;

import android.content.Context;
import com.longmai.security.plugin.device.Device;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface SKFLib {
    int SKF_ChangePin(int i, int i2, byte[] bArr, byte[] bArr2, byte[] bArr3, int[] iArr);

    int SKF_ChangePin(int i, byte[] bArr, byte[] bArr2, byte[] bArr3, int[] iArr);

    int SKF_ClearSecureState(int i);

    int SKF_CloseContainer(int i, int i2);

    int SKF_Connect(Device device, byte[] bArr);

    int SKF_CreateApplication(byte[] bArr, byte[] bArr2, int i, byte[] bArr3, int i2, int i3);

    int SKF_CreateContainer(int i, byte[] bArr, int[] iArr);

    int SKF_CreaterFile(int i, byte[] bArr, int i2, int i3, int i4);

    int SKF_DecryptFinal(int i, int i2, int i3, byte[] bArr, int i4, byte[] bArr2, int[] iArr);

    int SKF_DecryptInit(int i, int i2, int i3, byte[] bArr, int i4, int i5);

    int SKF_DecryptUpdate(int i, int i2, int i3, byte[] bArr, int i4, byte[] bArr2, int[] iArr);

    int SKF_DeleteApplication(byte[] bArr);

    int SKF_DeleteCertificate(int i, int i2, int i3);

    int SKF_DeleteContainer(int i, byte[] bArr);

    int SKF_DeleteFile(int i, byte[] bArr);

    int SKF_DeleteKeyPair(int i, int i2, int i3);

    int SKF_DeriveCommSessionKey(byte[] bArr, int i);

    int SKF_DestroySymtricKey(int i, int i2, int i3);

    int SKF_DeviceAuth(byte[] bArr, byte[] bArr2);

    int SKF_DeviceInternalAuth();

    int SKF_DigestFinal(byte[] bArr, int i, byte[] bArr2, int[] iArr);

    int SKF_DigestInit(int i);

    int SKF_DigestInit_Display(int i);

    int SKF_DigestUpdate(byte[] bArr, int i);

    int SKF_DigestUpdate_Display(byte[] bArr, int i);

    int SKF_Disconnect();

    int SKF_ECCExportPublicKey(int i, int i2, int i3, byte[] bArr, int[] iArr);

    int SKF_ECCPrivateDecrypt(int i, int i2, int i3, byte[] bArr, int i4, byte[] bArr2, int[] iArr);

    int SKF_ECCSignData(int i, int i2, int i3, int i4, byte[] bArr, byte[] bArr2, int i5, byte[] bArr3, int[] iArr);

    int SKF_ECCSignDataInteractive(int i, int i2, int i3, int i4, int i5, byte[] bArr, int i6, byte[] bArr2, int i7, byte[] bArr3, int[] iArr);

    int SKF_ECCSignDataInteractiveCancel(int i, int i2, int i3, int i4);

    int SKF_ECCVerify(byte[] bArr, int i, byte[] bArr2, int i2, byte[] bArr3, int i3);

    int SKF_EncryptFinal(int i, int i2, int i3, byte[] bArr, int i4, byte[] bArr2, int[] iArr);

    int SKF_EncryptInit(int i, int i2, int i3, byte[] bArr, int i4, int i5);

    int SKF_EncryptUpdate(int i, int i2, int i3, byte[] bArr, int i4, byte[] bArr2, int[] iArr);

    int SKF_EnumApplication(List<String> list);

    int SKF_EnumContainers(int i, List<String> list);

    int SKF_EnumDeviceByName(Context context, String str, List<Device> list, int i);

    int SKF_EnumDevices(Context context, List<Device> list);

    int SKF_EnumDevices(Context context, List<Device> list, int i);

    int SKF_EnumFiles(int i, List<String> list);

    int SKF_ExportCertificate(int i, int i2, int i3, byte[] bArr, int[] iArr);

    int SKF_ExtECCEncrypt(byte[] bArr, int i, byte[] bArr2, int i2, byte[] bArr3, int[] iArr);

    int SKF_ExtRSAEncrypt(byte[] bArr, int i, byte[] bArr2, int i2, byte[] bArr3, int[] iArr);

    int SKF_ExtRSAPubKeyOperation(byte[] bArr, byte[] bArr2, int i, byte[] bArr3, int[] iArr);

    int SKF_ExtRSAVerify(byte[] bArr, int i, int i2, byte[] bArr2, int i3, byte[] bArr3, int i4) throws NoSuchAlgorithmException;

    int SKF_GenECCKeyPair(int i, int i2, int i3, int i4, byte[] bArr, int[] iArr);

    int SKF_GenRSAKeyPair(int i, int i2, int i3, int i4, byte[] bArr, int[] iArr);

    int SKF_GenRandom(int i, byte[] bArr);

    int SKF_GetContainerType(int i, byte[] bArr, int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4, int[] iArr5);

    int SKF_GetDevInfo(byte[] bArr, byte[] bArr2, int[] iArr);

    int SKF_GetDevInfo(byte[] bArr, byte[] bArr2, int[] iArr, int[] iArr2, byte[] bArr3, int[] iArr3);

    int SKF_GetFileInfo(int i, byte[] bArr, int[] iArr, int[] iArr2, int[] iArr3);

    int SKF_GetLastError();

    int SKF_GetPINInfo(int i, int i2, int[] iArr, int[] iArr2, int[] iArr3);

    int SKF_ImportCertificate(int i, int i2, int i3, int i4, byte[] bArr, int i5);

    int SKF_ImportECCKeyPair(int i, int i2, byte[] bArr, int i3);

    int SKF_ImportExtRSAKeyPair(int i, int i2, int i3, int i4, byte[] bArr, int i5);

    int SKF_ImportRSAKeyPair(int i, int i2, int i3, int i4, int i5, byte[] bArr, int i6, byte[] bArr2, int i7);

    int SKF_LedControl(int i, int i2);

    int SKF_LoadLibrary(Context context, String str, String str2, String str3);

    int SKF_Login(int i, int i2, byte[] bArr, byte[] bArr2, int[] iArr);

    int SKF_Login(int i, byte[] bArr, byte[] bArr2, int[] iArr);

    int SKF_OpenApplication(byte[] bArr, int[] iArr);

    int SKF_OpenContainer(int i, byte[] bArr, int[] iArr);

    int SKF_RSADecrypt(int i, int i2, int i3, byte[] bArr, int i4, byte[] bArr2, int[] iArr);

    int SKF_RSAEncrypt(int i, int i2, int i3, byte[] bArr, int i4, byte[] bArr2, int[] iArr);

    int SKF_RSAExportPublicKey(int i, int i2, int i3, byte[] bArr, int[] iArr);

    int SKF_RSAPrivate(int i, int i2, int i3, byte[] bArr, int i4, byte[] bArr2, int[] iArr);

    int SKF_RSAPublic(int i, int i2, int i3, byte[] bArr, int i4, byte[] bArr2, int[] iArr);

    int SKF_RSASignData(int i, int i2, int i3, int i4, byte[] bArr, int i5, byte[] bArr2, int[] iArr) throws NoSuchAlgorithmException;

    int SKF_RSASignDataInteractive(int i, int i2, int i3, int i4, int i5, byte[] bArr, int i6, byte[] bArr2, int[] iArr);

    int SKF_RSASignDataInteractiveCancel(int i, int i2, int i3, int i4);

    int SKF_RSAVerify(int i, int i2, int i3, int i4, byte[] bArr, int i5, byte[] bArr2, int i6) throws NoSuchAlgorithmException;

    int SKF_ReadFile(int i, byte[] bArr, int i2, int i3, byte[] bArr2, int[] iArr);

    int SKF_SM3Digest(byte[] bArr, int i, byte[] bArr2, int[] iArr);

    int SKF_SetLabel(byte[] bArr);

    void SKF_SetLastError(int i);

    int SKF_SetSymtricKey(int i, int i2, int i3, byte[] bArr, int i4, int[] iArr);

    int SKF_UnblockPIN(int i, byte[] bArr, byte[] bArr2, byte[] bArr3, int[] iArr);

    int SKF_WriteFile(int i, byte[] bArr, int i2, byte[] bArr2, int i3);

    int getConnState();

    int getWorkState();
}
