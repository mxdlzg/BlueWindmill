package android.mxdlzg.com.bluewindmill.net;

import org.apache.http.client.CookieStore;
import org.apache.http.message.BasicNameValuePair;

import java.util.List;

/**
 * Created by 廷江 on 2017/3/8.
 */
public class GetSchedule {

    public GetSchedule(final SuccessCallback successCallback, final FailCallback failCallback, CookieStore cookieStore, List<BasicNameValuePair> params, String url) {
        new Post(new Post.SuccessCallback() {
            @Override
            public void onSuccess(String result) {
                if (successCallback != null){
                    successCallback.onSuccess(result);
                }
            }
        }, new Post.FailCallback() {
            @Override
            public void onFail() {
                if (failCallback != null){
                    failCallback.onFail();
                }
            }
        },cookieStore,null,url,params).start();
    }

    public static interface SuccessCallback{
        void onSuccess(String result);
    }
    public static interface FailCallback{
        void onFail();
    }
}
