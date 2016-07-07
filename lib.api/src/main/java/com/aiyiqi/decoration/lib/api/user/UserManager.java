package com.aiyiqi.decoration.lib.api.user;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.aiyiqi.decoration.lib.api.BaseManager;
import com.aiyiqi.decoration.lib.api.Server;
import com.aiyiqi.decoration.lib.api.VolleyManager;
import com.aiyiqi.decoration.lib.api.event.ResponseEvent;
import com.aiyiqi.decoration.lib.constants.Constants;
import com.aiyiqi.lib.utils.AppUtil;
import com.aiyiqi.lib.utils.ShareUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hubing on 16/3/20.
 */
public class UserManager extends BaseManager implements IUserManager {

    private static Object clockObj = new Object();
    private static UserManager mInstance;
    private VolleyManager volleyManager;

    private UserManager(Context context) {
        volleyManager = VolleyManager.getInstance(context);
    }

    public static UserManager getInstance(Context context) {
        synchronized (clockObj) {
            if (mInstance == null) {
                mInstance = new UserManager(context);
            }
            return mInstance;
        }
    }


    @Override
    public void login(final Context context, HashMap<String, String> map) {

        Log.e(Constants.TAG, "Log >> UserManager >> login >> json : " + map.toString());
        if (map != null) {
            final HashMap<String, String> params = resolveJsonToPost(context, map);
            volleyManager.post(context, formatUrl("/housekeep/login"), params, new VolleyManager.ServerResponse<String>() {
                @Override
                public void onSuccess(String result) {
                    if (AppUtil.ensureJson(result)) {
                        resolveJson(result, new OnRepSuccessListener() {
                            @Override
                            public void onSuccess(JSONObject dataJson) {
                                if (dataJson != null) {
                                    String token = dataJson.optString(Params.PARAM_TOKEN);
                                    ShareUtil.getInstance(context.getApplicationContext()).save(API.API_TOKEN, token);
                                    int  bossId= dataJson.optInt(Params.PARAM_BOSS_ID);
                                    ShareUtil.getInstance(context.getApplicationContext()).save(Constants.Key.KEY_BOSS_ID, bossId);
                                    ShareUtil.getInstance(context.getApplicationContext()).save(Constants.Key.KEY_LOGIN_IS_READY, true);
                                    JSONObject vendorJson = dataJson.optJSONObject(Params.PARAM_VENDOR_DETAIL);
                                    String vendorId = vendorJson.optString(Params.PARAM_VENDOR_ID);
                                    String nickName = vendorJson.optString(Params.NICK_NAME);
                                    String realName = vendorJson.optString(Params.REAL_NAME);
                                    if (!TextUtils.isEmpty(nickName)) {
                                        ShareUtil.getInstance(context.getApplicationContext()).save(Constants.Key.KEY_NICK_NAME, nickName);
                                    }else {
                                        ShareUtil.getInstance(context.getApplicationContext()).remove(Constants.Key.KEY_NICK_NAME);
                                    }

                                    if (!TextUtils.isEmpty(realName)) {
                                        ShareUtil.getInstance(context.getApplicationContext()).save(Constants.Key.KEY_REAL_NAME, realName);
                                    }else {
                                        ShareUtil.getInstance(context.getApplicationContext()).remove(Constants.Key.KEY_REAL_NAME);
                                    }
                                    String lifePhoto = vendorJson.optString(Params.LIFE_PHOTO);
                                    if (!TextUtils.isEmpty(lifePhoto)) {
                                        ShareUtil.getInstance(context.getApplicationContext()).save(Constants.Key.KEY_USER_LIFE_PIC, lifePhoto);
                                    }else {
                                        ShareUtil.getInstance(context.getApplicationContext()).remove(Constants.Key.KEY_USER_LIFE_PIC);
                                    }

                                    String singnature = vendorJson.optString(Params.SIGNATURE);
                                    if (!TextUtils.isEmpty(singnature)) {
                                        ShareUtil.getInstance(context.getApplicationContext()).save(Constants.Key.KEY_SIGNATURE, singnature);
                                    }else {
                                        ShareUtil.getInstance(context.getApplicationContext()).remove(Constants.Key.KEY_SIGNATURE);
                                    }
                                    String avadar = vendorJson.optString(Params.AVATAR);
                                    if (!TextUtils.isEmpty(avadar)) {
                                        ShareUtil.getInstance(context.getApplicationContext()).save(Constants.Key.KEY_USER_AVATAR, avadar);
                                    }else {
                                        ShareUtil.getInstance(context.getApplicationContext()).remove(Constants.Key.KEY_USER_AVATAR);
                                    }
                                    ShareUtil.getInstance(context.getApplicationContext()).save(Constants.Key.KEY_VENDOR_ID, vendorId);
                                    postEvent(ResponseEvent.TYPE_SERVER_USER_LOGIN, null, Server.Code.SUCCESS, "");
                                }
                            }

                            @Override
                            public void onFail(int errorCode, String errorMsg) {
                                postEvent(ResponseEvent.TYPE_SERVER_USER_LOGIN, null, errorCode, errorMsg);
                            }
                        });
                    }
                }

                @Override
                public void onError(int errorCode, String errorMsg) {
                    postEvent(ResponseEvent.TYPE_SERVER_USER_LOGIN, null, errorCode, errorMsg);
                }
            });
        }
    }

