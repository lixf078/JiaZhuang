package com.aiyiqi.decoration.lib.api.user;

import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by hubing on 16/3/22.
 */
public interface IUserManager {

    public void login(Context context, HashMap<String, String> map);
    public void logout(Context context);
    public void editPswd(Context context, HashMap<String, String> map);
    public void upLoadMyInfor(Context context, HashMap<String, String> map);
    public void getMyInfor(Context context, HashMap<String, String> map);
    public void listProvinces(Context context, HashMap<String, String> map);
    public void getBuildingSiteVendor(Context context, HashMap<String, String> map);
    public void upLoadXgToKen(Context context, HashMap<String, String> map);
}
