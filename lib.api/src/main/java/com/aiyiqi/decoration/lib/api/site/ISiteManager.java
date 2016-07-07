package com.aiyiqi.decoration.lib.api.site;

import android.content.Context;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by hubing on 16/3/23.
 */
public interface ISiteManager {
    public void addBuildingSite(Context context, HashMap<String, String> map);
    public void listBuildingSite(Context context, HashMap<String, String> map);
    public void listBuildingSiteTrack(Context context, HashMap<String, String> map);
    public void listMyRemind(Context context);
    public void addBuildingSiteTrack(Context context, HashMap<String, String> map); // 加跟进
    public void addBuildingSiteTrackReply(Context context, HashMap<String, String> map); // 回复跟进
    public void tagReadedBuildingSiteTrack(Context context, HashMap<String, String> map);
    public void deleteBuildingSiteTrack(Context context, HashMap<String, String> map);
    public void readBuildingSiteTrack(Context context, HashMap<String, String> map);
    public void countUnReaded(Context context, HashMap<String, String> map);
    public void buildingMessage(Context context, HashMap<String, String> map);
    public void ossToken(Context context, HashMap<String, String> map);
    public void editBuildingSiteAllot(Context context, HashMap<String, String> map);

    public void getMaterialList(Context context, HashMap<String, String> map);
    public void getSeachBrandList(Context context, HashMap<String, String> map);
    public void  loadSiteProgressData(Context context, HashMap<String, String> map);
    public void  upLoadMaterialData(Context context, HashMap<String, String> map);

    public  void updateBuildingSiteProgress2(Context context, HashMap<String, String> map);



}
