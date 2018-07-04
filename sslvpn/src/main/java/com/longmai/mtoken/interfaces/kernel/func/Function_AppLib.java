package com.longmai.mtoken.interfaces.kernel.func;

import android.util.Base64;
import com.longmai.mtoken.interfaces.kernel.func.base.Function;
import com.longmai.security.plugin.SOF_AppLib;
import com.longmai.security.plugin.SOF_DeviceLib;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Function_AppLib implements Function {
    private static final String TAG = Function_AppLib.class.getName();
    private SOF_AppLib app;
    private String function = this.result.getString("function");
    private JSONObject result;
    private String r11;
    private byte[] output,input;
    private int[] outputLen;
    private String fileName;
    private int offset,keyId,inputLen;

    public Function_AppLib(String request) throws JSONException {
        this.result = new JSONObject(new String(request));
        if (this.function == null) {
            throw new JSONException("get function fail");
        }
        String appName = this.result.getString("appName");
        this.app = SOF_DeviceLib.SOF_GetInstance(appName);
        if (this.app == null) {
            throw new JSONException("get key: " + appName + " fail");
        }
    }

    public String exec() throws RuntimeException, JSONException {
        JSONObject person = new JSONObject();
        int[] retryCount;
        int rtn;
        if (this.function.equals(Function.SOF_Login)) {
            person.put("function", Function.SOF_Login);
            retryCount = new int[1];
            rtn = this.app.SOF_Login(this.result.getString("userPin"), retryCount);
            person.put("return", rtn);
            if (rtn != 0) {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
                person.put("retry", retryCount[0]);
            }
        } else if (this.function.equals(Function.SOF_Logout)) {
            person.put("function", Function.SOF_Logout);
            rtn = this.app.SOF_Logout();
            person.put("return", rtn);
            if (rtn != 0) {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_ChanegPassWd)) {
            person.put("function", Function.SOF_ChanegPassWd);
            int[] retry = new int[1];
            rtn = this.app.SOF_ChanegPassWd(this.result.getString("oldUpin"), this.result.getString("newUpin"), retry);
            person.put("return", rtn);
            if (rtn != 0) {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
                person.put("retry", retry[0]);
            }
        } else if (this.function.equals(Function.SOF_EnumContainers)) {
            person.put("function", Function.SOF_EnumContainers);
            List<String> containers = new ArrayList();
            rtn = this.app.SOF_EnumContainers(containers);
            person.put("return", rtn);
            if (rtn == 0) {
                JSONArray _containers = new JSONArray();
                for (int i = 0; i < containers.size(); i++) {
                    _containers.put(containers.get(i));
                }
                person.put("containers", _containers);
            } else {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_GetContainerInfo)) {
            person.put("function", Function.SOF_GetContainerInfo);
            int[] containerType = new int[1];
            int[] signKeyLen = new int[1];
            int[] exchKeyLen = new int[1];
            int[] signCertFlag = new int[1];
            int[] exchCertFlag = new int[1];
            rtn = this.app.SOF_GetContainerInfo(this.result.getString("containerName"), containerType, signKeyLen, exchKeyLen, signCertFlag, exchCertFlag);
            person.put("return", rtn);
            if (rtn == 0) {
                person.put("containerType", containerType[0]);
                person.put("signKeyLen", signKeyLen[0]);
                person.put("exchKeyLen", exchKeyLen[0]);
                person.put("signCertFlag", signCertFlag[0]);
                person.put("exchCertFlag", exchCertFlag[0]);
            } else {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_CreateContainer)) {
            person.put("function", Function.SOF_CreateContainer);
            rtn = this.app.SOF_CreateContainer(this.result.getString("containerName"));
            person.put("return", rtn);
            if (rtn != 0) {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_DeleteContainer)) {
            person.put("function", Function.SOF_DeleteContainer);
            rtn = this.app.SOF_DeleteContainer(this.result.getString("containerName"));
            person.put("return", rtn);
            if (rtn != 0) {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_ExportUserCert)) {
            person.put("function", Function.SOF_ExportUserCert);
            byte[] cert = new byte[4096];
            int[] certLen = new int[1];
            rtn = this.app.SOF_ExportUserCert(this.result.getString("containerName"), this.result.getInt("signFlag"), cert, certLen);
            person.put("return", rtn);
            if (rtn == 0) {
                String r11 = new String(Base64.encode(cert, 0, certLen[0], 2));
                person.put("cert", r11);
                person.put("certLen", certLen[0]);
            } else {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_ImportCertificate)) {
            person.put("function", Function.SOF_ImportCertificate);
            rtn = this.app.SOF_ImportCertificate(this.result.getString("containerName"), this.result.getInt("signFlag"), Base64.decode(this.result.getString("cert"), 2), this.result.getInt("certLen"));
            person.put("return", rtn);
            if (rtn != 0) {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_GenKeyPair)) {
            person.put("function", Function.SOF_GenKeyPair);
            byte[] pubKey = new byte[512];
            int[] keyLen = new int[1];
            rtn = this.app.SOF_GenKeyPair(this.result.getString("containerName"), this.result.getInt("signFlag"), this.result.getInt("algoId"), this.result.getInt("bitLen"), pubKey, keyLen);
            person.put("return", rtn);
            if (rtn == 0) {
                String r11 = new String(Base64.encode(pubKey, 0, keyLen[0], 2));
                person.put("pubKey", r11);
                person.put("keyLen", keyLen[0]);
            } else {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_ImportRSAKeyPair)) {
            person.put("function", Function.SOF_ImportRSAKeyPair);
            rtn = this.app.SOF_ImportRSAKeyPair(this.result.getString("containerName"), this.result.getInt("algId"), this.result.getInt("bitLen"), Base64.decode(this.result.getString("wrappedKey"), 2), this.result.getInt("wrappedKeyLen"), Base64.decode(this.result.getString("encryptedData"), 2), this.result.getInt("encryptedDataLen"));
            person.put("return", rtn);
            if (rtn != 0) {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_ImportECCKeyPair)) {
            person.put("function", Function.SOF_ImportECCKeyPair);
            rtn = this.app.SOF_ImportECCKeyPair(this.result.getString("containerName"), Base64.decode(this.result.getString("envelopedKeyBlob"), 2), this.result.getInt("envelopedKeyBlobLen"));
            person.put("return", rtn);
            if (rtn != 0) {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_ImportExtRSAKeyPair)) {
            person.put("function", Function.SOF_ImportExtRSAKeyPair);
            rtn = this.app.SOF_ImportExtRSAKeyPair(this.result.getString("containerName"), this.result.getInt("signFlag"), Base64.decode(this.result.getString("blob"), 2), this.result.getInt("blobLen"));
            person.put("return", rtn);
            if (rtn != 0) {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_ExportPublicKeyBlob)) {
            person.put("function", Function.SOF_ExportPublicKeyBlob);
            byte[] publicKeyBlob = new byte[1024];
            int[] blobLen = new int[1];
            rtn = this.app.SOF_ExportPublicKeyBlob(this.result.getString("containerName"), this.result.getInt("signFlag"), publicKeyBlob, blobLen);
            person.put("return", rtn);
            if (rtn == 0) {
                String r11 = new String(Base64.encode(publicKeyBlob, 0, blobLen[0], 2));
                person.put("publicKeyBlob", r11);
                person.put("blobLen", blobLen[0]);
            } else {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_SignData)) {
            person.put("function", Function.SOF_SignData);
            byte[] signature = new byte[1024];
            int[] signLen = new int[1];
            rtn = this.app.SOF_SignData(this.result.getString("containerName"), this.result.getInt("signFlag"), this.result.getInt("hashAlgo"), Base64.decode(this.result.getString("inData"), 2), this.result.getInt("inDataLen"), signature, signLen);
            person.put("return", rtn);
            if (rtn == 0) {
                String r11 = new String(Base64.encode(signature, 0, signLen[0], 2));
                person.put("signature", r11);
                person.put("signLen", signLen[0]);
            } else {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_VerifySignedData)) {
            person.put("function", Function.SOF_VerifySignedData);
            rtn = this.app.SOF_VerifySignedData(Base64.decode(this.result.getString("pubKeyBlob"), 2), this.result.getInt("blobLen"), this.result.getInt("algoId"), this.result.getInt("hashAlgo"), Base64.decode(this.result.getString("inData"), 2), this.result.getInt("inDataLen"), Base64.decode(this.result.getString("signature"), 2), this.result.getInt("signLen"));
            person.put("return", rtn);
            if (rtn != 0) {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_EncryptData)) {
            person.put("function", Function.SOF_EncryptData);
            int algoId = this.result.getInt("algoId");
            byte[] key = Base64.decode(this.result.getString("key"), 2);
            int keyLen = this.result.getInt("keyLen");
            int padding = this.result.getInt("padding");
            byte[] iv = Base64.decode(this.result.getString("iv"), 2);
            int ivLen = this.result.getInt("ivLen");
            byte[] input = Base64.decode(this.result.getString("input"), 2);
            int inputLen = this.result.getInt("inputLen");
            byte[] output = new byte[(inputLen * 2)];
            int[] outputLen = new int[1];
            rtn = this.app.SOF_EncryptData(algoId, key, keyLen, padding, iv, ivLen, input, inputLen, output, outputLen);
            person.put("return", rtn);
            if (rtn == 0) {
                String r11 = new String(Base64.encode(output, 0, outputLen[0], 2));
                person.put("output", r11);
                person.put("outputLen", outputLen[0]);
            } else {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_DecryptData)) {
            person.put("function", Function.SOF_DecryptData);
            int algoId = this.result.getInt("algoId");
            byte[] key = Base64.decode(this.result.getString("key"), 2);
            int keyLen = this.result.getInt("keyLen");
            int padding = this.result.getInt("padding");
            byte[] iv = Base64.decode(this.result.getString("iv"), 2);
            int ivLen = this.result.getInt("ivLen");
            byte[] input =Base64.decode(this.result.getString("input"), 2);
            int inputLen = this.result.getInt("inputLen");
            byte[] output = new byte[(inputLen * 2)];
            int[] outputLen = new int[1];
            rtn = this.app.SOF_DecryptData(algoId, key, keyLen, padding, iv, ivLen, input, inputLen, output, outputLen);
            person.put("return", rtn);
            if (rtn == 0) {
                String r11 = new String(Base64.encode(output, 0, outputLen[0], 2));
                person.put("output", r11);
                person.put("outputLen", outputLen[0]);
            } else {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_PublicEncrypt)) {
            person.put("function", Function.SOF_PublicEncrypt);
            byte[] output = new byte[1024];
            int[] outputLen = new int[1];
            rtn = this.app.SOF_PublicEncrypt(this.result.getString("containerName"), this.result.getInt("signFlag"), Base64.decode(this.result.getString("input"), 2), this.result.getInt("inputLen"), output, outputLen);
            person.put("return", rtn);
            if (rtn == 0) {
                String r11 = new String(Base64.encode(output, 0, outputLen[0], 2));
                person.put("output", r11);
                person.put("outputLen", outputLen[0]);
            } else {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_PrivateDecrypt)) {
            person.put("function", Function.SOF_PrivateDecrypt);
            output = new byte[1024];
            outputLen = new int[1];
            rtn = this.app.SOF_PrivateDecrypt(this.result.getString("containerName"), this.result.getInt("signFlag"), Base64.decode(this.result.getString("input"), 2), this.result.getInt("inputLen"), output, outputLen);
            person.put("return", rtn);
            if (rtn == 0) {
                String r11 = new String(Base64.encode(output, 0, outputLen[0], 2));
                person.put("output", r11);
                person.put("outputLen", outputLen[0]);
            } else {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_ExtPublicEncrypt)) {
            person.put("function", Function.SOF_ExtPublicEncrypt);
            output = new byte[1024];
            outputLen = new int[1];
            rtn = this.app.SOF_ExtPublicEncrypt(Base64.decode(this.result.getString("pubKeyBlob"), 2), this.result.getInt("blobLen"), this.result.getInt("algoId"), Base64.decode(this.result.getString("input"), 2), this.result.getInt("inputLen"), output, outputLen);
            person.put("return", rtn);
            if (rtn == 0) {
                String r11 = new String(Base64.encode(output, 0, outputLen[0], 2));
                person.put("output", r11);
                person.put("outputLen", outputLen[0]);
            } else {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_EnumFiles)) {
            person.put("function", Function.SOF_EnumFiles);
            List<String> files = new ArrayList();
            rtn = this.app.SOF_EnumFiles(files);
            person.put("return", rtn);
            if (rtn == 0) {
                JSONArray _files = new JSONArray();
                for (int i = 0; i < files.size(); i++) {
                    _files.put(files.get(i));
                }
                person.put("files", _files);
            } else {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_GetFileInfo)) {
            person.put("function", Function.SOF_GetFileInfo);
            int[] fileSize = new int[1];
            int[] readRights = new int[1];
            int[] writeRights = new int[1];
            rtn = this.app.SOF_GetFileInfo(this.result.getString("fileName"), fileSize, readRights, writeRights);
            person.put("return", rtn);
            if (rtn == 0) {
                person.put("fileSize", fileSize[0]);
                person.put("readRights", readRights[0]);
                person.put("writeRights", writeRights[0]);
            } else {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_ReadFile)) {
            person.put("function", Function.SOF_ReadFile);
            fileName = this.result.getString("fileName");
            offset = this.result.getInt("offset");
            int readLen = this.result.getInt("readLen");
            output = new byte[readLen];
            rtn = this.app.SOF_ReadFile(fileName, offset, readLen, output);
            person.put("return", rtn);
            if (rtn == 0) {
                JSONObject jSONObject = person;
                jSONObject.put("output", new String(Base64.encode(output, 0, readLen, 2)));
            } else {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_WriteFile)) {
            person.put("function", Function.SOF_WriteFile);
            fileName = this.result.getString("fileName");
            offset = this.result.getInt("offset");
            int length = this.result.getInt("length");
            rtn = this.app.SOF_WriteFile(fileName, offset, Base64.decode(this.result.getString("input"), 2), length);
            person.put("return", rtn);
            if (rtn != 0) {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_CreaterFile)) {
            person.put("function", Function.SOF_CreaterFile);
            rtn = this.app.SOF_CreaterFile(this.result.getString("fileName"), this.result.getInt("fileSize"), this.result.getInt("readRights"), this.result.getInt("writeRights"));
            person.put("return", rtn);
            if (rtn != 0) {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_DeleteFile)) {
            person.put("function", Function.SOF_DeleteFile);
            rtn = this.app.SOF_DeleteFile(this.result.getString("fileName"));
            person.put("return", rtn);
            if (rtn != 0) {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_DigestData)) {
            person.put("function", Function.SOF_DigestData);
            byte[] hashVal = new byte[128];
            int[] hashValLen = new int[1];
            rtn = this.app.SOF_DigestData(this.result.getInt("algoId"), Base64.decode(this.result.getString("data"), 2), this.result.getInt("dataLen"), hashVal, hashValLen);
            person.put("return", rtn);
            if (rtn == 0) {
                String r11 = new String(Base64.encode(hashVal, 0, hashValLen[0], 2));
                person.put("hashVal", r11);
                person.put("hashValLen", hashValLen[0]);
            } else {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_SM3Digest)) {
            person.put("function", Function.SOF_SM3Digest);
            byte[] digest = new byte[128];
            int[] digestLen = new int[1];
            rtn = this.app.SOF_SM3Digest(Base64.decode(this.result.getString("data"), 2), this.result.getInt("length"), digest, digestLen);
            person.put("return", rtn);
            if (rtn == 0) {
                String r11 = new String(Base64.encode(digest, 0, digestLen[0], 2));
                person.put("digest", r11);
                person.put("digestLen", digestLen[0]);
            } else {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_UnblockPIN)) {
            person.put("function", Function.SOF_UnblockPIN);
            retryCount = new int[1];
            rtn = this.app.SOF_UnblockPIN(this.result.getString("soPin"), this.result.getString("userPin"), retryCount);
            person.put("return", rtn);
            if (rtn != 0) {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
                person.put("retryCount", retryCount[0]);
            }
        } else if (this.function.equals(Function.SOF_SetSymtricKey)) {
            person.put("function", Function.SOF_SetSymtricKey);
            int[] keyId = new int[1];
            rtn = this.app.SOF_SetSymtricKey(this.result.getInt("algoId"), Base64.decode(this.result.getString("key"), 2), this.result.getInt("keyLen"), keyId);
            person.put("return", rtn);
            if (rtn == 0) {
                person.put("keyId", keyId[0]);
            } else {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_DestroySymtricKey)) {
            person.put("function", Function.SOF_DestroySymtricKey);
            rtn = this.app.SOF_DestroySymtricKey(this.result.getInt("keyId"));
            person.put("return", rtn);
            if (rtn != 0) {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_EncryptInit)) {
            person.put("function", Function.SOF_EncryptInit);
            rtn = this.app.SOF_EncryptInit(this.result.getInt("keyId"), Base64.decode(this.result.getString("iv"), 2), this.result.getInt("ivLen"), this.result.getInt("padding"));
            person.put("return", rtn);
            if (rtn != 0) {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_EncryptUpdate)) {
            person.put("function", Function.SOF_EncryptUpdate);
            keyId = this.result.getInt("keyId");
            input = Base64.decode(this.result.getString("input"), 2);
            inputLen = this.result.getInt("inputLen");
            output = new byte[((inputLen * 2) + 1024)];
            outputLen = new int[1];
            rtn = this.app.SOF_EncryptUpdate(keyId, input, inputLen, output, outputLen);
            person.put("return", rtn);
            if (rtn == 0) {
                String r11 = new String(Base64.encode(output, 0, outputLen[0], 2));
                person.put("output", r11);
                person.put("outputLen", outputLen[0]);
            } else {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_EncryptFinal)) {
            person.put("function", Function.SOF_EncryptFinal);
            keyId = this.result.getInt("keyId");
            input = Base64.decode(this.result.getString("input"), 2);
            inputLen = this.result.getInt("inputLen");
            output = new byte[((inputLen * 2) + 1024)];
            outputLen = new int[1];
            rtn = this.app.SOF_EncryptFinal(keyId, input, inputLen, output, outputLen);
            person.put("return", rtn);
            if (rtn == 0) {
                String r11 = new String(Base64.encode(output, 0, outputLen[0], 2));
                person.put("output", r11);
                person.put("outputLen", outputLen[0]);
            } else {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_DecryptInit)) {
            person.put("function", Function.SOF_DecryptInit);
            rtn = this.app.SOF_DecryptInit(this.result.getInt("keyId"), Base64.decode(this.result.getString("iv"), 2), this.result.getInt("ivLen"), this.result.getInt("padding"));
            person.put("return", rtn);
            if (rtn != 0) {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_DecryptUpdate)) {
            person.put("function", Function.SOF_DecryptUpdate);
            keyId = this.result.getInt("keyId");
            input = Base64.decode(this.result.getString("input"), 2);
            inputLen = this.result.getInt("inputLen");
            output = new byte[((inputLen * 2) + 1024)];
            outputLen = new int[1];
            rtn = this.app.SOF_DecryptUpdate(keyId, input, inputLen, output, outputLen);
            person.put("return", rtn);
            if (rtn == 0) {
                String r11 = new String(Base64.encode(output, 0, outputLen[0], 2));
                person.put("output", r11);
                person.put("outputLen", outputLen[0]);
            } else {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_DecryptFinal)) {
            person.put("function", Function.SOF_DecryptFinal);
            keyId = this.result.getInt("keyId");
            input = Base64.decode(this.result.getString("input"), 2);
            inputLen = this.result.getInt("inputLen");
            output = new byte[((inputLen * 2) + 1024)];
            outputLen = new int[1];
            rtn = this.app.SOF_DecryptFinal(keyId, input, inputLen, output, outputLen);
            person.put("return", rtn);
            if (rtn == 0) {
                r11 = new String(Base64.encode(output, 0, outputLen[0], 2));
                person.put("output", r11);
                person.put("outputLen", outputLen[0]);
            } else {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_ExtRSAPubKeyOperation)) {
            person.put("function", Function.SOF_ExtRSAPubKeyOperation);
            output = new byte[1024];
            outputLen = new int[1];
            rtn = this.app.SOF_ExtRSAPubKeyOperation(Base64.decode(this.result.getString("pubKeyBlob"), 2), Base64.decode(this.result.getString("input"), 2), this.result.getInt("inputLen"), output, outputLen);
            person.put("return", rtn);
            if (rtn == 0) {
                r11 = new String(Base64.encode(output, 0, outputLen[0], 2));
                person.put("output", r11);
                person.put("outputLen", outputLen[0]);
            } else {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_DeleteCertificate)) {
            person.put("function", Function.SOF_DeleteCertificate);
            rtn = this.app.SOF_DeleteCertificate(this.result.getString("containerName"), this.result.getInt("signFlag"));
            person.put("return", rtn);
            if (rtn != 0) {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_DeleteKeyPair)) {
            person.put("function", Function.SOF_DeleteKeyPair);
            rtn = this.app.SOF_DeleteKeyPair(this.result.getString("containerName"), this.result.getInt("signFlag"));
            person.put("return", rtn);
            if (rtn != 0) {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_GetPINInfo)) {
            person.put("function", Function.SOF_GetPINInfo);
            int[] maxRetryCount = new int[1];
            retryCount = new int[1];
            int[] defaultPin = new int[1];
            rtn = this.app.SOF_GetPINInfo(this.result.getInt("pinType"), maxRetryCount, retryCount, defaultPin);
            person.put("return", rtn);
            if (rtn == 0) {
                person.put("maxRetryCount", maxRetryCount[0]);
                person.put("retryCount", retryCount[0]);
                person.put("defaultPin", defaultPin[0]);
            } else {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else {
            throw new RuntimeException(this.result.toString());
        }
        return person.toString();
    }

    public void init() {
    }
}
