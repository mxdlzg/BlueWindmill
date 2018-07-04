package com.topsec.topsap.manager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;

public class HandleProperties {
    private String mFilePath;

    public HandleProperties(String path) {
        this.mFilePath = path;
    }

    public String readValue(String key) {
        Properties props = new Properties();
        try {
            props.load(new BufferedInputStream(new FileInputStream(new File(this.mFilePath))));
            return props.getProperty(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean createFile() {
        File filename = new File(this.mFilePath);
        if (filename.exists()) {
            return false;
        }
        try {
            filename.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void readProperties() {
        Properties props = new Properties();
        try {
            props.load(new BufferedInputStream(new FileInputStream(this.mFilePath)));
            Enumeration en = props.propertyNames();
            while (en.hasMoreElements()) {
                props.getProperty((String) en.nextElement());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeProperties(String parameterName, String parameterValue) {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(this.mFilePath));
            OutputStream fos = new FileOutputStream(this.mFilePath);
            prop.setProperty(parameterName, parameterValue);
            prop.store(fos, "Update '" + parameterName + "' value");
        } catch (IOException e) {
            System.err.println("Visit " + this.mFilePath + " for updating " + parameterName + " value error");
        }
    }
}
