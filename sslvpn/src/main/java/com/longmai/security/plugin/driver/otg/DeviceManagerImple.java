package com.longmai.security.plugin.driver.otg;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import com.longmai.security.plugin.base.PluginException;
import com.longmai.security.plugin.device.Device;
import com.longmai.security.plugin.device.DeviceManager;
import com.longmai.security.plugin.driver.conn.Connection;
import com.longmai.security.plugin.util.LogUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class DeviceManagerImple implements DeviceManager {
    private static final String ACTION_USB_PERMISSION = "com.longmai.USB_PERMISSION";
    private static final String TAG = DeviceManagerImple.class.getName();
    private Connection conn;
    private Context context;
    private HashMap<String, UsbDevice> devices = new HashMap();
    private UsbManager usbManager;

    public DeviceManagerImple(Context context) throws PluginException {
        if (context == null) {
            throw new PluginException("Context is null");
        }
        this.context = context;
        this.usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
    }

    public List<Device> find(int timeOut, String... parameter) throws PluginException {
        return find(parameter);
    }

    public List<Device> find(String... parameter) throws PluginException {
        LogUtil.m10d(TAG, "find(...)");
        if (this.usbManager == null) {
            throw new PluginException("UsbManager is null");
        }
        List<Device> list = new ArrayList();
        this.devices = this.usbManager.getDeviceList();
        for (Entry<String, UsbDevice> entry : this.devices.entrySet()) {
            String key = (String) entry.getKey();
            UsbDevice usbDevice = (UsbDevice) entry.getValue();
            if (Constant.VIDs.containsKey(Integer.valueOf(usbDevice.getVendorId()))) {
                list.add(new UsbDeviceContext(this.context, this.usbManager, usbDevice, ((int[]) Constant.VIDs.get(Integer.valueOf(usbDevice.getVendorId())))[0], ((int[]) Constant.VIDs.get(Integer.valueOf(usbDevice.getVendorId())))[1]));
            }
        }
        return list;
    }

    public Connection getConnection(Device device) throws PluginException {
        return getConnection(device, 0);
    }

    public Connection getConnection(Device device, int timeout) throws PluginException {
        LogUtil.m10d(TAG, "getConnection()");
        UsbDeviceContext usbContext = (UsbDeviceContext) device;
        if (this.conn != null) {
            this.conn.close();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (usbContext.hasPermission()) {
            LogUtil.m10d(TAG, "hasPermission Yes");
            Connection connectionImpl = new ConnectionImpl(usbContext);
            this.conn = connectionImpl;
            return connectionImpl;
        }
        LogUtil.m18w(TAG, "hasPermission No");
        if (usbContext.requestPermission()) {
            ConnectionImpl connectionImpl = new ConnectionImpl(usbContext);
            this.conn = connectionImpl;
            return connectionImpl;
        }
        throw new PluginException(17);
    }
}
