package com.topsec.sslvpn.lib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.util.ArrayMap;
import android.util.Log;
import android.webkit.WebView;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.apache.http.HttpHost;

class ProxySettings {
    private static final String LOG_TAG = "ProxySettings";

    ProxySettings() {
    }

    public static int setProxy(List<WebView> wvSrcLst, String strSrcHost, int iSrcPort) {
        int iFailedCount = 0;
        if (wvSrcLst == null) {
            return -1;
        }
        for (WebView wvTmp : wvSrcLst) {
            if (wvTmp != null) {
                try {
                    if (!setProxy(wvTmp, strSrcHost, iSrcPort)) {
                        iFailedCount++;
                    }
                } catch (Exception ex) {
                    iFailedCount++;
                    Log.e(LOG_TAG, "setproxy error:" + ex.getMessage());
                }
            }
        }
        return iFailedCount;
    }

    public static int cancelProxy(List<WebView> wvSrcLst) {
        int iFailedCount = 0;
        if (wvSrcLst == null) {
            return -1;
        }
        for (WebView wvTmp : wvSrcLst) {
            if (wvTmp != null) {
                try {
                    if (!cancelProxy(wvTmp)) {
                        iFailedCount++;
                    }
                } catch (Exception ex) {
                    iFailedCount++;
                    Log.e(LOG_TAG, "setproxy error:" + ex.getMessage());
                }
            }
        }
        return iFailedCount;
    }

    public static boolean setProxy(WebView webview, String host, int port) {
        Log.i(LOG_TAG, "SDK==" + VERSION.SDK_INT);
        if (VERSION.SDK_INT <= 13) {
            return setProxyUpToHC(webview, host, port);
        }
        if (VERSION.SDK_INT <= 15) {
            return setProxyICS(webview, host, port);
        }
        if (VERSION.SDK_INT <= 18) {
            return setProxyJB(webview, host, port);
        }
        if (VERSION.SDK_INT < 25) {
            return setProxyKK(webview, host, port, "android.app.Application");
        }
        return setLWebViewProxy(webview, host, port);
    }

    public static boolean cancelProxy(WebView webview) {
        try {
            if (VERSION.SDK_INT <= 18) {
                Class<?> networkClass = Class.forName("android.webkit.Network");
                if (networkClass != null) {
                    Object networkObj = invokeMethod(networkClass, "getInstance", new Object[]{webview.getContext()}, Context.class);
                    if (networkObj == null) {
                        return true;
                    }
                    Field f = networkObj.getClass().getDeclaredField("mRequestQueue");
                    f.setAccessible(true);
                    Object ret = f.get(networkObj);
                    f = ret.getClass().getDeclaredField("mProxyHost");
                    f.setAccessible(true);
                    f.set(ret, null);
                    return true;
                }
                return false;
            }
            setProxy(webview, null, 0);
            return true;
        } catch (ClassNotFoundException e) {
            Log.e(LOG_TAG, e.getMessage());
        } catch (Exception e2) {
            Log.e(LOG_TAG, e2.getMessage());
        }
        return false;
    }
    private static Object invokeMethod(Object object, String methodName, Object[] params, Class<?> types) throws Exception {
        Class<?> c = object instanceof Class ? (Class) object : object.getClass();
        if (types == null) {
            return c.getMethod(methodName, new Class[0]).invoke(object, new Object[0]);
        }
        return c.getMethod(methodName, new Class[]{types}).invoke(object, params);
    }

    private static boolean setProxyUpToHC(WebView webview, String host, int port) {
        Log.d(LOG_TAG, "Adding to Monitor list with <= 3.2 API.");
        HttpHost proxyServer = new HttpHost(host, port);
        try {
            Class networkClass = Class.forName("android.webkit.Network");
            if (networkClass == null) {
                Log.e(LOG_TAG, "failed to get class for android.webkit.Network");
                return false;
            }
            Method getInstanceMethod = networkClass.getMethod("getInstance", new Class[]{Context.class});
            if (getInstanceMethod == null) {
                Log.e(LOG_TAG, "failed to get getInstance method");
            }
            Object network = getInstanceMethod.invoke(networkClass, new Object[]{webview.getContext()});
            if (network == null) {
                Log.e(LOG_TAG, "error getting network: network is null");
                return false;
            }
            try {
                Object requestQueue = getFieldValueSafely(networkClass.getDeclaredField("mRequestQueue"), network);
                if (requestQueue == null) {
                    Log.e(LOG_TAG, "Request queue is null");
                    return false;
                }
                try {
                    Field proxyHostField = Class.forName("android.net.http.RequestQueue").getDeclaredField("mProxyHost");
                    boolean temp = proxyHostField.isAccessible();
                    try {
                        proxyHostField.setAccessible(true);
                        proxyHostField.set(requestQueue, proxyServer);
                    } catch (Exception e) {
                        Log.e(LOG_TAG, "error Adding to Monitor list host");
                    } finally {
                        proxyHostField.setAccessible(temp);
                    }
                    Log.d(LOG_TAG, "Adding to Monitor list with <= 3.2 API successful!");
                    return true;
                } catch (Exception e2) {
                    Log.e(LOG_TAG, "error getting proxy host field");
                    return false;
                }
            } catch (Exception e3) {
                Log.e(LOG_TAG, "error getting field value");
                return false;
            }
        } catch (Exception ex) {
            Log.e(LOG_TAG, "error getting network: " + ex);
            return false;
        }
    }

