package project.mxdlzg.com.bluewindmill.net.request;

import android.content.Context;

import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.request.base.Request;

/**
 * Created by 廷江 on 2018/1/2.
 */

public class BaseRequest<T> {
    public Request request(Context context,Request request,Callback<T> callback,String...kvs){
        request.tag(context);
        for (int i = 0; i < kvs.length; i+=2) {
            request.params(kvs[i],kvs[i+1]);
        }
        return null;// TODO: 2018/1/2
    }
}
