package android.mxdlzg.com.bluewindmill.net;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by 廷江 on 2017/3/11.
 */

public class Get extends Thread{
    private SuccessCallback successCallback;
    private FailCallback failCallback;
    private HttpClient client;
    private CookieStore cookieStore;
    private HttpClientBuilder builder;
    private Header header;
    private String url;

    @Override
    public void run() {
        HttpGet get =new HttpGet(url);
        builder = HttpClientBuilder.create().setDefaultCookieStore(cookieStore);
        client = builder.build();
        try {
            get.setHeader(header);

            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity,"utf-8");

            if (successCallback != null){
                successCallback.onSuccess(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (failCallback != null){
                failCallback.onFail();
            }
        }
    }

    public Get(SuccessCallback successCallback, CookieStore cookieStore, Header header, String url) {
        this.successCallback = successCallback;
        this.cookieStore = cookieStore;
        this.header = header;
        this.url = url;
    }

    public static interface SuccessCallback{
        void onSuccess(String result);
    }
    public static interface FailCallback{
        void onFail();
    }
}