    private static boolean setProxyICS(WebView webview, String host, int port) {
        try {
            Log.d(LOG_TAG, "Adding to Monitor list with 4.0 API.");
            Method updateProxyInstance = Class.forName("android.webkit.JWebCoreJavaBridge").getDeclaredMethod("updateProxy", new Class[]{Class.forName("android.net.ProxyProperties")});
            Object sJavaBridge = getFieldValueSafely(Class.forName("android.webkit.BrowserFrame").getDeclaredField("sJavaBridge"), getFieldValueSafely(Class.forName("android.webkit.WebViewCore").getDeclaredField("mBrowserFrame"), getFieldValueSafely(Class.forName("android.webkit.WebView").getDeclaredField("mWebViewCore"), webview)));
            Object[] objArr = new Object[1];
            objArr[0] = Class.forName("android.net.ProxyProperties").getConstructor(new Class[]{String.class, Integer.TYPE, String.class}).newInstance(new Object[]{host, Integer.valueOf(port), null});
            updateProxyInstance.invoke(sJavaBridge, objArr);
            Log.d(LOG_TAG, "Adding to Monitor list with 4.0 API successful!");
            return true;
        } catch (Exception ex) {
            Log.e(LOG_TAG, "failed to set HTTP proxy: " + ex);
            return false;
        }
    }

    private static boolean setProxyJB(WebView webview, String host, int port) {
        Log.d(LOG_TAG, "Adding to Monitor list with >= 4.1 API.");
        try {
            Object sJavaBridge = getFieldValueSafely(Class.forName("android.webkit.BrowserFrame").getDeclaredField("sJavaBridge"), getFieldValueSafely(Class.forName("android.webkit.WebViewCore").getDeclaredField("mBrowserFrame"), getFieldValueSafely(Class.forName("android.webkit.WebViewClassic").getDeclaredField("mWebViewCore"), Class.forName("android.webkit.WebViewClassic").getDeclaredMethod("fromWebView", new Class[]{Class.forName("android.webkit.WebView")}).invoke(null, new Object[]{webview}))));
            Constructor ppcont = Class.forName("android.net.ProxyProperties").getConstructor(new Class[]{String.class, Integer.TYPE, String.class});
            Method updateProxyInstance = Class.forName("android.webkit.JWebCoreJavaBridge").getDeclaredMethod("updateProxy", new Class[]{Class.forName("android.net.ProxyProperties")});
            Object[] objArr = new Object[1];
            objArr[0] = ppcont.newInstance(new Object[]{host, Integer.valueOf(port), null});
            updateProxyInstance.invoke(sJavaBridge, objArr);
            Log.d(LOG_TAG, "Adding to Monitor list with >= 4.1 API successful!");
            return true;
        } catch (Exception ex) {
            Log.e(LOG_TAG, "Adding to Monitor list with >= 4.1 API failed with error: " + ex.getMessage());
            return false;
        }
    }

