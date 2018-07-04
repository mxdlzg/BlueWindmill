package com.longmai.security.plugin.driver.tf.ndk;

import com.longmai.security.plugin.driver.tf.base.TF;

public class mTokenTF implements TF {
    public native synchronized int check_env(byte[] bArr);

    public native synchronized int connect(int i);

    public native synchronized int deviceio(int i, byte[] bArr, int i2, byte[] bArr2, int[] iArr);

    public native synchronized int disconnect(int i);

    public native synchronized int find(int[] iArr);

    public native synchronized int get_tf_path(byte[] bArr, int[] iArr);

    public native synchronized int init(byte[] bArr);

    public native synchronized int read(int i, byte[] bArr, int[] iArr);

    public native synchronized int write(int i, byte[] bArr, int i2);

    static {
        System.loadLibrary("TFDriver");
    }
}
