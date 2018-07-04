package com.longmai.security.plugin.driver.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build.VERSION;
import android.text.TextUtils;

import com.longmai.security.plugin.base.PluginException;
import com.longmai.security.plugin.device.Device;
import com.longmai.security.plugin.device.DeviceManager;
import com.longmai.security.plugin.driver.conn.Connection;
import com.longmai.security.plugin.util.Hex;
import com.longmai.security.plugin.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class DeviceManagerImple implements DeviceManager, LeScanCallback {
    private static final String TAG = DeviceManagerImple.class.getName();
    private static final int currentapiVersion = VERSION.SDK_INT;
    private Connection conn;
    private Context context;
    private Map<String, BluetoothDevice> devices = new ConcurrentHashMap();
    private String findByName;
    private BluetoothAdapter mBluetoothAdapter;

    public DeviceManagerImple(Context context) throws PluginException {
        if (currentapiVersion < 18) {
            throw new PluginException(5);
        } else if (context == null) {
            throw new PluginException("Context is null");
        } else {
            this.context = context;
            this.mBluetoothAdapter = ((BluetoothManager) context.getSystemService("bluetooth")).getAdapter();
            if (this.mBluetoothAdapter == null) {
                throw new PluginException(5);
            }
        }
    }

    public List<Device> find(String... parameter) throws PluginException {
        return find(3000, parameter);
    }

    public synchronized List<Device> find(int timeOut, String... parameter) throws PluginException {
        List<Device> list = new ArrayList<>();
        LogUtil.m18w(TAG, "find() - timeOut:" + timeOut + " Thread: " + Thread.currentThread().getName() + " Id: " + Thread.currentThread().getId());
        this.devices.clear();
        if (parameter.length > 0) {
            this.findByName = parameter[0];
        } else {
            this.findByName = null;
        }
        if (this.conn != null && this.conn.isValid()) {
            BluetoothDevice device = (BluetoothDevice) this.conn.getValue(0);
            this.devices.put(device.getName(), device);
            if (device.getName().equals(this.findByName)) {
                list = new ArrayList();
                list.add(new Device(0, device.getName(), 128));
            }
        }
        try {
            if (this.mBluetoothAdapter.isEnabled()) {
                this.mBluetoothAdapter.startLeScan(this);
                try {
                    LogUtil.m18w(TAG, "DeviceManagerImple.find.waiting()");
                    wait((long) timeOut);
                    LogUtil.m18w(TAG, "DeviceManagerImple.find.waited()");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.mBluetoothAdapter.stopLeScan(this);
                LogUtil.m10d(TAG, "find() - devices.size():" + this.devices.size());
                if (this.findByName == null || this.devices.containsKey(this.findByName)) {
                    list = new ArrayList();
                    for (Entry<String, BluetoothDevice> entry : this.devices.entrySet()) {
                        String key = (String) entry.getKey();
                        list.add(new Device(0, key, 128));
                        LogUtil.m10d(TAG, "find() - list.add() - " + key);
                    }
                    LogUtil.m10d(TAG, "find() - list.size():" + list.size());
                    return list;
                } else {
                    throw new PluginException(12);
                }
            }
            throw new PluginException(17);
        } catch (SecurityException e2) {
            throw new PluginException(17);
        }
    }

    public synchronized Connection getConnection(Device device) throws PluginException {
        Connection connection;
        boolean z = false;
        synchronized (this) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder("getConnection() ");
            if (this.conn == null) {
                z = true;
            }
            LogUtil.m10d(str, stringBuilder.append(z).toString());
            BluetoothDevice dev = (BluetoothDevice) this.devices.get(device.getName());
            if (dev == null) {
                dev = this.mBluetoothAdapter.getRemoteDevice(device.getName());
            }
            if (this.conn != null && this.conn.isValid()) {
                if (((BluetoothDevice) this.conn.getValue(0)).getName().trim().equals(device.getName())) {
                    connection = this.conn;
                } else {
                    this.conn.close();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            connection = new ConnectionImpl(this.context, dev);
            this.conn = connection;
        }
        return connection;
    }

    public Connection getConnection(Device device, int timeOut) throws PluginException {
        return getConnection(device);
    }

    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        LogUtil.m18w(TAG, "onLeScan() - name: " + device.getName() + " mac: " + device.getAddress() + " scanRecord: " + new String(Hex.encode(scanRecord)) + " Thread: " + Thread.currentThread().getName() + " Id: " + Thread.currentThread().getId());
        String devName = device.getName();
        if (TextUtils.isEmpty(devName)) {
            devName = device.getAddress();
        }
        this.devices.put(devName, device);
        LogUtil.m10d(TAG, "onLeScan() - devices: " + this.devices.size());
        if (this.findByName != null && this.findByName.equals(devName)) {
            synchronized (this) {
                notify();
            }
        }
    }
}
