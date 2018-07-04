package com.longmai.mtoken.interfaces.kernel.func.base;

import android.content.Context;
import com.longmai.mtoken.interfaces.kernel.func.Function_AppLib;
import com.longmai.mtoken.interfaces.kernel.func.Function_DeviceLib;
import com.longmai.security.plugin.SOF_DeviceLib;
import org.json.JSONException;
import org.json.JSONObject;

public class FunctionFactory {
    public static final String PACKAGE_SOF_AppLib = "com.longmai.security.plugin.SOF_AppLib";
    public static final String PACKAGE_SOF_DeviceLib = "com.longmai.security.plugin.SOF_DeviceLib";
    private Context context;

    public FunctionFactory(Context context) {
        this.context = context;
        SOF_DeviceLib.SOF_LoadLibrary(context, "0", "mToken K5", "com.longmai.security.plugin.driver.ble.BLEDriver", "BLE 蓝牙  Key", new String[0]);
    }

    public Function getFunction(String json) throws JSONException {
        Function function = null;
        String _package = new JSONObject(json).getString("package");
        if (_package.equals(PACKAGE_SOF_DeviceLib)) {
            function = new Function_DeviceLib(this.context, json);
        }
        if (_package.equals(PACKAGE_SOF_AppLib)) {
            return new Function_AppLib(json);
        }
        return function;
    }

    public Function getFunction(byte[] json) throws JSONException {
        return getFunction(new String(json));
    }
}
