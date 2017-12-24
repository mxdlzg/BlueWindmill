package android.mxdlzg.com.bluewindmill.net.asold;

import android.mxdlzg.com.bluewindmill.model.entity.config.HttpMethod;

import java.net.CookieStore;
import java.net.HttpCookie;

/**
 * Created by 廷江 on 2017/3/24.
 */

public class GetCookieTest {
    private String url = "http://ems.sit.edu.cn:85/login.jsp";

    public GetCookieTest(String url, String user, String password, final SuccessCallback successCallback, final FailCallback failCallback, String[] uris, HttpCookie[] httpCookies) {
        new NetConnection(url, HttpMethod.POST, uris, httpCookies, null, new NetConnection.FailCallback() {
            @Override
            public void onFail() {
                if (failCallback != null){
                    failCallback.onFail();
                }
            }
        }, new NetConnection.ConfigCallback() {
            @Override
            public void onConfig(CookieStore cookieStore) {
                if (successCallback != null){
                    successCallback.onSuccess(cookieStore);
                }
            }
        }, "loginName", user, "password", password, "authtype", "2", "loginYzm", "", "Login.Token1", "", "Login.Token2", "");
    }

    public static interface SuccessCallback{
        void onSuccess(CookieStore cookieStore);
    }
    public static interface FailCallback{
        void onFail();
    }
}
