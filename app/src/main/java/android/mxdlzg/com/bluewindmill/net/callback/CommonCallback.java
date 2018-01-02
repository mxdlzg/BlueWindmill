package android.mxdlzg.com.bluewindmill.net.callback;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 廷江 on 2017/12/18.
 */

public abstract class CommonCallback<T> {
    //
    public abstract void onSuccess(T message);

    //own error,
    public void onFail(T message){

    }

    //404 | >500 will raise this callback function
    public void onError(String message){

    }

    public void onError(String message,Context context){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void onProgress(String status){

    }
}
