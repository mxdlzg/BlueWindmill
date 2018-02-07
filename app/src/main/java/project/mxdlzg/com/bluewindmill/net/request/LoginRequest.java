package project.mxdlzg.com.bluewindmill.net.request;

import android.content.Context;
import project.mxdlzg.com.bluewindmill.R;
import project.mxdlzg.com.bluewindmill.model.config.Config;
import project.mxdlzg.com.bluewindmill.model.local.ManageSetting;
import project.mxdlzg.com.bluewindmill.net.callback.CommonCallback;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.cookie.store.CookieStore;
import com.lzy.okgo.model.Response;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

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
//        OkGo.<String>post(Config.SC_LOGIN_URL)
//                .tag(context)
//                .params("j_username",ManageSetting.getStringSetting(context,Config.USER_NAME))
//                .params("j_password",ManageSetting.getStringSetting(context,Config.USER_PASSWORD))
//                .params("returnUrl","")
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(Response<String> response) {
//                        scLoginStatus = Config.LOGIN;
//                        callback.onSuccess(Config.codeConvertor(response.code()));
//                    }
//
//                    @Override
//                    public void onError(Response<String> response) {
//                        scLoginStatus = Config.NOT_LOGIN;
//                        callback.onError(Config.codeConvertor(response.code()));
//                    }
//                });


        OkGo.<String>post(Config.SC_LOGIN_PASS_URL)
                .tag(context)
                .params("Login.Token1",ManageSetting.getStringSetting(context,Config.USER_NAME))
                .params("Login.Token2",ManageSetting.getStringSetting(context,Config.USER_PASSWORD))
                .params("goto",Config.SC_LOGIN_SUCCESS_URL)
                .params("gotoOnFail",Config.SC_LOGIN_FAIL_URL)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        CookieStore cookieStore = OkGo.getInstance().getCookieJar().getCookieStore();
                        List<Cookie> cookies = cookieStore.getCookie(HttpUrl.parse(Config.SC_LOGIN_PASS_URL));
                        addCookie(cookies,HttpUrl.parse(Config.SC_LOGIN_URL));
                        securityCheck(context,callback);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        scLoginStatus = Config.NOT_LOGIN;
                        callback.onError(Config.codeConvertor(response.code()));
                    }
                });


    }

    private static void securityCheck(Context context, final CommonCallback<String> callback){
        OkGo.<String>post(Config.SC_LOGIN_URL)
                .tag(context)
                .params("j_username",ManageSetting.getStringSetting(context,Config.USER_NAME))
                .params("j_password",ManageSetting.getStringSetting(context,Config.USER_PASSWORD))
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

    private static void addCookie(List<Cookie> cookies,HttpUrl aimURL){
        CookieStore cookieStore = OkGo.getInstance().getCookieJar().getCookieStore();
        Cookie.Builder builder = new Cookie.Builder();
        for (Cookie cookie :
                cookies) {
            cookieStore.saveCookie(aimURL,builder.name(cookie.name()).value(cookie.value()).domain(aimURL.host()).expiresAt(cookie.expiresAt()).path(cookie.path()).build());
        }
    }
}
