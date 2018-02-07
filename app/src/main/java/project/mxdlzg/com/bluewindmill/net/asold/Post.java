package project.mxdlzg.com.bluewindmill.net.asold;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by 廷江 on 2017/3/11.
 */

public class Post extends Thread {
    private SuccessCallback successCallback;
    private FailCallback failCallback;
    private HttpClient client;
    private CookieStore cookieStore;
    List<BasicNameValuePair> parameters;
    private HttpClientBuilder builder;
    private Header header;
    private String url;
    private String result = "";

    public Post(SuccessCallback successCallback, FailCallback failCallback, CookieStore cookieStore, Header header, String url, List<BasicNameValuePair> parameters) {
        this.successCallback = successCallback;
        this.failCallback = failCallback;
        this.cookieStore = cookieStore;
        this.parameters = parameters;
        this.header = header;
        this.url = url;

    }

    @Override
    public void run() {

        HttpPost post = new HttpPost(url);
        builder = HttpClientBuilder.create().setDefaultCookieStore(cookieStore);
        client = builder.build();

        try {
            post.setEntity(new UrlEncodedFormEntity(parameters,"gb2312"));
            post.setHeader(header);

            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity,"gb2312");

            if (successCallback!=null){
                successCallback.onSuccess(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (failCallback!=null){
                failCallback.onFail();
            }
        }
    }


    public static interface SuccessCallback{
        void onSuccess(String result);
    }
    public static interface FailCallback{
        void onFail();
    }
}
