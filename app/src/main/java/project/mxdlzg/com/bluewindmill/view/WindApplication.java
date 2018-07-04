package project.mxdlzg.com.bluewindmill.view;

import android.app.Application;
import android.content.Context;

import project.mxdlzg.com.bluewindmill.model.config.Config;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

/**
 * Created by 廷江 on 2017/12/17.
 */

public class WindApplication extends Application {
    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        //LeakCanary.install(this);
        //Stetho.initializeWithDefaults(this);

        //Init Config
        Config.getInstance().init(this);

        //Init OkGo Framework
        try{
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            //Log
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
            //Log Level
            loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.HEADERS);
            //Log Color
            loggingInterceptor.setColorLevel(Level.INFO);

            builder.addInterceptor(loggingInterceptor);
            builder.addNetworkInterceptor(new StethoInterceptor());

            //Timeout
            builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);      //Read
            builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS,TimeUnit.MILLISECONDS);      //Write
            builder.connectTimeout(8000,TimeUnit.MILLISECONDS);    //Connect
            builder.followRedirects(false);

            //Cookie
            builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));  //Use Database to maintain Cookie

            //Https(not necessary currently)
            HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();      //Default to trust all
            builder.sslSocketFactory(sslParams.sSLSocketFactory,sslParams.trustManager);

            //Header & Params
            HttpHeaders headers = new HttpHeaders();
//            headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文，不允许有特殊字符
//            headers.put("commonHeaderKey2", "commonHeaderValue2");
            HttpParams params = new HttpParams();
//            params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
//            params.put("commonParamsKey2", "这里支持中文参数");

            //OkGo
            OkGo.getInstance().init(this)
                    .setOkHttpClient(builder.build())
                    .setCacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)      //First to request
                    .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)           //Never
                    .setRetryCount(0);                                         //0 Times to retry
//                    .addCommonHeaders(headers)
//                    .addCommonParams(params);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static RefWatcher getRefWatcher(Context context) {
        WindApplication application = (WindApplication) context.getApplicationContext();
        return application.refWatcher;
    }
}
