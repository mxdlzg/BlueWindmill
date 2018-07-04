package com.longmai.security.plugin.driver;

public class DriverInfo {
    Driver driver;
    Class driverClass;
    String driverClassName;

    public String toString() {
        return "driver[className=" + this.driverClassName + "," + this.driver + "]";
    }
}
