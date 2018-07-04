package com.longmai.security.plugin.driver.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import com.longmai.security.plugin.base.PluginException;
import com.longmai.security.plugin.driver.ble.base.GattAttributesUUID;
import com.longmai.security.plugin.driver.ble.base.MessagePool;
import com.longmai.security.plugin.driver.ble.io.BLEInputStream;
import com.longmai.security.plugin.driver.ble.io.BLEOutputStream;
import com.longmai.security.plugin.driver.ble.io.stack.BleMessagePool;
import com.longmai.security.plugin.driver.conn.Connection;
import com.longmai.security.plugin.driver.conn.SecurityConnection;
import com.longmai.security.plugin.util.LogUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ConnectionImpl extends BluetoothGattCallback implements SecurityConnection {
    public static final String Action_ConnectionState = "com.android.longmai.ble";
    public static final int Client = 0;
    private static final String TAG = ConnectionImpl.class.getName();
    private volatile int connState;
    private Context context;
    private BluetoothDevice device;
    private volatile boolean isDiscoverServices;
    private MessagePool messagePool;
    private BluetoothGatt serverGatt;
    private BluetoothGattCharacteristic writeCharacteristic;

    public ConnectionImpl(BluetoothDevice device) throws PluginException {
        this.messagePool = new BleMessagePool();
        this.connState = 0;
        this.isDiscoverServices = false;
        this.device = device;
        BluetoothGatt gatt = device.connectGatt(this.context, false, this);
        if (gatt == null) {
            throw new PluginException(13);
        }
        this.serverGatt = gatt;
        if (this.connState != 2) {
            synchronized (this) {
                try {
                    LogUtil.m18w(TAG, "ConnectionImpl.connectGatt.waiting()");
                    wait(10000);
                    LogUtil.m18w(TAG, "ConnectionImpl.connectGatt.waited()");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if (this.connState != 2) {
            throw new PluginException(13);
        }
        this.isDiscoverServices = false;
        this.serverGatt.discoverServices();
        if (!this.isDiscoverServices) {
            synchronized (this) {
                try {
                    LogUtil.m18w(TAG, "ConnectionImpl.discoverServices.waiting()");
                    wait(10000);
                    LogUtil.m18w(TAG, "ConnectionImpl.discoverServices.waited()");
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        List<BluetoothGattService> services = this.serverGatt.getServices();
        if (services == null) {
            LogUtil.m18w(TAG, "getServices() Services is null");
            close();
            throw new PluginException(6);
        }
        BluetoothGattCharacteristic characteristic;
        HashMap<String, BluetoothGattCharacteristic> characteristicGroup = new HashMap();
        for (BluetoothGattService service : services) {
            if (service.getUuid().toString().equals(GattAttributesUUID.SERVICE_ID)) {
                List<BluetoothGattCharacteristic> gattCharacteristics = service.getCharacteristics();
                if (gattCharacteristics == null || gattCharacteristics.size() == 0) {
                    LogUtil.m18w(TAG, "getCharacteristics() Characteristics is null");
                    close();
                    throw new PluginException(6);
                }
                for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                    characteristicGroup.put(gattCharacteristic.getUuid().toString(), gattCharacteristic);
                }
                this.writeCharacteristic = (BluetoothGattCharacteristic) characteristicGroup.get(GattAttributesUUID.WRITE_UUID);
                for (String notify_UUID : GattAttributesUUID.notifyAttributes) {
                    characteristic = (BluetoothGattCharacteristic) characteristicGroup.get(notify_UUID);
                    if (characteristic == null) {
                        LogUtil.m18w(TAG, "No find Characteristic " + notify_UUID);
                        close();
                        throw new PluginException(6);
                    } else if (!setCharacteristicNotification(characteristic, true)) {
                        LogUtil.m18w(TAG, "set Characteristic " + notify_UUID + " failure");
                        close();
                        throw new PluginException(6);
                    }
                }
                try {
                    this.messagePool.setWriteCharacteristic(this.serverGatt, this.writeCharacteristic);
                    this.messagePool.write(new byte[]{(byte) 1}, 0, 1, 255);
                    if (this.messagePool.read()[0] != (byte) 1) {
                        close();
                        throw new PluginException(6);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        this.writeCharacteristic = (BluetoothGattCharacteristic) characteristicGroup.get(GattAttributesUUID.WRITE_UUID);
        for (String notify_UUID2 : GattAttributesUUID.notifyAttributes) {
            characteristic = (BluetoothGattCharacteristic) characteristicGroup.get(notify_UUID2);
            if (characteristic == null) {
                LogUtil.m18w(TAG, "No find Characteristic " + notify_UUID2);
                close();
                throw new PluginException(6);
            } else if (setCharacteristicNotification(characteristic, true)) {
                LogUtil.m18w(TAG, "set Characteristic " + notify_UUID2 + " failure");
                close();
                throw new PluginException(6);
            }
        }
        try {
            this.messagePool.setWriteCharacteristic(this.serverGatt, this.writeCharacteristic);
            this.messagePool.write(new byte[]{(byte) 1}, 0, 1, 255);
            if (this.messagePool.read()[0] != (byte) 1) {
                close();
                throw new PluginException(6);
            }
        } catch (IOException e3) {
            e3.printStackTrace();
            close();
            throw new PluginException(11);
        }
    }

    public ConnectionImpl(Context context, BluetoothDevice device) throws PluginException {
        this(device);
        this.context = context;
    }

    public InputStream getInputStream() throws PluginException {
        LogUtil.m10d(TAG, "getInputStream()");
        return new BLEInputStream();
    }

    public OutputStream getOutputStream() throws PluginException {
        LogUtil.m10d(TAG, "getOutputStream()");
        return new BLEOutputStream();
    }

    public boolean isValid() {
        LogUtil.m10d(TAG, "isValid() - connState:" + this.connState);
        return this.connState == 2;
    }

    public void close() throws PluginException {
        LogUtil.m10d(TAG, "close()");
        if (this.connState == 2) {
            this.serverGatt.disconnect();
            synchronized (this) {
                int i = 0;
                while (i < 30) {
                    try {
                        LogUtil.m18w(TAG, "ConnectionImpl.disconnect.waiting()");
                        wait(100);
                        LogUtil.m18w(TAG, "ConnectionImpl.disconnect.waited()");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (this.connState == 2) {
                        i++;
                    }
                }
            }
        }
    }

    public synchronized int deviceio(byte[] input, int input_len, byte[] output, int[] output_len, int protocol) throws PluginException {
        int i = 0;
        synchronized (this) {
            LogUtil.m10d(TAG, "deviceio()");
            try {
                this.messagePool.write(input, 0, input_len, protocol);
                byte[] apdu = this.messagePool.read();
                if (apdu == null) {
                    i = 1;
                } else if (output.length < apdu.length) {
                    throw new PluginException(2);
                } else {
                    System.arraycopy(apdu, 0, output, 0, apdu.length);
                    output_len[0] = apdu.length;
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new PluginException(15);
            }
        }
        return i;
    }

    public void setValue(int type, Object value) throws PluginException {
        LogUtil.m10d(TAG, "setValue()");
    }

    public BluetoothDevice getValue(int type) throws PluginException {
        LogUtil.m10d(TAG, "getValue() - type: " + type);
        if (type == 0) {
            return this.device;
        }
        return null;
    }

    public byte[] deriveSecurityCommSession(byte[] apdu) throws PluginException {
        LogUtil.m10d(TAG, "deriveSecurityCommSession()");
        try {
            this.messagePool.write(apdu, 0, apdu.length, Connection.UART_OP_KEYEXCHG);
            try {
                return this.messagePool.read();
            } catch (IOException e) {
                e.printStackTrace();
                throw new PluginException(15);
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            throw new PluginException(14);
        }
    }

    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        LogUtil.m18w(TAG, "onConnectionStateChange() - status:" + status + " newState:" + newState);
        this.connState = newState;
        switch (newState) {
            case 0:
                if (status == 133) {
                    this.serverGatt.disconnect();
                    LogUtil.m10d(TAG, "serverGatt.disconnect()");
                }
                if (this.serverGatt != null) {
                    this.serverGatt.close();
                }
                try {
                    this.messagePool.destroy();
                } catch (PluginException e) {
                    e.printStackTrace();
                }
                synchronized (this) {
                    LogUtil.m18w(TAG, "ConnectionImpl.disconnect.notify()");
                    notify();
                }
                break;
            case 2:
                try {
                    this.messagePool.init();
                } catch (PluginException e2) {
                    e2.printStackTrace();
                }
                this.messagePool.setTimeOut(10000);
                synchronized (this) {
                    LogUtil.m18w(TAG, "ConnectionImpl.connectGatt.notify()");
                    notify();
                }
                break;
        }
        if (this.context != null) {
            this.context.sendBroadcast(new Intent().setAction(Action_ConnectionState).putExtra("status", status).putExtra("newState", newState).putExtra("devName", this.device.getName()));
        }
    }

    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        LogUtil.m10d(TAG, "onServicesDiscovered()");
        this.isDiscoverServices = true;
        synchronized (this) {
            LogUtil.m18w(TAG, "ConnectionImpl.discoverServices.notify()");
            notify();
        }
    }

    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        long l = System.nanoTime();
        LogUtil.m10d(TAG, "onCharacteristicChanged() - random: " + UUID.randomUUID().toString());
        if (this.messagePool != null) {
            try {
                this.messagePool.onReceive(characteristic.getValue());
            } catch (IOException e) {
                LogUtil.m13e(TAG, "onReceive() pool.write()", e);
            }
        } else {
            LogUtil.m10d(TAG, "receive is null");
        }
        LogUtil.m12e(TAG, "end - " + ((System.nanoTime() - l) / 1000000));
    }

    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        LogUtil.m10d(TAG, "onCharacteristicWrite() - " + characteristic.getUuid().toString() + " " + status);
        synchronized (this.messagePool) {
            LogUtil.m18w(TAG, "BleMessagePool.write.notify()");
            this.messagePool.notify();
        }
    }

    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        LogUtil.m10d(TAG, "onDescriptorWrite() - " + descriptor.getUuid().toString() + " " + status);
        synchronized (this) {
            LogUtil.m18w(TAG, "ConnectionImpl.writeDescriptor.notify()");
            notify();
        }
    }

    private boolean setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        LogUtil.m10d(TAG, "setCharacteristicNotification()");
        boolean flag = this.serverGatt.setCharacteristicNotification(characteristic, enabled);
        if (!flag) {
            return flag;
        }
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(GattAttributesUUID.CONFIG));
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        flag = this.serverGatt.writeDescriptor(descriptor);
        if (!flag) {
            return flag;
        }
        synchronized (this) {
            try {
                LogUtil.m18w(TAG, "ConnectionImpl.writeDescriptor.waiting()");
                wait();
                LogUtil.m18w(TAG, "ConnectionImpl.writeDescriptor.waited()");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    public int deviceio(byte[] input, int input_len, byte[] output, int[] output_len) throws PluginException {
        return deviceio(input, input_len, output, output_len, 128);
    }

    public void setTimeOut(int timeOut) {
        LogUtil.m10d(TAG, "setTimeOut()");
        this.messagePool.setTimeOut(timeOut);
    }

    public int getTimeOut() {
        LogUtil.m10d(TAG, "getTimeOut()");
        return this.messagePool.getTimeOut();
    }
}
