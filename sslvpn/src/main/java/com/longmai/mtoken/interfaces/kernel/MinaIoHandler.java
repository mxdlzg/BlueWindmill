package com.longmai.mtoken.interfaces.kernel;

import com.longmai.mtoken.interfaces.kernel.func.base.FunctionFactory;
import com.longmai.security.plugin.util.LogUtil;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.json.JSONException;
import org.json.JSONObject;

public class MinaIoHandler extends IoHandlerAdapter {
    private static final String TAG = MinaIoHandler.class.getName();
    private FunctionFactory factory;

    public void setFunctionFactory(FunctionFactory factory) {
        this.factory = factory;
    }

    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    public void sessionCreated(IoSession session) throws Exception {
        LogUtil.m10d(TAG, "sessionCreated: " + session.getId());
    }

    public void sessionOpened(IoSession session) throws Exception {
        LogUtil.m10d(TAG, "sessionOpened: " + session.getId());
    }

    public void sessionClosed(IoSession session) throws Exception {
        LogUtil.m10d(TAG, "sessionClosed: " + session.getId());
    }

    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        LogUtil.m10d(TAG, "sessionIdle: " + session.getId());
    }

    public void messageReceived(IoSession session, Object message) throws Exception {
        String response;
        LogUtil.m10d(TAG, "messageReceived: " + session.getId());
        String request = message.toString();
        LogUtil.m18w(TAG, "request: " + request);
        try {
            response = this.factory.getFunction(request).exec();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                String function = new JSONObject(new String(request)).getString("function");
                JSONObject person = new JSONObject();
                person.put("function", function);
                person.put("return", -1);
                person.put("errorCode", -1);
                response = person.toString();
            } catch (JSONException e1) {
                e1.printStackTrace();
                response = "{\"function\":\"MINA_Server\", \"return\":-2, \"errorCode\":-2}";
            }
        }
        session.write(response);
        LogUtil.m18w(TAG, "response: " + new String(response));
    }

    public void messageSent(IoSession session, Object message) throws Exception {
        LogUtil.m10d(TAG, "messageSent: " + session.getId());
    }
}
