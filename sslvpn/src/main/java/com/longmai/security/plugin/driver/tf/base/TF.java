package com.longmai.security.plugin.driver.tf.base;

public interface TF {
    int check_env(byte[] bArr);

    int connect(int i);

    int deviceio(int i, byte[] bArr, int i2, byte[] bArr2, int[] iArr);

    int disconnect(int i);

    int find(int[] iArr);

    int get_tf_path(byte[] bArr, int[] iArr);

    int init(byte[] bArr);

    int read(int i, byte[] bArr, int[] iArr);

    int write(int i, byte[] bArr, int i2);
}
