package project.mxdlzg.com.bluewindmill.net.request;

import android.content.Context;

import okhttp3.OkHttpClient;
import okhttp3.internal.http.HttpDate;
import project.mxdlzg.com.bluewindmill.R;
import project.mxdlzg.com.bluewindmill.model.config.Config;
import project.mxdlzg.com.bluewindmill.model.entity.NetResult;
import project.mxdlzg.com.bluewindmill.model.local.ManageSetting;
import project.mxdlzg.com.bluewindmill.net.callback.CommonCallback;

import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.cookie.store.CookieStore;
import com.lzy.okgo.model.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;
import project.mxdlzg.com.bluewindmill.util.Util;
import project.mxdlzg.com.bluewindmill.view.activity.LoginActivity;

/**
 * Created by 廷江 on 2017/12/18.
 */

public class LoginRequest {
    public static int emsLoginStatus = Config.NOT_LOGIN;
    public static int scLoginStatus = Config.NOT_LOGIN;

    /**
     * Read Params from config(user,password)
     */
    public static void loginEMS(final Context context,String loginYzm, final CommonCallback<String> callback) {
        OkGo.<String>get(Config.EMS_LOGIN_URL)
                .tag(context)
                .params("loginName", ManageSetting.getStringSetting(context, Config.USER_NAME))
                .params("password", ManageSetting.getStringSetting(context, Config.USER_PASSWORD))
                .params("authtype", Config.AUTH_TYPE)
                .params("loginYzm", loginYzm)
                .params("Login.Token1", "")
                .params("Login.Token2", "")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String body = null;
                        try {
                            body = URLDecoder.decode(response.body(),"gbk");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        if (body == null){
                            callback.onError("获取错误信息失败！");
                            return;
                        }
                        if (body.contains(context.getString(R.string.loginEmsLocation))){
                            emsLoginStatus = Config.LOGIN;
                            List<Cookie> cookies = new ArrayList<>();
                            Cookie.Builder builder = new Cookie.Builder();
                            cookies.add(builder.name("token").value(String.valueOf(System.currentTimeMillis())).domain("sit.edu.cn").expiresAt(HttpDate.MAX_DATE).path("/").build());
                            addCookie(cookies,HttpUrl.parse(Config.EMS_URL),"token");
                            callback.onSuccess("教务系统登录成功");
                        }else {
                            emsLoginStatus = Config.NOT_LOGIN;
                            if (body.contains(context.getString(R.string.loginCheckEMSPassError))){
                                callback.onError(context.getString(R.string.loginCheckEMSPassError));
                            }else if (body.contains(context.getString(R.string.loginCheckCaptchaError))){
                                callback.onFail(context.getString(R.string.loginCheckCaptchaError));
                            }
                            else {
                                Document document = Jsoup.parse(body);
                                callback.onError(document.select("div[style=color:red;font-size:10pt;text-align:center]").text());
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        emsLoginStatus = Config.NOT_LOGIN;
                        Toast.makeText(context, Config.codeConvertor(response.code()), Toast.LENGTH_SHORT).show();
                        if (callback!=null){
                            callback.onError(Config.codeConvertor(response.code()));
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
                        addCookie(cookies,HttpUrl.parse(Config.SC_LOGIN_URL),"iPlanetDirectoryPro");
                        addCookie(cookies,HttpUrl.parse(Config.EMS_URL),"iPlanetDirectoryPro");
                        getJsessionId(context,callback);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        scLoginStatus = Config.NOT_LOGIN;
                        callback.onError(context,response,"第二课堂登录：");
                    }
                });


    }

    private static void getJsessionId(final Context context, final CommonCallback<String> callback) {
        OkGo.<String>get(Config.MAIN_INDEX)
                .tag(context)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        portal(context,callback);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        callback.onError(context,response,"第二课堂登录：");
                    }
                });
    }

    private static void portal(final Context context, final CommonCallback<String> callback) {
        CookieStore cookieStore = OkGo.getInstance().getCookieJar().getCookieStore();
        List<Cookie> cookies = cookieStore.getCookie(HttpUrl.parse(Config.SC_PORTAL));
        System.out.println(cookies.toString());

        OkGo.<String>get(Config.SC_PORTAL)
                .tag(context)// TODO: 18-3-7 GET JSESSIONID
                .params("jsessionid",cookies.get(0).value())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        securityCheck(context,callback);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        callback.onError(context,response,"第二课堂登录：");
                    }
                });
    }

    public static void refershCaptchaAsBitmap(Context context,int type,final CommonCallback<Bitmap> callback){
        String url = "";
        switch (type){
            case 0:
                url = Config.EMS_CAPTCHA_URL;
                break;
            case 1:
                url = Config.SC_CAPTCHA_URL;
                break;
            default:break;
        }
        OkGo.<Bitmap>get(url)
                .tag(context).
                execute(new BitmapCallback() {
                    @Override
                    public void onSuccess(Response<Bitmap> response) {
                        callback.onSuccess(response.body());
                    }

                    @Override
                    public void onError(Response<Bitmap> response) {
                        callback.onFail(null);
                    }
                });
    }

    private static void securityCheck(final Context context, final CommonCallback<String> callback){
        OkGo.<String>get(Config.SC_LOGIN_URL)
                .tag(context)
                .params("j_username",ManageSetting.getStringSetting(context,Config.USER_NAME))
                .params("j_password",ManageSetting.getStringSetting(context,Config.USER_PASSWORD))
                .params("returnUrl","")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        System.out.println("login----------------->securityCheck success!!");
                        scLoginStatus = Config.LOGIN;
                        callback.onSuccess("第二课堂登录成功");
                    }

                    @Override
                    public void onError(Response<String> response) {
                        System.out.println("login----------------->securityCheck fail!!");
                        scLoginStatus = Config.NOT_LOGIN;
                        callback.onError(context,response,"第二课堂登录：");
                    }
                });
    }

    private static void addCookie(List<Cookie> cookies,HttpUrl aimURL,String aimName){
        CookieStore cookieStore = OkGo.getInstance().getCookieJar().getCookieStore();
        Cookie.Builder builder = new Cookie.Builder();
        for (Cookie cookie :
                cookies) {
            if (!TextUtils.isEmpty(aimName) && cookie.name().equals(aimName)){
                cookieStore.saveCookie(aimURL,builder.name(cookie.name()).value(cookie.value()).domain(aimURL.host()).expiresAt(cookie.expiresAt()).path(cookie.path()).build());
                break;
            }
        }
    }

    public static void login(final Context context,String loginYzm, final CommonCallback<String> callback) {
            loginSC(context, callback);
            loginEMS(context,loginYzm, callback);
    }

    public static void sendCaptcha(Context context, String captchaEditTextText, CommonCallback<String> callback) {

    }
}
