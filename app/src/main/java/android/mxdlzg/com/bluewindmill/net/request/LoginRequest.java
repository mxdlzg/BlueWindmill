package android.mxdlzg.com.bluewindmill.net.request;

import android.content.Context;
import android.mxdlzg.com.bluewindmill.R;
import android.mxdlzg.com.bluewindmill.model.config.Config;
import android.mxdlzg.com.bluewindmill.model.local.ManageSetting;
import android.mxdlzg.com.bluewindmill.net.callback.CommonCallback;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

/**
 * Created by 廷江 on 2017/12/18.
 */

public class LoginRequest {
    public static int emsLoginStatus = Config.NOT_LOGIN;
    public static int scLoginStatus = Config.NOT_LOGIN;

    /**
     * Read Params from config(user,password)
     */
    public void loginEMS(final Context context, final CommonCallback<String> callback) {
        OkGo.<String>get(Config.EMS_LOGIN_URL)
                .tag(this)
                .params("loginName", ManageSetting.getStringSetting(context, Config.USER_NAME))
                .params("password", ManageSetting.getStringSetting(context, Config.USER_PASSWORD))
                .params("authtype", Config.AUTH_TYPE)
                .params("loginYzm", "")
                .params("Login.Token1", "")
                .params("Login.Token2", "")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (!response.body().contains(context.getString(R.string.loginCheckEMS))){
                            emsLoginStatus = Config.NOT_LOGIN;
                            callback.onFail(context.getString(R.string.loginFailEMS));
                        }else {
                            emsLoginStatus = Config.LOGIN;
                            callback.onSuccess(response.message());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        emsLoginStatus = Config.NOT_LOGIN;
                        Toast.makeText(context, Config.codeConvertor(response.code()), Toast.LENGTH_SHORT).show();
                        if (callback!=null){
                            callback.onFail(Config.codeConvertor(response.code()));
                        }
                    }
                });
    }

    /**
     * login into SC
     * @param context context
     * @param callback callback
     */
    public static void loginSC(final Context context,final CommonCallback<String> callback){
        OkGo.<String>head(Config.SC_LOGIN_URL)
                .tag(context)
                .params("j_username",Config.USER_NAME)
                .params("returnUrl","")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        scLoginStatus = Config.LOGIN;
                        callback.onSuccess(Config.codeConvertor(response.code()));
                    }

                    @Override
                    public void onError(Response<String> response) {
                        scLoginStatus = Config.NOT_LOGIN;
                        callback.onError(Config.codeConvertor(response.code()));
                    }
                });
    }
}
