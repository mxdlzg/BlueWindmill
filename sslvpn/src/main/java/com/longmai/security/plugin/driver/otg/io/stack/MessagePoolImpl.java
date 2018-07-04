package com.longmai.security.plugin.driver.otg.io.stack;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbRequest;
import com.longmai.security.plugin.base.PluginException;
import com.longmai.security.plugin.driver.otg.UsbDeviceContext;
import com.longmai.security.plugin.util.Hex;
import com.longmai.security.plugin.util.Int2Bytes;
import com.longmai.security.plugin.util.LogUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class MessagePoolImpl implements MessagePool, Runnable {
    private static final int DATA_BAD = 7;
    private static final int MAX_BUFF = 64;
    private static final int MSG_PROTOCOL = 8;
    private static final int RECEIVCE_FAIL = 9;
    private static final int RECEIVCE_ING = 5;
    private static final int RECEIVCE_OK = 3;
    private static final int RECEIVCE_WAIT = 4;
    private static final int SEND_FAIL = 1;
    private static final int SEND_ING = 2;
    private static final int SEND_OK = 0;
    private static final int SEND_TIMEOUT = 6;
    private static final String TAG = MessagePoolImpl.class.getName();
    private static volatile byte counter = Byte.MIN_VALUE;
    private static volatile int msgState;
    private UsbDeviceConnection connection;
    private UsbDeviceContext context;
    private UsbEndpoint inEndpoint;
    private UsbEndpoint outEndpoint;
    private ByteArrayOutputStream pool;
    private volatile boolean runFlag;
    private int timeOut = 0;
    private UsbDevice usbDevice;
    private UsbInterface usbInterface;

    public MessagePoolImpl(UsbDeviceConnection connection, UsbDevice usbDevice) {
        this.connection = connection;
        this.usbDevice = usbDevice;
    }

    public MessagePoolImpl(UsbDeviceContext context) {
        this.context = context;
        this.connection = context.getUsbDeviceConnection();
        this.usbDevice = context.getUsbDevice();
    }

    public void init() throws PluginException {
        LogUtil.m10d(TAG, "init()");
        this.pool = new ByteArrayOutputStream(4096);
        int interfaceCount = this.usbDevice.getInterfaceCount();
        if (interfaceCount <= 0) {
            throw new PluginException("UsbDevice Interface Count " + interfaceCount);
        }
        for (int i = 0; i < interfaceCount; i++) {
            UsbInterface _usbInterface = this.usbDevice.getInterface(i);
            LogUtil.m10d(TAG, "InterfaceClass:" + _usbInterface.getInterfaceClass());
            LogUtil.m10d(TAG, "InterfaceSubclass:" + _usbInterface.getInterfaceSubclass());
            if (_usbInterface.getInterfaceClass() == 3) {
                this.usbInterface = _usbInterface;
                int endpointCount = this.usbInterface.getEndpointCount();
                if (endpointCount < 1) {
                    throw new PluginException("UsbInterface Endpoint Count " + endpointCount);
                }
                for (int j = 0; j < endpointCount; j++) {
                    UsbEndpoint point = this.usbInterface.getEndpoint(j);
                    int nDirection = point.getDirection();
                    if (point.getType() == 3) {
                        if (nDirection == 0) {
                            this.outEndpoint = point;
                        } else if (nDirection == 128) {
                            this.inEndpoint = point;
                        }
                    }
                }
                if (!this.connection.claimInterface(this.usbInterface, true)) {
                    throw new PluginException("message pool init exception");
                } else if (this.inEndpoint != null) {
                    Thread receive = new Thread(this);
                    receive.start();
                    LogUtil.m10d(TAG, "receive.start() - Thread: " + receive.getName() + " Id: " + receive.getId());
                }
            }
        }
    }

    public int getTimeOut() {
        LogUtil.m10d(TAG, "getTimeOut()");
        return this.timeOut;
    }

    public void setTimeOut(int timeOut) {
        LogUtil.m10d(TAG, "setTimeOut() timeOut:" + timeOut);
        this.timeOut = timeOut;
    }

    public int write(byte[] apdu) throws IOException {
        LogUtil.m10d(TAG, "write()");
        return write(apdu, 0, apdu.length);
    }

    public synchronized int write(byte[] apdu, int off, int len) throws IOException {
        LogUtil.m10d(TAG, "writeEx()");
        msgState = 2;
        int nCount = len / 63;
        int nLast = len % 63;
        byte[] nLen = Int2Bytes.int2byte(len, 2, false);
        byte[] tmp = new byte[64];
        tmp[0] = (byte) -108;
        tmp[1] = (byte) -2;
        tmp[2] = (byte) 1;
        tmp[17] = nLen[0];
        tmp[18] = nLen[1];
        byte b = (byte) (counter + 1);
        counter = b;
        tmp[20] = b;
        LogUtil.m10d(TAG, "Head: " + new String(Hex.encode(tmp)));
        if (this.connection.controlTransfer(33, 9, 512, 0, tmp, 64, this.timeOut) <= 0) {
            throw new IOException("control transfer exception 0x80");
        }
        for (int i = 0; i < nCount; i++) {
            tmp = new byte[64];
            System.arraycopy(apdu, (i * 63) + off, tmp, 1, 63);
            tmp[0] = (byte) 63;
            LogUtil.m10d(TAG, "Body: " + new String(Hex.encode(tmp)));
            if (this.connection.controlTransfer(33, 9, 512, 0, tmp, 64, this.timeOut) <= 0) {
                throw new IOException("control transfer exception");
            }
        }
        tmp = new byte[64];
        System.arraycopy(apdu, (nCount * 63) + off, tmp, 1, nLast);
        tmp[0] = (byte) (nLast | 64);
        LogUtil.m10d(TAG, "Tail: " + new String(Hex.encode(tmp)));
        if (this.connection.controlTransfer(33, 9, 512, 0, tmp, 64, this.timeOut) <= 0) {
            throw new IOException("control transfer exception 0x40");
        }
        msgState = 0;
        return len;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized byte[] read() throws java.io.IOException, java.util.concurrent.TimeoutException {
        /*
        r19 = this;
        monitor-enter(r19);
        r2 = TAG;	 Catch:{ all -> 0x0055 }
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0055 }
        r4 = "read() - msgState:";
        r3.<init>(r4);	 Catch:{ all -> 0x0055 }
        r4 = msgState;	 Catch:{ all -> 0x0055 }
        r3 = r3.append(r4);	 Catch:{ all -> 0x0055 }
        r4 = ". Thread: ";
        r3 = r3.append(r4);	 Catch:{ all -> 0x0055 }
        r4 = java.lang.Thread.currentThread();	 Catch:{ all -> 0x0055 }
        r4 = r4.getName();	 Catch:{ all -> 0x0055 }
        r3 = r3.append(r4);	 Catch:{ all -> 0x0055 }
        r4 = " Id: ";
        r3 = r3.append(r4);	 Catch:{ all -> 0x0055 }
        r4 = java.lang.Thread.currentThread();	 Catch:{ all -> 0x0055 }
        r4 = r4.getId();	 Catch:{ all -> 0x0055 }
        r3 = r3.append(r4);	 Catch:{ all -> 0x0055 }
        r3 = r3.toString();	 Catch:{ all -> 0x0055 }
        com.longmai.security.plugin.util.LogUtil.m10d(r2, r3);	 Catch:{ all -> 0x0055 }
        r0 = r19;
        r2 = r0.inEndpoint;	 Catch:{ all -> 0x0055 }
        if (r2 == 0) goto L_0x0080;
    L_0x0041:
        r2 = msgState;	 Catch:{ all -> 0x0055 }
        switch(r2) {
            case 0: goto L_0x0058;
            case 1: goto L_0x0068;
            case 2: goto L_0x0058;
            case 3: goto L_0x0046;
            case 4: goto L_0x0058;
            case 5: goto L_0x0058;
            case 6: goto L_0x0068;
            case 7: goto L_0x0078;
            case 8: goto L_0x0078;
            case 9: goto L_0x0070;
            default: goto L_0x0046;
        };	 Catch:{ all -> 0x0055 }
    L_0x0046:
        r2 = msgState;	 Catch:{ all -> 0x0055 }
        r3 = 3;
        if (r2 == r3) goto L_0x0107;
    L_0x004b:
        r2 = msgState;	 Catch:{ all -> 0x0055 }
        if (r2 != 0) goto L_0x00ff;
    L_0x004f:
        r2 = new java.util.concurrent.TimeoutException;	 Catch:{ all -> 0x0055 }
        r2.<init>();	 Catch:{ all -> 0x0055 }
        throw r2;	 Catch:{ all -> 0x0055 }
    L_0x0055:
        r2 = move-exception;
        monitor-exit(r19);
        throw r2;
    L_0x0058:
        r0 = r19;
        r2 = r0.timeOut;	 Catch:{ InterruptedException -> 0x0063 }
        r2 = (long) r2;	 Catch:{ InterruptedException -> 0x0063 }
        r0 = r19;
        r0.wait(r2);	 Catch:{ InterruptedException -> 0x0063 }
        goto L_0x0046;
    L_0x0063:
        r13 = move-exception;
        r13.printStackTrace();	 Catch:{ all -> 0x0055 }
        goto L_0x0046;
    L_0x0068:
        r2 = new java.io.IOException;	 Catch:{ all -> 0x0055 }
        r3 = "control transfer send exception";
        r2.<init>(r3);	 Catch:{ all -> 0x0055 }
        throw r2;	 Catch:{ all -> 0x0055 }
    L_0x0070:
        r2 = new java.io.IOException;	 Catch:{ all -> 0x0055 }
        r3 = "control transfer receivce exception";
        r2.<init>(r3);	 Catch:{ all -> 0x0055 }
        throw r2;	 Catch:{ all -> 0x0055 }
    L_0x0078:
        r2 = new java.io.IOException;	 Catch:{ all -> 0x0055 }
        r3 = "data exception";
        r2.<init>(r3);	 Catch:{ all -> 0x0055 }
        throw r2;	 Catch:{ all -> 0x0055 }
    L_0x0080:
        r2 = 5;
        msgState = r2;	 Catch:{ all -> 0x0055 }
    L_0x0083:
        r2 = 64;
        r7 = new byte[r2];	 Catch:{ all -> 0x0055 }
        r0 = r19;
        r2 = r0.connection;	 Catch:{ all -> 0x0055 }
        r3 = 161; // 0xa1 float:2.26E-43 double:7.95E-322;
        r4 = 1;
        r5 = 512; // 0x200 float:7.175E-43 double:2.53E-321;
        r6 = 0;
        r8 = 64;
        r0 = r19;
        r9 = r0.timeOut;	 Catch:{ all -> 0x0055 }
        r16 = r2.controlTransfer(r3, r4, r5, r6, r7, r8, r9);	 Catch:{ all -> 0x0055 }
        if (r16 > 0) goto L_0x00a9;
    L_0x009d:
        r2 = 9;
        msgState = r2;	 Catch:{ all -> 0x0055 }
        r2 = new java.io.IOException;	 Catch:{ all -> 0x0055 }
        r3 = "control transfer read failed";
        r2.<init>(r3);	 Catch:{ all -> 0x0055 }
        throw r2;	 Catch:{ all -> 0x0055 }
    L_0x00a9:
        r2 = TAG;	 Catch:{ all -> 0x0055 }
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0055 }
        r4 = "Read : ";
        r3.<init>(r4);	 Catch:{ all -> 0x0055 }
        r4 = new java.lang.String;	 Catch:{ all -> 0x0055 }
        r5 = 0;
        r0 = r16;
        r5 = com.longmai.security.plugin.util.Hex.encode(r7, r5, r0);	 Catch:{ all -> 0x0055 }
        r4.<init>(r5);	 Catch:{ all -> 0x0055 }
        r3 = r3.append(r4);	 Catch:{ all -> 0x0055 }
        r3 = r3.toString();	 Catch:{ all -> 0x0055 }
        com.longmai.security.plugin.util.LogUtil.m10d(r2, r3);	 Catch:{ all -> 0x0055 }
        r2 = 0;
        r18 = r7[r2];	 Catch:{ all -> 0x0055 }
        r2 = r18 >> 7;
        r14 = r2 & 1;
        r2 = r18 >> 6;
        r15 = r2 & 1;
        r12 = r18 & 63;
        if (r12 != 0) goto L_0x00dd;
    L_0x00d8:
        r2 = 10;
        java.lang.Thread.sleep(r2);	 Catch:{ InterruptedException -> 0x00fa }
    L_0x00dd:
        r2 = 1;
        if (r14 != r2) goto L_0x00ea;
    L_0x00e0:
        r2 = 5;
        msgState = r2;	 Catch:{ all -> 0x0055 }
        r0 = r19;
        r2 = r0.pool;	 Catch:{ all -> 0x0055 }
        r2.reset();	 Catch:{ all -> 0x0055 }
    L_0x00ea:
        r0 = r19;
        r2 = r0.pool;	 Catch:{ all -> 0x0055 }
        r3 = 1;
        r2.write(r7, r3, r12);	 Catch:{ all -> 0x0055 }
        r2 = 1;
        if (r15 != r2) goto L_0x0083;
    L_0x00f5:
        r2 = 3;
        msgState = r2;	 Catch:{ all -> 0x0055 }
        goto L_0x0046;
    L_0x00fa:
        r13 = move-exception;
        r13.printStackTrace();	 Catch:{ all -> 0x0055 }
        goto L_0x00dd;
    L_0x00ff:
        r2 = new java.io.IOException;	 Catch:{ all -> 0x0055 }
        r3 = "control transfer receivce exception";
        r2.<init>(r3);	 Catch:{ all -> 0x0055 }
        throw r2;	 Catch:{ all -> 0x0055 }
    L_0x0107:
        r0 = r19;
        r2 = r0.pool;	 Catch:{ all -> 0x0055 }
        r11 = r2.toByteArray();	 Catch:{ all -> 0x0055 }
        r2 = TAG;	 Catch:{ all -> 0x0055 }
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0055 }
        r4 = "read() - ";
        r3.<init>(r4);	 Catch:{ all -> 0x0055 }
        r4 = new java.lang.String;	 Catch:{ all -> 0x0055 }
        r5 = com.longmai.security.plugin.util.Hex.encode(r11);	 Catch:{ all -> 0x0055 }
        r4.<init>(r5);	 Catch:{ all -> 0x0055 }
        r3 = r3.append(r4);	 Catch:{ all -> 0x0055 }
        r3 = r3.toString();	 Catch:{ all -> 0x0055 }
        android.util.Log.d(r2, r3);	 Catch:{ all -> 0x0055 }
        r2 = 0;
        r2 = r11[r2];	 Catch:{ all -> 0x0055 }
        r3 = -86;
        if (r2 != r3) goto L_0x013a;
    L_0x0133:
        r2 = 1;
        r2 = r11[r2];	 Catch:{ all -> 0x0055 }
        r3 = -86;
        if (r2 == r3) goto L_0x013d;
    L_0x013a:
        r2 = 0;
    L_0x013b:
        monitor-exit(r19);
        return r2;
    L_0x013d:
        r2 = 19;
        r10 = r11[r2];	 Catch:{ all -> 0x0055 }
        r2 = counter;	 Catch:{ all -> 0x0055 }
        if (r10 == r2) goto L_0x014d;
    L_0x0145:
        r2 = new java.io.IOException;	 Catch:{ all -> 0x0055 }
        r3 = "counter exception";
        r2.<init>(r3);	 Catch:{ all -> 0x0055 }
        throw r2;	 Catch:{ all -> 0x0055 }
    L_0x014d:
        r2 = 2;
        r3 = 2;
        r4 = 0;
        r17 = com.longmai.security.plugin.util.Int2Bytes.bytes2int(r11, r2, r3, r4);	 Catch:{ all -> 0x0055 }
        r2 = r11.length;	 Catch:{ all -> 0x0055 }
        r3 = r17 + 20;
        if (r2 >= r3) goto L_0x0161;
    L_0x0159:
        r2 = new java.io.IOException;	 Catch:{ all -> 0x0055 }
        r3 = "read data length exception";
        r2.<init>(r3);	 Catch:{ all -> 0x0055 }
        throw r2;	 Catch:{ all -> 0x0055 }
    L_0x0161:
        r2 = 20;
        r3 = r17 + 20;
        r2 = java.util.Arrays.copyOfRange(r11, r2, r3);	 Catch:{ all -> 0x0055 }
        goto L_0x013b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.longmai.security.plugin.driver.otg.io.stack.MessagePoolImpl.read():byte[]");
    }

    public synchronized void destroy() {
        LogUtil.m10d(TAG, "destroy() Thread: " + Thread.currentThread().getName() + " Id: " + Thread.currentThread().getId());
        this.runFlag = false;
        if (!(this.connection == null || this.usbInterface == null)) {
            this.connection.releaseInterface(this.usbInterface);
        }
    }

    public void run() {
        LogUtil.m18w(TAG, "Interrupt Transfer Receive Thread Run. Thread: " + Thread.currentThread().getName() + " Id: " + Thread.currentThread().getId());
        this.runFlag = true;
        int maxPacketSize = this.inEndpoint.getMaxPacketSize();
        ByteBuffer buffer = ByteBuffer.allocate(maxPacketSize);
        UsbRequest request = new UsbRequest();
        request.initialize(this.connection, this.inEndpoint);
        while (this.runFlag) {
            boolean isQueueOk = request.queue(buffer, maxPacketSize);
            if (!isQueueOk || this.connection.requestWait() != request) {
                synchronized (this) {
                    msgState = 9;
                    notifyAll();
                }
                LogUtil.m12e(TAG, "requestWait failed, exiting " + isQueueOk);
                break;
            }
            byte[] tmp = buffer.array();
            LogUtil.m10d(TAG, "Receiver : " + new String(Hex.encode(tmp, 0, tmp.length)));
            int token = tmp[0];
            int isLastPackage = (token >> 6) & 1;
            int dataLen = token & 63;
            if (((token >> 7) & 1) == 1) {
                msgState = 5;
                this.pool.reset();
            }
            this.pool.write(tmp, 1, dataLen);
            if (isLastPackage == 1) {
                synchronized (this) {
                    msgState = 3;
                    notifyAll();
                }
            }
        }
        LogUtil.m18w(TAG, "Interrupt Transfer Receive Thread end. Thread: " + Thread.currentThread().getName() + " Id: " + Thread.currentThread().getId());
    }
}