    @SuppressLint({"NewApi"})
    private static boolean setProxyKK(WebView webView, String host, int port, String applicationClassName) {
        Writer sw;
        String exceptionAsString;
        Log.d(LOG_TAG, "Adding to Monitor list with >= 4.4 API.");
        Context appContext = webView.getContext().getApplicationContext();
        if (host != null) {
            System.setProperty("http.proxyHost", host);
            System.setProperty("http.proxyPort", new StringBuilder(String.valueOf(port)).toString());
            System.setProperty("https.proxyHost", host);
            System.setProperty("https.proxyPort", new StringBuilder(String.valueOf(port)).toString());
        } else {
            System.setProperty("http.proxyHost", "");
            System.setProperty("http.proxyPort", "");
            System.setProperty("https.proxyHost", "");
            System.setProperty("https.proxyPort", "");
        }
        try {
            Field loadedApkField = Class.forName(applicationClassName).getField("mLoadedApk");
            loadedApkField.setAccessible(true);
            Object loadedApk = loadedApkField.get(appContext);
            Field receiversField = Class.forName("android.app.LoadedApk").getDeclaredField("mReceivers");
            receiversField.setAccessible(true);
            for (Object receiverMap : ((ArrayMap) receiversField.get(loadedApk)).values()) {
                for (Object rec : ((ArrayMap)receiverMap).keySet()) {
                    Class clazz = rec.getClass();
                    if (clazz.getName().contains("ProxyChangeListener")) {
                        Method onReceiveMethod = clazz.getDeclaredMethod("onReceive", new Class[]{Context.class, Intent.class});
                        Intent intent = new Intent("android.intent.action.PROXY_CHANGE");
                        onReceiveMethod.invoke(rec, new Object[]{appContext, intent});
                    }
                }
            }
            Log.d(LOG_TAG, "Adding to Monitor list with >= 4.4 API successful!");
            return true;
        } catch (ClassNotFoundException e) {
            sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            exceptionAsString = sw.toString();
            Log.v(LOG_TAG, e.getMessage());
            Log.v(LOG_TAG, exceptionAsString);
        } catch (NoSuchFieldException e2) {
            sw = new StringWriter();
            e2.printStackTrace(new PrintWriter(sw));
            exceptionAsString = sw.toString();
            Log.v(LOG_TAG, e2.getMessage());
            Log.v(LOG_TAG, exceptionAsString);
        } catch (IllegalAccessException e3) {
            sw = new StringWriter();
            e3.printStackTrace(new PrintWriter(sw));
            exceptionAsString = sw.toString();
            Log.v(LOG_TAG, e3.getMessage());
            Log.v(LOG_TAG, exceptionAsString);
        } catch (IllegalArgumentException e4) {
            sw = new StringWriter();
            e4.printStackTrace(new PrintWriter(sw));
            exceptionAsString = sw.toString();
            Log.v(LOG_TAG, e4.getMessage());
            Log.v(LOG_TAG, exceptionAsString);
        } catch (NoSuchMethodException e5) {
            sw = new StringWriter();
            e5.printStackTrace(new PrintWriter(sw));
            exceptionAsString = sw.toString();
            Log.v(LOG_TAG, e5.getMessage());
            Log.v(LOG_TAG, exceptionAsString);
        } catch (InvocationTargetException e6) {
            sw = new StringWriter();
            e6.printStackTrace(new PrintWriter(sw));
            exceptionAsString = sw.toString();
            Log.v(LOG_TAG, e6.getMessage());
            Log.v(LOG_TAG, exceptionAsString);
        }
        return false;
    }

    @SuppressLint({"NewApi"})
    public static boolean setLWebViewProxy(WebView webView, String host, int port) {
        System.setProperty("http.proxyHost", host);
        System.setProperty("http.proxyPort", new StringBuilder(String.valueOf(port)).toString());
        System.setProperty("https.proxyHost", host);
        System.setProperty("https.proxyPort", new StringBuilder(String.valueOf(port)).toString());
        try {
            Context appContext = webView.getContext().getApplicationContext();
            Field mLoadedApkField = Class.forName("android.app.Application").getDeclaredField("mLoadedApk");
            mLoadedApkField.setAccessible(true);
            Object mloadedApk = mLoadedApkField.get(appContext);
            Field mReceiversField = Class.forName("android.app.LoadedApk").getDeclaredField("mReceivers");
            mReceiversField.setAccessible(true);
            for (Object receiverMap : ((ArrayMap) mReceiversField.get(mloadedApk)).values()) {
                for (Object receiver : ((ArrayMap)receiverMap).keySet()) {
                    Class<?> clazz = receiver.getClass();
                    if (clazz.getName().contains("ProxyChangeListener")) {
                        Method onReceiveMethod = clazz.getDeclaredMethod("onReceive", new Class[]{Context.class, Intent.class});
                        Intent intent = new Intent("android.intent.action.PROXY_CHANGE");
                        onReceiveMethod.invoke(receiver, new Object[]{appContext, intent});
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static Object getFieldValueSafely(Field field, Object classInstance) throws IllegalArgumentException, IllegalAccessException {
        boolean oldAccessibleValue = field.isAccessible();
        field.setAccessible(true);
        Object result = field.get(classInstance);
        field.setAccessible(oldAccessibleValue);
        return result;
    }
}
