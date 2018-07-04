package com.longmai.security.plugin.device;

public class Device {
    private int devId;
    private String devName;
    private int devType;

    public Device(int id, String name, int type) {
        this.devId = id;
        this.devName = name;
        this.devType = type;
    }

    public int getId() {
        return this.devId;
    }

    public String getName() {
        return this.devName;
    }

    public int getType() {
        return this.devType;
    }
}
