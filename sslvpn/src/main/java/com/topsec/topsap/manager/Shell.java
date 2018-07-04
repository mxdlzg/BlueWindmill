package com.topsec.topsap.manager;

import android.util.Log;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Shell extends Thread {
    public static final boolean SH = false;
    public static final boolean SU = true;
    private BufferedReader br;
    private InputStream is;
    private InputStreamReader isr;
    private final String mCmd;
    private Process mProcess;
    private final boolean mRoot;
    private final String mTag;
    private DataOutputStream ostream;

    public final int waitForQuietly() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x008d in list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r6 = this;
        r1 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r3 = r6.mProcess;
        if (r3 == 0) goto L_0x0077;
    L_0x0007:
        r3 = r6.mProcess;	 Catch:{ OutOfMemoryError -> 0x007c, Exception -> 0x00bb, all -> 0x00f3 }
        r3 = r3.getOutputStream();	 Catch:{ OutOfMemoryError -> 0x007c, Exception -> 0x00bb, all -> 0x00f3 }
        r3.close();	 Catch:{ OutOfMemoryError -> 0x007c, Exception -> 0x00bb, all -> 0x00f3 }
        r3 = r6.mProcess;	 Catch:{ OutOfMemoryError -> 0x007c, Exception -> 0x00bb, all -> 0x00f3 }
        r3 = r3.getErrorStream();	 Catch:{ OutOfMemoryError -> 0x007c, Exception -> 0x00bb, all -> 0x00f3 }
        r3.close();	 Catch:{ OutOfMemoryError -> 0x007c, Exception -> 0x00bb, all -> 0x00f3 }
        r3 = r6.mProcess;	 Catch:{ OutOfMemoryError -> 0x007c, Exception -> 0x00bb, all -> 0x00f3 }
        r3 = r3.getInputStream();	 Catch:{ OutOfMemoryError -> 0x007c, Exception -> 0x00bb, all -> 0x00f3 }
        r6.is = r3;	 Catch:{ OutOfMemoryError -> 0x007c, Exception -> 0x00bb, all -> 0x00f3 }
        r3 = new java.io.InputStreamReader;	 Catch:{ OutOfMemoryError -> 0x007c, Exception -> 0x00bb, all -> 0x00f3 }
        r4 = r6.is;	 Catch:{ OutOfMemoryError -> 0x007c, Exception -> 0x00bb, all -> 0x00f3 }
        r3.<init>(r4);	 Catch:{ OutOfMemoryError -> 0x007c, Exception -> 0x00bb, all -> 0x00f3 }
        r6.isr = r3;	 Catch:{ OutOfMemoryError -> 0x007c, Exception -> 0x00bb, all -> 0x00f3 }
        r3 = new java.io.BufferedReader;	 Catch:{ OutOfMemoryError -> 0x007c, Exception -> 0x00bb, all -> 0x00f3 }
        r4 = r6.isr;	 Catch:{ OutOfMemoryError -> 0x007c, Exception -> 0x00bb, all -> 0x00f3 }
        r5 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;	 Catch:{ OutOfMemoryError -> 0x007c, Exception -> 0x00bb, all -> 0x00f3 }
        r3.<init>(r4, r5);	 Catch:{ OutOfMemoryError -> 0x007c, Exception -> 0x00bb, all -> 0x00f3 }
        r6.br = r3;	 Catch:{ OutOfMemoryError -> 0x007c, Exception -> 0x00bb, all -> 0x00f3 }
        r2 = 0;	 Catch:{ OutOfMemoryError -> 0x007c, Exception -> 0x00bb, all -> 0x00f3 }
        r3 = r6.br;	 Catch:{ OutOfMemoryError -> 0x007c, Exception -> 0x00bb, all -> 0x00f3 }
        if (r3 == 0) goto L_0x0042;	 Catch:{ OutOfMemoryError -> 0x007c, Exception -> 0x00bb, all -> 0x00f3 }
    L_0x003a:
        r3 = r6.br;	 Catch:{ OutOfMemoryError -> 0x007c, Exception -> 0x00bb, all -> 0x00f3 }
        r2 = r3.readLine();	 Catch:{ OutOfMemoryError -> 0x007c, Exception -> 0x00bb, all -> 0x00f3 }
        if (r2 != 0) goto L_0x0078;	 Catch:{ OutOfMemoryError -> 0x007c, Exception -> 0x00bb, all -> 0x00f3 }
    L_0x0042:
        r3 = r6.is;	 Catch:{ OutOfMemoryError -> 0x007c, Exception -> 0x00bb, all -> 0x00f3 }
        r3.close();	 Catch:{ OutOfMemoryError -> 0x007c, Exception -> 0x00bb, all -> 0x00f3 }
        r3 = r6.isr;	 Catch:{ OutOfMemoryError -> 0x007c, Exception -> 0x00bb, all -> 0x00f3 }
        r3.close();	 Catch:{ OutOfMemoryError -> 0x007c, Exception -> 0x00bb, all -> 0x00f3 }
        r3 = r6.br;	 Catch:{ OutOfMemoryError -> 0x007c, Exception -> 0x00bb, all -> 0x00f3 }
        r3.close();	 Catch:{ OutOfMemoryError -> 0x007c, Exception -> 0x00bb, all -> 0x00f3 }
        r3 = r6.is;	 Catch:{ Exception -> 0x012b }
        if (r3 == 0) goto L_0x005a;	 Catch:{ Exception -> 0x012b }
    L_0x0055:
        r3 = r6.is;	 Catch:{ Exception -> 0x012b }
        r3.close();	 Catch:{ Exception -> 0x012b }
    L_0x005a:
        r3 = r6.isr;	 Catch:{ Exception -> 0x0135 }
        if (r3 == 0) goto L_0x0063;	 Catch:{ Exception -> 0x0135 }
    L_0x005e:
        r3 = r6.isr;	 Catch:{ Exception -> 0x0135 }
        r3.close();	 Catch:{ Exception -> 0x0135 }
    L_0x0063:
        r3 = r6.br;	 Catch:{ Exception -> 0x013f }
        if (r3 == 0) goto L_0x006c;	 Catch:{ Exception -> 0x013f }
    L_0x0067:
        r3 = r6.br;	 Catch:{ Exception -> 0x013f }
        r3.close();	 Catch:{ Exception -> 0x013f }
    L_0x006c:
        r3 = r6.mProcess;	 Catch:{ InterruptedException -> 0x0149 }
        r1 = r3.waitFor();	 Catch:{ InterruptedException -> 0x0149 }
    L_0x0072:
        r3 = r6.mProcess;
        r3.destroy();
    L_0x0077:
        return r1;
    L_0x0078:
        r6.onStdout(r2);	 Catch:{ OutOfMemoryError -> 0x007c, Exception -> 0x00bb, all -> 0x00f3 }
        goto L_0x003a;
    L_0x007c:
        r0 = move-exception;
        r3 = "SAM_TEST";	 Catch:{ OutOfMemoryError -> 0x007c, Exception -> 0x00bb, all -> 0x00f3 }
        r4 = "OutOfMemoryError";	 Catch:{ OutOfMemoryError -> 0x007c, Exception -> 0x00bb, all -> 0x00f3 }
        android.util.Log.e(r3, r4, r0);	 Catch:{ OutOfMemoryError -> 0x007c, Exception -> 0x00bb, all -> 0x00f3 }
        r3 = r6.is;
        if (r3 == 0) goto L_0x008d;
    L_0x0088:
        r3 = r6.is;
        r3.close();
    L_0x008d:
        r3 = r6.isr;	 Catch:{ Exception -> 0x00b2 }
        if (r3 == 0) goto L_0x0096;	 Catch:{ Exception -> 0x00b2 }
    L_0x0091:
        r3 = r6.isr;	 Catch:{ Exception -> 0x00b2 }
        r3.close();	 Catch:{ Exception -> 0x00b2 }
    L_0x0096:
        r3 = r6.br;	 Catch:{ Exception -> 0x00a0 }
        if (r3 == 0) goto L_0x006c;	 Catch:{ Exception -> 0x00a0 }
    L_0x009a:
        r3 = r6.br;	 Catch:{ Exception -> 0x00a0 }
        r3.close();	 Catch:{ Exception -> 0x00a0 }
        goto L_0x006c;
    L_0x00a0:
        r0 = move-exception;
        r3 = "SAM_TEST";
        r4 = "closing BufferedReader";
        android.util.Log.e(r3, r4, r0);
        goto L_0x006c;
    L_0x00a9:
        r0 = move-exception;
        r3 = "SAM_TEST";
        r4 = "closing InputStream";
        android.util.Log.e(r3, r4, r0);
        goto L_0x008d;
    L_0x00b2:
        r0 = move-exception;
        r3 = "SAM_TEST";
        r4 = "closing InputStreamReader";
        android.util.Log.e(r3, r4, r0);
        goto L_0x0096;
    L_0x00bb:
        r3 = move-exception;
        r3 = r6.is;	 Catch:{ Exception -> 0x00e1 }
        if (r3 == 0) goto L_0x00c5;	 Catch:{ Exception -> 0x00e1 }
    L_0x00c0:
        r3 = r6.is;	 Catch:{ Exception -> 0x00e1 }
        r3.close();	 Catch:{ Exception -> 0x00e1 }
    L_0x00c5:
        r3 = r6.isr;	 Catch:{ Exception -> 0x00ea }
        if (r3 == 0) goto L_0x00ce;	 Catch:{ Exception -> 0x00ea }
    L_0x00c9:
        r3 = r6.isr;	 Catch:{ Exception -> 0x00ea }
        r3.close();	 Catch:{ Exception -> 0x00ea }
    L_0x00ce:
        r3 = r6.br;	 Catch:{ Exception -> 0x00d8 }
        if (r3 == 0) goto L_0x006c;	 Catch:{ Exception -> 0x00d8 }
    L_0x00d2:
        r3 = r6.br;	 Catch:{ Exception -> 0x00d8 }
        r3.close();	 Catch:{ Exception -> 0x00d8 }
        goto L_0x006c;
    L_0x00d8:
        r0 = move-exception;
        r3 = "SAM_TEST";
        r4 = "closing BufferedReader";
        android.util.Log.e(r3, r4, r0);
        goto L_0x006c;
    L_0x00e1:
        r0 = move-exception;
        r3 = "SAM_TEST";
        r4 = "closing InputStream";
        android.util.Log.e(r3, r4, r0);
        goto L_0x00c5;
    L_0x00ea:
        r0 = move-exception;
        r3 = "SAM_TEST";
        r4 = "closing InputStreamReader";
        android.util.Log.e(r3, r4, r0);
        goto L_0x00ce;
    L_0x00f3:
        r3 = move-exception;
        r4 = r6.is;	 Catch:{ Exception -> 0x0110 }
        if (r4 == 0) goto L_0x00fd;	 Catch:{ Exception -> 0x0110 }
    L_0x00f8:
        r4 = r6.is;	 Catch:{ Exception -> 0x0110 }
        r4.close();	 Catch:{ Exception -> 0x0110 }
    L_0x00fd:
        r4 = r6.isr;	 Catch:{ Exception -> 0x0119 }
        if (r4 == 0) goto L_0x0106;	 Catch:{ Exception -> 0x0119 }
    L_0x0101:
        r4 = r6.isr;	 Catch:{ Exception -> 0x0119 }
        r4.close();	 Catch:{ Exception -> 0x0119 }
    L_0x0106:
        r4 = r6.br;	 Catch:{ Exception -> 0x0122 }
        if (r4 == 0) goto L_0x010f;	 Catch:{ Exception -> 0x0122 }
    L_0x010a:
        r4 = r6.br;	 Catch:{ Exception -> 0x0122 }
        r4.close();	 Catch:{ Exception -> 0x0122 }
    L_0x010f:
        throw r3;
    L_0x0110:
        r0 = move-exception;
        r4 = "SAM_TEST";
        r5 = "closing InputStream";
        android.util.Log.e(r4, r5, r0);
        goto L_0x00fd;
    L_0x0119:
        r0 = move-exception;
        r4 = "SAM_TEST";
        r5 = "closing InputStreamReader";
        android.util.Log.e(r4, r5, r0);
        goto L_0x0106;
    L_0x0122:
        r0 = move-exception;
        r4 = "SAM_TEST";
        r5 = "closing BufferedReader";
        android.util.Log.e(r4, r5, r0);
        goto L_0x010f;
    L_0x012b:
        r0 = move-exception;
        r3 = "SAM_TEST";
        r4 = "closing InputStream";
        android.util.Log.e(r3, r4, r0);
        goto L_0x005a;
    L_0x0135:
        r0 = move-exception;
        r3 = "SAM_TEST";
        r4 = "closing InputStreamReader";
        android.util.Log.e(r3, r4, r0);
        goto L_0x0063;
    L_0x013f:
        r0 = move-exception;
        r3 = "SAM_TEST";
        r4 = "closing BufferedReader";
        android.util.Log.e(r3, r4, r0);
        goto L_0x006c;
    L_0x0149:
        r3 = move-exception;
        goto L_0x0072;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.topsec.topsap.manager.Shell.waitForQuietly():int");
    }

    public Shell(String tag, String cmd, boolean root) {
        super(new StringBuilder(String.valueOf(tag)).append("-stdin").toString());
        this.mTag = tag;
        this.mCmd = cmd;
        this.mRoot = root;
    }

    public final void run() {
        try {
            Runtime runtime = Runtime.getRuntime();
            if (this.mRoot) {
                this.mProcess = runtime.exec("su");
                this.ostream = new DataOutputStream(this.mProcess.getOutputStream());
                if (this.ostream != null) {
                    this.ostream.writeBytes(this.mCmd);
                    this.ostream.writeBytes("exit\n");
                    this.ostream.flush();
                    this.ostream.close();
                }
            } else {
                this.mProcess = runtime.exec(this.mCmd);
            }
        } catch (IOException e) {
            Log.e(this.mTag, String.format("Run Cmd %s error: %s", new Object[]{this.mCmd, e}));
        } finally {
            try {
                if (this.ostream != null) {
                    this.ostream.close();
                }
            } catch (Exception e2) {
                Log.e("SAM_TEST", "closing DataOutputStream", e2);
            }
            onCmdTerminated();
        }
    }

    protected void onStdout(String line) {
    }

    protected void onStderr(String line) {
    }

    protected void onCmdStarted() {
    }

    protected void onCmdTerminated() {
    }

    public final void joinLoggers() throws InterruptedException {
    }
}
