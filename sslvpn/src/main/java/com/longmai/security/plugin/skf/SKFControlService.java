package com.longmai.security.plugin.skf;

import android.content.Context;
import com.longmai.security.plugin.base.PluginException;
import com.longmai.security.plugin.device.Device;
import com.longmai.security.plugin.device.DeviceManager;
import com.longmai.security.plugin.driver.Driver;
import com.longmai.security.plugin.driver.DriverManager;
import com.longmai.security.plugin.driver.conn.Connection;
import com.longmai.security.plugin.driver.conn.SecurityConnection;
import com.longmai.security.plugin.skf.ndk.SKF_CMDPackerImple;
import com.longmai.security.plugin.util.LogUtil;
import java.io.ByteArrayOutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class SKFControlService implements SKFLib {
    private static final String TAG = SKFControlService.class.getName();
    private static final byte[] virtual_container = "********************".getBytes();
    private Connection conn;
    private DeviceManager deviceManager;
    private int lastErrorCode;
    private SKF_CMDPacker packer = new SKF_CMDPackerImple();
    private int workState;

    public int SKF_LoadLibrary(Context context, String id, String name, String clazz) {
        LogUtil.m10d(TAG, "SKF_LoadLibrary()");
        SKF_SetLastError(0);
        try {
            Class.forName(clazz);
            try {
                Driver driver = DriverManager.getDriver(clazz);
                if (context == null) {
                    try {
                        driver.init();
                    } catch (PluginException e) {
                        e.printStackTrace();
                        SKF_SetLastError(e.getErrorCode());
                        return 3;
                    }
                }
                driver.init(context);
                try {
                    this.deviceManager = driver.getDeviceManager();
                    return 0;
                } catch (PluginException e2) {
                    e2.printStackTrace();
                    SKF_SetLastError(e2.getErrorCode());
                    return 3;
                }
            } catch (PluginException e22) {
                e22.printStackTrace();
                SKF_SetLastError(e22.getErrorCode());
                return 2;
            }
        } catch (ClassNotFoundException e3) {
            e3.printStackTrace();
            SKF_SetLastError(9);
            return 1;
        }
    }

    public int SKF_EnumDevices(Context context, List<Device> devices) {
        LogUtil.m10d(TAG, "SKF_EnumDevices()");
        SKF_SetLastError(0);
        if (this.deviceManager == null) {
            SKF_SetLastError(19);
            return 1;
        }
        try {
            devices.addAll(this.deviceManager.find(new String[0]));
            return 0;
        } catch (PluginException e) {
            e.printStackTrace();
            SKF_SetLastError(e.getErrorCode());
            return 2;
        }
    }

    public int SKF_EnumDeviceByName(Context context, String devName, List<Device> devices, int timeOut) {
        LogUtil.m10d(TAG, "SKF_EnumDeviceByName()");
        SKF_SetLastError(0);
        if (this.deviceManager == null) {
            SKF_SetLastError(19);
            return 1;
        }
        try {
            devices.addAll(this.deviceManager.find(timeOut, devName));
            return 0;
        } catch (PluginException e) {
            e.printStackTrace();
            SKF_SetLastError(e.getErrorCode());
            return 2;
        }
    }

    public int SKF_EnumDevices(Context context, List<Device> devices, int timeOut) {
        LogUtil.m10d(TAG, "SKF_EnumDevices()");
        SKF_SetLastError(0);
        if (this.deviceManager == null) {
            SKF_SetLastError(19);
            return 1;
        }
        try {
            devices.addAll(this.deviceManager.find(timeOut, new String[0]));
            return 0;
        } catch (PluginException e) {
            e.printStackTrace();
            SKF_SetLastError(e.getErrorCode());
            return 2;
        }
    }

    public int SKF_Connect(Device device, byte[] authCode) {
        LogUtil.m10d(TAG, "SKF_Connect()");
        SKF_SetLastError(0);
        if (this.deviceManager == null) {
            SKF_SetLastError(19);
            return 1;
        }
        try {
            this.conn = this.deviceManager.getConnection(device);
            this.packer.SKF_Init();
            if (device.getType() != 128 || !(this.conn instanceof SecurityConnection)) {
                return 0;
            }
            if (SKF_DeriveCommSessionKey(authCode, authCode.length) != 0) {
                SKF_Disconnect();
                return 3;
            } else if (SKF_GenRandom(8, new byte[8]) == 0) {
                return 0;
            } else {
                SKF_Disconnect();
                return 4;
            }
        } catch (PluginException e) {
            e.printStackTrace();
            SKF_SetLastError(e.getErrorCode());
            this.conn = null;
            return 2;
        }
    }

    public int SKF_Disconnect() {
        LogUtil.m10d(TAG, "SKF_Disconnect()");
        if (this.conn != null) {
            try {
                this.conn.close();
            } catch (PluginException e) {
                e.printStackTrace();
                SKF_SetLastError(e.getErrorCode());
            } finally {
                this.conn = null;
            }
        }
        return 0;
    }

    public int SKF_DeriveCommSessionKey(byte[] authCode, int codeLen) {
        LogUtil.m10d(TAG, "SKF_DeriveCommSessionKey()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[512];
        int buff_length = this.packer.SKF_DeriveCommSessionKey_Request(authCode, codeLen, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        try {
            int[] errorCode = new int[1];
            int rtn = this.packer.SKF_DeriveCommSessionKey_Response(((SecurityConnection) this.conn).deriveSecurityCommSession(Arrays.copyOfRange(buffer, 0, buff_length)), errorCode);
            if (rtn != 0) {
                SKF_SetLastError(rtn);
                return 4;
            } else if (errorCode[0] == 36864) {
                return 0;
            } else {
                SKF_SetLastError(errorCode[0]);
                return 5;
            }
        } catch (PluginException e) {
            e.printStackTrace();
            SKF_SetLastError(e.getErrorCode());
            return 3;
        }
    }

    public int SKF_DeviceInternalAuth() {
        LogUtil.m10d(TAG, "SKF_DeviceInternalAuth()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[512];
        int buff_length = this.packer.SKF_DeviceInternalAuth_Request(buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_DeviceInternalAuth_Response(apdu, errorCode);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    private byte[] send(byte[] apdu, int off, int len) {
        byte[] bArr = null;
        if (this.conn == null) {
            SKF_SetLastError(7);
        } else {
            try {
                byte[] response = new byte[8192];
                int[] res_len = new int[1];
                if (this.conn.deviceio(Arrays.copyOfRange(apdu, off, off + len), len, response, res_len) != 0) {
                    SKF_SetLastError(11);
                } else {
                    bArr = Arrays.copyOfRange(response, 0, res_len[0]);
                }
            } catch (PluginException e) {
                e.printStackTrace();
                SKF_SetLastError(e.getErrorCode());
            }
        }
        return bArr;
    }

    private byte[] send(byte[] apdu, int off, int len, int protocol) {
        if (this.conn == null) {
            SKF_SetLastError(7);
            return null;
        }
        try {
            int rtn;
            byte[] response = new byte[8192];
            int[] res_len = new int[1];
            if (this.conn instanceof SecurityConnection) {
                rtn = ((SecurityConnection) this.conn).deviceio(apdu, len, response, res_len, protocol);
            } else {
                rtn = this.conn.deviceio(apdu, len, response, res_len);
            }
            if (rtn == 0) {
                return Arrays.copyOfRange(response, 0, res_len[0]);
            }
            SKF_SetLastError(11);
            return null;
        } catch (PluginException e) {
            e.printStackTrace();
            SKF_SetLastError(e.getErrorCode());
            return null;
        }
    }

    public int SKF_OpenApplication(byte[] appName, int[] appId) {
        LogUtil.m10d(TAG, "SKF_OpenApp()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[512];
        int buff_length = this.packer.SKF_OpenApplication_Request(appName, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_OpenApplication_Response(apdu, errorCode, appId);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_GenRandom(int length, byte[] random) {
        LogUtil.m10d(TAG, "SKF_GenRandom() - length:" + length);
        if (random == null || random.length < length) {
            throw new IllegalArgumentException();
        }
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[512];
        int buff_length = this.packer.SKF_GenRandom_Request(length, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_GenRandom_Response(apdu, errorCode, random, new int[1]);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_Login(int appId, byte[] random, byte[] pin, int[] retryCount) {
        LogUtil.m10d(TAG, "SKF_Login()");
        return SKF_Login(appId, 1, random, pin, retryCount);
    }

    public int SKF_Login(int appId, int pinType, byte[] random, byte[] pin, int[] retryCount) {
        LogUtil.m10d(TAG, "SKF_Login()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[512];
        int buff_length = this.packer.SKF_VerifyPIN_Request(appId, pinType, random, pin, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_VerifyPIN_Response(apdu, errorCode, retryCount);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_ClearSecureState(int appId) {
        LogUtil.m10d(TAG, "SKF_ClearSecureState()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[512];
        int buff_length = this.packer.SKF_ClearSecureState_Request(appId, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_ClearSecureState_Response(apdu, errorCode);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_ChangePin(int appId, byte[] random, byte[] oldPin, byte[] newPin, int[] retryCount) {
        LogUtil.m10d(TAG, "SKF_ChangePin()");
        return SKF_ChangePin(appId, 1, random, oldPin, newPin, retryCount);
    }

    public int SKF_ChangePin(int appId, int pinType, byte[] random, byte[] oldPin, byte[] newPin, int[] retryCount) {
        LogUtil.m10d(TAG, "SKF_ChangePin()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[512];
        int buff_length = this.packer.SKF_ChangePIN_Request(appId, pinType, random, oldPin, newPin, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_ChangePIN_Response(apdu, errorCode, retryCount);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_EnumContainers(int appId, List<String> containerNames) {
        LogUtil.m10d(TAG, "SKF_EnumContainers()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[512];
        int buff_length = this.packer.SKF_EnumContainer_Request(appId, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        byte[] namesBuff = new byte[apdu.length];
        int[] length = new int[1];
        int rtn = this.packer.SKF_EnumContainer_Response(apdu, errorCode, namesBuff, length);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] != 36864) {
            SKF_SetLastError(errorCode[0]);
            return 5;
        } else {
            int index = 0;
            int i = 0;
            while (i < length[0]) {
                if (namesBuff[i] == (byte) 0 && index != i) {
                    byte[] name = new byte[(i - index)];
                    System.arraycopy(namesBuff, index, name, 0, i - index);
                    containerNames.add(new String(name));
                    index = i + 1;
                }
                i++;
            }
            return 0;
        }
    }

    public int SKF_OpenContainer(int appId, byte[] containerName, int[] containerId) {
        LogUtil.m10d(TAG, "SKF_OpenContainer()");
        byte[] tmp_containerName = containerName;
        if (Arrays.equals(containerName, virtual_container)) {
            tmp_containerName = new byte[]{(byte) -16, (byte) -15, (byte) -14, (byte) -13, (byte) -12, (byte) -11, (byte) -10, (byte) -9};
        }
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[512];
        int buff_length = this.packer.SKF_OpenContainer_Request(appId, tmp_containerName, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_OpenContainer_Response(apdu, errorCode, containerId);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_CloseContainer(int appId, int containerId) {
        LogUtil.m10d(TAG, "SKF_CloseContainer()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[512];
        int buff_length = this.packer.SKF_CloseContainer_Request(appId, containerId, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_CloseContainer_Response(apdu, errorCode);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_GetContainerType(int appId, byte[] containerName, int[] containerType, int[] signKeyLen, int[] exchKeyLen, int[] signCertFlag, int[] exchCertFlag) {
        LogUtil.m10d(TAG, "SKF_GetContainerType()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[512];
        int buff_length = this.packer.SKF_GetContainerType_Request(appId, containerName, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_GetContainerType_Response(apdu, errorCode, containerType, signKeyLen, exchKeyLen, signCertFlag, exchCertFlag);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_ExportCertificate(int appId, int containerId, int signFlag, byte[] cert, int[] certLen) throws IllegalArgumentException {
        LogUtil.m10d(TAG, "SKF_ExportCertificate()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        ByteArrayOutputStream certBos = new ByteArrayOutputStream();
        int isFirst = 1;
        boolean whileFlag;
        do {
            byte[] buffer = new byte[512];
            int buff_length = this.packer.SKF_ExportCertificate_Request(appId, containerId, signFlag, buffer);
            if (buff_length <= 0) {
                SKF_SetLastError(8);
                return 2;
            }
            byte[] apdu = send(buffer, 0, buff_length);
            if (apdu == null) {
                return 3;
            }
            int[] errorCode = new int[1];
            int[] length = new int[1];
            byte[] _certTmp = new byte[apdu.length];
            int rtn = this.packer.SKF_ExportCertificate_Response(apdu, isFirst, errorCode, _certTmp, length);
            if (rtn != 0) {
                SKF_SetLastError(rtn);
                return 4;
            } else if (errorCode[0] == 27294) {
                certBos.write(_certTmp, 0, length[0]);
                whileFlag = true;
                isFirst = 0;
                continue;
            } else if (errorCode[0] == 36864) {
                certBos.write(_certTmp, 0, length[0]);
                if (certBos.size() > cert.length) {
                    throw new IllegalArgumentException();
                }
                System.arraycopy(certBos.toByteArray(), 0, cert, 0, certBos.size());
                certLen[0] = certBos.size();
                whileFlag = false;
                continue;
            } else {
                SKF_SetLastError(errorCode[0]);
                return 5;
            }
        } while (whileFlag);
        return 0;
    }

    public int SKF_RSASignData(int appId, int containerId, int keySpec, int hash_algo, byte[] data, int dataLen, byte[] signature, int[] signLen) throws NoSuchAlgorithmException {
        LogUtil.m10d(TAG, "SKF_RSASignData()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[512];
        int buff_length = this.packer.SKF_RSASignData_Request(appId, containerId, keySpec, hash_algo, data, dataLen, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_RSASignData_Response(apdu, errorCode, signature, signLen);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_RSAVerify(int appId, int containerId, int keySpec, int hash_algo, byte[] data, int dataLen, byte[] signature, int signLen) throws NoSuchAlgorithmException {
        LogUtil.m10d(TAG, "SKF_RSAVerify()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[512];
        int buff_length = this.packer.SKF_RSAVerify_Request(appId, containerId, keySpec, hash_algo, data, dataLen, signature, signLen, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_RSAVerify_Response(apdu, errorCode);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_SetSymtricKey(int appId, int containerId, int algoId, byte[] key, int length, int[] keyId) {
        LogUtil.m10d(TAG, "SKF_SetSymtricKey()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        int mode = this.packer.SKF_GetApduMode();
        try {
            this.packer.SKF_SetApduMode(0);
            byte[] buffer = new byte[512];
            int buff_length = this.packer.SKF_SetSymtricKey_Request(appId, containerId, algoId, key, length, buffer);
            if (buff_length <= 0) {
                SKF_SetLastError(8);
                return 2;
            }
            byte[] apdu = send(buffer, 0, buff_length, 0);
            if (apdu == null) {
                this.packer.SKF_SetApduMode(mode);
                return 3;
            }
            int[] errorCode = new int[1];
            int rtn = this.packer.SKF_SetSymtricKey_Response(apdu, errorCode, keyId);
            if (rtn != 0) {
                SKF_SetLastError(rtn);
                this.packer.SKF_SetApduMode(mode);
                return 4;
            } else if (errorCode[0] != 36864) {
                SKF_SetLastError(errorCode[0]);
                this.packer.SKF_SetApduMode(mode);
                return 5;
            } else {
                this.packer.SKF_SetApduMode(mode);
                return 0;
            }
        } finally {
            this.packer.SKF_SetApduMode(mode);
        }
    }

    public int SKF_DestroySymtricKey(int appId, int containerId, int keyId) {
        SKF_CMDPacker sKF_CMDPacker;
        LogUtil.m10d(TAG, "SKF_DestroySymtricKey()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        int mode = this.packer.SKF_GetApduMode();
        try {
            this.packer.SKF_SetApduMode(0);
            byte[] buffer = new byte[512];
            int buff_length = this.packer.SKF_DestroySymtricKey_Request(appId, containerId, keyId, buffer);
            if (buff_length <= 0) {
                SKF_SetLastError(8);
                return 2;//sKF_CMDPacker;
            }
            byte[] apdu = send(buffer, 0, buff_length, 0);
            if (apdu == null) {
                this.packer.SKF_SetApduMode(mode);
                return 3;
            }
            int[] errorCode = new int[1];
            int rtn = this.packer.SKF_DestroySymtricKey_Response(apdu, errorCode);
            if (rtn != 0) {
                SKF_SetLastError(rtn);
                this.packer.SKF_SetApduMode(mode);
                return 4;
            } else if (errorCode[0] != 36864) {
                SKF_SetLastError(errorCode[0]);
                this.packer.SKF_SetApduMode(mode);
                return 5;
            } else {
                this.packer.SKF_SetApduMode(mode);
                return 0;
            }
        } finally {
            sKF_CMDPacker = this.packer;
            sKF_CMDPacker.SKF_SetApduMode(mode);
        }
    }

    public int SKF_EncryptInit(int appId, int containerId, int keyId, byte[] iv, int ivLen, int paddingType) {
        LogUtil.m10d(TAG, "SKF_EncryptInit()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        int mode = this.packer.SKF_GetApduMode();
        try {
            this.packer.SKF_SetApduMode(0);
            byte[] buffer = new byte[512];
            int buff_length = this.packer.SKF_EncryptInit_Request(appId, containerId, keyId, iv, ivLen, paddingType, buffer);
            if (buff_length <= 0) {
                SKF_SetLastError(8);
                return 2;
            }
            byte[] apdu = send(buffer, 0, buff_length, 0);
            if (apdu == null) {
                this.packer.SKF_SetApduMode(mode);
                return 3;
            }
            int[] errorCode = new int[1];
            int rtn = this.packer.SKF_EncryptInit_Response(apdu, errorCode);
            if (rtn != 0) {
                SKF_SetLastError(rtn);
                this.packer.SKF_SetApduMode(mode);
                return 4;
            } else if (errorCode[0] != 36864) {
                SKF_SetLastError(errorCode[0]);
                this.packer.SKF_SetApduMode(mode);
                return 5;
            } else {
                this.packer.SKF_SetApduMode(mode);
                return 0;
            }
        } finally {
            this.packer.SKF_SetApduMode(mode);
        }
    }

    public int SKF_EncryptUpdate(int appId, int containerId, int keyId, byte[] input, int inputLen, byte[] output, int[] outputLen) {
        LogUtil.m10d(TAG, "SKF_EncryptUpdate()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        int mode = this.packer.SKF_GetApduMode();
        try {
            this.packer.SKF_SetApduMode(0);
            byte[] buffer = new byte[2048];
            int buff_length = this.packer.SKF_EncryptUpdate_Request(appId, containerId, keyId, input, inputLen, buffer);
            if (buff_length <= 0) {
                SKF_SetLastError(8);
                return 2;
            }
            byte[] apdu = send(buffer, 0, buff_length, 0);
            if (apdu == null) {
                this.packer.SKF_SetApduMode(mode);
                return 3;
            }
            int[] errorCode = new int[1];
            int rtn = this.packer.SKF_EncryptUpdate_Response(apdu, errorCode, output, outputLen);
            if (rtn != 0) {
                SKF_SetLastError(rtn);
                this.packer.SKF_SetApduMode(mode);
                return 4;
            } else if (errorCode[0] != 36864) {
                SKF_SetLastError(errorCode[0]);
                this.packer.SKF_SetApduMode(mode);
                return 5;
            } else {
                this.packer.SKF_SetApduMode(mode);
                return 0;
            }
        } finally {
            this.packer.SKF_SetApduMode(mode);
        }
    }

    public int SKF_EncryptFinal(int appId, int containerId, int keyId, byte[] input, int inputLen, byte[] output, int[] outputLen) {
        LogUtil.m10d(TAG, "SKF_EncryptFinal()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        int mode = this.packer.SKF_GetApduMode();
        try {
            this.packer.SKF_SetApduMode(0);
            byte[] buffer = new byte[2048];
            int buff_length = this.packer.SKF_EncryptFinal_Request(appId, containerId, keyId, input, inputLen, buffer);
            if (buff_length <= 0) {
                SKF_SetLastError(8);
                return 2;
            }
            byte[] apdu = send(buffer, 0, buff_length, 0);
            if (apdu == null) {
                this.packer.SKF_SetApduMode(mode);
                return 3;
            }
            int[] errorCode = new int[1];
            int rtn = this.packer.SKF_EncryptFinal_Response(apdu, errorCode, output, outputLen);
            if (rtn != 0) {
                SKF_SetLastError(rtn);
                this.packer.SKF_SetApduMode(mode);
                return 4;
            } else if (errorCode[0] != 36864) {
                SKF_SetLastError(errorCode[0]);
                this.packer.SKF_SetApduMode(mode);
                return 5;
            } else {
                this.packer.SKF_SetApduMode(mode);
                return 0;
            }
        } finally {
            this.packer.SKF_SetApduMode(mode);
        }
    }

    public int SKF_DecryptInit(int appId, int containerId, int keyId, byte[] iv, int length, int paddingType) {
        LogUtil.m10d(TAG, "SKF_DecryptInit()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        int mode = this.packer.SKF_GetApduMode();
        try {
            this.packer.SKF_SetApduMode(0);
            byte[] buffer = new byte[512];
            int buff_length = this.packer.SKF_DecryptInit_Request(appId, containerId, keyId, iv, length, paddingType, buffer);
            if (buff_length <= 0) {
                SKF_SetLastError(8);
                return 2;
            }
            byte[] apdu = send(buffer, 0, buff_length, 0);
            if (apdu == null) {
                this.packer.SKF_SetApduMode(mode);
                return 3;
            }
            int[] errorCode = new int[1];
            int rtn = this.packer.SKF_DecryptInit_Response(apdu, errorCode);
            if (rtn != 0) {
                SKF_SetLastError(rtn);
                this.packer.SKF_SetApduMode(mode);
                return 4;
            } else if (errorCode[0] != 36864) {
                SKF_SetLastError(errorCode[0]);
                this.packer.SKF_SetApduMode(mode);
                return 5;
            } else {
                this.packer.SKF_SetApduMode(mode);
                return 0;
            }
        } finally {
            this.packer.SKF_SetApduMode(mode);
        }
    }

    public int SKF_DecryptUpdate(int appId, int containerId, int keyId, byte[] input, int input_len, byte[] output, int[] output_len) {
        LogUtil.m10d(TAG, "SKF_DecryptUpdate()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        int mode = this.packer.SKF_GetApduMode();
        try {
            this.packer.SKF_SetApduMode(0);
            byte[] buffer = new byte[2048];
            int buff_length = this.packer.SKF_DecryptUpdate_Request(appId, containerId, keyId, input, input_len, buffer);
            if (buff_length <= 0) {
                SKF_SetLastError(8);
                return 2;
            }
            byte[] apdu = send(buffer, 0, buff_length, 0);
            if (apdu == null) {
                this.packer.SKF_SetApduMode(mode);
                return 3;
            }
            int[] errorCode = new int[1];
            int rtn = this.packer.SKF_DecryptUpdate_Response(apdu, errorCode, output, output_len);
            if (rtn != 0) {
                SKF_SetLastError(rtn);
                this.packer.SKF_SetApduMode(mode);
                return 4;
            } else if (errorCode[0] != 36864) {
                SKF_SetLastError(errorCode[0]);
                this.packer.SKF_SetApduMode(mode);
                return 5;
            } else {
                this.packer.SKF_SetApduMode(mode);
                return 0;
            }
        } finally {
            this.packer.SKF_SetApduMode(mode);
        }
    }

    public int SKF_DecryptFinal(int appId, int containerId, int keyId, byte[] input, int inputLen, byte[] output, int[] outputLen) {
        LogUtil.m10d(TAG, "SKF_DecryptFinal()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        int mode = this.packer.SKF_GetApduMode();
        try {
            this.packer.SKF_SetApduMode(0);
            byte[] buffer = new byte[2048];
            int buff_length = this.packer.SKF_DecryptFinal_Request(appId, containerId, keyId, input, inputLen, buffer);
            if (buff_length <= 0) {
                SKF_SetLastError(8);
                return 2;
            }
            byte[] apdu = send(buffer, 0, buff_length, 0);
            if (apdu == null) {
                this.packer.SKF_SetApduMode(mode);
                return 3;
            }
            int[] errorCode = new int[1];
            int rtn = this.packer.SKF_DecryptFinal_Response(apdu, errorCode, output, outputLen);
            if (rtn != 0) {
                SKF_SetLastError(rtn);
                this.packer.SKF_SetApduMode(mode);
                return 4;
            } else if (errorCode[0] != 36864) {
                SKF_SetLastError(errorCode[0]);
                this.packer.SKF_SetApduMode(mode);
                return 5;
            } else {
                this.packer.SKF_SetApduMode(mode);
                return 0;
            }
        } finally {
            this.packer.SKF_SetApduMode(mode);
        }
    }

    public int SKF_DigestInit(int algoId) {
        LogUtil.m10d(TAG, "SKF_DigestInit()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[512];
        int buff_length = this.packer.SKF_DigestInit_Request(algoId, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_DigestInit_Response(apdu, errorCode);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_DigestUpdate(byte[] input, int inputLen) {
        LogUtil.m10d(TAG, "SKF_DigestUpdate()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[2048];
        int buff_length = this.packer.SKF_DigestUpdate_Request(input, inputLen, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_DigestUpdate_Response(apdu, errorCode);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_DigestFinal(byte[] input, int inputLen, byte[] output, int[] outputLen) {
        LogUtil.m10d(TAG, "SKF_DigestFinal()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[2048];
        int buff_length = this.packer.SKF_DigestFinal_Request(input, inputLen, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_DigestFinal_Response(apdu, errorCode, output, outputLen);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_GetDevInfo(byte[] label, byte[] serialNumber, int[] version) {
        LogUtil.m10d(TAG, "SKF_GetDevInfo()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[512];
        int buff_length = this.packer.SKF_GetDevInfo_Request(buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_GetDevInfo_Response(apdu, errorCode, label, serialNumber, version);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_RSAExportPublicKey(int appId, int containerId, int keySpec, byte[] publicKeyBlob, int[] blobLen) {
        LogUtil.m10d(TAG, "SKF_RSAExportPublicKey()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[512];
        int buff_length = this.packer.SKF_RSAExportPublicKey_Request(appId, containerId, keySpec, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_RSAExportPublicKey_Response(apdu, errorCode, publicKeyBlob, blobLen);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_ExtRSAEncrypt(byte[] pubKeyBlob, int blobLen, byte[] input, int inputLen, byte[] output, int[] outputLen) {
        LogUtil.m10d(TAG, "SKF_ExtRSAEncrypt()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        } else if (inputLen > 512) {
            SKF_SetLastError(2);
            return 2;
        } else {
            byte[] key = new byte[blobLen];
            System.arraycopy(pubKeyBlob, 0, key, 0, blobLen);
            byte[] buffer = new byte[2048];
            int buff_length = this.packer.SKF_ExtRSAEncrypt_Request(key, input, inputLen, buffer);
            if (buff_length <= 0) {
                SKF_SetLastError(8);
                return 3;
            }
            byte[] apdu = send(buffer, 0, buff_length);
            if (apdu == null) {
                return 3;
            }
            int[] errorCode = new int[1];
            int rtn = this.packer.SKF_ExtRSAEncrypt_Response(apdu, errorCode, output, outputLen);
            if (rtn != 0) {
                SKF_SetLastError(rtn);
                return 5;
            } else if (errorCode[0] == 36864) {
                return 0;
            } else {
                SKF_SetLastError(errorCode[0]);
                return 6;
            }
        }
    }

    public int SKF_RSADecrypt(int appId, int containerId, int keySpec, byte[] input, int inputLen, byte[] output, int[] outputLen) {
        LogUtil.m10d(TAG, "SKF_RSADecrypt()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[2048];
        int buff_length = this.packer.SKF_RSADecrypt_Request(appId, containerId, keySpec, input, inputLen, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_RSADecrypt_Response(apdu, errorCode, output, outputLen);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_ExtRSAVerify(byte[] pubKeyBlob, int blobLen, int hash_algo, byte[] data, int dataLen, byte[] signature, int signLen) throws NoSuchAlgorithmException {
        LogUtil.m10d(TAG, "SKF_ExtRSAVerify()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] blob = new byte[blobLen];
        System.arraycopy(pubKeyBlob, 0, blob, 0, blobLen);
        byte[] buffer = new byte[2048];
        int buff_length = this.packer.SKF_ExtRSAVerify_Request(blob, hash_algo, data, dataLen, signature, signLen, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_ExtRSAVerify_Response(apdu, errorCode);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_ECCSignData(int appId, int containerId, int inputMode, int keySpec, byte[] userId, byte[] input, int inputLen, byte[] signature, int[] signLen) {
        LogUtil.m10d(TAG, "SKF_ECCSignData()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        } else if (inputLen > 1024) {
            SKF_SetLastError(2);
            return 2;
        } else {
            byte[] buffer = new byte[2048];
            int buff_length = this.packer.SKF_ECCSignData_Request(appId, containerId, inputMode, keySpec, userId, userId.length, input, inputLen, buffer);
            if (buff_length <= 0) {
                SKF_SetLastError(8);
                return 3;
            }
            byte[] apdu = send(buffer, 0, buff_length);
            if (apdu == null) {
                return 3;
            }
            int[] errorCode = new int[1];
            int rtn = this.packer.SKF_ECCSignData_Response(apdu, errorCode, signature, signLen);
            if (rtn != 0) {
                SKF_SetLastError(rtn);
                return 5;
            } else if (errorCode[0] == 36864) {
                return 0;
            } else {
                SKF_SetLastError(errorCode[0]);
                return 6;
            }
        }
    }

    public int SKF_ECCVerify(byte[] pubKeyBlob, int blobLen, byte[] input, int inputLen, byte[] signature, int signLen) {
        LogUtil.m10d(TAG, "SKF_ECCVerify()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        } else if (inputLen > 1024) {
            SKF_SetLastError(2);
            return 2;
        } else {
            byte[] key = new byte[blobLen];
            System.arraycopy(pubKeyBlob, 0, key, 0, blobLen);
            byte[] buffer = new byte[2048];
            int buff_length = this.packer.SKF_ECCVerify_Request(key, input, inputLen, signature, signLen, buffer);
            if (buff_length <= 0) {
                SKF_SetLastError(8);
                return 3;
            }
            byte[] apdu = send(buffer, 0, buff_length);
            if (apdu == null) {
                return 3;
            }
            int[] errorCode = new int[1];
            int rtn = this.packer.SKF_ECCVerify_Response(apdu, errorCode);
            if (rtn != 0) {
                SKF_SetLastError(rtn);
                return 5;
            } else if (errorCode[0] == 36864) {
                return 0;
            } else {
                SKF_SetLastError(errorCode[0]);
                return 6;
            }
        }
    }

    public int SKF_ExtECCEncrypt(byte[] pubKeyBlob, int blobLen, byte[] input, int inputLen, byte[] output, int[] outputLen) {
        LogUtil.m10d(TAG, "SKF_ExtECCEncrypt()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        } else if (inputLen > 1024) {
            SKF_SetLastError(2);
            return 2;
        } else {
            byte[] key = new byte[blobLen];
            System.arraycopy(pubKeyBlob, 0, key, 0, blobLen);
            byte[] buffer = new byte[2048];
            int buff_length = this.packer.SKF_ExtECCEncrypt_Request(key, input, inputLen, buffer);
            if (buff_length <= 0) {
                SKF_SetLastError(8);
                return 3;
            }
            byte[] apdu = send(buffer, 0, buff_length);
            if (apdu == null) {
                return 3;
            }
            int[] errorCode = new int[1];
            int rtn = this.packer.SKF_ExtECCEncrypt_Response(apdu, errorCode, output, outputLen);
            if (rtn != 0) {
                SKF_SetLastError(rtn);
                return 5;
            } else if (errorCode[0] == 36864) {
                return 0;
            } else {
                SKF_SetLastError(errorCode[0]);
                return 6;
            }
        }
    }

    public int SKF_ECCPrivateDecrypt(int appId, int containerId, int signFlag, byte[] input, int inputLen, byte[] output, int[] outputLen) {
        LogUtil.m10d(TAG, "SKF_ECCPrivateDecrypt()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[2048];
        int buff_length = this.packer.SKF_ECCPrivateDecrypt_Request(appId, containerId, signFlag, input, inputLen, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_ECCPrivateDecrypt_Response(apdu, errorCode, output, outputLen);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_EnumFiles(int appId, List<String> files) {
        LogUtil.m10d(TAG, "SKF_EnumFiles()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[512];
        int buff_length = this.packer.SKF_EnumFiles_Request(appId, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        byte[] filesBuff = new byte[apdu.length];
        int[] length = new int[1];
        int rtn = this.packer.SKF_EnumFiles_Response(apdu, errorCode, filesBuff, length);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] != 36864) {
            SKF_SetLastError(errorCode[0]);
            return 5;
        } else {
            int flag = 0;
            int i = 0;
            while (i < length[0]) {
                if (filesBuff[i] == (byte) 0 && flag != i) {
                    byte[] file = new byte[(i - flag)];
                    System.arraycopy(filesBuff, flag, file, 0, i - flag);
                    files.add(new String(file));
                    flag = i + 1;
                }
                i++;
            }
            return 0;
        }
    }

    public int SKF_GetFileInfo(int appId, byte[] fileName, int[] fileSize, int[] readRights, int[] writeRights) {
        LogUtil.m10d(TAG, "SKF_GetFileInfo()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[512];
        int buff_length = this.packer.SKF_GetFileInfo_Request(appId, fileName, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_GetFileInfo_Response(apdu, errorCode, fileSize, readRights, writeRights);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_ReadFile(int appId, byte[] fileName, int offset, int readLen, byte[] output, int[] outputLen) {
        LogUtil.m10d(TAG, "SKF_ReadFile()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[512];
        int buff_length = this.packer.SKF_ReadFile_Request(appId, fileName, offset, readLen, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_ReadFile_Response(apdu, errorCode, output, outputLen);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_WriteFile(int appId, byte[] fileName, int offset, byte[] input, int inputLen) {
        LogUtil.m10d(TAG, "SKF_WriteFile()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[2048];
        int buff_length = this.packer.SKF_WriteFile_Request(appId, fileName, offset, input, inputLen, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_WriteFile_Response(apdu, errorCode);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_GetLastError() {
        LogUtil.m10d(TAG, "SKF_GetLastError()");
        return this.lastErrorCode;
    }

    public void SKF_SetLastError(int errorCode) {
        LogUtil.m10d(TAG, "SKF_SetLastError() - errorCode:" + errorCode);
        this.lastErrorCode = errorCode;
    }

    public int getWorkState() {
        LogUtil.m10d(TAG, "getWorkState()");
        return this.workState;
    }

    public void setWorkState(int state) {
        LogUtil.m10d(TAG, "setWorkState()");
        this.workState = state;
    }

    public int SKF_CreaterFile(int appId, byte[] fileName, int fileSize, int readRights, int writeRights) {
        LogUtil.m10d(TAG, "SKF_CreaterFile()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[2048];
        int buff_length = this.packer.SKF_CreaterFile_Request(appId, fileName, fileSize, readRights, writeRights, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_CreaterFile_Response(apdu, errorCode);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_DeleteFile(int appId, byte[] fileName) {
        LogUtil.m10d(TAG, "SKF_DeleteFile()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[2048];
        int buff_length = this.packer.SKF_DeleteFile_Request(appId, fileName, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_DeleteFile_Response(apdu, errorCode);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_SetLabel(byte[] label) {
        LogUtil.m10d(TAG, "SKF_SetLabel()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[2048];
        int buff_length = this.packer.SKF_SetLabel_Request(label, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_SetLabel_Response(apdu, errorCode);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_GetPINInfo(int appId, int pinType, int[] maxRetryCount, int[] retryCount, int[] defaultPin) {
        LogUtil.m10d(TAG, "SKF_GetPINInfo()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[2048];
        int buff_length = this.packer.SKF_GetPINInfo_Request(appId, pinType, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_GetPINInfo_Response(apdu, errorCode, maxRetryCount, retryCount, defaultPin);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_UnblockPIN(int appId, byte[] random, byte[] soPin, byte[] uPin, int[] retryCount) {
        LogUtil.m10d(TAG, "SKF_UnblockPIN()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[2048];
        int buff_length = this.packer.SKF_UnblockPIN_Request(appId, random, soPin, uPin, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_UnblockPIN_Response(apdu, errorCode, retryCount);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_CreateContainer(int appId, byte[] containerName, int[] containerId) {
        LogUtil.m10d(TAG, "SKF_CreateContainer()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[2048];
        int buff_length = this.packer.SKF_CreateContainer_Request(appId, containerName, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_CreateContainer_Response(apdu, errorCode, containerId);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_DeleteContainer(int appId, byte[] containerName) {
        LogUtil.m10d(TAG, "SKF_DeleteContainer()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[2048];
        int buff_length = this.packer.SKF_DeleteContainer_Request(appId, containerName, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_DeleteContainer_Response(apdu, errorCode);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_ImportCertificate(int appId, int containerId, int signFlag, int phase, byte[] cert, int certLen) {
        LogUtil.m10d(TAG, "SKF_ImportCertificate()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[2048];
        int buff_length = this.packer.SKF_ImportCertificate_Request(appId, containerId, signFlag, phase, cert, certLen, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_ImportCertificate_Response(apdu, errorCode);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_GenRSAKeyPair(int appId, int containerId, int signFlag, int bitLen, byte[] pubKey, int[] keyLen) {
        LogUtil.m10d(TAG, "SKF_GenRSAKeyPair()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[2048];
        int buff_length = this.packer.SKF_GenRSAKeyPair_Request(appId, containerId, signFlag, bitLen, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        int timeout = this.conn.getTimeOut();
        this.conn.setTimeOut(20000);
        byte[] apdu = send(buffer, 0, buff_length);
        this.conn.setTimeOut(timeout);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_GenRSAKeyPair_Response(apdu, errorCode, pubKey, keyLen);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_ImportRSAKeyPair(int appId, int containerId, int algId, int phase, int bitLen, byte[] wrappedKey, int wrappedKeyLen, byte[] encryptedData, int encryptedDataLen) {
        LogUtil.m10d(TAG, "SKF_ImportRSAKeyPair()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[2048];
        int buff_length = this.packer.SKF_ImportRSAKeyPair_Request(appId, containerId, algId, phase, bitLen, wrappedKey, wrappedKeyLen, encryptedData, encryptedDataLen, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_ImportRSAKeyPair_Response(apdu, errorCode);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_GenECCKeyPair(int appId, int containerId, int signFlag, int bitLen, byte[] pubKey, int[] keyLen) {
        LogUtil.m10d(TAG, "SKF_GenECCKeyPair()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[2048];
        int buff_length = this.packer.SKF_GenECCKeyPair_Request(appId, containerId, signFlag, bitLen, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        int timeout = this.conn.getTimeOut();
        this.conn.setTimeOut(20000);
        byte[] apdu = send(buffer, 0, buff_length);
        this.conn.setTimeOut(timeout);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_GenECCKeyPair_Response(apdu, errorCode, pubKey, keyLen);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_ImportECCKeyPair(int appId, int containerId, byte[] envelopedKeyBlob, int envelopedKeyBlobLen) {
        LogUtil.m10d(TAG, "SKF_ImportECCKeyPair()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[2048];
        int buff_length = this.packer.SKF_ImportECCKeyPair_Request(appId, containerId, envelopedKeyBlob, envelopedKeyBlobLen, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_ImportECCKeyPair_Response(apdu, errorCode);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_ImportExtRSAKeyPair(int appId, int containerId, int signFlag, int phase, byte[] blob, int blobLen) {
        LogUtil.m10d(TAG, "SKF_ImportExtRSAKeyPair()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[2048];
        int buff_length = this.packer.SKF_ImportExtRSAKeyPair_Request(appId, containerId, signFlag, phase, blob, blobLen, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_ImportExtRSAKeyPair_Response(apdu, errorCode);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_LedControl(int state, int interval) {
        LogUtil.m10d(TAG, "SKF_LedControl() - state:" + state + " interval:" + interval);
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[2048];
        int buff_length = this.packer.SKF_LedControl_Request(state, interval, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_LedControl_Response(apdu, errorCode);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_ECCExportPublicKey(int appId, int containerId, int keySpec, byte[] publicKeyBlob, int[] blobLen) {
        LogUtil.m10d(TAG, "SKF_ECCExportPublicKey()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[512];
        int buff_length = this.packer.SKF_ECCExportPublicKey_Request(appId, containerId, keySpec, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_ECCExportPublicKey_Response(apdu, errorCode, publicKeyBlob, blobLen);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_SM3Digest(byte[] data, int length, byte[] digest, int[] digestLen) {
        LogUtil.m10d(TAG, "SKF_SM3Digest()");
        return this.packer.SKF_SM3Digest(data, length, digest, digestLen);
    }

    public int SKF_RSAEncrypt(int appId, int containerId, int keySpec, byte[] input, int inputLen, byte[] output, int[] outputLen) {
        LogUtil.m10d(TAG, "SKF_RSAEncrypt()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[2048];
        int buff_length = this.packer.SKF_RSAEncrypt_Request(appId, containerId, keySpec, input, inputLen, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_RSAEncrypt_Response(apdu, errorCode, output, outputLen);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int getConnState() {
        if (this.conn == null || !this.conn.isValid()) {
            return 0;
        }
        return 2;
    }

    public int SKF_RSAPublic(int appId, int containerId, int signFlag, byte[] input, int inputLen, byte[] output, int[] outputLen) {
        LogUtil.m10d(TAG, "SKF_RSAPublic()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[1024];
        int buff_length = this.packer.SKF_RSAPublic_Request(appId, containerId, signFlag, input, inputLen, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_RSAPublic_Response(apdu, errorCode, output, outputLen);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_RSAPrivate(int appId, int containerId, int signFlag, byte[] input, int inputLen, byte[] output, int[] outputLen) {
        LogUtil.m10d(TAG, "SKF_RSAPrivate()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[1024];
        int buff_length = this.packer.SKF_RSAPrivate_Request(appId, containerId, signFlag, input, inputLen, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_RSAPrivate_Response(apdu, errorCode, output, outputLen);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_GetDevInfo(byte[] label, byte[] serialNumber, int[] version, int[] freeSpace, byte[] struct, int[] structLen) {
        LogUtil.m10d(TAG, "SKF_GetDevInfo()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[512];
        int buff_length = this.packer.SKF_GetDevInfoEx_Request(buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_GetDevInfoEx_Response(apdu, errorCode, label, serialNumber, version, freeSpace, struct, structLen);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_ExtRSAPubKeyOperation(byte[] pubKeyBlob, byte[] input, int input_len, byte[] output, int[] output_len) {
        LogUtil.m10d(TAG, "SKF_ExtRSAPubKeyOperation()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[1024];
        int buff_length = this.packer.SKF_ExtRSAPubKeyOperation_Request(pubKeyBlob, input, input_len, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_ExtRSAPubKeyOperation_Response(apdu, errorCode, output, output_len);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_DeleteCertificate(int appId, int containerId, int signFlag) {
        LogUtil.m10d(TAG, "SKF_DeleteCertificate()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[512];
        int buff_length = this.packer.SKF_DeleteCertificate_Request(appId, containerId, signFlag, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_DeleteCertificate_Response(apdu, errorCode);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_DeleteKeyPair(int appId, int containerId, int signFlag) {
        LogUtil.m10d(TAG, "SKF_DeleteKeyPair()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[512];
        int buff_length = this.packer.SKF_DeleteKeyPair_Request(appId, containerId, signFlag, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_DeleteKeyPair_Response(apdu, errorCode);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_RSASignDataInteractive(int appId, int containerId, int keySpec, int hashAlgo, int timeout, byte[] data, int length, byte[] signature, int[] signLen) {
        LogUtil.m10d(TAG, "SKF_RSASignDataInteractive()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[512];
        int buff_length = this.packer.SKF_RSASignDataInteractive_Request(appId, containerId, keySpec, hashAlgo, timeout, data, length, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_RSASignDataInteractive_Response(apdu, errorCode, signature, signLen);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_RSASignDataInteractiveCancel(int appId, int containerId, int keySpec, int hashAlgo) {
        LogUtil.m10d(TAG, "SKF_RSASignDataInteractiveCancel()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[512];
        int buff_length = this.packer.SKF_RSASignDataInteractiveCancel_Request(appId, containerId, keySpec, hashAlgo, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_RSASignDataInteractiveCancel_Response(apdu, errorCode);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_ECCSignDataInteractive(int appId, int containerId, int inputMode, int keySpec, int timeout, byte[] userId, int userIdLen, byte[] input, int inputLen, byte[] signature, int[] signLen) {
        LogUtil.m10d(TAG, "SKF_ECCSignDataInteractive()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[512];
        int buff_length = this.packer.SKF_ECCSignDataInteractive_Request(appId, containerId, inputMode, keySpec, timeout, userId, userIdLen, input, inputLen, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_ECCSignDataInteractive_Response(apdu, errorCode, signature, signLen);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_ECCSignDataInteractiveCancel(int appId, int containerId, int inputMode, int keySpec) {
        LogUtil.m10d(TAG, "SKF_ECCSignDataInteractiveCancel()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[512];
        int buff_length = this.packer.SKF_ECCSignDataInteractiveCancel_Request(appId, containerId, inputMode, keySpec, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_ECCSignDataInteractiveCancel_Response(apdu, errorCode);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_DigestInit_Display(int algoId) {
        LogUtil.m10d(TAG, "SKF_DigestInit_Display()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[512];
        int buff_length = this.packer.SKF_DigestInit_Display_Request(algoId, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_DigestInit_Display_Response(apdu, errorCode);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_DigestUpdate_Display(byte[] data, int length) {
        LogUtil.m10d(TAG, "SKF_DigestUpdate_Display()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[2048];
        int buff_length = this.packer.SKF_DigestUpdate_Display_Request(data, length, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_DigestUpdate_Display_Response(apdu, errorCode);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_EnumApplication(List<String> appNames) {
        LogUtil.m10d(TAG, "SKF_EnumApps()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[512];
        int buff_length = this.packer.SKF_EnumApplication_Request(buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        byte[] namesBuff = new byte[apdu.length];
        int[] length = new int[1];
        int rtn = this.packer.SKF_EnumApplication_Response(apdu, errorCode, namesBuff, length);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] != 36864) {
            SKF_SetLastError(errorCode[0]);
            return 5;
        } else {
            int index = 0;
            int i = 0;
            while (i < length[0]) {
                if (namesBuff[i] == (byte) 0 && index != i) {
                    byte[] name = new byte[(i - index)];
                    System.arraycopy(namesBuff, index, name, 0, i - index);
                    appNames.add(new String(name));
                    index = i + 1;
                }
                i++;
            }
            return 0;
        }
    }

    public int SKF_DeviceAuth(byte[] random, byte[] devAuthKey) {
        LogUtil.m10d(TAG, "SKF_DeviceAuth()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[512];
        int buff_length = this.packer.SKF_DeviceAuth_Request(random, devAuthKey, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_DeviceAuth_Response(apdu, errorCode);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_CreateApplication(byte[] appName, byte[] adminPin, int adminPinRetryCount, byte[] userPin, int userPinRetryCount, int dwCreateFileRights) {
        LogUtil.m10d(TAG, "SKF_CreateApp()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[512];
        int buff_length = this.packer.SKF_CreateApp_Request(appName, adminPin, adminPinRetryCount, userPin, userPinRetryCount, dwCreateFileRights, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_CreateApp_Response(apdu, errorCode);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }

    public int SKF_DeleteApplication(byte[] appName) {
        LogUtil.m10d(TAG, "SKF_DeleteApp()");
        SKF_SetLastError(0);
        if (getConnState() != 2) {
            SKF_SetLastError(7);
            return 1;
        }
        byte[] buffer = new byte[512];
        int buff_length = this.packer.SKF_DeleteApp_Request(appName, buffer);
        if (buff_length <= 0) {
            SKF_SetLastError(8);
            return 2;
        }
        byte[] apdu = send(buffer, 0, buff_length);
        if (apdu == null) {
            return 3;
        }
        int[] errorCode = new int[1];
        int rtn = this.packer.SKF_DeleteApp_Response(apdu, errorCode);
        if (rtn != 0) {
            SKF_SetLastError(rtn);
            return 4;
        } else if (errorCode[0] == 36864) {
            return 0;
        } else {
            SKF_SetLastError(errorCode[0]);
            return 5;
        }
    }
}
