package com.topsec.sslvpn.datadef;

public class ServiceAuthCfg {
    public boolean m_bEnableBootstrap;
    public boolean m_bEnableCert;
    public boolean m_bEnableCodeWord;
    public boolean m_bEnableTwoFactor;
    public boolean m_bNeedPhoneFeaCode;
    public boolean m_bTwoFactorHide;
    public eCaptchaType m_ectCaptchaType;

    public enum eCaptchaType {
        GID_TYPE_OFF,
        GID_TYPE_ON,
        GID_TYPE_AUTO
    }

    public void SetCaptchaType(int iCaptchaType) {
        switch (iCaptchaType) {
            case 0:
                this.m_ectCaptchaType = eCaptchaType.GID_TYPE_OFF;
                return;
            case 1:
                this.m_ectCaptchaType = eCaptchaType.GID_TYPE_ON;
                return;
            case 2:
                this.m_ectCaptchaType = eCaptchaType.GID_TYPE_AUTO;
                return;
            default:
                return;
        }
    }
}
