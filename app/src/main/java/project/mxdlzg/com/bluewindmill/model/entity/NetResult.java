package project.mxdlzg.com.bluewindmill.model.entity;

import project.mxdlzg.com.bluewindmill.model.config.Config;

/**
 * Created by 廷江 on 2017/12/24.
 */

public class NetResult<T>{
    private T data = null;
    private int code = Config.NET_RESULT_DEFAULT_CODE;
    private String msg = "";
    private boolean isSuccess = false;

    private NetResult(){

    }

    public NetResult(T data) {
        this.data = data;
    }

    public NetResult(T data, boolean isSuccess) {
        this.data = data;
        this.isSuccess = isSuccess;
        if (isSuccess){
            this.code = Config.NET_RESULT_SUCCESS;
        }
    }

    public NetResult(T data,String msg, boolean isSuccess) {
        this.data = data;
        this.isSuccess = isSuccess;
        if (isSuccess){
            this.code = Config.NET_RESULT_SUCCESS;
        }
        this.msg = msg;
    }

    public NetResult(T data, int code) {
        this.data = data;
        this.code = code;
    }

    public NetResult(T data, int code, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    public static NetResult<Object> Default(){
        return new NetResult<Object>();
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
        this.code = Config.NET_RESULT_DEFAULT_CODE;
    }

    public void setData(T data,int code){
        this.data = data;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
