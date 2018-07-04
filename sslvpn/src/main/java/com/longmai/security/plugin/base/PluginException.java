package com.longmai.security.plugin.base;

public class PluginException extends BaseException {
    protected int errorCode;

    public PluginException(int errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = -1;
        this.errorCode = errorCode;
    }

    public PluginException(int errorCode) {
        super(getMessageFromErrorCode(errorCode));
        this.errorCode = -1;
        this.errorCode = errorCode;
    }

    public PluginException(String errorMsg) {
        super(errorMsg);
        this.errorCode = -1;
        this.errorCode = 1;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    protected static String getMessageFromErrorCode(int errorCode) {
        switch (errorCode) {
            case 1:
                return "Unknown error - " + errorCode;
            case 2:
                return "Data format or type error - " + errorCode;
            case 3:
                return "The key to the invalid - " + errorCode;
            case 4:
                return "Unknown to the algorithm - " + errorCode;
            case 5:
                return "The current environment is not supported - " + errorCode;
            case 6:
                return "Unknown device - " + errorCode;
            case 7:
                return "Invalid connection - " + errorCode;
            case 8:
                return "Generate APDU failure - " + errorCode;
            case 9:
                return "Driver not found - " + errorCode;
            case 10:
                return "Config not found - " + errorCode;
            case 11:
                return "IO error - " + errorCode;
            case 12:
                return "No devices found - " + errorCode;
            case 13:
                return "Connection failed - " + errorCode;
            case 16:
                return "Safety certification does not pass - " + errorCode;
            case 17:
                return "Permission denied - " + errorCode;
            case 20:
                return "The message digest algorithm does not match the SM2 signature - " + errorCode;
            default:
                return "Unspecified error - " + errorCode;
        }
    }
}
