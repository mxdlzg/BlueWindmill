package com.longmai.security.plugin.base;

import java.io.IOException;

public abstract class Blob {
    public abstract byte[] getEncode() throws IOException;
}