    @Override
    public void logout(final Context context) {
        String toke = getToken(context);
        if (!TextUtils.isEmpty(toke)) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put(Params.PARAM_TOKEN, toke);
            volleyManager.post(context, formatUrl("/vendor/logout"), params, new VolleyManager.ServerResponse<String>() {
                @Override
                public void onSuccess(String result) {
                    resolveJson(result, new OnRepSuccessListener() {

                        @Override
                        public void onSuccess(JSONObject dataJson) {
                            ShareUtil.getInstance(context).remove(API.API_TOKEN);
                            ShareUtil.getInstance(context.getApplicationContext()).remove(Constants.Key.KEY_LOGIN_IS_READY);
                        }

                        @Override
                        public void onFail(int errorCode, String errorMsg) {

                        }
                    });
                }

                @Override
                public void onError(int errorCode, String errorMsg) {

                }
            });
        }
    }

    @Override
    public void editPswd(Context context, HashMap<String, String> map) {

        HashMap<String, String> params = resolveJsonToPost(context, map);

        volleyManager.post(context, formatUrl("/vendor/editPswd"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {
                    @Override
                    public void onSuccess() {
                        super.onSuccess();
                        postEvent(ResponseEvent.TYPE_SERVER_MY_MODIYF_PD, null, Server.Code.SUCCESS, "");
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_MY_MODIYF_PD, null, errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_MY_MODIYF_PD, null, errorCode, errorMsg);
            }
        });

    }

    @Override
    public void upLoadMyInfor(Context context, HashMap<String, String> map) {
        if (map != null) {
            HashMap<String, String> params = resolveJsonToPost(context, map);
            volleyManager.post(context, formatUrl("/housekeep/editVendorDetail"), params, new VolleyManager.ServerResponse<String>() {
                @Override
                public void onSuccess(String result) {
                    resolveJson(result, new OnRepSuccessListener() {
                        @Override
                        public void onSuccess() {
                            super.onSuccess();
                            postEvent(ResponseEvent.TYPE_SERVER_UPLOAD_MY_INFO, null, Server.Code.SUCCESS, "");
                        }

                        @Override
                        public void onFail(int errorCode, String errorMsg) {
                            postEvent(ResponseEvent.TYPE_SERVER_UPLOAD_MY_INFO, null, errorCode, errorMsg);
                        }
                    });
                }

                @Override
                public void onError(int errorCode, String errorMsg) {
                    postEvent(ResponseEvent.TYPE_SERVER_UPLOAD_MY_INFO, null, errorCode, errorMsg);
                }
            });
        }


    }

    /*
    得到个人信息

    */
    @Override
    public void getMyInfor(final Context context, HashMap<String, String> map) {
        if (map != null) {
            HashMap<String, String> params = resolveJsonToPost(context, map);
            volleyManager.post(context, formatUrl("/housekeep/getVendorDetail"), params, new VolleyManager.ServerResponse<String>() {
                @Override
                public void onSuccess(String result) {
                    resolveJson(result, new OnRepSuccessListener() {
                        @Override
                        public void onSuccess(JSONObject vendorJson) {
                            super.onSuccess(vendorJson);
                            String vendorId = vendorJson.optString(Params.PARAM_VENDOR_ID);
                            String nickName = vendorJson.optString(Params.NICK_NAME);
                            String realName = vendorJson.optString(Params.REAL_NAME);
                            UserBean.UserInfo  userInfo = new UserBean.UserInfo();
                            userInfo.nickName= nickName;
                            if (!TextUtils.isEmpty(nickName)) {
                                ShareUtil.getInstance(context.getApplicationContext()).save(Constants.Key.KEY_NICK_NAME, nickName);
                            }else {
                                ShareUtil.getInstance(context.getApplicationContext()).remove(Constants.Key.KEY_NICK_NAME);
                            }
                            userInfo.realName= realName;
                            if (!TextUtils.isEmpty(realName)) {
                                ShareUtil.getInstance(context.getApplicationContext()).save(Constants.Key.KEY_REAL_NAME, realName);
                            }else {
                                ShareUtil.getInstance(context.getApplicationContext()).remove(Constants.Key.KEY_REAL_NAME);
                            }
                            String lifePhoto = vendorJson.optString(Params.LIFE_PHOTO);
                            if (!TextUtils.isEmpty(lifePhoto)) {
                                ShareUtil.getInstance(context.getApplicationContext()).save(Constants.Key.KEY_USER_LIFE_PIC, lifePhoto);
                            }else {
                                ShareUtil.getInstance(context.getApplicationContext()).remove(Constants.Key.KEY_USER_LIFE_PIC);
                            }
                            userInfo.lifePhoto= lifePhoto;
                            String singnature = vendorJson.optString(Params.SIGNATURE);
                            if (!TextUtils.isEmpty(singnature)) {
                                ShareUtil.getInstance(context.getApplicationContext()).save(Constants.Key.KEY_SIGNATURE, singnature);
                            }else {
                                ShareUtil.getInstance(context.getApplicationContext()).remove(Constants.Key.KEY_SIGNATURE);
                            }
                            userInfo.signature = singnature;
                            String avadar = vendorJson.optString(Params.AVATAR);
                            if (!TextUtils.isEmpty(avadar)) {
                                ShareUtil.getInstance(context.getApplicationContext()).save(Constants.Key.KEY_USER_AVATAR, avadar);
                            }else {
                                ShareUtil.getInstance(context.getApplicationContext()).remove(Constants.Key.KEY_USER_AVATAR);
                            }
                            userInfo.avatar =  avadar;
                            userInfo.province= vendorJson.optString("province");
                            userInfo.goodat =  vendorJson.optString("goodat");
                            userInfo.provinceId= vendorJson.optInt("provinceId");
                            userInfo.workYear= vendorJson.optInt("workYear");
                            userInfo.siteNum = vendorJson.optInt("siteNum");
                            userInfo.sendMsgNum=vendorJson.optInt("sendMsgNum");
                            userInfo.applyFromShareNum= vendorJson.optInt("applyFromShareNum");
                            ShareUtil.getInstance(context.getApplicationContext()).save(Constants.Key.KEY_VENDOR_ID, vendorId);

                            postEvent(ResponseEvent.TYPE_SERVER_GET_MY_INFO, userInfo, Server.Code.SUCCESS, "");
                        }

                        @Override
                        public void onFail(int errorCode, String errorMsg) {
                            postEvent(ResponseEvent.TYPE_SERVER_GET_MY_INFO, null, errorCode, errorMsg);
                        }
                    });
                }

                @Override
                public void onError(int errorCode, String errorMsg) {
                    postEvent(ResponseEvent.TYPE_SERVER_GET_MY_INFO, null, errorCode, errorMsg);
                }
            });
        }
    }

    @Override
    public void listProvinces(Context context, HashMap<String, String> map) {
        if (map != null) {
            HashMap<String, String> params = resolveJsonToPost(context, map);
            volleyManager.post(context, formatUrl("/location/listProvinces"), params, new VolleyManager.ServerResponse<String>() {
                @Override
                public void onSuccess(String result) {
                    resolveJson(result, new OnRepSuccessListener() {
                        @Override
                        public void onJsonArraySuccess(JSONArray jsonArray) {
                            super.onJsonArraySuccess(jsonArray);
                            if (jsonArray!=null){
                                ArrayList<UserBean.Province> mList = new ArrayList<UserBean.Province>();

                                int size= jsonArray.length();
                                for (int i= 0 ; i<size;i++){
                                    JSONObject itemJsonObject= jsonArray.optJSONObject(i);
                                    UserBean.Province province= new UserBean.Province();
                                    province.provinceId= itemJsonObject.optInt("provinceId");
                                    province.adCode= itemJsonObject.optString("adCode");
                                    province.cityName= itemJsonObject.optString("cityName");
                                    province.cityNamePinyin= itemJsonObject.optString("cityNamePinyin");
                                    province.latitude=itemJsonObject.optDouble("latitude");
                                    province.longitude =itemJsonObject.optDouble("longitude");
                                    mList.add(province);
                                }
                                postEvent(ResponseEvent.TYPE_SERVER_LIST_PROVINCES,mList,Server.Code.SUCCESS,"");
                            }
                        }
                        @Override
                        public void onFail(int errorCode, String errorMsg) {
                            postEvent(ResponseEvent.TYPE_SERVER_LIST_PROVINCES, null, errorCode, errorMsg);
                        }
                    });
                }
                @Override
                public void onError(int errorCode, String errorMsg) {
                    postEvent(ResponseEvent.TYPE_SERVER_LIST_PROVINCES, null, errorCode, errorMsg);
                }
            });
        }
    }

    @Override
    public void getBuildingSiteVendor(final Context context, HashMap<String, String> map) {
        if (map != null) {
            HashMap<String, String> params = resolveJsonToPost(context, map);
            volleyManager.post(context, formatUrl("/housekeep/getBuildingSiteVendor"), params, new VolleyManager.ServerResponse<String>() {
                @Override
                public void onSuccess(String result) {
                    resolveJson(result, new OnRepSuccessListener() {
                        @Override
                        public void onSuccess(JSONObject vendorJson) {
                            super.onSuccess(vendorJson);
                            String vendorId = vendorJson.optString(Params.PARAM_VENDOR_ID);
                            String nickName = vendorJson.optString(Params.NICK_NAME);
                            String realName = vendorJson.optString(Params.REAL_NAME);
                            UserBean.UserInfo  userInfo = new UserBean.UserInfo();
                            userInfo.nickName= nickName;
                            if (!TextUtils.isEmpty(nickName)) {
                                ShareUtil.getInstance(context.getApplicationContext()).save(Constants.Key.KEY_NICK_NAME, nickName);
                            }else {
                                ShareUtil.getInstance(context.getApplicationContext()).remove(Constants.Key.KEY_NICK_NAME);
                            }
                            userInfo.realName= realName;
                            if (!TextUtils.isEmpty(realName)) {
                                ShareUtil.getInstance(context.getApplicationContext()).save(Constants.Key.KEY_REAL_NAME, realName);
                            }else {
                                ShareUtil.getInstance(context.getApplicationContext()).remove(Constants.Key.KEY_REAL_NAME);
                            }
                            String lifePhoto = vendorJson.optString(Params.LIFE_PHOTO);
                            if (!TextUtils.isEmpty(lifePhoto)) {
                                ShareUtil.getInstance(context.getApplicationContext()).save(Constants.Key.KEY_USER_LIFE_PIC, lifePhoto);
                            }else {
                                ShareUtil.getInstance(context.getApplicationContext()).remove(Constants.Key.KEY_USER_LIFE_PIC);
                            }
                            userInfo.lifePhoto= lifePhoto;
                            String singnature = vendorJson.optString(Params.SIGNATURE);
                            if (!TextUtils.isEmpty(singnature)) {
                                ShareUtil.getInstance(context.getApplicationContext()).save(Constants.Key.KEY_SIGNATURE, singnature);
                            }else {
                                ShareUtil.getInstance(context.getApplicationContext()).remove(Constants.Key.KEY_SIGNATURE);
                            }
                            userInfo.signature = singnature;
                            String avadar = vendorJson.optString(Params.AVATAR);
                            if (!TextUtils.isEmpty(avadar)) {
                                ShareUtil.getInstance(context.getApplicationContext()).save(Constants.Key.KEY_USER_AVATAR, avadar);
                            }else {
                                ShareUtil.getInstance(context.getApplicationContext()).remove(Constants.Key.KEY_USER_AVATAR);
                            }
                            userInfo.avatar =  avadar;
                            userInfo.province= vendorJson.optString("provinceName");
                            userInfo.goodat =  vendorJson.optString("goodAt");
                            userInfo.provinceId= vendorJson.optInt("provinceId");
                            userInfo.workYear= vendorJson.optInt("workYear");
                            userInfo.siteNum = vendorJson.optInt("caseNumber");
                            userInfo.sendMsgNum=vendorJson.optInt("messageNumber");
                            userInfo.applyFromShareNum= vendorJson.optInt("sponsorNumber");
                            ShareUtil.getInstance(context.getApplicationContext()).save(Constants.Key.KEY_VENDOR_ID, vendorId);

                            postEvent(ResponseEvent.TYPE_SERVER_LIST_GETBUILDINGSITEVENDO, userInfo, Server.Code.SUCCESS, "");
                        }

                        @Override
                        public void onFail(int errorCode, String errorMsg) {
                            postEvent(ResponseEvent.TYPE_SERVER_LIST_GETBUILDINGSITEVENDO, null, errorCode, errorMsg);
                        }
                    });
                }

                @Override
                public void onError(int errorCode, String errorMsg) {
                    postEvent(ResponseEvent.TYPE_SERVER_LIST_GETBUILDINGSITEVENDO, null, errorCode, errorMsg);
                }
            });
        }
    }

    @Override
    public void upLoadXgToKen(Context context, HashMap<String, String> map) {
        if (map != null) {
            HashMap<String, String> params = resolveJsonToPost(context, map);
            volleyManager.post(context, formatUrl("/vendor/editXinggeToken"), params, new VolleyManager.ServerResponse<String>() {
                @Override
                public void onSuccess(String result) {
                    resolveJson(result, new OnRepSuccessListener() {
                        @Override
                        public void onSuccess() {
                            super.onSuccess();
                            postEvent(ResponseEvent.TYPE_SERVER_SITE_UPLOAD_XG_TOKEN,true, Server.Code.SUCCESS, "");
                        }

                        @Override
                        public void onFail(int errorCode, String errorMsg) {
                            postEvent(ResponseEvent.TYPE_SERVER_SITE_UPLOAD_XG_TOKEN, false, errorCode, errorMsg);
                        }
                    });
                }

                @Override
                public void onError(int errorCode, String errorMsg) {
                    postEvent(ResponseEvent.TYPE_SERVER_SITE_UPLOAD_XG_TOKEN, false, errorCode, errorMsg);
                }
            });
        }
    }
}
