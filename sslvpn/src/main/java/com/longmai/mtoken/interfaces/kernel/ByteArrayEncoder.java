package com.longmai.mtoken.interfaces.kernel;

import com.longmai.security.plugin.util.LogUtil;
import java.net.InetSocketAddress;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class ByteArrayEncoder extends ProtocolEncoderAdapter {
    private static final String TAG = ByteArrayEncoder.class.getName();

    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        LogUtil.m10d(TAG, "encode session: " + session.getId() + " RemoteAddress: " + ((InetSocketAddress) session.getRemoteAddress()).getAddress().getHostAddress());
        LogUtil.m10d(TAG, message.toString());
        out.write(IoBuffer.wrap(message.toString().getBytes()));
    }
}
