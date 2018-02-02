package android.mxdlzg.com.bluewindmill.net.asold;

import android.mxdlzg.com.bluewindmill.model.config.Config;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mxdlzg on 2017/3/7.
 */

public class GetCookie extends Thread{
    private CookieStore cookieStore = new BasicCookieStore();
    private HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(cookieStore);
    private HttpClient client = builder.build();
    private getCookieCallback cookieCallback;
    private FailCallback failCallback;

    public GetCookie(getCookieCallback cookieCallback,FailCallback failCallback) {
        this.cookieCallback = cookieCallback;
        this.failCallback = failCallback;
    }

    @Override
    public void run() {
        HttpPost post = new HttpPost("http://ems.sit.edu.cn:85/login.jsp");

        try {
            List<BasicNameValuePair> parameters = new ArrayList<>();
            parameters.add(new BasicNameValuePair("loginName", "1510400212"));
            parameters.add(new BasicNameValuePair("password", "607391"));
            parameters.add(new BasicNameValuePair("authtype", "2"));
            parameters.add(new BasicNameValuePair("loginYzm", ""));
            parameters.add(new BasicNameValuePair("Login.Token1", ""));
            parameters.add(new BasicNameValuePair("Login.Token2", ""));

            post.setEntity(new UrlEncodedFormEntity(parameters, "gb2312"));
            HttpResponse response = client.execute(post);
            cookieCallback.cookieStore(cookieStore);
        }catch (IOException e) {
            e.printStackTrace();
            if (failCallback != null){
                failCallback.onFail(Config.GET_COOKIE_ERROR);
            }
        }
    }

    public void setCookieCallback(getCookieCallback cookieCallback) {
        this.cookieCallback = cookieCallback;
    }

    public static interface getCookieCallback{
        void cookieStore(CookieStore cookieStore);
    }
    public static interface FailCallback{
        void onFail(int status);
    }
}
