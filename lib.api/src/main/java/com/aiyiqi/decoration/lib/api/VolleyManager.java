package com.aiyiqi.decoration.lib.api;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;


import com.aiyiqi.decoration.lib.constants.Constants;
import com.aiyiqi.lib.utils.AppUtil;
import com.aiyiqi.lib.utils.DeviceUtil;
import com.aiyiqi.lib.utils.Logger;
import com.aiyiqi.lib.utils.NetUtil;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.MultiPartRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hubing on 16/3/17.
 */
public class VolleyManager {
    private static final String SERVER_INTERFACE_VERSION = "1.1";
    private static Object clockObj = new Object();
    private static VolleyManager mInstance;

    private final RequestQueue mRequestQueue;
    private final HashMap<String, String> mHeaders = new HashMap<String, String>();
    private final HashMap<String, String> mFileHeaders = new HashMap<String, String>();
    private VolleyManager (Context context){
        mRequestQueue = Volley.newRequestQueue(context);
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        mHeaders.put("model", AppUtil.getBrand() + AppUtil.getModel());
        mHeaders.put("imei", tm.getDeviceId());
        mHeaders.put("uuid", "");
        mHeaders.put("deviceId", DeviceUtil.getAndroidId(context));
        mHeaders.put("netType", NetUtil.getNetworkTypeName(tm.getNetworkType()));
        mHeaders.put("appVersion", AppUtil.getVersionName(context));
        mHeaders.put("location", "");
        mHeaders.put("channel", AppUtil.getMetaDataValue(context, Constants.Key.KEY_CHANNEL, ""));
        mHeaders.put("platform", "Android");
        mFileHeaders.putAll(mHeaders);
    }
    public static VolleyManager getInstance(Context context){
        synchronized (clockObj){
            if(mInstance == null){
                mInstance = new VolleyManager(context);
            }
            return mInstance;
        }
    }

    public void get(Context context,String url,ServerResponse<String> response){
        StringBuilder builder = new StringBuilder(url);
        builder.append("&app_version=");
        builder.append("android_");
        builder.append(AppUtil.getPackageName(context));
        builder.append("_");
        builder.append(SERVER_INTERFACE_VERSION);
        requestStringDataFromServer(Request.Method.GET,builder.toString(),null,response);
    }

    public void post(Context context,String url,HashMap<String,String> paramsMap,ServerResponse<String> response){
        StringBuilder builder = new StringBuilder();
        builder.append("android_");
        builder.append(AppUtil.getPackageName(context));
        builder.append("_");
        builder.append(SERVER_INTERFACE_VERSION);
        paramsMap.put("app_version", builder.toString());
        requestStringDataFromServer(Request.Method.POST,url,paramsMap,response);
    }

    private void requestStringDataFromServer(int method,final String url,final HashMap<String,String> paramsMap,final ServerResponse<String> serverResponse){

        if(serverResponse == null){
            throw new IllegalStateException("call back is null");
        }
        Logger.e(Constants.TAG, "request url:  { " + url + " }");
        final StringRequest request = new StringRequest(method, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Logger.e(Constants.TAG, "request url:  { " + url + " } ; response : "+response);
                serverResponse.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                final int statusCode = ((error != null) && (error.networkResponse != null)) ? error.networkResponse.statusCode : Constants.INVALID;
                final String errorMsg = error!=null ? (!TextUtils.isEmpty(error.getMessage())  ?  "网络异常，请稍后再试" : "网络异常，请稍后再试") : "网络异常，请稍后再试";
                serverResponse.onError(statusCode,errorMsg);
                Logger.e(Constants.TAG, "error response for url: {" + url + "} " + error.getMessage(), error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return mHeaders;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                final int method = getMethod();
                if (method == Method.GET) {
                    return null;
                } else if (method == Method.POST) {
                    Logger.e(Constants.TAG, "request post data: " + paramsMap.toString());
                    return paramsMap;
                }
                return null;
            }
        };
        request.setShouldCache(false);
        mRequestQueue.add(request);
    }

    private void uploadFile2Server(int method,final String url,final HashMap<String,String> paramsMap,final ServerResponse<String> serverResponse){

        if(serverResponse == null){
            throw new IllegalStateException("call back is null");
        }
        Logger.e(Constants.TAG, "request url:  { " + url + " }");
        final SimpleMultiPartRequest request = new SimpleMultiPartRequest(method, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Logger.e(Constants.TAG, "request url:  { " + url + " } ; response : "+response);
                serverResponse.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                final int statusCode = ((error != null) && (error.networkResponse != null)) ? error.networkResponse.statusCode : Constants.INVALID;
                final String errorMsg = error!=null ? !TextUtils.isEmpty(error.getMessage())? "网络异常请稍后重试" : "网络异常请稍后重试" : "网络异常请稍后重试";
                serverResponse.onError(statusCode,errorMsg);
                Logger.e(Constants.TAG, "error response for url: {" + url + "} " + error.getMessage(), error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return mHeaders;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                final int method = getMethod();
                if (method == Method.GET) {
                    return null;
                } else if (method == Method.POST) {
                    Logger.e(Constants.TAG, "request post data: " + paramsMap.toString());
                    return paramsMap;
                }
                return null;
            }
        };

        request.setShouldCache(false);
        mRequestQueue.add(request);
    }



    public static interface ServerResponse<T>{
        public void onSuccess(T result);
        public void onError(int errorCode, String errorMsg);
    }

}
