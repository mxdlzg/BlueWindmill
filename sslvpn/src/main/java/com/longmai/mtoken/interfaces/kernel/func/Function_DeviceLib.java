package com.longmai.mtoken.interfaces.kernel.func;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import com.longmai.mtoken.interfaces.kernel.func.base.Function;
import com.longmai.security.plugin.SOF_DeviceLib;
import com.longmai.security.plugin.util.LogUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Function_DeviceLib implements Function {
    private static final String TAG = Function_DeviceLib.class.getName();
    private static String lastClazz;
    private Context context;
    private String devName;
    private String function = this.result.getString("function");
    private JSONObject result;

    public Function_DeviceLib(Context context, String request) throws JSONException {
        this.context = context;
        this.result = new JSONObject(request);
        if (this.function == null) {
            throw new JSONException("get function fail");
        }
    }

    public String exec() throws RuntimeException, JSONException {
        LogUtil.m10d(TAG, "exec()");
        JSONObject person = new JSONObject();
        int rtn;
        if (this.function.equals(Function.SOF_LoadLibrary)) {
            person.put("function", Function.SOF_LoadLibrary);
            String id = this.result.getString("id");
            String name = this.result.getString("name");
            String clazz = this.result.getString("clazz");
            rtn = SOF_DeviceLib.SOF_LoadLibrary(this.context, id, name, clazz, null, new String[0]);
            person.put("return", rtn);
            if (rtn != 0) {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            } else {
                lastClazz = clazz;
            }
        } else if (this.function.equals(Function.SOF_EnumDevices)) {
            int timeOut;
            person.put("function", Function.SOF_EnumDevices);
            List<String> devices = new ArrayList();
            String regex = null;
            try {
                regex = this.result.getString("regex");
            } catch (JSONException e) {
            }
            try {
                timeOut = this.result.getInt("timeOut");
            } catch (JSONException e2) {
                timeOut = 3000;
            }
            if (regex == null) {
                rtn = SOF_DeviceLib.SOF_EnumDevices(this.context, devices, timeOut);
            } else {
                rtn = SOF_DeviceLib.SOF_EnumDevices(this.context, regex, devices, timeOut);
            }
            person.put("return", rtn);
            if (rtn == 0) {
                JSONArray _devices = new JSONArray();
                if (lastClazz.equals("com.longmai.security.plugin.driver.ble.BLEDriver")) {
                    String bind = this.context.getSharedPreferences("setting", 0).getString("bind", "");
                    LogUtil.m10d(TAG, "SOF_EnumDevices()  bind: " + bind);
                    if (TextUtils.isEmpty(bind)) {
                        for (int i = 0; i < devices.size(); i++) {
                            _devices.put((String) devices.get(i));
                        }
                    } else {
                        for (int i = 0; i < devices.size(); i++) {
                            String dev = (String) devices.get(i);
                            if (bind.contains(dev)) {
                                _devices.put(dev);
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < devices.size(); i++) {
                        _devices.put(devices.get(i));
                    }
                }
                person.put("devices", _devices);
            } else {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
            person.put("session", UUID.randomUUID());
        } else if (this.function.equals(Function.SOF_EnumDeviceByName)) {
            person.put("function", Function.SOF_EnumDeviceByName);
            rtn = SOF_DeviceLib.SOF_EnumDeviceByName(this.context, this.result.getString("devName"), this.result.getInt("timeOut"));
            person.put("return", rtn);
            if (rtn != 0) {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
            person.put("session", UUID.randomUUID());
        } else if (this.function.equals(Function.SOF_Connect)) {
            person.put("function", Function.SOF_Connect);
            this.devName = this.result.getString("devName");
            rtn = SOF_DeviceLib.SOF_Connect(this.devName, this.result.getString("authCode"));
            person.put("return", rtn);
            if (rtn != 0) {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_GenRandom)) {
            person.put("function", Function.SOF_GenRandom);
            int length = this.result.getInt("length");
            byte[] random = new byte[length];
            rtn = SOF_DeviceLib.SOF_GenRandom(length, random);
            person.put("return", rtn);
            if (rtn == 0) {
                JSONObject r0 = person;
                r0.put("random", Base64.encodeToString(random, 2));
            } else {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_GetDeviceInfo)) {
            person.put("function", Function.SOF_GetDeviceInfo);
            byte[] struct = new byte[1024];
            int[] structLen = new int[1];
            rtn = SOF_DeviceLib.SOF_GetDeviceInfo(struct, structLen);
            person.put("return", rtn);
            if (rtn == 0) {
                String str = new String(Base64.encode(struct, 0, structLen[0], 2));
                person.put("struct", str);
            } else {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_SetLabel)) {
            person.put("function", Function.SOF_SetLabel);
            rtn = SOF_DeviceLib.SOF_SetLabel(this.result.getString("label").getBytes());
            person.put("return", rtn);
            if (rtn != 0) {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_Disconnect)) {
            person.put("function", Function.SOF_Disconnect);
            rtn = SOF_DeviceLib.SOF_Disconnect();
            person.put("return", rtn);
            if (rtn != 0) {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals(Function.SOF_GetInstance)) {
            person.put("function", Function.SOF_GetInstance);
            if (SOF_DeviceLib.SOF_GetInstance(this.result.getString("appName")) == null) {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            } else {
                person.put("return", 0);
            }
        } else if (this.function.equals(Function.SOF_EnumApplication)) {
            person.put("function", Function.SOF_EnumApplication);
            List<String> appNames = new ArrayList();
            rtn = SOF_DeviceLib.SOF_EnumApplication(appNames);
            person.put("return", rtn);
            if (rtn == 0) {
                JSONArray _appNames = new JSONArray();
                for (int i = 0; i < appNames.size(); i++) {
                    _appNames.put(appNames.get(i));
                }
                person.put("appNames", _appNames);
            } else {
                person.put("errorCode", SOF_DeviceLib.SOF_GetLastError());
            }
        } else if (this.function.equals("getConnState")) {
            person.put("function", "getConnState");
            JSONObject r0 = person;
            r0.put("devName", this.devName);
            person.put("state", SOF_DeviceLib.SOF_GetDevState());
        } else {
            throw new RuntimeException(this.result.toString());
        }
        return person.toString();
    }

    public void init() {
    }
}
