package com.longmai.security.plugin.base;

public class ContainerInfo extends BaseContainerType {
    public ContainerInfo(int appId, String containerName, int containerType, int signKeyLen, int exchKeyLen, int signCertFlag, int exchCertFlag) {
        this.appId = appId;
        this.containerName = containerName;
        this.containerType = containerType;
        this.signKeyLen = signKeyLen;
        this.exchKeyLen = exchKeyLen;
        this.signCertFlag = signCertFlag;
        this.exchCertFlag = exchCertFlag;
    }
}
