package com.longmai.security.plugin.driver.ble.io.stack;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import com.longmai.security.plugin.base.PluginException;
import com.longmai.security.plugin.driver.ble.base.MessagePool;
import com.longmai.security.plugin.driver.conn.Connection;
import com.longmai.security.plugin.util.Hex;
import com.longmai.security.plugin.util.Int2Bytes;
import com.longmai.security.plugin.util.LogUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BleMessagePool extends MessagePool {
    private static final int BLE_DATA = 4;
    private static final int BLE_PROTOCOL = 5;
    private static final int MAX_BUFF = 20;
    private static final int SEND_ING = 1;
    private static final int SEND_OK = 0;
    private static final String TAG = BleMessagePool.class.getName();
    private static final int WAIT_ING = 2;
    private static final int WAIT_TIMEOUT = 3;
    private static final byte[] _ETX = "@ETX".getBytes();
    private static final byte[] _STX = "@STX".getBytes();
    private static volatile byte counter = Byte.MIN_VALUE;
    private static volatile int transferState;
    private ByteArrayOutputStream pool;
    private byte[] readBuffer;
    private BluetoothGatt serverGatt;
    private volatile int timeOut;
    private BluetoothGattCharacteristic writeCharacteristic;

    public void init() throws PluginException {
        LogUtil.m10d(TAG, "init()");
        this.pool = new ByteArrayOutputStream(4096);
    }

    public void setTimeOut(int timeOut) {
        LogUtil.m10d(TAG, "setTimeOut() timeOut:" + timeOut);
        this.timeOut = timeOut;
    }

    public int getTimeOut() {
        LogUtil.m10d(TAG, "getTimeOut() timeOut:" + this.timeOut);
        return this.timeOut;
    }

    public int write(byte[] apdu) throws IOException {
        return write(apdu, 0, apdu.length, 128);
    }

    public synchronized int write(byte[] apdu, int off, int len, int protocol) throws IOException {
        LogUtil.m10d(TAG, "write() - apdu:" + new String(Hex.encode(apdu, off, len)) + " protocol:" + protocol);
        byte[] cmd = encode(apdu, off, len);
        if (cmd == null) {
            throw new IOException("apdu padding exception");
        }
        switch (protocol) {
            case 0:
            case 128:
            case Connection.UART_OP_KEYEXCHG /*240*/:
            case Connection.UART_OP_INTERNALAUTH /*241*/:
            case Connection.UART_OP_DESTROYSESSION /*242*/:
            case 255:
                cmd[4] = (byte) protocol;
                for (int i = 0; i < 3; i++) {
                    byte b = (byte) (counter + 1);
                    counter = b;
                    cmd[5] = b;
                    LogUtil.m18w(TAG, new String(Hex.encode(cmd)));
                    bleSendApdu(cmd);
                    try {
                        LogUtil.m10d(TAG, "BleMessagePool.read.waiting() - timeOut:" + this.timeOut);
                        wait((long) this.timeOut);
                        LogUtil.m10d(TAG, "BleMessagePool.read.waited()");
                        return len;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    switch (transferState) {
                        case 0:
                            LogUtil.m10d(TAG, "Send success");
                        case 2:
                            LogUtil.m18w(TAG, "Send timeout " + i);
                            break;
                        case 4:
                            LogUtil.m18w(TAG, "Send failed");
                            break;
                        case 5:
                            throw new IOException("key exchange failed");
                        default:
                            break;
                    }
                }
                throw new IOException("ble transfer Exception");
            default:
                throw new IOException("apdu protocol " + protocol + " exception");
        }
        //return len;
    }

    public byte[] read() throws IOException {
        LogUtil.m10d(TAG, "read()");
        return decode(this.readBuffer, 0, this.readBuffer.length);
    }

    public void destroy() throws PluginException {
        LogUtil.m10d(TAG, "destroy()");
        this.pool = null;
    }

    public void onReceive(byte[] buffer) throws IOException {
        onReceive(buffer, 0, buffer.length);
    }

    public void onReceive(byte[] buffer, int offset, int length) throws IOException {
        LogUtil.m10d(TAG, "onReceive() - buffer:" + new String(Hex.encode(buffer, offset, length)));
        if (this.pool != null) {
            this.pool.write(buffer);
            byte[] cmd = extract();
            if (cmd == null) {
                LogUtil.m18w(TAG, "extract() is null");
                return;
            } else if (cmd[5] == counter) {
                switch (getApduErrorCode(cmd)) {
                    case 0:
                        this.readBuffer = cmd;
                        transferState = 0;
                        break;
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        transferState = 4;
                        try {
                            Thread.sleep(500);
                            break;
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                            break;
                        }
                    case 225:
                    case 226:
                    case 227:
                    case 228:
                    case 229:
                    case 239:
                        transferState = 5;
                        break;
                }
                LogUtil.m10d(TAG, "BleMessagePool.read.notifying()");
                synchronized (this) {
                    LogUtil.m10d(TAG, "BleMessagePool.read.notify()");
                    notify();
                }
                return;
            } else {
                LogUtil.m18w(TAG, "counter:" + cmd[5] + " do not match.");
                LogUtil.m18w(TAG, new String(Hex.encode(cmd)));
                return;
            }
        }
        throw new IOException();
    }

    public void setWriteCharacteristic(BluetoothGatt serverGatt, BluetoothGattCharacteristic writeCharacteristic) throws IOException {
        LogUtil.m10d(TAG, "setWriteCharacteristic()");
        if (serverGatt == null) {
            throw new IOException("ble gatt is null");
        } else if (writeCharacteristic == null) {
            throw new IOException("writeCharacteristic is null");
        } else {
            this.serverGatt = serverGatt;
            this.writeCharacteristic = writeCharacteristic;
        }
    }

    public int bleSendApdu(byte[] cmd) throws IOException {
        LogUtil.m10d(TAG, "bleSendApdu()  counter: " + counter);
        transferState = 1;
        if (this.writeCharacteristic == null) {
            throw new IOException("writeCharacteristic is null");
        }
        byte[] stx = new byte[8];
        System.arraycopy(cmd, 0, stx, 0, 8);
        this.writeCharacteristic.setValue(stx);
        if (writeCharacteristic(this.writeCharacteristic)) {
            byte[] tmp;
            byte[] data = new byte[(cmd.length - 16)];
            System.arraycopy(cmd, 8, data, 0, data.length);
            int nCount = data.length / 20;
            int nLeft = data.length % 20;
            int i = 0;
            while (i < nCount) {
                tmp = new byte[20];
                System.arraycopy(data, i * 20, tmp, 0, 20);
                this.writeCharacteristic.setValue(tmp);
                if (writeCharacteristic(this.writeCharacteristic)) {
                    i++;
                } else {
                    throw new IOException("ble transfer apdu exception");
                }
            }
            if (nLeft > 0) {
                tmp = new byte[nLeft];
                System.arraycopy(data, nCount * 20, tmp, 0, nLeft);
                this.writeCharacteristic.setValue(tmp);
                if (!writeCharacteristic(this.writeCharacteristic)) {
                    throw new IOException("ble transfer apdu exception");
                }
            }
            byte[] etx = new byte[8];
            System.arraycopy(cmd, cmd.length - 8, etx, 0, 8);
            this.writeCharacteristic.setValue(etx);
            if (writeCharacteristic(this.writeCharacteristic)) {
                transferState = 2;
                return 0;
            }
            throw new IOException("ble transfer etx exception");
        }
        throw new IOException("ble transfer stx exception");
    }

    public synchronized boolean writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        boolean flag;
        LogUtil.m10d(TAG, "writeCharacteristic()");
        flag = this.serverGatt.writeCharacteristic(characteristic);
        if (flag) {
            try {
                LogUtil.m18w(TAG, "BleMessagePool.write.waiting()");
                wait(5000);
                LogUtil.m18w(TAG, "BleMessagePool.write.waited()");
            } catch (InterruptedException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        return flag;
    }

    private byte[] encode(byte[] apdu, int off, int length) {
        LogUtil.m10d(TAG, "encode()");
        byte[] BLELen = Int2Bytes.int2byte(length, 2, false);
        byte[] CMD = new byte[((((_STX.length + 4) + length) + 4) + _ETX.length)];
        System.arraycopy(_STX, 0, CMD, 0, 4);
        System.arraycopy(BLELen, 0, CMD, 6, 2);
        System.arraycopy(apdu, off, CMD, 8, length);
        System.arraycopy(_ETX, 0, CMD, (length + 8) + 4, 4);
        return CMD;
    }

    private byte[] encodex(byte[] apdu, int off, int length) {
        LogUtil.m10d(TAG, "encodex()");
        ByteBuffer buf = ByteBuffer.allocate((((_STX.length + 4) + length) + 4) + _ETX.length);
        buf.put(_STX);
        buf.putShort((short) 0);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.putShort((short) length);
        buf.order(ByteOrder.BIG_ENDIAN);
        buf.put(apdu, off, length);
        buf.put(_ETX);
        return buf.array();
    }

    private byte[] decode(byte[] CMD, int off, int length) {
        LogUtil.m10d(TAG, "decode()");
        if (getApduErrorCode(CMD) != 0) {
            return null;
        }
        int apduLength = Int2Bytes.bytes2int(new byte[]{CMD[6], CMD[7]}, false);
        byte[] apdu = new byte[apduLength];
        System.arraycopy(CMD, 8, apdu, 0, apduLength);
        return apdu;
    }

    private int getApduErrorCode(byte[] cmd) throws RuntimeException {
        LogUtil.m10d(TAG, "getApduErrorCode() - cmd: " + new String(Hex.encode(cmd)));
        if (!new String(cmd).startsWith("@STX")) {
            return 4;
        }
        byte[] BLELen = new byte[]{cmd[6], cmd[7]};
        int _BLELen = Int2Bytes.bytes2int(BLELen, false);
        byte[] ErrorCode = new byte[4];
        try {
            ErrorCode[0] = cmd[((_STX.length + BLELen.length) + 2) + _BLELen];
            ErrorCode[1] = cmd[(((_STX.length + BLELen.length) + 2) + _BLELen) + 1];
            ErrorCode[2] = cmd[(((_STX.length + BLELen.length) + 2) + _BLELen) + 2];
            ErrorCode[3] = cmd[(((_STX.length + BLELen.length) + 2) + _BLELen) + 3];
            return Int2Bytes.bytes2int(ErrorCode, false);
        } catch (Exception e) {
            e.printStackTrace();
            return 4;
        }
    }

    private byte[] extract() {
        LogUtil.m10d(TAG, "extract()");
        byte[] buffer = this.pool.toByteArray();
        int endIndex = indexOf(buffer, _ETX);
        if (endIndex == -1) {
            return null;
        }
        int remainderLength = buffer.length - (endIndex + 4);
        int beginIndex = lastIndexOf(buffer, _STX, endIndex);
        if (beginIndex == -1) {
            this.pool.reset();
            this.pool.write(buffer, endIndex + 4, remainderLength);
            if (remainderLength >= 16) {
                return extract();
            }
            return null;
        }
        ByteArrayOutputStream _tmp = new ByteArrayOutputStream();
        _tmp.write(buffer, beginIndex, (endIndex - beginIndex) + 4);
        this.pool.reset();
        this.pool.write(buffer, endIndex + 4, remainderLength);
        return _tmp.toByteArray();
    }

    private int indexOf(byte[] buffer, byte[] value) {
        int buff_len = buffer.length - value.length;
        int val_len = value.length;
        int i = 0;
        while (i <= buff_len) {
            if (buffer[i] == value[0]) {
                if (val_len <= 1) {
                    return i;
                }
                int j = 1;
                while (j < val_len && buffer[i + j] == value[j]) {
                    if (j == val_len - 1) {
                        return i;
                    }
                    j++;
                }
            }
            i++;
        }
        return -1;
    }

    private int lastIndexOf(byte[] buffer, byte[] value, int fromIndex) {
        int val_len = value.length;
        if (fromIndex > buffer.length - val_len) {
            fromIndex = buffer.length - val_len;
        }
        int i = fromIndex;
        while (i >= 0) {
            if (buffer[i] == value[0]) {
                if (val_len <= 1) {
                    return i;
                }
                int j = 1;
                while (j < val_len && buffer[i + j] == value[j]) {
                    if (j == val_len - 1) {
                        return i;
                    }
                    j++;
                }
            }
            i--;
        }
        return -1;
    }
}
