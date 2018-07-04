package com.longmai.mtoken.interfaces.kernel;

import com.longmai.security.plugin.util.LogUtil;
import java.net.InetSocketAddress;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderAdapter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.json.JSONObject;

public class ByteArrayDecoder extends ProtocolDecoderAdapter {
    private static final String TAG = ByteArrayDecoder.class.getName();
    private final AttributeKey CONTEXT = new AttributeKey(getClass(), "context");
    private int bufferLength = 1024;
    private int maxLineLength = 8196;

    private class Context {
        private final IoBuffer buf;

        private Context(int bufferLength) {
            this.buf = IoBuffer.allocate(bufferLength).setAutoExpand(true);
        }

        public IoBuffer getBuffer() {
            return this.buf;
        }

        public void clear() {
            this.buf.clear();
        }

        public void append(String in) throws CharacterCodingException {
            getBuffer().putString(in, Charset.defaultCharset().newEncoder());
        }
    }

    public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        LogUtil.m10d(TAG, "decode session: " + session.getId() + " RemoteAddress: " + ((InetSocketAddress) session.getRemoteAddress()).getAddress().getHostAddress());
        decodeMessage(getContext(session), session, in, out);
    }

    private void decodeMessage(Context ctx, IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws CharacterCodingException {
        String message = in.getString(Charset.defaultCharset().newDecoder());
        LogUtil.m10d(TAG, message);
        ctx.append(message);
        if (message.trim().endsWith("}")) {
            IoBuffer buffer = ctx.getBuffer();
            buffer.flip();
            String str = buffer.getString(Charset.defaultCharset().newDecoder());
            if (isGoodMessage(str)) {
                try {
                    writeText(session, str, out);
                } finally {
                    ctx.clear();
                }
            }
        }
    }

    private boolean isGoodMessage(String message) {
        try {
            JSONObject jSONObject = new JSONObject(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected void writeText(IoSession session, String text, ProtocolDecoderOutput out) {
        out.write(text);
    }

    private Context getContext(IoSession session) {
        Context ctx = (Context) session.getAttribute(this.CONTEXT);
        if (ctx != null) {
            return ctx;
        }
        ctx = new Context(this.bufferLength);
        session.setAttribute(this.CONTEXT, ctx);
        return ctx;
    }
}
