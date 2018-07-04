package com.longmai.security.plugin;

import android.os.AsyncTask;
import android.support.v4.internal.view.SupportMenu;
import android.text.TextUtils;
import com.longmai.security.plugin.base.BaseCallback;
import com.longmai.security.plugin.base.BaseContainerType;
import com.longmai.security.plugin.base.ContainerInfo;
import com.longmai.security.plugin.base.ECCCipherBlob;
import com.longmai.security.plugin.base.ECCPublicKeyBlob;
import com.longmai.security.plugin.base.EnvlopedKeyBlob;
import com.longmai.security.plugin.skf.SKFLib;
import com.longmai.security.plugin.util.DigestUtil;
import com.longmai.security.plugin.util.LogUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SOF_AppLib {
    private static final String TAG = SOF_AppLib.class.getName();
    private Map<String, BaseContainerType> ContainerInfos = new ConcurrentHashMap();
    private Map<String, Integer> Containers = new ConcurrentHashMap();
    private ByteArrayOutputStream _bos = new ByteArrayOutputStream();
    private int _paddingType;
    private int appId;
    private String appName;
    DigestUtil digest;
    private SKFLib skf;

    class SignAsyncTask extends AsyncTask<Void, Void, Integer> {
        BaseCallback<byte[]> callback;
        String containerName;
        byte[] hash;
        int hashAlgo;
        int hashLen;
        int signFlag;
        int[] signLen = new int[1];
        byte[] signature = new byte[1024];
        int timeOut;

        public SignAsyncTask(String containerName, int signFlag, int hashAlgo, byte[] hash, int hashLen, int timeOut, BaseCallback<byte[]> callback) {
            this.containerName = containerName;
            this.signFlag = signFlag;
            this.hashAlgo = hashAlgo;
            this.hash = hash;
            this.hashLen = hashLen;
            this.timeOut = timeOut;
            this.callback = callback;
        }

        protected Integer doInBackground(Void... arg0) {
            Integer containerId = (Integer) SOF_AppLib.this.Containers.get(this.containerName);
            if (containerId == null) {
                int[] id = new int[1];
                if (SOF_AppLib.this.skf.SKF_OpenContainer(SOF_AppLib.this.appId, this.containerName.getBytes(), id) != 0) {
                    return Integer.valueOf(1);
                }
                containerId = Integer.valueOf(id[0]);
                SOF_AppLib.this.Containers.put(this.containerName, containerId);
            }
            BaseContainerType containerInfo = (BaseContainerType) SOF_AppLib.this.ContainerInfos.get(this.containerName);
            if (containerInfo == null) {
                int[] containerType = new int[1];
                int[] signKeyLen = new int[1];
                int[] exchKeyLen = new int[1];
                int[] signCertFlag = new int[1];
                int[] exchCertFlag = new int[1];
                if (SOF_AppLib.this.skf.SKF_GetContainerType(SOF_AppLib.this.appId, this.containerName.getBytes(), containerType, signKeyLen, exchKeyLen, signCertFlag, exchCertFlag) != 0) {
                    return Integer.valueOf(2);
                }
                containerInfo = new ContainerInfo(SOF_AppLib.this.appId, this.containerName, containerType[0], signKeyLen[0], exchKeyLen[0], signCertFlag[0], exchCertFlag[0]);
                SOF_AppLib.this.ContainerInfos.put(this.containerName, containerInfo);
            }
            if (containerInfo.containerType == 0) {
                SOF_AppLib.this.skf.SKF_SetLastError(3);
                return Integer.valueOf(3);
            }
            switch (this.signFlag) {
                case 0:
                    if (containerInfo.exchKeyLen == 0) {
                        SOF_AppLib.this.skf.SKF_SetLastError(3);
                        return Integer.valueOf(4);
                    }
                    break;
                case 1:
                    if (containerInfo.signKeyLen == 0) {
                        SOF_AppLib.this.skf.SKF_SetLastError(3);
                        return Integer.valueOf(5);
                    }
                    break;
                default:
                    SOF_AppLib.this.skf.SKF_SetLastError(4);
                    return Integer.valueOf(6);
            }
            int _timeOut = this.timeOut + 2;
            switch (containerInfo.containerType) {
                case 1:
                    if (SOF_AppLib.this.skf.SKF_RSASignDataInteractiveCancel(SOF_AppLib.this.appId, containerId.intValue(), this.signFlag, this.hashAlgo) != 0) {
                        return Integer.valueOf(7);
                    }
                    while (SOF_AppLib.this.skf.SKF_RSASignDataInteractive(SOF_AppLib.this.appId, containerId.intValue(), this.signFlag, this.hashAlgo, this.timeOut, this.hash, this.hashLen, this.signature, this.signLen) != 0) {
                        if (SOF_AppLib.this.skf.SKF_GetLastError() != 28417) {
                            return Integer.valueOf(9);
                        }
                        try {
                            Thread.sleep(1000);
                            _timeOut--;
                            if (_timeOut <= 0) {
                                break;
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return Integer.valueOf(8);
                        }
                    }
                    return Integer.valueOf(0);
                case 2:
                    byte[] userId = new byte[0];
                    if (SOF_AppLib.this.skf.SKF_ECCSignDataInteractiveCancel(SOF_AppLib.this.appId, containerId.intValue(), 2, this.signFlag) != 0) {
                        return Integer.valueOf(10);
                    }
                    while (SOF_AppLib.this.skf.SKF_ECCSignDataInteractive(SOF_AppLib.this.appId, containerId.intValue(), 2, this.signFlag, this.timeOut, userId, 0, this.hash, this.hashLen, this.signature, this.signLen) != 0) {
                        if (SOF_AppLib.this.skf.SKF_GetLastError() != 28417) {
                            return Integer.valueOf(12);
                        }
                        try {
                            Thread.sleep(1000);
                            _timeOut--;
                            if (_timeOut <= 0) {
                                break;
                            }
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                            return Integer.valueOf(11);
                        }
                    }
                    return Integer.valueOf(0);
                default:
                    SOF_AppLib.this.skf.SKF_SetLastError(1);
                    return Integer.valueOf(13);
            }
            //SOF_AppLib.this.skf.SKF_SetLastError(21);
            //return Integer.valueOf(14);
        }

        protected void onPostExecute(Integer result) {
            if (result.intValue() == 0) {
                this.callback.onResult(0, Arrays.copyOf(this.signature, this.signLen[0]));
                return;
            }
            this.callback.onResult(SOF_AppLib.this.skf.SKF_GetLastError(), null);
        }
    }

    private SOF_AppLib() {
    }

    protected SOF_AppLib(SKFLib skf, String policyName, int appId) {
        LogUtil.m10d(TAG, "SOF_AppLib - create");
        this.skf = skf;
        this.appId = appId;
        this.appName = policyName;
    }

    public String SOF_GetAppName() {
        LogUtil.m10d(TAG, "SOF_GetAppName()");
        return this.appName;
    }

    public int SOF_Login(String userPin, int[] retryCount) {
        LogUtil.m10d(TAG, "SOF_Login()");
        if (TextUtils.isEmpty(userPin)) {
            this.skf.SKF_SetLastError(2);
            return 1;
        } else if (retryCount == null || retryCount.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 2;
        } else {
            byte[] random = new byte[8];
            if (this.skf.SKF_GenRandom(8, random) != 0) {
                return 3;
            }
            if (this.skf.SKF_Login(this.appId, random, userPin.getBytes(), retryCount) != 0) {
                return 4;
            }
            return 0;
        }
    }

    public int SOF_Login(int pinType, String userPin, int[] retryCount) {
        LogUtil.m10d(TAG, "SOF_Login()");
        if (TextUtils.isEmpty(userPin)) {
            this.skf.SKF_SetLastError(2);
            return 1;
        } else if (retryCount == null || retryCount.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 2;
        } else {
            byte[] random = new byte[8];
            if (this.skf.SKF_GenRandom(8, random) != 0) {
                return 3;
            }
            if (this.skf.SKF_Login(this.appId, pinType, random, userPin.getBytes(), retryCount) != 0) {
                return 4;
            }
            return 0;
        }
    }

    public int SOF_Logout() {
        LogUtil.m10d(TAG, "SOF_LogUtilout()");
        return this.skf.SKF_ClearSecureState(this.appId);
    }

    public int SOF_ChanegPassWd(String oldUpin, String newUpin, int[] retry) {
        LogUtil.m10d(TAG, "SOF_ChanegPassWd()");
        return SOF_ChanegPassWd(1, oldUpin, newUpin, retry);
    }

    public int SOF_ChanegPassWd(int pinType, String oldUpin, String newUpin, int[] retry) {
        LogUtil.m10d(TAG, "SOF_ChanegPassWd()");
        if (TextUtils.isEmpty(oldUpin)) {
            this.skf.SKF_SetLastError(2);
            return 1;
        } else if (TextUtils.isEmpty(newUpin)) {
            this.skf.SKF_SetLastError(2);
            return 2;
        } else if (retry == null || retry.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 3;
        } else {
            byte[] random = new byte[8];
            if (this.skf.SKF_GenRandom(8, random) != 0) {
                return 4;
            }
            if (this.skf.SKF_ChangePin(this.appId, random, oldUpin.getBytes(), newUpin.getBytes(), retry) != 0) {
                return 5;
            }
            return 0;
        }
    }

    public int SOF_EnumContainers(List<String> containers) {
        LogUtil.m10d(TAG, "SOF_EnumContainers()");
        if (containers == null) {
            this.skf.SKF_SetLastError(2);
            return 1;
        } else if (this.skf.SKF_EnumContainers(this.appId, containers) == 0) {
            return 0;
        } else {
            return 2;
        }
    }

    public int SOF_GetContainerInfo(String containerName, int[] containerType, int[] signKeyLen, int[] exchKeyLen, int[] signCertFlag, int[] exchCertFlag) {
        LogUtil.m10d(TAG, "SOF_GetContainerInfo() - containerName:" + containerName);
        if (TextUtils.isEmpty(containerName)) {
            this.skf.SKF_SetLastError(2);
            return 1;
        } else if (containerType == null || containerType.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 2;
        } else if (signKeyLen == null || signKeyLen.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 3;
        } else if (exchKeyLen == null || exchKeyLen.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 4;
        } else if (signCertFlag == null || signCertFlag.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 5;
        } else if (exchCertFlag == null || exchCertFlag.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 6;
        } else if (this.skf.SKF_GetContainerType(this.appId, containerName.getBytes(), containerType, signKeyLen, exchKeyLen, signCertFlag, exchCertFlag) != 0) {
            return 7;
        } else {
            this.ContainerInfos.put(containerName, new ContainerInfo(this.appId, containerName, containerType[0], signKeyLen[0], exchKeyLen[0], signCertFlag[0], exchCertFlag[0]));
            return 0;
        }
    }

    public int SOF_ExportUserCert(String containerName, int signFlag, byte[] cert, int[] certLen) {
        LogUtil.m10d(TAG, "SOF_ExportUserCert() - containerName:" + containerName + " signFlag:" + signFlag);
        if (TextUtils.isEmpty(containerName)) {
            this.skf.SKF_SetLastError(2);
            return 1;
        } else if (cert == null || cert.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 2;
        } else if (certLen == null || certLen.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 3;
        } else {
            Integer containerId = (Integer) this.Containers.get(containerName);
            if (containerId == null) {
                int[] id = new int[1];
                if (this.skf.SKF_OpenContainer(this.appId, containerName.getBytes(), id) != 0) {
                    return 4;
                }
                containerId = Integer.valueOf(id[0]);
                this.Containers.put(containerName, containerId);
            }
            return this.skf.SKF_ExportCertificate(this.appId, containerId.intValue(), signFlag, cert, certLen) != 0 ? 5 : 0;
        }
    }

    public int SOF_ExportPublicKeyBlob(String containerName, int signFlag, byte[] publicKeyBlob, int[] blobLen) {
        LogUtil.m10d(TAG, "SOF_ExportPublicKeyBlob() - containerName:" + containerName + " signFlag:" + signFlag);
        if (TextUtils.isEmpty(containerName)) {
            this.skf.SKF_SetLastError(2);
            return 1;
        } else if (publicKeyBlob == null || publicKeyBlob.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 2;
        } else if (blobLen == null || blobLen.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 3;
        } else {
            Integer containerId = (Integer) this.Containers.get(containerName);
            if (containerId == null) {
                int[] id = new int[1];
                if (this.skf.SKF_OpenContainer(this.appId, containerName.getBytes(), id) != 0) {
                    return 4;
                }
                containerId = Integer.valueOf(id[0]);
                this.Containers.put(containerName, containerId);
            }
            BaseContainerType containerInfo = (BaseContainerType) this.ContainerInfos.get(containerName);
            if (containerInfo == null) {
                int[] containerType = new int[1];
                int[] signKeyLen = new int[1];
                int[] exchKeyLen = new int[1];
                int[] signCertFlag = new int[1];
                int[] exchCertFlag = new int[1];
                if (this.skf.SKF_GetContainerType(this.appId, containerName.getBytes(), containerType, signKeyLen, exchKeyLen, signCertFlag, exchCertFlag) != 0) {
                    return 5;
                }
                containerInfo = new ContainerInfo(this.appId, containerName, containerType[0], signKeyLen[0], exchKeyLen[0], signCertFlag[0], exchCertFlag[0]);
                this.ContainerInfos.put(containerName, containerInfo);
            }
            switch (containerInfo.containerType) {
                case 0:
                    this.skf.SKF_SetLastError(3);
                    return 6;
                case 1:
                    if (this.skf.SKF_RSAExportPublicKey(this.appId, containerId.intValue(), signFlag, publicKeyBlob, blobLen) != 0) {
                        return 7;
                    }
                    break;
                case 2:
                    if (this.skf.SKF_ECCExportPublicKey(this.appId, containerId.intValue(), signFlag, publicKeyBlob, blobLen) != 0) {
                        return 8;
                    }
                    break;
                default:
                    this.skf.SKF_SetLastError(2);
                    return 9;
            }
            return 0;
        }
    }

    public int SOF_SignData(String containerName, int signFlag, int hashAlgo, byte[] inData, int inDataLen, byte[] signature, int[] signLen) {
        LogUtil.m10d(TAG, "SOF_SignData() - containerName:" + containerName + " signFlag:" + signFlag + " hashAlgo:" + hashAlgo);
        if (TextUtils.isEmpty(containerName)) {
            this.skf.SKF_SetLastError(2);
            return 1;
        } else if (inData == null || inData.length < inDataLen) {
            this.skf.SKF_SetLastError(2);
            return 2;
        } else if (signature == null || signature.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 3;
        } else if (signLen == null || signLen.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 4;
        } else {
            Integer containerId = (Integer) this.Containers.get(containerName);
            if (containerId == null) {
                int[] id = new int[1];
                if (this.skf.SKF_OpenContainer(this.appId, containerName.getBytes(), id) != 0) {
                    return 5;
                }
                containerId = Integer.valueOf(id[0]);
                this.Containers.put(containerName, containerId);
            }
            BaseContainerType containerInfo = (BaseContainerType) this.ContainerInfos.get(containerName);
            if (containerInfo == null) {
                int[] containerType = new int[1];
                int[] signKeyLen = new int[1];
                int[] exchKeyLen = new int[1];
                int[] signCertFlag = new int[1];
                int[] exchCertFlag = new int[1];
                if (this.skf.SKF_GetContainerType(this.appId, containerName.getBytes(), containerType, signKeyLen, exchKeyLen, signCertFlag, exchCertFlag) != 0) {
                    return 6;
                }
                containerInfo = new ContainerInfo(this.appId, containerName, containerType[0], signKeyLen[0], exchKeyLen[0], signCertFlag[0], exchCertFlag[0]);
                this.ContainerInfos.put(containerName, containerInfo);
            }
            if (containerInfo.containerType == 0) {
                this.skf.SKF_SetLastError(3);
                return 7;
            }
            switch (signFlag) {
                case 0:
                    if (containerInfo.exchKeyLen == 0) {
                        this.skf.SKF_SetLastError(3);
                        return 8;
                    }
                    break;
                case 1:
                    if (containerInfo.signKeyLen == 0) {
                        this.skf.SKF_SetLastError(3);
                        return 9;
                    }
                    break;
                default:
                    this.skf.SKF_SetLastError(4);
                    return 10;
            }
            switch (containerInfo.containerType) {
                case 1:
                    try {
                        if (this.skf.SKF_RSASignData(this.appId, containerId.intValue(), signFlag, hashAlgo, inData, inDataLen, signature, signLen) != 0) {
                            return 11;
                        }
                    } catch (Throwable e) {
                        LogUtil.m13e(TAG, "SKF_RSASignData()", e);
                        this.skf.SKF_SetLastError(1);
                        return 12;
                    }
                    break;
                case 2:
                    if (hashAlgo != 1) {
                        this.skf.SKF_SetLastError(20);
                        return 15;
                    }
                    int inputMode = 1;
                    if (inDataLen == 32) {
                        inputMode = 2;
                    }
                    if (this.skf.SKF_ECCSignData(this.appId, containerId.intValue(), inputMode, signFlag, new byte[0], inData, inDataLen, signature, signLen) != 0) {
                        return 13;
                    }
                    break;
                default:
                    this.skf.SKF_SetLastError(1);
                    return 14;
            }
            return 0;
        }
    }

    public int SOF_VerifySignedData(byte[] pubKeyBlob, int blobLen, int algoId, int hashAlgo, byte[] inData, int inDataLen, byte[] signature, int signLen) {
        LogUtil.m10d(TAG, "SOF_VerifySignedData()");
        if (pubKeyBlob == null || pubKeyBlob.length < blobLen) {
            this.skf.SKF_SetLastError(2);
            return 1;
        } else if (inData == null || inData.length < inDataLen) {
            this.skf.SKF_SetLastError(2);
            return 2;
        } else if (signature == null || signature.length < signLen) {
            this.skf.SKF_SetLastError(2);
            return 3;
        } else {
            switch (algoId) {
                case 65536:
                    try {
                        if (this.skf.SKF_ExtRSAVerify(pubKeyBlob, blobLen, hashAlgo, inData, inDataLen, signature, signLen) != 0) {
                            return 4;
                        }
                    } catch (NoSuchAlgorithmException e) {
                        LogUtil.m13e(TAG, "", e);
                        this.skf.SKF_SetLastError(1);
                        return 5;
                    }
                    break;
                case SOF_DeviceLib.SGD_SM2_1 /*131328*/:
                case SOF_DeviceLib.SGD_SM2_2 /*131584*/:
                case SOF_DeviceLib.SGD_SM2_3 /*132096*/:
                    if (this.skf.SKF_ECCVerify(pubKeyBlob, blobLen, inData, inDataLen, signature, signLen) != 0) {
                        return 6;
                    }
                    break;
                default:
                    this.skf.SKF_SetLastError(4);
                    return 7;
            }
            return 0;
        }
    }

    public int SOF_DigestData(int algoId, byte[] data, int dataLen, byte[] hashVal, int[] hashValLen) {
        LogUtil.m10d(TAG, "SOF_DigestData() - algoId:" + algoId);
        if (data == null || data.length < dataLen) {
            this.skf.SKF_SetLastError(2);
            return 1;
        } else if (hashVal == null || hashVal.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 2;
        } else if (hashValLen == null || hashValLen.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 3;
        } else if (this.skf.SKF_DigestInit(algoId) != 0) {
            return 4;
        } else {
            byte[] tmp;
            int nCount = dataLen / 1024;
            int left = dataLen % 1024;
            for (int i = 0; i < nCount; i++) {
                tmp = new byte[1024];
                System.arraycopy(data, i * 1024, tmp, 0, 1024);
                if (this.skf.SKF_DigestUpdate(tmp, 1024) != 0) {
                    return 5;
                }
            }
            tmp = new byte[left];
            System.arraycopy(data, nCount * 1024, tmp, 0, left);
            if (this.skf.SKF_DigestFinal(tmp, left, hashVal, hashValLen) != 0) {
                return 6;
            }
            return 0;
        }
    }

    public int SOF_EncryptData(int algoId, byte[] key, int keyLen, int padding, byte[] iv, int ivLen, byte[] input, int inputLen, byte[] output, int[] outputLen) {
        LogUtil.m10d(TAG, "SOF_EncryptData() - algoId:" + algoId);
        if (key == null || key.length < keyLen) {
            this.skf.SKF_SetLastError(2);
            return 1;
        } else if (input == null || input.length < inputLen) {
            this.skf.SKF_SetLastError(2);
            return 2;
        } else if (output == null || output.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 3;
        } else if (outputLen == null || outputLen.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 4;
        } else {
            int[] keyId = new int[1];
            if (this.skf.SKF_SetSymtricKey(this.appId, SupportMenu.USER_MASK, algoId, key, keyLen, keyId) != 0) {
                return 5;
            }
            if (this.skf.SKF_EncryptInit(this.appId, SupportMenu.USER_MASK, keyId[0], iv, ivLen, padding) != 0) {
                return 6;
            }
            byte[] tmp;
            byte[] outputTmp;
            int[] outputLenTmp;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int nCount = inputLen / 1024;
            int left = inputLen % 1024;
            for (int i = 0; i < nCount; i++) {
                tmp = new byte[1024];
                System.arraycopy(input, i * 1024, tmp, 0, 1024);
                outputTmp = new byte[2048];
                outputLenTmp = new int[1];
                if (this.skf.SKF_EncryptUpdate(this.appId, SupportMenu.USER_MASK, keyId[0], tmp, 1024, outputTmp, outputLenTmp) != 0) {
                    return 7;
                }
                bos.write(outputTmp, 0, outputLenTmp[0]);
            }
            tmp = new byte[left];
            System.arraycopy(input, nCount * 1024, tmp, 0, left);
            outputTmp = new byte[2048];
            outputLenTmp = new int[1];
            if (this.skf.SKF_EncryptFinal(this.appId, SupportMenu.USER_MASK, keyId[0], tmp, left, outputTmp, outputLenTmp) != 0) {
                return 8;
            }
            bos.write(outputTmp, 0, outputLenTmp[0]);
            if (output.length < bos.size()) {
                this.skf.SKF_SetLastError(28161);
                return 9;
            }
            if (padding == 0) {
                outputLen[0] = inputLen;
            } else {
                outputLen[0] = bos.size();
            }
            System.arraycopy(bos.toByteArray(), 0, output, 0, bos.size());
            if (this.skf.SKF_DestroySymtricKey(this.appId, SupportMenu.USER_MASK, keyId[0]) != 0) {
                return 10;
            }
            return 0;
        }
    }

    public int SOF_DecryptData(int algoId, byte[] key, int keyLen, int padding, byte[] iv, int ivLen, byte[] input, int inputLen, byte[] output, int[] outputLen) {
        LogUtil.m10d(TAG, "SOF_DecryptData() - algoId:" + algoId);
        if (key == null || key.length < keyLen) {
            this.skf.SKF_SetLastError(2);
            return 1;
        } else if (input == null || input.length < inputLen) {
            this.skf.SKF_SetLastError(2);
            return 2;
        } else if (output == null || output.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 3;
        } else if (outputLen == null || outputLen.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 4;
        } else {
            int[] keyId = new int[1];
            if (this.skf.SKF_SetSymtricKey(this.appId, SupportMenu.USER_MASK, algoId, key, keyLen, keyId) != 0) {
                return 5;
            }
            if (this.skf.SKF_DecryptInit(this.appId, SupportMenu.USER_MASK, keyId[0], iv, ivLen, padding) != 0) {
                return 6;
            }
            byte[] tmp;
            byte[] outputTmp;
            int[] outputLenTmp;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int nCount = inputLen / 1024;
            int left = inputLen % 1024;
            for (int i = 0; i < nCount; i++) {
                tmp = new byte[1024];
                System.arraycopy(input, i * 1024, tmp, 0, 1024);
                outputTmp = new byte[2048];
                outputLenTmp = new int[1];
                if (this.skf.SKF_DecryptUpdate(this.appId, SupportMenu.USER_MASK, keyId[0], tmp, 1024, outputTmp, outputLenTmp) != 0) {
                    return 7;
                }
                bos.write(outputTmp, 0, outputLenTmp[0]);
            }
            tmp = new byte[left];
            System.arraycopy(input, nCount * 1024, tmp, 0, left);
            outputTmp = new byte[2048];
            outputLenTmp = new int[1];
            if (this.skf.SKF_DecryptFinal(this.appId, SupportMenu.USER_MASK, keyId[0], tmp, left, outputTmp, outputLenTmp) != 0) {
                return 8;
            }
            bos.write(outputTmp, 0, outputLenTmp[0]);
            if (output.length < bos.size()) {
                this.skf.SKF_SetLastError(28161);
                return 11;
            }
            outputLen[0] = bos.size();
            System.arraycopy(bos.toByteArray(), 0, output, 0, bos.size());
            if (this.skf.SKF_DestroySymtricKey(this.appId, SupportMenu.USER_MASK, keyId[0]) != 0) {
                return 12;
            }
            return 0;
        }
    }

    public int SOF_EnumFiles(List<String> files) {
        LogUtil.m10d(TAG, "SOF_EnumFiles()");
        if (files == null) {
            this.skf.SKF_SetLastError(2);
            return 1;
        } else if (this.skf.SKF_EnumFiles(this.appId, files) == 0) {
            return 0;
        } else {
            return 2;
        }
    }

    public int SOF_GetFileInfo(String fileName, int[] fileSize, int[] readRights, int[] writeRights) {
        LogUtil.m10d(TAG, "SOF_GetFileInfo() - fileName:" + fileName);
        if (TextUtils.isEmpty(fileName)) {
            this.skf.SKF_SetLastError(2);
            return 1;
        } else if (fileSize == null || fileSize.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 2;
        } else if (readRights == null || readRights.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 3;
        } else if (writeRights == null || writeRights.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 4;
        } else if (this.skf.SKF_GetFileInfo(this.appId, fileName.getBytes(), fileSize, readRights, writeRights) != 0) {
            return 5;
        } else {
            return 0;
        }
    }

    public int SOF_ReadFile(String fileName, int offset, int readLen, byte[] output) {
        LogUtil.m10d(TAG, "SOF_ReadFile() - fileName:" + fileName);
        if (TextUtils.isEmpty(fileName)) {
            this.skf.SKF_SetLastError(2);
            return 1;
        } else if (output == null || output.length < readLen) {
            this.skf.SKF_SetLastError(2);
            return 2;
        } else {
            byte[] outputTmp;
            int[] outputLenTmp;
            int count = readLen / 1024;
            int left = readLen % 1024;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            for (int i = 0; i < count; i++) {
                outputTmp = new byte[2048];
                outputLenTmp = new int[1];
                if (this.skf.SKF_ReadFile(this.appId, fileName.getBytes(), (i * 1024) + offset, 1024, outputTmp, outputLenTmp) != 0) {
                    return 3;
                }
                bos.write(outputTmp, 0, outputLenTmp[0]);
            }
            if (left > 0) {
                outputTmp = new byte[2048];
                outputLenTmp = new int[1];
                if (this.skf.SKF_ReadFile(this.appId, fileName.getBytes(), (count * 1024) + offset, left, outputTmp, outputLenTmp) != 0) {
                    return 4;
                }
                bos.write(outputTmp, 0, outputLenTmp[0]);
            }
            System.arraycopy(bos.toByteArray(), 0, output, 0, bos.size());
            return 0;
        }
    }

    public int SOF_WriteFile(String fileName, int offset, byte[] input, int length) {
        LogUtil.m10d(TAG, "SOF_WriteFile() - fileName:" + fileName + " length:" + length);
        if (TextUtils.isEmpty(fileName)) {
            this.skf.SKF_SetLastError(2);
            return 1;
        } else if (input == null || input.length < length) {
            this.skf.SKF_SetLastError(2);
            return 2;
        } else {
            byte[] buff;
            int count = length / 1024;
            int left = length % 1024;
            for (int i = 0; i < count; i++) {
                buff = new byte[1024];
                System.arraycopy(input, i * 1024, buff, 0, 1024);
                if (this.skf.SKF_WriteFile(this.appId, fileName.getBytes(), (i * 1024) + offset, buff, 1024) != 0) {
                    return 3;
                }
            }
            if (left > 0) {
                buff = new byte[left];
                System.arraycopy(input, count * 1024, buff, 0, left);
                if (this.skf.SKF_WriteFile(this.appId, fileName.getBytes(), (count * 1024) + offset, buff, left) != 0) {
                    return 4;
                }
            }
            return 0;
        }
    }

    public int SOF_ExtPublicEncrypt(byte[] pubKeyBlob, int blobLen, int algoId, byte[] input, int inputLen, byte[] output, int[] outputLen) {
        LogUtil.m10d(TAG, "SOF_ExtPublicEncrypt()");
        if (pubKeyBlob == null || pubKeyBlob.length < blobLen) {
            this.skf.SKF_SetLastError(2);
            return 1;
        } else if (input == null || input.length < inputLen) {
            this.skf.SKF_SetLastError(2);
            return 2;
        } else if (output == null || output.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 3;
        } else if (outputLen == null || outputLen.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 4;
        } else {
            switch (algoId) {
                case 65536:
                    if (this.skf.SKF_ExtRSAEncrypt(pubKeyBlob, blobLen, input, inputLen, output, outputLen) != 0) {
                        return 5;
                    }
                    break;
                case SOF_DeviceLib.SGD_SM2_1 /*131328*/:
                case SOF_DeviceLib.SGD_SM2_2 /*131584*/:
                case SOF_DeviceLib.SGD_SM2_3 /*132096*/:
                    if (inputLen > 128) {
                        this.skf.SKF_SetLastError(2);
                        return 6;
                    } else if (this.skf.SKF_ExtECCEncrypt(pubKeyBlob, blobLen, input, inputLen, output, outputLen) != 0) {
                        return 7;
                    }
                    break;
                default:
                    this.skf.SKF_SetLastError(4);
                    return 8;
            }
            return 0;
        }
    }

    public int SOF_PublicEncrypt(String containerName, int signFlag, byte[] input, int inputLen, byte[] output, int[] outputLen) {
        LogUtil.m10d(TAG, "SOF_PrivateDecrypt() - containerName:" + containerName);
        if (TextUtils.isEmpty(containerName)) {
            this.skf.SKF_SetLastError(2);
            return 1;
        } else if (input == null || input.length < inputLen) {
            this.skf.SKF_SetLastError(2);
            return 2;
        } else if (output == null || output.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 3;
        } else if (outputLen == null || outputLen.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 4;
        } else {
            Integer containerId = (Integer) this.Containers.get(containerName);
            if (containerId == null) {
                int[] id = new int[1];
                if (this.skf.SKF_OpenContainer(this.appId, containerName.getBytes(), id) != 0) {
                    return 5;
                }
                containerId = Integer.valueOf(id[0]);
                this.Containers.put(containerName, containerId);
            }
            BaseContainerType containerInfo = (BaseContainerType) this.ContainerInfos.get(containerName);
            if (containerInfo == null) {
                int[] containerType = new int[1];
                int[] signKeyLen = new int[1];
                int[] exchKeyLen = new int[1];
                int[] signCertFlag = new int[1];
                int[] exchCertFlag = new int[1];
                if (this.skf.SKF_GetContainerType(this.appId, containerName.getBytes(), containerType, signKeyLen, exchKeyLen, signCertFlag, exchCertFlag) != 0) {
                    return 6;
                }
                containerInfo = new ContainerInfo(this.appId, containerName, containerType[0], signKeyLen[0], exchKeyLen[0], signCertFlag[0], exchCertFlag[0]);
                this.ContainerInfos.put(containerName, containerInfo);
            }
            if (containerInfo.containerType == 0) {
                this.skf.SKF_SetLastError(3);
                return 7;
            }
            switch (signFlag) {
                case 0:
                    if (containerInfo.exchKeyLen == 0) {
                        this.skf.SKF_SetLastError(3);
                        return 8;
                    }
                    break;
                case 1:
                    if (containerInfo.signKeyLen == 0) {
                        this.skf.SKF_SetLastError(3);
                        return 9;
                    }
                    break;
                default:
                    this.skf.SKF_SetLastError(1);
                    return 10;
            }
            switch (containerInfo.containerType) {
                case 1:
                    if (this.skf.SKF_RSAEncrypt(this.appId, containerId.intValue(), signFlag, input, inputLen, output, outputLen) != 0) {
                        return 11;
                    }
                    break;
                case 2:
                    if (inputLen > 128) {
                        this.skf.SKF_SetLastError(2);
                        return 12;
                    }
                    byte[] publicKeyBlob = new byte[1024];
                    int[] blobLen = new int[1];
                    if (this.skf.SKF_ECCExportPublicKey(this.appId, containerId.intValue(), signFlag, publicKeyBlob, blobLen) != 0) {
                        return 13;
                    }
                    if (this.skf.SKF_ExtECCEncrypt(publicKeyBlob, blobLen[0], input, inputLen, output, outputLen) != 0) {
                        return 14;
                    }
                    break;
                default:
                    this.skf.SKF_SetLastError(1);
                    return 15;
            }
            return 0;
        }
    }

    public int SOF_PrivateDecrypt(String containerName, int signFlag, byte[] input, int inputLen, byte[] output, int[] outputLen) {
        LogUtil.m10d(TAG, "SOF_PrivateDecrypt() - containerName:" + containerName);
        if (TextUtils.isEmpty(containerName)) {
            this.skf.SKF_SetLastError(2);
            return 1;
        } else if (input == null || input.length < inputLen) {
            this.skf.SKF_SetLastError(2);
            return 2;
        } else if (output == null || output.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 3;
        } else if (outputLen == null || outputLen.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 4;
        } else {
            Integer containerId = (Integer) this.Containers.get(containerName);
            if (containerId == null) {
                int[] id = new int[1];
                if (this.skf.SKF_OpenContainer(this.appId, containerName.getBytes(), id) != 0) {
                    return 5;
                }
                containerId = Integer.valueOf(id[0]);
                this.Containers.put(containerName, containerId);
            }
            BaseContainerType containerInfo = (BaseContainerType) this.ContainerInfos.get(containerName);
            if (containerInfo == null) {
                int[] containerType = new int[1];
                int[] signKeyLen = new int[1];
                int[] exchKeyLen = new int[1];
                int[] signCertFlag = new int[1];
                int[] exchCertFlag = new int[1];
                if (this.skf.SKF_GetContainerType(this.appId, containerName.getBytes(), containerType, signKeyLen, exchKeyLen, signCertFlag, exchCertFlag) != 0) {
                    return 6;
                }
                containerInfo = new ContainerInfo(this.appId, containerName, containerType[0], signKeyLen[0], exchKeyLen[0], signCertFlag[0], exchCertFlag[0]);
                this.ContainerInfos.put(containerName, containerInfo);
            }
            if (containerInfo.containerType == 0) {
                this.skf.SKF_SetLastError(3);
                return 7;
            }
            switch (signFlag) {
                case 0:
                    if (containerInfo.exchKeyLen == 0) {
                        this.skf.SKF_SetLastError(3);
                        return 8;
                    }
                    break;
                case 1:
                    if (containerInfo.signKeyLen == 0) {
                        this.skf.SKF_SetLastError(3);
                        return 9;
                    }
                    break;
                default:
                    this.skf.SKF_SetLastError(1);
                    return 10;
            }
            switch (containerInfo.containerType) {
                case 1:
                    if (this.skf.SKF_RSADecrypt(this.appId, containerId.intValue(), signFlag, input, inputLen, output, outputLen) != 0) {
                        return 11;
                    }
                    break;
                case 2:
                    if (this.skf.SKF_ECCPrivateDecrypt(this.appId, containerId.intValue(), signFlag, input, inputLen, output, outputLen) != 0) {
                        return 12;
                    }
                    break;
                default:
                    this.skf.SKF_SetLastError(1);
                    return 13;
            }
            return 0;
        }
    }

    public int SOF_CreaterFile(String fileName, int fileSize, int readRights, int writeRights) {
        LogUtil.m10d(TAG, "SOF_CreaterFile() - fileName:" + fileName);
        if (TextUtils.isEmpty(fileName)) {
            this.skf.SKF_SetLastError(2);
            return 1;
        } else if (this.skf.SKF_CreaterFile(this.appId, fileName.getBytes(), fileSize, readRights, writeRights) != 0) {
            return 2;
        } else {
            return 0;
        }
    }

    public int SOF_DeleteFile(String fileName) {
        LogUtil.m10d(TAG, "SOF_DeleteFile() - fileName:" + fileName);
        if (TextUtils.isEmpty(fileName)) {
            this.skf.SKF_SetLastError(2);
            return 1;
        } else if (this.skf.SKF_DeleteFile(this.appId, fileName.getBytes()) == 0) {
            return 0;
        } else {
            return 2;
        }
    }

    public int SOF_GetPINInfo(int pinType, int[] maxRetryCount, int[] retryCount, int[] defaultPin) {
        LogUtil.m10d(TAG, "SOF_GetPINInfo()");
        return this.skf.SKF_GetPINInfo(this.appId, pinType, maxRetryCount, retryCount, defaultPin);
    }

    public int SOF_UnblockPIN(String soPin, String userPin, int[] retryCount) {
        LogUtil.m10d(TAG, "SOF_UnblockPIN()");
        if (TextUtils.isEmpty(soPin)) {
            this.skf.SKF_SetLastError(2);
            return 1;
        } else if (TextUtils.isEmpty(userPin)) {
            this.skf.SKF_SetLastError(2);
            return 2;
        } else if (retryCount == null || retryCount.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 3;
        } else {
            byte[] random = new byte[8];
            if (this.skf.SKF_GenRandom(8, random) != 0) {
                return 4;
            }
            if (this.skf.SKF_UnblockPIN(this.appId, random, soPin.getBytes(), userPin.getBytes(), retryCount) != 0) {
                return 5;
            }
            return 0;
        }
    }

    public int SOF_CreateContainer(String containerName) {
        LogUtil.m10d(TAG, "SOF_CreateContainer() - containerName:" + containerName);
        if (TextUtils.isEmpty(containerName)) {
            this.skf.SKF_SetLastError(2);
            return 1;
        }
        int[] containerId = new int[1];
        if (this.skf.SKF_CreateContainer(this.appId, containerName.getBytes(), containerId) != 0) {
            return 2;
        }
        this.Containers.put(containerName, Integer.valueOf(containerId[0]));
        return 0;
    }

    public int SOF_DeleteContainer(String containerName) {
        LogUtil.m10d(TAG, "SOF_DeleteContainer() - containerName:" + containerName);
        if (TextUtils.isEmpty(containerName)) {
            this.skf.SKF_SetLastError(2);
            return 1;
        }
        this.Containers.remove(containerName);
        if (this.skf.SKF_DeleteContainer(this.appId, containerName.getBytes()) == 0) {
            return 0;
        }
        return 2;
    }

    public int SOF_ImportCertificate(String containerName, int signFlag, byte[] cert, int certLen) {
        LogUtil.m10d(TAG, "SOF_ImportCertificate() - containerName:" + containerName);
        if (TextUtils.isEmpty(containerName)) {
            this.skf.SKF_SetLastError(2);
            return 1;
        } else if (cert == null || cert.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 2;
        } else if (cert.length < certLen) {
            this.skf.SKF_SetLastError(2);
            return 3;
        } else {
            Integer containerId = (Integer) this.Containers.get(containerName);
            if (containerId == null) {
                int[] id = new int[1];
                if (this.skf.SKF_OpenContainer(this.appId, containerName.getBytes(), id) != 0) {
                    return 4;
                }
                containerId = Integer.valueOf(id[0]);
                this.Containers.put(containerName, containerId);
            }
            if (this.skf.SKF_ImportCertificate(this.appId, containerId.intValue(), signFlag, 0, cert, certLen) != 0) {
                return 5;
            }
            byte[] tmp;
            int nCount = certLen / 1024;
            int left = certLen % 1024;
            for (int i = 0; i < nCount; i++) {
                tmp = new byte[1024];
                System.arraycopy(cert, i * 1024, tmp, 0, 1024);
                if (this.skf.SKF_ImportCertificate(this.appId, containerId.intValue(), signFlag, 1, tmp, 1024) != 0) {
                    return 6;
                }
            }
            if (left > 0) {
                tmp = new byte[left];
                System.arraycopy(cert, nCount * 1024, tmp, 0, left);
                if (this.skf.SKF_ImportCertificate(this.appId, containerId.intValue(), signFlag, 2, tmp, left) != 0) {
                    return 7;
                }
            }
            return 0;
        }
    }

    public int SOF_GenKeyPair(String containerName, int signFlag, int algoId, int bitLen, byte[] pubKey, int[] keyLen) {
        LogUtil.m10d(TAG, "SOF_GenRSAKeyPair() - containerName:" + containerName);
        if (TextUtils.isEmpty(containerName)) {
            this.skf.SKF_SetLastError(2);
            return 1;
        }
        Integer containerId = (Integer) this.Containers.get(containerName);
        if (containerId == null) {
            int[] id = new int[1];
            if (this.skf.SKF_OpenContainer(this.appId, containerName.getBytes(), id) != 0) {
                return 2;
            }
            containerId = Integer.valueOf(id[0]);
            this.Containers.put(containerName, containerId);
        }
        switch (algoId) {
            case 65536:
                if (this.skf.SKF_GenRSAKeyPair(this.appId, containerId.intValue(), signFlag, bitLen, pubKey, keyLen) != 0) {
                    return 3;
                }
                break;
            case SOF_DeviceLib.SGD_SM2_1 /*131328*/:
            case SOF_DeviceLib.SGD_SM2_2 /*131584*/:
            case SOF_DeviceLib.SGD_SM2_3 /*132096*/:
                if (this.skf.SKF_GenECCKeyPair(this.appId, containerId.intValue(), signFlag, bitLen, pubKey, keyLen) != 0) {
                    return 4;
                }
                break;
            default:
                this.skf.SKF_SetLastError(4);
                return 5;
        }
        this.ContainerInfos.remove(containerName);
        return 0;
    }

    public int SOF_ImportRSAKeyPair(String containerName, int algId, int bitLen, byte[] wrappedKey, int wrappedKeyLen, byte[] encryptedData, int encryptedDataLen) {
        LogUtil.m10d(TAG, "SOF_ImportRSAKeyPair() - containerName:" + containerName);
        if (TextUtils.isEmpty(containerName)) {
            this.skf.SKF_SetLastError(2);
            return 1;
        }
        Integer containerId = (Integer) this.Containers.get(containerName);
        if (containerId == null) {
            int[] id = new int[1];
            if (this.skf.SKF_OpenContainer(this.appId, containerName.getBytes(), id) != 0) {
                return 2;
            }
            containerId = Integer.valueOf(id[0]);
            this.Containers.put(containerName, containerId);
        }
        if (this.skf.SKF_ImportRSAKeyPair(this.appId, containerId.intValue(), algId, 0, bitLen, wrappedKey, wrappedKeyLen, encryptedData, encryptedDataLen) != 0) {
            return 3;
        }
        if (this.skf.SKF_ImportRSAKeyPair(this.appId, containerId.intValue(), algId, 1, bitLen, wrappedKey, wrappedKeyLen, encryptedData, 1024) != 0) {
            return 4;
        }
        if (this.skf.SKF_ImportRSAKeyPair(this.appId, containerId.intValue(), algId, 2, bitLen, wrappedKey, wrappedKeyLen, Arrays.copyOfRange(encryptedData, 1024, encryptedData.length), encryptedDataLen - 1024) != 0) {
            return 5;
        }
        this.ContainerInfos.remove(containerName);
        return 0;
    }

    public int SOF_ImportECCKeyPair(String containerName, byte[] envelopedKeyBlob, int envelopedKeyBlobLen) {
        LogUtil.m10d(TAG, "SOF_ImportECCKeyPair() - containerName:" + containerName);
        if (TextUtils.isEmpty(containerName)) {
            this.skf.SKF_SetLastError(2);
            return 1;
        }
        Integer containerId = (Integer) this.Containers.get(containerName);
        if (containerId == null) {
            int[] id = new int[1];
            if (this.skf.SKF_OpenContainer(this.appId, containerName.getBytes(), id) != 0) {
                return 2;
            }
            containerId = Integer.valueOf(id[0]);
            this.Containers.put(containerName, containerId);
        }
        if (this.skf.SKF_ImportECCKeyPair(this.appId, containerId.intValue(), envelopedKeyBlob, envelopedKeyBlobLen) != 0) {
            return 3;
        }
        this.ContainerInfos.remove(containerName);
        return 0;
    }

    public int SOF_ImportExtRSAKeyPair(String containerName, int signFlag, byte[] blob, int blobLen) {
        LogUtil.m10d(TAG, "SOF_ImportExtRSAKeyPair() - containerName:" + containerName);
        if (TextUtils.isEmpty(containerName)) {
            this.skf.SKF_SetLastError(2);
            return 1;
        } else if (blob == null || blob.length < blobLen) {
            this.skf.SKF_SetLastError(2);
            return 2;
        } else {
            Integer containerId = (Integer) this.Containers.get(containerName);
            if (containerId == null) {
                int[] id = new int[1];
                if (this.skf.SKF_OpenContainer(this.appId, containerName.getBytes(), id) != 0) {
                    return 3;
                }
                containerId = Integer.valueOf(id[0]);
                this.Containers.put(containerName, containerId);
            }
            if (this.skf.SKF_ImportExtRSAKeyPair(this.appId, containerId.intValue(), signFlag, 0, blob, blobLen) != 0) {
                return 4;
            }
            byte[] tmp;
            int nCount = blobLen / 1024;
            int left = blobLen % 1024;
            for (int i = 0; i < nCount; i++) {
                tmp = new byte[1024];
                System.arraycopy(blob, i * 1024, tmp, 0, 1024);
                if (this.skf.SKF_ImportExtRSAKeyPair(this.appId, containerId.intValue(), signFlag, 1, tmp, 1024) != 0) {
                    return 5;
                }
            }
            tmp = new byte[left];
            System.arraycopy(blob, nCount * 1024, tmp, 0, left);
            if (this.skf.SKF_ImportExtRSAKeyPair(this.appId, containerId.intValue(), signFlag, 2, tmp, left) != 0) {
                return 6;
            }
            this.ContainerInfos.remove(containerName);
            return 0;
        }
    }

    public int SOF_SM3Digest(byte[] data, int length, byte[] digest, int[] digestLen) {
        LogUtil.m10d(TAG, "SOF_SM3Digest()");
        return this.skf.SKF_SM3Digest(data, length, digest, digestLen);
    }

    public int SOF_GetLastError() {
        LogUtil.m10d(TAG, "SOF_GetLastError()");
        return this.skf.SKF_GetLastError();
    }

    public int SOF_Public(String containerName, int signFlag, byte[] input, int inputLen, byte[] output, int[] outputLen) {
        LogUtil.m10d(TAG, "SOF_Public() - containerName:" + containerName);
        if (TextUtils.isEmpty(containerName)) {
            this.skf.SKF_SetLastError(2);
            return 1;
        } else if (input == null || input.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 2;
        } else if (input.length < inputLen) {
            this.skf.SKF_SetLastError(2);
            return 3;
        } else {
            Integer containerId = (Integer) this.Containers.get(containerName);
            if (containerId == null) {
                int[] id = new int[1];
                if (this.skf.SKF_OpenContainer(this.appId, containerName.getBytes(), id) != 0) {
                    return 4;
                }
                containerId = Integer.valueOf(id[0]);
                this.Containers.put(containerName, containerId);
            }
            return this.skf.SKF_RSAPublic(this.appId, containerId.intValue(), signFlag, input, inputLen, output, outputLen);
        }
    }

    public int SOF_Private(String containerName, int signFlag, byte[] input, int inputLen, byte[] output, int[] outputLen) {
        LogUtil.m10d(TAG, "SOF_Private() - containerName:" + containerName);
        if (TextUtils.isEmpty(containerName)) {
            this.skf.SKF_SetLastError(2);
            return 1;
        } else if (input == null || input.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 2;
        } else if (input.length < inputLen) {
            this.skf.SKF_SetLastError(2);
            return 3;
        } else {
            Integer containerId = (Integer) this.Containers.get(containerName);
            if (containerId == null) {
                int[] id = new int[1];
                if (this.skf.SKF_OpenContainer(this.appId, containerName.getBytes(), id) != 0) {
                    return 4;
                }
                containerId = Integer.valueOf(id[0]);
                this.Containers.put(containerName, containerId);
            }
            return this.skf.SKF_RSAPrivate(this.appId, containerId.intValue(), signFlag, input, inputLen, output, outputLen);
        }
    }

    public int SOF_SetSymtricKey(int algoId, byte[] key, int keyLen, int[] keyId) {
        LogUtil.m10d(TAG, "SOF_SetSymtricKey() - algoId:" + algoId);
        if (key != null && key.length >= keyLen) {
            return this.skf.SKF_SetSymtricKey(this.appId, SupportMenu.USER_MASK, algoId, key, keyLen, keyId);
        }
        this.skf.SKF_SetLastError(2);
        return 1;
    }

    public int SOF_DestroySymtricKey(int keyId) {
        LogUtil.m10d(TAG, "SOF_DestroySymtricKey() - keyId:" + keyId);
        return this.skf.SKF_DestroySymtricKey(this.appId, SupportMenu.USER_MASK, keyId);
    }

    public int SOF_EncryptInit(int keyId, byte[] iv, int ivLen, int paddingType) {
        LogUtil.m10d(TAG, "SOF_EncryptInit() - keyId:" + keyId);
        this._bos = new ByteArrayOutputStream();
        this._bos.reset();
        this._paddingType = paddingType;
        return this.skf.SKF_EncryptInit(this.appId, SupportMenu.USER_MASK, keyId, iv, ivLen, paddingType);
    }

    public int SOF_EncryptUpdate(int keyId, byte[] input, int inputLen, byte[] output, int[] outputLen) {
        LogUtil.m10d(TAG, "SOF_EncryptUpdate() - keyId:" + keyId);
        this._bos.write(input, 0, inputLen);
        ByteArrayInputStream bis = new ByteArrayInputStream(this._bos.toByteArray());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.reset();
        this._bos.reset();
        byte[] tmp = new byte[1024];
        while (true) {
            int len = bis.read(tmp, 0, 1024);
            if (len == -1) {
                break;
            } else if (len == 1024) {
                byte[] outputTmp = new byte[2048];
                int[] outputLenTmp = new int[1];
                if (this.skf.SKF_EncryptUpdate(this.appId, SupportMenu.USER_MASK, keyId, tmp, 1024, outputTmp, outputLenTmp) != 0) {
                    return 1;
                }
                bos.write(outputTmp, 0, outputLenTmp[0]);
            } else {
                this._bos.write(tmp, 0, len);
            }
        }
        if (output.length < bos.size()) {
            this.skf.SKF_SetLastError(28161);
            return 2;
        }
        outputLen[0] = bos.size();
        System.arraycopy(bos.toByteArray(), 0, output, 0, bos.size());
        return 0;
    }

    public int SOF_EncryptFinal(int keyId, byte[] input, int inputLen, byte[] output, int[] outputLen) {
        byte[] tmp;
        byte[] outputTmp;
        int[] outputLenTmp;
        LogUtil.m10d(TAG, "SOF_EncryptFinal() - keyId:" + keyId);
        this._bos.write(input, 0, inputLen);
        byte[] buff = this._bos.toByteArray();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.reset();
        int nCount = buff.length / 1024;
        int left = buff.length % 1024;
        for (int i = 0; i < nCount; i++) {
            tmp = new byte[1024];
            System.arraycopy(buff, i * 1024, tmp, 0, 1024);
            outputTmp = new byte[2048];
            outputLenTmp = new int[1];
            if (this.skf.SKF_EncryptUpdate(this.appId, SupportMenu.USER_MASK, keyId, tmp, 1024, outputTmp, outputLenTmp) != 0) {
                return 7;
            }
            bos.write(outputTmp, 0, outputLenTmp[0]);
        }
        tmp = new byte[left];
        System.arraycopy(buff, nCount * 1024, tmp, 0, left);
        outputTmp = new byte[2048];
        outputLenTmp = new int[1];
        if (this.skf.SKF_EncryptFinal(this.appId, SupportMenu.USER_MASK, keyId, tmp, left, outputTmp, outputLenTmp) != 0) {
            return 8;
        }
        bos.write(outputTmp, 0, outputLenTmp[0]);
        if (output.length < bos.size()) {
            this.skf.SKF_SetLastError(28161);
            return 9;
        }
        if (this._paddingType == 0 && left != 0) {
            outputLen[0] = left;
        } else if (this._paddingType == 0 && nCount != 0 && left == 0) {
            outputLen[0] = 1024;
        } else {
            outputLen[0] = bos.size();
        }
        System.arraycopy(bos.toByteArray(), 0, output, 0, bos.size());
        return 0;
    }

    public int SOF_DecryptInit(int keyId, byte[] iv, int ivLen, int paddingType) {
        LogUtil.m10d(TAG, "SOF_DecryptInit() - keyId:" + keyId);
        this._bos = new ByteArrayOutputStream();
        this._bos.reset();
        return this.skf.SKF_DecryptInit(this.appId, SupportMenu.USER_MASK, keyId, iv, ivLen, paddingType);
    }

    public int SOF_DecryptUpdate(int keyId, byte[] input, int inputLen, byte[] output, int[] outputLen) {
        LogUtil.m10d(TAG, "SOF_DecryptUpdate() - keyId:" + keyId);
        this._bos.write(input, 0, inputLen);
        ByteArrayInputStream bis = new ByteArrayInputStream(this._bos.toByteArray());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.reset();
        this._bos.reset();
        byte[] tmp = new byte[1024];
        while (true) {
            int len = bis.read(tmp, 0, 1024);
            if (len == -1) {
                break;
            } else if (len == 1024) {
                byte[] outputTmp = new byte[2048];
                int[] outputLenTmp = new int[1];
                if (this.skf.SKF_DecryptUpdate(this.appId, SupportMenu.USER_MASK, keyId, tmp, 1024, outputTmp, outputLenTmp) != 0) {
                    return 1;
                }
                bos.write(outputTmp, 0, outputLenTmp[0]);
            } else {
                this._bos.write(tmp, 0, len);
            }
        }
        if (output.length < bos.size()) {
            this.skf.SKF_SetLastError(28161);
            return 2;
        }
        outputLen[0] = bos.size();
        System.arraycopy(bos.toByteArray(), 0, output, 0, bos.size());
        return 0;
    }

    public int SOF_DecryptFinal(int keyId, byte[] input, int inputLen, byte[] output, int[] outputLen) {
        byte[] tmp;
        byte[] outputTmp;
        int[] outputLenTmp;
        LogUtil.m10d(TAG, "SOF_DecryptFinal() - keyId:" + keyId);
        this._bos.write(input, 0, inputLen);
        byte[] buff = this._bos.toByteArray();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.reset();
        int nCount = buff.length / 1024;
        int left = buff.length % 1024;
        for (int i = 0; i < nCount; i++) {
            tmp = new byte[1024];
            System.arraycopy(buff, i * 1024, tmp, 0, 1024);
            outputTmp = new byte[2048];
            outputLenTmp = new int[1];
            if (this.skf.SKF_DecryptUpdate(this.appId, SupportMenu.USER_MASK, keyId, tmp, 1024, outputTmp, outputLenTmp) != 0) {
                return 7;
            }
            bos.write(outputTmp, 0, outputLenTmp[0]);
        }
        tmp = new byte[left];
        System.arraycopy(buff, nCount * 1024, tmp, 0, left);
        outputTmp = new byte[2048];
        outputLenTmp = new int[1];
        if (this.skf.SKF_DecryptFinal(this.appId, SupportMenu.USER_MASK, keyId, tmp, left, outputTmp, outputLenTmp) != 0) {
            return 8;
        }
        bos.write(outputTmp, 0, outputLenTmp[0]);
        if (output.length < bos.size()) {
            this.skf.SKF_SetLastError(28161);
            return 11;
        }
        outputLen[0] = bos.size();
        System.arraycopy(bos.toByteArray(), 0, output, 0, bos.size());
        return 0;
    }

    public int SOF_SoftDigestInit(int algoId, byte[] eccPubKey) {
        LogUtil.m10d(TAG, "SOF_DecryptFinal() - algoId:" + algoId);
        this.digest = new DigestUtil();
        try {
            return this.digest.SoftDigestInit(algoId, eccPubKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return 1;
        }
    }

    public int SOF_SoftDigestUpdate(byte[] input, int inputLen) {
        LogUtil.m10d(TAG, "SOF_DigestUpdate()");
        return this.digest.SoftDigestUpdate(input, inputLen);
    }

    public int SOF_SoftDigestUpdate(byte[] input) {
        LogUtil.m10d(TAG, "SOF_DigestUpdate()");
        return this.digest.SoftDigestUpdate(input, input.length);
    }

    public int SOF_SoftDigestFinal(byte[] input, int inputLen, byte[] output, int[] outputLen) {
        LogUtil.m10d(TAG, "SOF_DigestFinal()");
        return this.digest.SoftDigestFinal(input, inputLen, output, outputLen);
    }

    public int SOF_SoftDigestFinal(byte[] output, int[] outputLen) {
        LogUtil.m10d(TAG, "SOF_DigestFinal()");
        return this.digest.SoftDigestFinal(output, outputLen);
    }

    public int SOF_ExtRSAPubKeyOperation(byte[] pubKeyBlob, byte[] input, int inputLen, byte[] output, int[] outputLen) {
        LogUtil.m10d(TAG, "SOF_ExtRSAPubKeyOperation()");
        if (pubKeyBlob == null) {
            this.skf.SKF_SetLastError(2);
            return 1;
        } else if (input == null || input.length < inputLen) {
            this.skf.SKF_SetLastError(2);
            return 2;
        } else if (output == null || output.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 3;
        } else if (outputLen == null || outputLen.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 4;
        } else if (this.skf.SKF_ExtRSAPubKeyOperation(pubKeyBlob, input, inputLen, output, outputLen) != 0) {
            return 5;
        } else {
            return 0;
        }
    }

    public int SOF_DeleteCertificate(String containerName, int signFlag) {
        LogUtil.m10d(TAG, "SOF_DeleteCertificate()");
        if (TextUtils.isEmpty(containerName)) {
            this.skf.SKF_SetLastError(2);
            return 1;
        }
        Integer containerId = (Integer) this.Containers.get(containerName);
        if (containerId == null) {
            int[] id = new int[1];
            if (this.skf.SKF_OpenContainer(this.appId, containerName.getBytes(), id) != 0) {
                return 2;
            }
            containerId = Integer.valueOf(id[0]);
            this.Containers.put(containerName, containerId);
        }
        return this.skf.SKF_DeleteCertificate(this.appId, containerId.intValue(), signFlag) != 0 ? 3 : 0;
    }

    public int SOF_DeleteKeyPair(String containerName, int signFlag) {
        LogUtil.m10d(TAG, "SOF_DeleteKeyPair()");
        if (TextUtils.isEmpty(containerName)) {
            this.skf.SKF_SetLastError(2);
            return 1;
        }
        Integer containerId = (Integer) this.Containers.get(containerName);
        if (containerId == null) {
            int[] id = new int[1];
            if (this.skf.SKF_OpenContainer(this.appId, containerName.getBytes(), id) != 0) {
                return 2;
            }
            containerId = Integer.valueOf(id[0]);
            this.Containers.put(containerName, containerId);
        }
        return this.skf.SKF_DeleteKeyPair(this.appId, containerId.intValue(), signFlag) != 0 ? 3 : 0;
    }

    public int SOF_SignData(String containerName, int signFlag, int hashAlgo, byte[] hash, int hashLen, int timeOut, BaseCallback<byte[]> callback) {
        LogUtil.m10d(TAG, "SOF_SignData()");
        if (TextUtils.isEmpty(containerName)) {
            this.skf.SKF_SetLastError(2);
            return 1;
        } else if (hash == null || hash.length < hashLen) {
            this.skf.SKF_SetLastError(2);
            return 2;
        } else {
            new SignAsyncTask(containerName, signFlag, hashAlgo, hash, hashLen, timeOut, callback).execute(new Void[0]);
            return 0;
        }
    }

    public int SOF_SignData_Cancel() {
        return this.skf.SKF_RSASignDataInteractiveCancel(this.appId, 0, 0, 0);
    }

    public int SOF_DigestData_Display(int algoId, byte[] data, int dataLen, byte[] hashVal, int[] hashValLen) {
        LogUtil.m10d(TAG, "SOF_DigestData() - algoId:" + algoId);
        if (data == null || data.length < dataLen) {
            this.skf.SKF_SetLastError(2);
            return 1;
        } else if (hashVal == null || hashVal.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 2;
        } else if (hashValLen == null || hashValLen.length <= 0) {
            this.skf.SKF_SetLastError(2);
            return 3;
        } else if (this.skf.SKF_DigestInit_Display(algoId) != 0) {
            return 4;
        } else {
            byte[] tmp;
            int nCount = dataLen / 1024;
            int left = dataLen % 1024;
            for (int i = 0; i < nCount; i++) {
                tmp = new byte[1024];
                System.arraycopy(data, i * 1024, tmp, 0, 1024);
                if (this.skf.SKF_DigestUpdate_Display(tmp, 1024) != 0) {
                    return 5;
                }
            }
            tmp = new byte[left];
            System.arraycopy(data, nCount * 1024, tmp, 0, left);
            if (this.skf.SKF_DigestUpdate_Display(tmp, left) != 0) {
                return 6;
            }
            if (this.skf.SKF_DigestFinal(new byte[0], 0, hashVal, hashValLen) != 0) {
                return 7;
            }
            return 0;
        }
    }

    public int SOF_Construct_ECC_EnvlopedKeyBlob(int symmAlgID, byte[] sm2cipher_x, byte[] sm2cipher_y, byte[] sm2cipher_hash, byte[] sm2cipher_text, int sm2_Bits, byte[] sm2_pubKey_x, byte[] sm2_pubKey_y, byte[] sm2_EncryptedPrivateKey, byte[] blob, int[] blobLen) {
        LogUtil.m10d(TAG, "SOF_Construct_ECC_EnvlopedKeyBlob()");
        try {
            byte[] envloped = new EnvlopedKeyBlob(symmAlgID, sm2_Bits, sm2_EncryptedPrivateKey, new ECCPublicKeyBlob(sm2_Bits, sm2_pubKey_x, sm2_pubKey_y), new ECCCipherBlob(sm2cipher_x, sm2cipher_y, sm2cipher_hash, sm2cipher_text.length, sm2cipher_text)).getEncode();
            System.arraycopy(envloped, 0, blob, 0, envloped.length);
            blobLen[0] = envloped.length;
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            this.skf.SKF_SetLastError(1);
            return 1;
        }
    }
}
