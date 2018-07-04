package com.longmai.security.plugin.driver.ble.base;

import java.util.ArrayList;
import java.util.List;

public class GattAttributesUUID {
    public static final String CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    public static final String NOTIFY1_UUID = "d44bc439-abfd-45a2-b575-925416129601";
    public static final String NOTIFY2_UUID = "d44bc439-abfd-45a2-b575-925416129602";
    public static final String NOTIFY3_UUID = "d44bc439-abfd-45a2-b575-925416129603";
    public static final String NOTIFY4_UUID = "d44bc439-abfd-45a2-b575-925416129604";
    public static final String NOTIFY5_UUID = "d44bc439-abfd-45a2-b575-925416129605";
    public static final String NOTIFY6_UUID = "d44bc439-abfd-45a2-b575-925416129606";
    public static final String NOTIFY7_UUID = "d44bc439-abfd-45a2-b575-925416129607";
    public static final String SERVICE_ID = "0000fee9-0000-1000-8000-00805f9b34fb";
    public static final String WRITE_UUID = "d44bc439-abfd-45a2-b575-925416129600";
    public static List<String> notifyAttributes = new ArrayList();

    static {
        notifyAttributes.add(NOTIFY1_UUID);
        notifyAttributes.add(NOTIFY2_UUID);
        notifyAttributes.add(NOTIFY3_UUID);
        notifyAttributes.add(NOTIFY4_UUID);
        notifyAttributes.add(NOTIFY5_UUID);
        notifyAttributes.add(NOTIFY6_UUID);
        notifyAttributes.add(NOTIFY7_UUID);
    }
}
