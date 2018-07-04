package com.longmai.security.plugin.driver.tf;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import com.longmai.security.plugin.base.PluginException;
import com.longmai.security.plugin.device.Device;
import com.longmai.security.plugin.device.DeviceManager;
import com.longmai.security.plugin.driver.conn.Connection;
import com.longmai.security.plugin.driver.tf.base.TF;
import com.longmai.security.plugin.driver.tf.ndk.mTokenTF;
import com.longmai.security.plugin.util.LogUtil;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeviceManagerImple implements DeviceManager {
    private static final String TAG = DeviceManagerImple.class.getName();
    private static final int currentapiVersion = VERSION.SDK_INT;
    private Context ctx;
    private List<Device> devices;
    private boolean mkdir;
    private String packageName;
    private TF tf;

    public DeviceManagerImple() throws PluginException {
        this.devices = new ArrayList();
        this.mkdir = false;
        this.tf = new mTokenTF();
    }

    @TargetApi(19)
    public DeviceManagerImple(Context ctx) throws PluginException {
        this.devices = new ArrayList();
        this.mkdir = false;
        LogUtil.m10d(TAG, "DeviceManagerImple Create");
        this.ctx = ctx;
        this.tf = new mTokenTF();
    }

    public List<Device> find(int timeOut, String... parameter) throws PluginException {
        return find(parameter);
    }

    @SuppressLint({"NewApi"})
    public List<Device> find(String... parameter) throws PluginException {
        LogUtil.m10d(TAG, "find()");
        this.devices.clear();
        if (!this.mkdir) {
            byte[] path = new byte[1024];
            int[] length = new int[1];
            if (this.tf.get_tf_path(path, length) != 0) {
                throw new PluginException(12, "Gets a TF card path failure");
            }
            ByteArrayOutputStream ioFilePath = new ByteArrayOutputStream();
            ioFilePath.write(path, 0, length[0] - 1);
            if (currentapiVersion <= 18) {
                try {
                    ioFilePath.write("/IO.SYS\u0000".getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (this.ctx == null) {
                throw new PluginException("Context is null");
            } else {
                this.packageName = "/Android/data/" + this.ctx.getPackageName() + "\u0000";
                try {
                    ioFilePath.write(this.packageName.getBytes());
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                File dir = new File(ioFilePath.toString().trim());
                if (!dir.exists()) {
                    if (currentapiVersion > 18) {
                        File[] dirs = this.ctx.getExternalFilesDirs(null);
                        if (!((dirs != null && dirs[0].exists() && dir.exists()) || dir.mkdirs())) {
                            throw new PluginException("Create \"" + dir + "\" directory failed");
                        }
                    } else if (!dir.mkdirs()) {
                        throw new PluginException("Create \"" + dir + "\" directory failed");
                    }
                }
            }
            if (this.tf.init(ioFilePath.toByteArray()) != 0) {
                throw new PluginException(5);
            }
            this.mkdir = true;
        }
        int[] token_number = new int[1];
        if (this.tf.find(token_number) != 0) {
            throw new PluginException("Find device failure");
        }
        for (int i = 0; i < token_number[0]; i++) {
            this.devices.add(new Device(i, "TF" + i, 0));
        }
        return this.devices;
    }

    public Connection getConnection(Device device) throws PluginException {
        return new ConnectionImpl(device, this.tf);
    }

    public Connection getConnection(Device device, int timeout) throws PluginException {
        return getConnection(device);
    }
}
