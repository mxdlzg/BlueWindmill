package android.mxdlzg.com.bluewindmill.model.entity;

import android.mxdlzg.com.bluewindmill.model.config.Config;

import com.lzy.okgo.db.ColumnEntity;
import com.lzy.okgo.db.TableEntity;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.RowSet;

/**
 * Created by 廷江 on 2017/12/24.
 */

public class NetResult<T>{
    private T data = null;
    private int code = Config.NET_RESULT_DEFAULT_CODE;
    private String msg = "";

    private NetResult(){

    }

    public NetResult(T data) {
        this.data = data;
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
}
