package com.aiyiqi.decoration.lib.api.pay;

import android.content.Context;
import android.text.TextUtils;

import com.aiyiqi.decoration.lib.api.BaseManager;
import com.aiyiqi.decoration.lib.api.Server;
import com.aiyiqi.decoration.lib.api.VolleyManager;
import com.aiyiqi.decoration.lib.api.event.ResponseEvent;
import com.aiyiqi.decoration.lib.constants.Constants;
import com.aiyiqi.lib.utils.DeviceUtil;
import com.aiyiqi.lib.utils.Logger;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by hubing on 16/3/18.
 */
public class PayManager extends BaseManager implements IPayManager{
    private static Object objClock = new Object();
    private static PayManager mInstance;
    private VolleyManager volleyManager;
    private PayManager(Context context){
        volleyManager = VolleyManager.getInstance(context);
    };
    public static PayManager getInstance(Context context){
        synchronized (objClock){
            if(mInstance == null){
                mInstance = new PayManager(context);
            }
            return mInstance;
        }
    }

    @Override
    public void pay(Context context, final HashMap<String, String> map) {
        HashMap<String,String> hashMap = resolveJsonToPost(context,map);
        hashMap.put(Params.PAYMENT_SOURCE,Pay.Source.ANDROID);
        hashMap.put(Params.CURRENCY,PAY_CURRENCY);
        hashMap.put(Params.IP, DeviceUtil.fetchIpAddress());
        hashMap.put(Params.DEVICE_INFO, DeviceUtil.getAndroidId(context));

        volleyManager.post(context, formatUrl("/pay/pay"), hashMap, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {

                    @Override
                    public void onSuccessString(String result) {
                        super.onSuccessString(result);

                        Pay pay = new Pay();
                        pay.result = result;
                        pay.checkoutId = map.get(Server.Param.CHECKOUT_ID);

                        postEvent(ResponseEvent.TYPE_SERVER_PAY_QR, pay, Server.Code.SUCCESS, "");
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_PAY_QR,null,errorCode,errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_PAY_QR,null,errorCode,errorMsg);
            }
        });


    }

    @Override
    public void queyPaymentStatus(Context context, final HashMap<String, String> map) {
        HashMap<String,String> hashMap = resolveJsonToPost(context,map);
        hashMap.put(Params.PAYMENT_SOURCE,Pay.Source.ANDROID);
        volleyManager.post(context, formatUrl("/pay/queyPaymentStatus"), hashMap, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {

                    @Override
                    public void onSuccess(JSONObject json) {
                        super.onSuccess(json);
                        if(json!=null){

                            Logger.e(Constants.TAG,"queyPaymentStatus >> json.optInt(\"paymentStatus\") : "+json.optInt("paymentStatus")+" ; TYPE_SERVER_PAY_QUERY_STATUS : "+ResponseEvent.TYPE_SERVER_PAY_QUERY_STATUS);
                            if(json.optInt("paymentStatus") == 2 ){
                                map.put(Server.Param.PAY_RESULT_STATE,Server.Code.PAY_RESULT_SUCCESS);
                            }else{
                                map.put(Server.Param.PAY_RESULT_STATE,Server.Code.PAY_RESULT_FAILURE);
                            }
                            postEvent(ResponseEvent.TYPE_SERVER_PAY_QUERY_STATUS, map, Server.Code.SUCCESS,"");
                        }
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_PAY_QUERY_STATUS,null,errorCode,errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_PAY_QUERY_STATUS,"",errorCode,errorMsg);
            }
        });
    }

    @Override
    public void queyPayment(Context context, final HashMap<String, String> map){
        HashMap<String,String> hashMap = resolveJsonToPost(context,map);
        hashMap.put(Params.PAYMENT_SOURCE,Pay.Source.ANDROID);
        volleyManager.post(context, formatUrl("/pay/queyPayment"), hashMap, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {

                    @Override
                    public void onSuccess(JSONObject json){
                        super.onSuccess(json);
                        if(json!=null){
                            if(json.optInt("paymentStatus") == 2 ){
                                map.put(Server.Param.PAY_RESULT_STATE,Server.Code.PAY_RESULT_SUCCESS);
                            }else{
                                map.put(Server.Param.PAY_RESULT_STATE,Server.Code.PAY_RESULT_FAILURE);
                            }

                            postEvent(ResponseEvent.TYPE_SERVER_PAY_QUERY_STATUS,map,Server.Code.SUCCESS,"");
                        }
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_PAY_QUERY_STATUS,null,errorCode,errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_PAY_QUERY_STATUS, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void checkout(Context context, final HashMap<String, String> map) {
        HashMap<String,String> hashMap = resolveJsonToPost(context,map);
        volleyManager.post(context, formatUrl("/housekeep/checkout"), hashMap, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {

                    @Override
                    public void onSuccessString(String result) {
                        super.onSuccessString(result);

                        Pay.Checkout checkout = new Pay.Checkout();
                        checkout.result = result;
                        checkout.payChannel = map.get(Server.Param.CHANNEL);
                        if(map.containsKey(Server.Param.IS_PAY_OFFO_NLINE)){
                            if(TextUtils.equals(map.get(Server.Param.IS_PAY_OFFO_NLINE) , "1")){
                                checkout.isPayOffLine = true;
                            }else{
                                checkout.isPayOffLine = false;
                            }
                        }else{
                            checkout.isPayOffLine = false;
                        }

                        postEvent(ResponseEvent.TYPE_SERVER_PAY_GET_CHECKOUT_ID, checkout, Server.Code.SUCCESS, "");

                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_PAY_GET_CHECKOUT_ID,null,errorCode,errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_PAY_GET_CHECKOUT_ID, null, errorCode, errorMsg);
            }
        });
    }
}
