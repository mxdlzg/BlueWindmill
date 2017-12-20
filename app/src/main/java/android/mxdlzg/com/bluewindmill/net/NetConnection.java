package android.mxdlzg.com.bluewindmill.net;

import android.mxdlzg.com.bluewindmill.model.entity.config.HttpMethod;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by 廷江 on 2017/3/24.
 */

public class NetConnection {
    private ConfigCallback configCallback;
    public NetConnection(final String url, final HttpMethod method,final String[] uris ,final HttpCookie[] httpCookies, final SuccessCallback successCallback, final FailCallback failCallback, final ConfigCallback configCallback , final String ...kvs){
        new AsyncTask<Void,Integer,String>(){
            @Override
            protected String doInBackground(Void... params) {
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0;i<kvs.length;i+=2){
                    stringBuffer.append(kvs[i]).append("=").append(kvs[i+1]).append("&");
                }

                try{
                    HttpURLConnection connection;
                    CookieManager manager = (CookieManager) CookieManager.getDefault();
                    System.out.println(manager.toString());
                    if (uris != null && httpCookies != null){       //添加cookie
                        if (uris.length == httpCookies.length){
                            for (int i = 0;i<httpCookies.length;i++){
                                manager.getCookieStore().add(new URI(uris[i]),httpCookies[i]);
                            }
                        }
                    }

                    switch (method){
                        case POST:
                            connection = (HttpURLConnection) new URL(url).openConnection();
                            connection.setRequestMethod(String.valueOf(method));
                            connection.setInstanceFollowRedirects(false);
                            connection.setConnectTimeout(5000);
                            connection.setReadTimeout(3000);
//                            connection.setDoInput(true);
                            connection.setDoOutput(true);
                            connection.setRequestProperty("contentType","gbk");
                            OutputStream outputStream = connection.getOutputStream();
                            outputStream.write(stringBuffer.toString().substring(0,stringBuffer.length()-1).getBytes("gbk"));
                            System.out.println("写出参数"+stringBuffer.toString().substring(0,stringBuffer.length()-1));
                            outputStream.flush();
                            break;
                        default:
                            connection = (HttpURLConnection) new URL(url + "?" + stringBuffer.toString()).openConnection();
                            connection.setDoInput(true);
                            connection.setDoOutput(false);
                            break;
                    }


                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"gbk"));
                    String line = null;
                    StringBuffer resultContent = new StringBuffer();
                    while ((line = bufferedReader.readLine())!= null){
                        resultContent.append(new String(line.getBytes(),"utf-8"));
                        resultContent.append("\n");
                    }
                    System.out.println(connection.getResponseCode());   //200之类
                    System.out.println(connection.getResponseMessage());//OK
//                    System.out.println(resultContent);
//                    System.out.println(manager.getCookieStore().getCookies());

                    if (configCallback != null){
                        configCallback.onConfig(manager.getCookieStore());
                    }
                    return resultContent.toString();

                }catch (IOException | URISyntaxException e){
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(String s) {
                if (s != null){
                    if (successCallback != null){
                        successCallback.onSuccess(s);
                    }
                }else {
                    if (failCallback != null){
                        failCallback.onFail();
                    }
                }
                super.onPostExecute(s);
            }
        }.execute();


    }

    public ConfigCallback getConfigCallback() {
        return configCallback;
    }

    /**
     * 如果需要获取header内的东西，否则不要设置
     * @param configCallback
     */
    public void setConfigCallback(ConfigCallback configCallback) {
        this.configCallback = configCallback;
    }

    public static interface ConfigCallback{
        void onConfig(CookieStore cookieStore);
    }

    public static interface SuccessCallback{
        void onSuccess(String result);
    }

    public static interface FailCallback{
        void onFail();
    }
}
