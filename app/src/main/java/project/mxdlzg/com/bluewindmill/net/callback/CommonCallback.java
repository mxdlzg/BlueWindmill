package project.mxdlzg.com.bluewindmill.net.callback;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.lzy.okgo.model.Response;

import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import project.mxdlzg.com.bluewindmill.model.config.Config;
import project.mxdlzg.com.bluewindmill.model.entity.NetResult;
import project.mxdlzg.com.bluewindmill.model.local.ManageSetting;
import project.mxdlzg.com.bluewindmill.net.request.LoginRequest;
import project.mxdlzg.com.bluewindmill.view.activity.LoginActivity;

/**
 * Created by 廷江 on 2017/12/18.
 */

public abstract class CommonCallback<T> {
    final List<String> list = new ArrayList<>(Arrays.asList(
            "http://sc.sit.edu.cn/portal.jsp",
            "http://sc.sit.edu.cn/private/menu/menu.action",
            "http://ems.sit.edu.cn/"));

    //
    public abstract void onSuccess(T message);

    //own error,
    public void onFail(T message){

    }

    public void onProgress(String status){

    }
    public void onError(String message){

    }
    public void onError(NetResult netResult){

    }

    //404 | >500 will raise this callback function
    public void onError(Context context,Response<T> response,String originSource){
        if (response.getException() instanceof SocketTimeoutException){
            Toast.makeText(context, originSource+"连接超时", Toast.LENGTH_SHORT).show();
        }
        String location = "";
        if (response.getRawResponse() != null){
            location = response.getRawResponse().header("Location");
        }
        //Auto login
        if (response.code() == 302 && list.contains(location)){
            Toast.makeText(context, "请登录！", Toast.LENGTH_SHORT).show();
            context.startActivity(new Intent(context, LoginActivity.class));
        }else if (response.getException() instanceof ProtocolException || response.getException() instanceof IndexOutOfBoundsException){
            if (response.getRawResponse() == null || response.getRawResponse().request().url().toString().equals("http://sc.sit.edu.cn/private/menu/menu.action")){
                Toast.makeText(context, "请登录！", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, LoginActivity.class));
            }
        }

        //Throw netresult
        NetResult netResult = null;
        if (response.body() == null){
            netResult = new NetResult<String>(null,response.code());
            netResult.setMsg("请求返回空值");
        }else {
            netResult = (NetResult) response.body();
            netResult.setCode(response.code());
            netResult.setMsg(response.message());
        }

        onError(netResult);
    }

    public void onError(String message,Context context){
        if (context != null){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public void onError(Context context,Response<T> response,boolean isCode){
        if (response.getException() instanceof SocketTimeoutException){
            Toast.makeText(context, "连接超时", Toast.LENGTH_SHORT).show();
        }
        String location = "";
        if (response.getRawResponse() != null){
            location = response.getRawResponse().header("Location");
        }
        //Auto login
        if (response.code() == 302 && list.contains(location)){
            Toast.makeText(context, "请登录！", Toast.LENGTH_SHORT).show();
            context.startActivity(new Intent(context, LoginActivity.class));
        }else if (response.getException() instanceof ProtocolException || response.getException() instanceof IndexOutOfBoundsException){
            if (response.getRawResponse() == null || response.getRawResponse().request().url().toString().equals("http://sc.sit.edu.cn/private/menu/menu.action")){
                Toast.makeText(context, "请登录！", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, LoginActivity.class));
            }
        }

        //Throw netresult
        NetResult netResult = null;
        if (response.body() == null){
            netResult = new NetResult<String>(null,response.code());
            netResult.setMsg("请求返回空值");
        }else {
            netResult = (NetResult) response.body();
            netResult.setCode(response.code());
            netResult.setMsg(response.message());
        }

        onError(netResult);
    }

}
