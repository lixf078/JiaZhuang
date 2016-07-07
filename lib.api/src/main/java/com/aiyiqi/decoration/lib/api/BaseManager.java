package com.aiyiqi.decoration.lib.api;

import android.content.Context;
import android.text.TextUtils;

import com.aiyiqi.decoration.lib.api.event.ResponseEvent;
import com.aiyiqi.decoration.lib.constants.Constants;
import com.aiyiqi.lib.base.BaseEvent;
import com.aiyiqi.lib.utils.Logger;
import com.aiyiqi.lib.utils.ShareUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by hubing on 16/3/17.
 */
public class BaseManager {

    public static final String PAY_CURRENCY = "CNY";
    public static final int PAY_STATUS_SUCCESS = 2;

    public static class Params {
        public final static String PARAM_BOSS_ID="bossId";
        public final static String PARAM_TOKEN = "token";
        public final static String PARAM_USER_ID = "user_id";
        public final static String PARAM_USER_NAME = "vendorAccount";
        public final static String PARAM_PASS_WORKD = "vendorPswd";
        public static final String CHANNEL = "channel";
        public static final String TYPE = "type";
        public static final String PAYMENT_SOURCE = "paymentSource";
        public static final String OPEN_ID = "openId";
        public static final String AUTH_CODE = "authCode";
        public static final String DEVICE_INFO = "deviceInfo";
        public static final String EXTRA_DATE = "extraData";
        public static final String CURRENCY = "currency";
        public static final String IP = "ip";
        public final static String PARAM_VENDOR_DETAIL = "vendorDetail";
        public final static String PARAM_VENDOR_ID = "vendorId";

        public final static String NICK_NAME = "nickName";
        public final static String REAL_NAME = "realName";
        public static final String SIGNATURE = "signature";
        public static final String AVATAR = "avatar";
        public static final String LIFE_PHOTO = "lifePhoto";


    }

    public static class API {
        public final static String API_TOKEN = "api_token";
    }

    protected boolean isResponseSuccess(int code) {
        return Server.Code.SUCCESS == code;
    }

    protected String formatUrl(String url) {
        StringBuilder builder = new StringBuilder();
        builder.append(Server.getServerUrl());
        builder.append(url);

        return builder.toString();
    }

    public String getToken(Context context) {
        return ShareUtil.getInstance(context).getStringValue(API.API_TOKEN, "");
    }

    public void postEvent(int eventType, Object resultObj, int errorCode, String msg) {
        ResponseEvent event = new ResponseEvent();
        event.eventType = eventType;
        event.errorCode = errorCode;
        if (resultObj != null) {
            event.resultObj = resultObj;
        }
        event.errorMsg = msg;
        EventBus.getDefault().post(event);
    }

    protected String resolveJsonToGet(JSONObject json) {
        StringBuilder builder = new StringBuilder();
        if (json != null) {
            Iterator<?> it = json.keys();
            while (it.hasNext()) {
                String key = it.next().toString();
                try {
                    String value = json.getString(key);
                    builder.append("&");
                    builder.append(key);
                    builder.append("=");
                    builder.append(value);
                } catch (JSONException e) {
                    Logger.e(Constants.TAG, "resolve json to get error.", e);
                }
            }
        }

        return builder.toString();
    }

    protected HashMap<String, String> resolveJsonToPost(Context context, HashMap<String, String> map) {
        String token = getToken(context);
        if (map == null) {
            map = new HashMap<String, String>();
        }
        if (!TextUtils.isEmpty(token)) {
            map.put(Params.PARAM_TOKEN, getToken(context));
        }
        /*if(json!=null){
            Iterator<?> it = json.keys();
            while (it.hasNext()){
                String key = it.next().toString();
                try {
                    String value = json.getString(key);
                    map.put(key,value);
                } catch (JSONException e) {
                    Logger.e(Constants.TAG,"resolve json to post error.",e);
                }
            }
        }*/

        return map;
    }


    protected void resolveJson(String json, OnRepSuccessListener listener) {
        if (!TextUtils.isEmpty(json)) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONObject baseOutputJson = jsonObject.getJSONObject("baseOutput");
                int code = baseOutputJson.optInt("code");
                String msg = baseOutputJson.optString("message");
                if (isResponseSuccess(code)) {
                    if (jsonObject.opt("data") != null) {
                        if (jsonObject.opt("data") instanceof JSONObject) {
                            listener.onSuccess(jsonObject.optJSONObject("data"));
                        } else if (jsonObject.opt("data") instanceof JSONArray) {
                            if (jsonObject.optJSONObject("pageInfo") != null) {
                                listener.onJsonArrayWidthPageSuccess(jsonObject.getJSONArray("data"), jsonObject.optJSONObject("pageInfo"));
                            } else {
                                listener.onJsonArraySuccess(jsonObject.getJSONArray("data"));
                            }

                        } else if (jsonObject.opt("data") instanceof String) {
                            listener.onSuccessString(jsonObject.optString("data"));
                        } else if (jsonObject.opt("data") instanceof Integer) {
                            listener.onSuccessInteger(jsonObject.optInt("data"));
                        }
                    } else {
                        listener.onSuccess();
                    }
                } else {
                    listener.onFail(code, msg);
                }
            } catch (Exception e) {
                Logger.e(Constants.TAG, "", e);
                listener.onFail(Constants.INVALID, Server.ERROR_SERVER_MSG);
            }
        }
    }

    protected abstract class OnRepSuccessListener {
        public void onSuccess() {
        }

        ;

        public void onSuccess(JSONObject json) {
        }

        ;

        public void onSuccessString(String result) {
        }

        ;

        public void onJsonArraySuccess(JSONArray jsonArray) {
        }

        ;

        public void onJsonArrayWidthPageSuccess(JSONArray jsonArray, JSONObject pageJson) {
        }

        ;

        public void onSuccessInteger(int result) {
        }

        ;

        public abstract void onFail(int errorCode, String errorMsg);
    }
}
