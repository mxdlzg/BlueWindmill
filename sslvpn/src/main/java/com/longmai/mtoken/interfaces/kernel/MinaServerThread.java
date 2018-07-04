package com.longmai.mtoken.interfaces.kernel;

import android.content.Context;
import com.longmai.mtoken.interfaces.kernel.func.base.FunctionFactory;
import com.longmai.security.plugin.util.LogUtil;
import java.io.IOException;
import java.net.InetSocketAddress;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class MinaServerThread extends Thread {
    private static final int PORT = 61234;
    private static final String TAG = MinaServerThread.class.getName();
    private SocketAcceptor acceptor;
    private Context context;
    private FunctionFactory factory;
    private MinaIoHandler handler = new MinaIoHandler();

    public MinaServerThread(Context context) throws RuntimeException {
        this.context = context;
        this.factory = new FunctionFactory(context);
    }

    public void run() {
        LogUtil.m10d(TAG, "run()");
        this.handler.setFunctionFactory(this.factory);
        this.acceptor = new NioSocketAcceptor();
        this.acceptor.setReuseAddress(true);
        this.acceptor.getFilterChain().addLast("protocol", new ProtocolCodecFilter(new ByteArrayCodecFactory()));
        this.acceptor.setHandler(this.handler);
        this.acceptor.getSessionConfig().setReadBufferSize(8196);
        this.acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
        try {
            this.acceptor.bind(new InetSocketAddress(PORT));
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogUtil.m10d(TAG, "end");
    }

    public void cancel() {
        LogUtil.m10d(TAG, "cancel()");
        if (this.acceptor != null) {
            this.acceptor.unbind();
        }
    }
}
