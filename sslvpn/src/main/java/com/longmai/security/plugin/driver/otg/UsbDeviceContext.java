package com.longmai.security.plugin.driver.otg;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import com.longmai.security.plugin.device.Device;
import com.longmai.security.plugin.util.LogUtil;

public class UsbDeviceContext extends Device {
    private static final String ACTION_USB_PERMISSION = "com.longmai.USB_PERMISSION";
    private static final String TAG = UsbDeviceContext.class.getName();
    private static volatile int id = 0;
    private UsbDeviceConnection conn;
    private Context context;
    private int inEndpointType;
    private int outEndpointType;
    private PendingIntent permissionIntent;
    private UsbDevice usbDevice;
    private UsbManager usbManager;
    final BroadcastReceiver usbReceiver = new C02861();

    class C02861 extends BroadcastReceiver {
        C02861() {
        }

        public void onReceive(Context context, Intent intent) {
            LogUtil.m10d(UsbDeviceContext.TAG, "Thread: " + Thread.currentThread().getName() + " Id: " + Thread.currentThread().getId());
            if (UsbDeviceContext.ACTION_USB_PERMISSION.equals(intent.getAction())) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra("device");
                    if (!intent.getBooleanExtra("permission", false)) {
                        LogUtil.m18w(UsbDeviceContext.TAG, "permission denied for device " + device);
                        LogUtil.m18w(UsbDeviceContext.TAG, "授权失败");
                    } else if (device != null) {
                        LogUtil.m10d(UsbDeviceContext.TAG, "授权成功");
                    }
                    synchronized (UsbDeviceContext.this) {
                        UsbDeviceContext.this.notify();
                    }
                }
            }
        }
    }

    public UsbDeviceContext(Context context, UsbManager usbManager, UsbDevice usbDevice, int inEndpointType, int outEndpointType) {
        super(id + 1, usbDevice.getDeviceName(), 0);
        int i = id + 1;
        id = i;
        this.context = context;
        this.usbManager = usbManager;
        this.usbDevice = usbDevice;
        this.inEndpointType = inEndpointType;
        this.outEndpointType = outEndpointType;
    }

    public UsbManager getUsbManager() {
        return this.usbManager;
    }

    public UsbDevice getUsbDevice() {
        return this.usbDevice;
    }

    public boolean hasPermission() {
        return this.usbManager.hasPermission(this.usbDevice);
    }

    public UsbDeviceConnection openDevice() {
        UsbDeviceConnection openDevice = this.usbManager.openDevice(this.usbDevice);
        this.conn = openDevice;
        return openDevice;
    }

    public UsbDeviceConnection getUsbDeviceConnection() {
        return this.conn;
    }

    public int getInEndpointType() {
        return this.inEndpointType;
    }

    public int getOutEndpointType() {
        return this.outEndpointType;
    }

    public synchronized boolean requestPermission() {
        this.context.registerReceiver(this.usbReceiver, new IntentFilter(ACTION_USB_PERMISSION));
        this.permissionIntent = PendingIntent.getBroadcast(this.context, 0, new Intent(ACTION_USB_PERMISSION), 0);
        this.usbManager.requestPermission(this.usbDevice, this.permissionIntent);
        try {
            LogUtil.m10d(TAG, "Thread: " + Thread.currentThread().getName() + " Id: " + Thread.currentThread().getId());
            wait(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.context.unregisterReceiver(this.usbReceiver);
        return hasPermission();
    }
}
