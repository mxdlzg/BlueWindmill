package android.mxdlzg.com.bluewindmill.net.callback;

/**
 * Created by 廷江 on 2017/12/18.
 */

public abstract class CommonCallback<T> {
    public abstract void onSuccess(T message);
    public abstract void onFail(T message);
    public void onProgress(String status){

    }
}
