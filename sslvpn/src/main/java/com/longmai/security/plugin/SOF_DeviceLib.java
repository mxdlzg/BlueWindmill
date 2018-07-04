package com.longmai.security.plugin;

import android.content.Context;
import com.longmai.security.plugin.base.BaseConfig;
import com.longmai.security.plugin.device.Device;
import com.longmai.security.plugin.skf.SKFControlService;
import com.longmai.security.plugin.skf.SKFLib;
import com.longmai.security.plugin.util.LogUtil;
import com.longmai.security.plugin.util.StringUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SOF_DeviceLib {
    public static final int ADMIN_TYPE = 0;
    private static final String ConfigFile = "/assets/config.properties";
    protected static final int ECC_CRYPT_MAX = 128;
    public static final int Exchange_Key = 0;
    public static final int SECURE_ADM_ACCOUNT = 1;
    public static final int SECURE_EVERYONE_ACCOUNT = 255;
    public static final int SECURE_NEVER_ACCOUNT = 0;
    public static final int SECURE_USER_ACCOUNT = 16;
    public static final int SGD_3DES112_CBC = -2147483102;
    public static final int SGD_3DES112_ECB = -2147483103;
    public static final int SGD_3DES168_CBC = -2147483070;
    public static final int SGD_3DES168_ECB = -2147483071;
    public static final int SGD_AES128_CBC = -2147483374;
    public static final int SGD_AES128_ECB = -2147483375;
    public static final int SGD_AES192_CBC = -2147483358;
    public static final int SGD_AES192_ECB = -2147483359;
    public static final int SGD_AES256_CBC = -2147483326;
    public static final int SGD_AES256_ECB = -2147483327;
    public static final int SGD_DES_CBC = -2147483118;
    public static final int SGD_DES_ECB = -2147483119;
    public static final int SGD_MD5 = 129;
    public static final int SGD_RAW = 128;
    public static final int SGD_RSA = 65536;
    public static final int SGD_SHA1 = 2;
    public static final int SGD_SHA256 = 4;
    public static final int SGD_SHA384 = 130;
    public static final int SGD_SHA512 = 131;
    public static final int SGD_SM1_CBC = 258;
    public static final int SGD_SM1_CFB = 260;
    public static final int SGD_SM1_ECB = 257;
    public static final int SGD_SM1_MAC = 272;
    public static final int SGD_SM1_OFB = 264;
    public static final int SGD_SM2_1 = 131328;
    public static final int SGD_SM2_2 = 131584;
    public static final int SGD_SM2_3 = 132096;
    public static final int SGD_SM3 = 1;
    public static final int SGD_SM4_CBC = 1026;
    public static final int SGD_SM4_CFB = 1028;
    public static final int SGD_SM4_ECB = 1025;
    public static final int SGD_SM4_MAC = 1040;
    public static final int SGD_SM4_OFB = 1032;
    public static final int SGD_SSF33_CBC = 514;
    public static final int SGD_SSF33_CFB = 516;
    public static final int SGD_SSF33_ECB = 513;
    public static final int SGD_SSF33_MAC = 528;
    public static final int SGD_SSF33_OFB = 520;
    public static final int SGD_VENDOR_DEFINED = Integer.MIN_VALUE;
    public static final int Signature_Key = 1;
    private static final String TAG = SOF_DeviceLib.class.getName();
    public static final int USER_TYPE = 1;
    private static final String VERSION = "2.0.1";
    private static Map<String, SOF_AppLib> _Apps = new ConcurrentHashMap();
    private static Map<String, Device> _devices = new ConcurrentHashMap();
    private static BaseConfig config;
    private static SKFLib skf = new SKFControlService();

    static {
        LogUtil.m10d(TAG, "Loading Config");
        try {
            config = new PluginConfig(ConfigFile);
            //config.loadConfig(ConfigFile);
        } catch (IOException e) {
            skf.SKF_SetLastError(10);
            config = null;
        }
    }

    public static String SOF_GetVersion() {
        LogUtil.m10d(TAG, "SOF_GetVersion()");
        return VERSION;
    }

    public static int SOF_GetDevState() {
        LogUtil.m10d(TAG, "SOF_GetDevState()");
        return skf.getConnState();
    }

    public static int SOF_EnumDevices(Context context, List<String> devices) {
        LogUtil.m10d(TAG, "SOF_EnumDevices()");
        if (config != null) {
            SOF_LoadLibrary(context, config.get("Id"), config.get("Name"), config.get("Device"), null, new String[0]);
        }
        if (devices == null) {
            skf.SKF_SetLastError(2);
            return 1;
        }
        _devices.clear();
        List<Device> dev = new ArrayList();
        if (skf.SKF_EnumDevices(context, dev) != 0) {
            return 2;
        }
        for (Device d : dev) {
            _devices.put(d.getName(), d);
        }
        devices.addAll(_devices.keySet());
        return 0;
    }

    public static int SOF_EnumDevices(Context context, List<String> devices, int timeOut) {
        LogUtil.m10d(TAG, "SOF_EnumDevices() - timeOut:" + timeOut);
        if (config != null) {
            SOF_LoadLibrary(context, config.get("Id"), config.get("Name"), config.get("Device"), null, new String[0]);
        }
        if (devices == null) {
            skf.SKF_SetLastError(2);
            return 1;
        } else if (timeOut < 100) {
            skf.SKF_SetLastError(2);
            return 2;
        } else {
            _devices.clear();
            List<Device> dev = new ArrayList();
            if (skf.SKF_EnumDevices(context, dev, timeOut) != 0) {
                return 3;
            }
            for (Device d : dev) {
                _devices.put(d.getName(), d);
            }
            devices.addAll(_devices.keySet());
            return 0;
        }
    }

    public static int SOF_EnumDevices(Context context, String regex, List<String> devices, int timeOut) {
        LogUtil.m10d(TAG, "SOF_EnumDevices() - regex:" + regex + " timeOut:" + timeOut);
        if (config != null) {
            SOF_LoadLibrary(context, config.get("Id"), config.get("Name"), config.get("Device"), null, new String[0]);
        }
        if (devices == null) {
            skf.SKF_SetLastError(2);
            return 1;
        } else if (timeOut < 100) {
            skf.SKF_SetLastError(2);
            return 2;
        } else {
            _devices.clear();
            List<Device> dev = new ArrayList();
            if (skf.SKF_EnumDevices(context, dev, timeOut) != 0) {
                return 3;
            }
            for (Device d : dev) {
                if (d.getName().matches(regex)) {
                    _devices.put(d.getName(), d);
                }
            }
            devices.addAll(_devices.keySet());
            return 0;
        }
    }

    public static int SOF_EnumDeviceByName(Context context, String devName, int timeOut) {
        LogUtil.m10d(TAG, "SOF_EnumDeviceByName() - devName:" + devName + " timeOut:" + timeOut);
        if (config != null) {
            SOF_LoadLibrary(context, config.get("Id"), config.get("Name"), config.get("Device"), null, new String[0]);
        }
        if (timeOut < 100) {
            skf.SKF_SetLastError(2);
            return 1;
        }
        List<Device> dev = new ArrayList();
        if (skf.SKF_EnumDeviceByName(context, devName, dev, timeOut) != 0) {
            return 2;
        }
        for (Device d : dev) {
            _devices.put(d.getName(), d);
        }
        return 0;
    }

    public static int SOF_Connect(String devName, String authCode) {
        LogUtil.m10d(TAG, "SOF_Connect() - devName:" + devName);
        if (StringUtils.isEmpty(authCode)) {
            authCode = "";
        }
        return SOF_Connect(devName, authCode.getBytes());
    }

    public static int SOF_Connect(String devName) {
        return SOF_Connect(devName, new byte[]{});
    }

    public static int SOF_Connect(String devName, byte[] authCode) {
        LogUtil.m10d(TAG, "SOF_Connect() - devName:" + devName);
        if (StringUtils.isEmpty(devName)) {
            skf.SKF_SetLastError(2);
            return 1;
        }
        Device device = (Device) _devices.get(devName);
        if (device == null) {
            skf.SKF_SetLastError(12);
            return 2;
        } else if (skf.SKF_Connect(device, authCode) != 0) {
            return 3;
        } else {
            return 0;
        }
    }

    public static int SOF_ConnectAsync(String devName, byte[] authCode) {
        LogUtil.m10d(TAG, "SOF_ConnectAsync() - devName:" + devName);
        return 0;
    }

    public static int SOF_ConnectEx(Device device, byte[] authCode) {
        LogUtil.m10d(TAG, "SOF_ConnectEx() - devName:" + device.getName());
        return skf.SKF_Connect(device, authCode);
    }

    public static int SOF_EnumApplication(List<String> appNames) {
        LogUtil.m10d(TAG, "SOF_EnumApplication()");
        if (appNames != null) {
            return skf.SKF_EnumApplication(appNames);
        }
        skf.SKF_SetLastError(2);
        return 1;
    }

    public static SOF_AppLib SOF_GetInstance(String appName) {
        LogUtil.m10d(TAG, "SOF_GetInstance() - appName:" + appName);
        if (StringUtils.isEmpty(appName)) {
            List<String> appNames = new ArrayList();
            if (skf.SKF_EnumApplication(appNames) != 0 || appNames.size() <= 0) {
                return null;
            }
            return SOF_GetInstance((String) appNames.get(0));
        }
        SOF_AppLib app = (SOF_AppLib) _Apps.get(appName);
        if (app != null) {
            return app;
        }
        LogUtil.m10d(TAG, "Apps.get() - app: null");
        int[] appId = new int[1];
        int rtn = skf.SKF_OpenApplication(appName.getBytes(), appId);
        if (rtn != 0) {
            LogUtil.m10d(TAG, "SKF_OpenApp() - return: " + rtn);
            return null;
        }
        app = new SOF_AppLib(skf, appName, appId[0]);
        _Apps.put(appName, app);
        return app;
    }

    public static int SOF_GetLastError() {
        LogUtil.m10d(TAG, "SOF_GetLastError()");
        return skf.SKF_GetLastError();
    }

    public static int SOF_GenRandom(int length, byte[] random) {
        LogUtil.m10d(TAG, "SOF_GenRandom() - length:" + length);
        if (random == null || random.length < length) {
            skf.SKF_SetLastError(2);
            return 1;
        } else if (skf.SKF_GenRandom(length, random) == 0) {
            return 0;
        } else {
            return 2;
        }
    }

    public static int SOF_GetDeviceInfo(byte[] label, byte[] serialNumber, int[] version) {
        LogUtil.m10d(TAG, "SOF_GetDeviceInfo()");
        if (skf.SKF_GetDevInfo(label, serialNumber, version) != 0) {
            return 2;
        }
        return 0;
    }

    public static int SOF_GetDeviceInfo(byte[] struct, int[] structLen) {
        LogUtil.m10d(TAG, "SOF_GetDeviceInfo()");
        if (skf.SKF_GetDevInfo(new byte[128], new byte[128], new int[1], new int[1], struct, structLen) != 0) {
            return 2;
        }
        return 0;
    }

    public static int SOF_SetLabel(byte[] label) {
        LogUtil.m10d(TAG, "SOF_SetLabel()");
        if (label == null) {
            skf.SKF_SetLastError(2);
            return 1;
        } else if (skf.SKF_SetLabel(label) == 0) {
            return 0;
        } else {
            return 2;
        }
    }

    public static int SOF_Disconnect() {
        LogUtil.m10d(TAG, "SOF_Disconnect()");
        _Apps.clear();
        if (skf.SKF_Disconnect() != 0) {
            return 2;
        }
        return 0;
    }

    public static int SOF_LedControl(int state, int interval) {
        LogUtil.m10d(TAG, "SOF_LedControl() - state:" + state + " interval:" + interval);
        if (skf.SKF_LedControl(state, interval) != 0) {
            return 2;
        }
        return 0;
    }

    public static int SOF_LoadLibraryXML(Context context, InputStream is) throws ParserConfigurationException, SAXException, IOException {
        LogUtil.m10d(TAG, "SOF_LoadLibraryXML()");
        NodeList drivers = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is).getDocumentElement().getElementsByTagName("driver");
        for (int i = 0; i < drivers.getLength(); i++) {
            Element driver = (Element) drivers.item(i);
            LogUtil.m10d(TAG, "id:" + driver.getAttribute("id"));
            LogUtil.m10d(TAG, "name:" + driver.getAttribute("name"));
            LogUtil.m10d(TAG, "class:" + driver.getAttribute("class"));
            LogUtil.m10d(TAG, "describe:" + driver.getAttribute("describe"));
            NodeList propertys = driver.getElementsByTagName("property");
            for (int j = 0; j < propertys.getLength(); j++) {
                Element property = (Element) propertys.item(j);
                LogUtil.m10d(TAG, "property - " + property.getAttribute("name") + ":" + property.getAttribute("value"));
            }
        }
        return 0;
    }

    public static int SOF_LoadLibrary(Context context, String id, String name, String clazz, String describe, String... propertys) {
        LogUtil.m10d(TAG, "SOF_LoadLibrary() - id:" + id + " name:" + name + " class:" + clazz + " describe:" + describe);
        config = null;
        return skf.SKF_LoadLibrary(context, id, name, clazz);
    }

    public static int SOF_DeviceAuth(byte[] authKey) {
        LogUtil.m10d(TAG, "SOF_DeviceAuth()");
        byte[] random = new byte[8];
        if (skf.SKF_GenRandom(8, random) != 0) {
            return 2;
        }
        if (skf.SKF_DeviceAuth(random, authKey) != 0) {
            return 3;
        }
        return 0;
    }

    public static int SOF_CreateApplication(String appName, String adminPin, int adminPinRetryCount, String userPin, int userPinRetryCount, int createFileRights) {
        LogUtil.m10d(TAG, "SOF_CreateApplication()");
        if (StringUtils.isEmpty(appName)) {
            skf.SKF_SetLastError(2);
            return 1;
        }
        return skf.SKF_CreateApplication(appName.getBytes(), adminPin.getBytes(), adminPinRetryCount, userPin.getBytes(), userPinRetryCount, createFileRights);
    }

    public static int SOF_DeleteApplication(String appName) {
        LogUtil.m10d(TAG, "SOF_DeleteApplication()");
        if (!StringUtils.isEmpty(appName)) {
            return skf.SKF_DeleteApplication(appName.getBytes());
        }
        skf.SKF_SetLastError(2);
        return 1;
    }
}
