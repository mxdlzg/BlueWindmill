package project.mxdlzg.com.bluewindmill.net.callback;

import android.content.Context;
import android.widget.Toast;

import com.lzy.okgo.model.Response;

import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import project.mxdlzg.com.bluewindmill.model.config.Config;

/**
 * Created by 廷江 on 2017/12/18.
 */

public abstract class CommonCallback<T> {
    //
    public abstract void onSuccess(T message);

    //own error,
    public void onFail(T message){

    }

    public void onError(Context context,Response response,boolean isCode){
        if (response.getException() instanceof SocketTimeoutException){
            Toast.makeText(context, "连接超时", Toast.LENGTH_SHORT).show();
        }else if (isCode){
            onError(Config.codeConvertor(response.code()));
        }else {
            onError(response.message(),context);
        }
    }

    //404 | >500 will raise this callback function
    public void onError(String message){

    }

    public void onError(String message,Context context){
        if (context != null){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public void onProgress(String status){

    }
}
