package com.longmai.security.plugin.driver.ble.base;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import java.io.IOException;

public interface Transmitter {
    void setWriteCharacteristic(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) throws IOException;
}
