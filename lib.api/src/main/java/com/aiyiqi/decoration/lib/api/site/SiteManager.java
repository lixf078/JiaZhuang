package com.aiyiqi.decoration.lib.api.site;

import android.content.Context;
import android.text.TextUtils;

import com.aiyiqi.decoration.lib.api.BaseManager;
import com.aiyiqi.decoration.lib.api.PageInfo;
import com.aiyiqi.decoration.lib.api.Role;
import com.aiyiqi.decoration.lib.api.Server;
import com.aiyiqi.decoration.lib.api.VolleyManager;
import com.aiyiqi.decoration.lib.api.event.ResponseEvent;
import com.aiyiqi.decoration.lib.api.house.House;
import com.aiyiqi.decoration.lib.api.msg.MsgBean;
import com.aiyiqi.decoration.lib.api.order.OrderBean;
import com.aiyiqi.decoration.lib.api.pay.Pay;
import com.aiyiqi.decoration.lib.constants.Constants;
import com.aiyiqi.lib.utils.Logger;
import com.aiyiqi.lib.utils.ShareUtil;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hubing on 16/3/17.
 */
public class SiteManager extends BaseManager implements ISiteManager {
    private static Object objClock = new Object();
    private static SiteManager mInstance;
    private VolleyManager volleyManager;

    private SiteManager(Context context) {
        volleyManager = VolleyManager.getInstance(context);
    }

    public static SiteManager getInstance(Context context) {
        synchronized (objClock) {
            if (mInstance == null) {
                mInstance = new SiteManager(context);
            }
            return mInstance;
        }
    }

    @Override
    public void addBuildingSite(Context context, HashMap<String, String> map) {
        HashMap<String, String> params = resolveJsonToPost(context, map);
        volleyManager.post(context, formatUrl("/housekeep/addBuildingSite"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {

                    @Override
                    public void onSuccessString(String result) {
                        super.onSuccessString(result);
                        postEvent(ResponseEvent.TYPE_SERVER_SITE_ADD, result, Constants.SERVER_RESPONSE_SUCCESS, "");
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_SITE_ADD, null, errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_SITE_ADD, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void listBuildingSite(Context context, HashMap<String, String> map) {
        HashMap<String, String> params = resolveJsonToPost(context, map);
        volleyManager.post(context, formatUrl("/housekeep/listBuildingSite"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {

                    @Override
                    public void onJsonArrayWidthPageSuccess(JSONArray jsonArray, JSONObject pageJson) {
                        super.onJsonArrayWidthPageSuccess(jsonArray, pageJson);
                        if (jsonArray != null) {
                            SiteBean.ItemList itemList = new SiteBean.ItemList();
                            int size = jsonArray.length();
                            ArrayList<SiteBean.Item> siteList = new ArrayList<SiteBean.Item>();
                            for (int i = 0; i < size; i++) {
                                try {
                                    JSONObject siteObj = jsonArray.getJSONObject(i);
                                    SiteBean.Item item = new SiteBean.Item();
                                    JSONObject houseJson = siteObj.optJSONObject("orderHouse");
                                    House house = new House();
                                    if (houseJson != null) {
                                        house.type = houseJson.optInt("newHouse");
                                        house.area = houseJson.optString("area");
                                        House.Source source = new House.Source();
                                        source.name = TextUtils.isEmpty(houseJson.optString("source")) ? "待定" : houseJson.optString("source");
                                        house.source = source;
                                        House.Layout layout = new House.Layout();
                                        layout.name = TextUtils.isEmpty(houseJson.optString("layout")) ? "待定" : houseJson.optString("layout");
                                        house.layout = layout;
                                        house.measuerDate = houseJson.optLong("measureTime");
                                        house.doorplate = TextUtils.isEmpty(houseJson.optString("doorplate")) ? "待定" : houseJson.optString("doorplate");

                                        House.Address address = new House.Address();
                                        address.title = TextUtils.isEmpty(houseJson.optString("community")) ? "待定" : houseJson.optString("community");
                                        address.specialAddress = TextUtils.isEmpty(houseJson.optString("address")) ? "待定" : houseJson.optString("address");
                                        house.address = address;

                                    }
                                    JSONObject userJson = siteObj.optJSONObject("userDetail");

                                    if (userJson != null) {
                                        House.Owner owner = new House.Owner();
                                        owner.name = userJson.optString("realName");
                                        owner.phoneNum = userJson.optString("mobile");
                                        owner.id = String.valueOf(userJson.optLong("userId"));
                                        house.owner = owner;
                                    }
                                    item.house = house;

                                    JSONObject buildJson = siteObj.optJSONObject("buildingSite");
                                    if (buildJson != null) {
                                        item.orderId = String.valueOf(buildJson.optLong("orderId"));
                                        item.siteId = String.valueOf(buildJson.optLong("buildingId"));

                                        Logger.e(Constants.TAG, "/housekeep/listBuildingSite >> item.siteId : " + item.siteId);
                                    }

                                    siteList.add(item);
                                    itemList.items=siteList;
                                    PageInfo pageInfo = new PageInfo();
                                    if (pageJson != null) {
                                        pageInfo.pageNo = pageJson.optInt("pageNo");
                                        pageInfo.pageSize = pageJson.optInt("pageSize");
                                        pageInfo.pageTotalNum = pageJson.optInt("pageTotalNum");
                                    }
                                    itemList.pageInfo = pageInfo;

                                } catch (JSONException e) {
                                    Logger.e(Constants.TAG, "", e);
                                    postEvent(ResponseEvent.TYPE_SERVER_SITE_LIST, null, Constants.INVALID, "数据异常");
                                }

                            }
                            postEvent(ResponseEvent.TYPE_SERVER_SITE_LIST, itemList, Constants.SERVER_RESPONSE_SUCCESS, "");
                        }
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_SITE_LIST, null, errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_SITE_LIST, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void listBuildingSiteTrack(final Context context, HashMap<String, String> map) {
        HashMap<String, String> params = resolveJsonToPost(context, map);
        //   volleyManager.post(context, formatUrl("/housekeep/listBuildingSiteTrack"), params, new VolleyManager.ServerResponse<String>() {
        volleyManager.post(context, formatUrl("/housekeep/listBuildingSiteTrackByProgress"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {
                    @Override
                    public void onJsonArrayWidthPageSuccess(JSONArray jsonArray, JSONObject pageJson) {
                        super.onJsonArrayWidthPageSuccess(jsonArray, pageJson);
                        ArrayList<SiteBean.TrackMsg> msgArrayList = new ArrayList<SiteBean.TrackMsg>();
                        if (jsonArray != null) {
                            int size = jsonArray.length();
                            for (int i = 0; i < size; i++) {
                                try {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    SiteBean.TrackMsg msg = new SiteBean.TrackMsg();
                                    msg.content = jsonObject.getString("message");
                                    msg.publishTime = jsonObject.optLong("createTime");
                                    msg.trackId = String.valueOf(jsonObject.optLong("trackId"));
                                    Logger.e(Constants.TAG, "ShareUtil.getInstance(context) ----->" + ShareUtil.getInstance(context));
                                    msg.buildingId = String.valueOf(jsonObject.optLong("buildingId"));
                                    msg.avatar = jsonObject.optString("avatar");
                                    msg.publicAdress = jsonObject.optString("address");

                                    Role publisher = new Role();
                                    publisher.id = String.valueOf(jsonObject.optString("vendorId"));
                                    publisher.name = jsonObject.optString("creatorName");
                                    publisher.typeName = jsonObject.optString("creatorRole");
                                    msg.publisher = publisher;

                                    msg.shareUrl = "";

                                    ArrayList<Role> atFriends = new ArrayList<Role>();
                                    String atList = jsonObject.optString("atList");
                                    if (!TextUtils.isEmpty(atList)) {
                                        JSONArray atFriendJson = new JSONArray(atList);
                                        if (atFriendJson != null) {
                                            int atSize = atFriendJson.length();
                                            for (int j = 0; j < atSize; j++) {
                                                Role atRole = new Role();
                                                atRole.name = atFriendJson.getJSONObject(j).optString("name");
                                                atRole.id = atFriendJson.getJSONObject(j).optString("id");
                                                atFriends.add(atRole);
                                            }
                                            msg.atFriends = atFriends;
                                        }
                                    }
                                    String imagesJson = jsonObject.optString(Server.Param.UPLOAD_IMG_LIST);
                                    if (!TextUtils.isEmpty(imagesJson)) {
                                        ArrayList<String> trackImages = new ArrayList<String>();
                                        String[] imgs = imagesJson.split(",");
                                        int atSize = imgs.length;
                                        for (int j = 0; j < atSize; j++) {
                                            trackImages.add(imgs[j]);
                                        }
                                        msg.trackImgs = trackImages;
                                    }

                                    JSONArray replyJson = jsonObject.optJSONArray("replyList");
                                    if (replyJson != null) {
                                        ArrayList<SiteBean.Comment> trackComments = new ArrayList<SiteBean.Comment>();
                                        for (int k = 0, t = replyJson.length(); k < t; k++) {
                                            JSONObject replyJsonObj = replyJson.optJSONObject(k);
                                            SiteBean.Comment comment = new SiteBean.Comment();
                                            comment.id = replyJsonObj.optString("replyId");
                                            comment.replyId = replyJsonObj.optString("trackId");
                                            comment.content = replyJsonObj.optString("message");
                                            comment.publishTime = replyJsonObj.optLong("createTime");
                                            Role role = new Role();
                                            role.id = replyJsonObj.optString("userId");
                                            role.name = replyJsonObj.optString("name");
                                            role.typeName = replyJsonObj.optString("roleName");
                                            role.typeId = replyJsonObj.optString("vendorId");
                                            comment.publisher = role;
                                            trackComments.add(comment);
                                        }
                                        msg.trackComments = trackComments;
                                    }

                                    msgArrayList.add(msg);
                                } catch (JSONException e) {
                                    Logger.e(Constants.TAG, "", e);
                                }
                            }
                        }

                        PageInfo pageInfo = new PageInfo();
                        if (pageJson != null) {
                            pageInfo.pageNo = pageJson.optInt("pageNo");
                            pageInfo.pageSize = pageJson.optInt("pageSize");
                            pageInfo.pageTotalNum = pageJson.optInt("pageTotalNum");
                        }
//                        msgArrayList.pageInfo = pageInfo;

                        postEvent(ResponseEvent.TYPE_SERVER_SITE_TRACK_LIST, msgArrayList, Server.Code.SUCCESS, "");
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_SITE_TRACK_LIST, null, errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_SITE_TRACK_LIST, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void listMyRemind(Context context) {
        String token = getToken(context);
        if (!TextUtils.isEmpty(token)) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put(Params.PARAM_TOKEN, token);
            volleyManager.post(context, formatUrl("/housekeep/listMyRemind"), params, new VolleyManager.ServerResponse<String>() {
                @Override
                public void onSuccess(String result) {
                    resolveJson(result, new OnRepSuccessListener() {
                        @Override
                        public void onJsonArraySuccess(JSONArray jsonArray) {
                            super.onJsonArraySuccess(jsonArray);
                            ArrayList<MsgBean.AtMsg> atMsgs = new ArrayList<MsgBean.AtMsg>();
                            if (jsonArray != null) {
                                int size = jsonArray.length();
                                for (int i = 0; i < size; i++) {
                                    JSONObject jsonObject = jsonArray.optJSONObject(i);
                                    if (jsonObject != null) {
                                        MsgBean.AtMsg atMsg = new MsgBean.AtMsg();
                                        atMsg.ownerName = jsonObject.optString("userName");
                                        atMsg.roleName = jsonObject.optString("sponsorName");
                                        atMsg.publistTime = jsonObject.optLong("createTime");
                                        atMsg.trackId = String.valueOf(jsonObject.optLong("trackId"));
                                        atMsgs.add(atMsg);
                                    }
                                }
                            }
                            postEvent(ResponseEvent.TYPE_SERVER_MY_MSG, atMsgs, Server.Code.SUCCESS, "");
                        }

                        @Override
                        public void onFail(int errorCode, String errorMsg) {
                            postEvent(ResponseEvent.TYPE_SERVER_MY_MSG, null, errorCode, errorMsg);
                        }
                    });
                }

                @Override
                public void onError(int errorCode, String errorMsg) {
                    postEvent(ResponseEvent.TYPE_SERVER_MY_MSG, null, errorCode, errorMsg);
                }
            });
        }
    }

    @Override
    public void addBuildingSiteTrack(Context context, HashMap<String, String> map) {
        HashMap<String, String> params = resolveJsonToPost(context, map);

        volleyManager.post(context, formatUrl("/housekeep/addBuildingSiteTrack"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {

                    @Override
                    public void onSuccessString(String result) {
                        super.onSuccessString(result);
                        postEvent(ResponseEvent.TYPE_SERVER_SITE_ADD_TRACK, result, Server.Code.SUCCESS, "");
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_SITE_ADD_TRACK, null, errorCode, errorMsg);

                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_SITE_ADD_TRACK, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void addBuildingSiteTrackReply(Context context, HashMap<String, String> map) {
        HashMap<String, String> params = resolveJsonToPost(context, map);

        volleyManager.post(context, formatUrl("/housekeep/addBuildingReply"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {

                    @Override
                    public void onSuccessString(String result) {
                        super.onSuccessString(result);
                        postEvent(ResponseEvent.TYPE_SERVER_SITE_ADD_TRACK_REPLY, result, Server.Code.SUCCESS, "");
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_SITE_ADD_TRACK_REPLY, null, errorCode, errorMsg);

                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_SITE_ADD_TRACK_REPLY, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void tagReadedBuildingSiteTrack(Context context, HashMap<String, String> map) {
        HashMap<String, String> params = resolveJsonToPost(context, map);
        volleyManager.post(context, formatUrl("/housekeep/tagReadedBuildingSiteTrack"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {
                    @Override
                    public void onSuccess() {
                        super.onSuccess();

                        postEvent(ResponseEvent.TYPE_SERVER_SITE_TRACK_READED, "success", Server.Code.SUCCESS, "");
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_SITE_TRACK_READED, null, errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_SITE_TRACK_READED, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void deleteBuildingSiteTrack(Context context, HashMap<String, String> map) {
        HashMap<String, String> params = resolveJsonToPost(context, map);
        volleyManager.post(context, formatUrl("/housekeep/deleteBuildingSiteTrack"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {

                    @Override
                    public void onSuccess() {
                        super.onSuccess();
                        Logger.e(Constants.TAG, "deleteBuildingSiteTrack success");
                        postEvent(ResponseEvent.TYPE_SERVER_SITE_TRACK_DELETEED, "Success", Server.Code.SUCCESS, "");
                    }

                    @Override
                    public void onSuccessString(String data) {
                        super.onSuccess();
                        Logger.e(Constants.TAG, "deleteBuildingSiteTrack success");
                        postEvent(ResponseEvent.TYPE_SERVER_SITE_TRACK_DELETEED, "Success", Server.Code.SUCCESS, data);
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        Logger.e(Constants.TAG, "deleteBuildingSiteTrack onFail");
                        postEvent(ResponseEvent.TYPE_SERVER_SITE_TRACK_DELETEED, "", errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                Logger.e(Constants.TAG, "deleteBuildingSiteTrack onError");
                postEvent(ResponseEvent.TYPE_SERVER_SITE_TRACK_DELETEED, "", errorCode, errorMsg);
            }
        });
    }

    @Override
    public void readBuildingSiteTrack(Context context, HashMap<String, String> map) {
        HashMap<String, String> params = resolveJsonToPost(context, map);
        volleyManager.post(context, formatUrl("/housekeep/readBuildingSiteTrack"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        super.onSuccess(jsonObject);
                        SiteBean.TrackMsg msg = new SiteBean.TrackMsg();
                        if (jsonObject != null) {
                            msg.content = jsonObject.optString("message");
                            msg.publishTime = jsonObject.optLong("createTime");
                            msg.trackId = String.valueOf(jsonObject.optLong("trackId"));
                            msg.buildingId = String.valueOf(jsonObject.optLong("buildingId"));
                            Role publisher = new Role();
                            publisher.id = String.valueOf(jsonObject.optString("vendorId"));
                            publisher.name = jsonObject.optString("creatorName");
                            publisher.typeName = jsonObject.optString("creatorRole");
                            msg.publisher = publisher;
                            ArrayList<Role> atFriends = new ArrayList<Role>();
                            String atList = jsonObject.optString("atList");
                            if (!TextUtils.isEmpty(atList)) {
                                JSONArray atFriendJson = null;
                                try {
                                    atFriendJson = new JSONArray(atList);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (atFriendJson != null) {
                                    int atSize = atFriendJson.length();
                                    for (int j = 0; j < atSize; j++) {
                                        Role atRole = new Role();
                                        atRole.name = atFriendJson.optJSONObject(j).optString("name");
                                        atRole.id = atFriendJson.optJSONObject(j).optString("id");
                                        atFriends.add(atRole);
                                    }
                                    msg.atFriends = atFriends;
                                }
                            }

                            String imagesJson = jsonObject.optString(Server.Param.UPLOAD_IMG_LIST);
                            if (!TextUtils.isEmpty(imagesJson)) {
                                ArrayList<String> trackImages = new ArrayList<String>();
                                String[] imgs = imagesJson.split(",");
                                int atSize = imgs.length;
                                for (int j = 0; j < atSize; j++) {
                                    trackImages.add(imgs[j]);
                                }
                                msg.trackImgs = trackImages;
                            }

                            JSONArray replyJson = jsonObject.optJSONArray("replyList");
                            if (replyJson != null) {
                                ArrayList<SiteBean.Comment> trackComments = new ArrayList<SiteBean.Comment>();
                                for (int k = 0, t = replyJson.length(); k < t; k++) {
                                    JSONObject replyJsonObj = replyJson.optJSONObject(k);
                                    SiteBean.Comment comment = new SiteBean.Comment();
                                    comment.id = replyJsonObj.optString("replyId");
                                    comment.replyId = replyJsonObj.optString("trackId");
                                    comment.content = replyJsonObj.optString("message");
                                    comment.publishTime = replyJsonObj.optLong("createTime");
                                    Role role = new Role();
                                    role.id = replyJsonObj.optString("userId");
                                    role.name = replyJsonObj.optString("name");
                                    role.typeName = replyJsonObj.optString("roleName");
                                    role.typeId = replyJsonObj.optString("vendorId");
                                    comment.publisher = role;
                                    trackComments.add(comment);
                                }
                                msg.trackComments = trackComments;
                            }

                            postEvent(ResponseEvent.TYPE_SERVER_MY_MSG_DETAIL, msg, Server.Code.SUCCESS, "");

                        }
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_MY_MSG_DETAIL, null, errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_MY_MSG_DETAIL, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void countUnReaded(Context context, HashMap<String, String> map) {
        HashMap<String, String> params = resolveJsonToPost(context, map);
        volleyManager.post(context, formatUrl("/housekeep/countUnReaded"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {
                    @Override
                    public void onSuccessInteger(int result) {
                        super.onSuccessInteger(result);
                        postEvent(ResponseEvent.TYPE_SERVER_MY_MSG_NUMBER, result, Server.Code.SUCCESS, "");
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_MY_MSG_NUMBER, null, errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_MY_MSG_NUMBER, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void buildingMessage(Context context, HashMap<String, String> map) {
        HashMap<String, String> params = resolveJsonToPost(context, map);
        volleyManager.post(context, formatUrl("/housekeep/buildingMessage"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {
                    @Override
                    public void onSuccess(JSONObject json) {
                        super.onSuccess(json);
                        if (json != null) {
                            SiteBean.Detail detail = new SiteBean.Detail();

                            House house = new House();
                            House.Address address = new House.Address();
                            JSONObject houseJson = json.optJSONObject("orderHouse");
                            if (houseJson != null) {
                                address.specialAddress = TextUtils.isEmpty(houseJson.optString("address")) ? "待定" : houseJson.optString("address");
                                address.title = TextUtils.isEmpty(houseJson.optString("community")) ? "待定" : houseJson.optString("community");
                                house.address = address;
                                house.area = TextUtils.isEmpty(houseJson.optString("area")) ? "待定" : houseJson.optString("area");
                                House.Source source = new House.Source();
                                if (houseJson.has("measureTime")) {
                                    house.measuerDate = houseJson.optLong("measureTime");
                                } else {
                                    house.measuerDate = Constants.INVALID;
                                }
                                source.name = TextUtils.isEmpty(houseJson.optString("source")) ? "待定" : houseJson.optString("source");
                                house.source = source;
                                House.Layout layout = new House.Layout();
                                layout.name = TextUtils.isEmpty(houseJson.optString("layout")) ? "待定" : houseJson.optString("layout");
                                house.layout = layout;
                                house.type = Integer.valueOf(houseJson.optString("newHouse"));
                                house.housing = TextUtils.isEmpty(houseJson.optString("deliveryHouse")) ? "待定" : houseJson.optString("deliveryHouse");
                                house.doorplate = TextUtils.isEmpty(houseJson.optString("doorplate")) ? "待定" : houseJson.optString("doorplate");
                                house.ownerNeed = TextUtils.isEmpty(houseJson.optString("houseComment")) ? "暂无" : houseJson.optString("houseComment");


                            }

                            JSONObject userJson = json.optJSONObject("userDetail");
                            House.Owner owner = new House.Owner();
                            if (userJson != null) {
                                owner.name = userJson.optString("realName");
                                owner.phoneNum = userJson.optString("mobile");
                                house.owner = owner;
                            }

                            JSONArray roleJson = json.optJSONArray("allotList");
                            if (roleJson != null) {
                                ArrayList<Role> rollList = new ArrayList<Role>();
                                int size = roleJson.length();
                                for (int i = 0; i < size; i++) {
                                    JSONObject jsonObject = roleJson.optJSONObject(i);
                                    if (jsonObject != null) {
                                        Role role = new Role();
                                        role.id = String.valueOf(jsonObject.optInt("vendorId"));
                                        role.name = jsonObject.optString("vendorName");
                                        role.typeName = String.valueOf(jsonObject.optInt("vendorRole"));
                                        rollList.add(role);
                                    }
                                }
                                detail.roleList = rollList;
                            }

                            JSONObject channelJsonObject = json.optJSONObject("channel");
                            if (channelJsonObject != null) {
                                House.Category category = new House.Category();
                                category.name = channelJsonObject.optString("source");
                                House.ChildSource childSource = new House.ChildSource();

                                childSource.parentName = channelJsonObject.optString("channelFirst");
                                childSource.sourceName = channelJsonObject.optString("channelSecond");
                                house.category = category;
                                house.childSource = childSource;
                            }

                            house.owner = owner;
                            detail.house = house;

                            postEvent(ResponseEvent.TYPE_SERVER_SITE_DETAIL, detail, Server.Code.SUCCESS, "");
                        }

                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_SITE_DETAIL, null, errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_SITE_DETAIL, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void ossToken(Context context, HashMap<String, String> map) {
        HashMap<String, String> params = resolveJsonToPost(context, map);
        volleyManager.post(context, formatUrl("/oss/token"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {
                    @Override
                    public void onSuccess(JSONObject json) {
                        super.onSuccess(json);
                        OssToken ossToken = new OssToken();
                        if (json != null) {
                            ossToken.accessKeyId = json.optString("accessKeyId");
                            ossToken.accessKeySecret = json.optString("accessKeySecret");
                            ossToken.expiration = json.optString("expiration");
                            ossToken.securityToken = json.optString("securityToken");
                        }

                        postEvent(ResponseEvent.TYPE_SERVER_SITE_FETCH_OSSTOKEN, ossToken, Server.Code.SUCCESS, "");
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_SITE_FETCH_OSSTOKEN, null, errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_SITE_FETCH_OSSTOKEN, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void editBuildingSiteAllot(Context context, HashMap<String, String> map) {
        HashMap<String, String> params = resolveJsonToPost(context, map);
        volleyManager.post(context, formatUrl("/housekeep/editBuildingSiteAllot"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {
                    @Override
                    public void onSuccessString(String result) {
                        super.onSuccessString(result);
                        postEvent(ResponseEvent.TYPE_SERVER_SITE_MODIFY_TRACK_ROLE, result, Server.Code.SUCCESS, "");
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_SITE_MODIFY_TRACK_ROLE, null, errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_SITE_MODIFY_TRACK_ROLE, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void getMaterialList(Context context, HashMap<String, String> map) {

        HashMap<String, String> params = resolveJsonToPost(context, map);
        volleyManager.post(context, formatUrl("/housekeep/listSupportMeterialCaterogy"), params, new VolleyManager.ServerResponse<String>() {

            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {
                    @Override
                    public void onJsonArraySuccess(JSONArray jsonArray) {
                        super.onJsonArraySuccess(jsonArray);
                        if (jsonArray != null) {
                            ArrayList<SiteBean.Material> mLists = new ArrayList<SiteBean.Material>();
                            int arraylength = jsonArray.length();
                            for (int i = 0; i < arraylength; i++) {
                                JSONObject itemJson = jsonArray.optJSONObject(i);
                                SiteBean.Material material = new SiteBean.Material();
                                material.tagId = itemJson.optLong("tagId");
                                material.parentName = itemJson.optString("parentName");
                                material.tagName = itemJson.optString("tagName");
                                material.parentId = itemJson.optInt("parentId");
                                material.updateTime = itemJson.optLong("updateTime");
                                material.createTime = itemJson.optLong("createTime");
                                mLists.add(material);
                            }
                            postEvent(ResponseEvent.TYPE_SERVER_GET_MATERIAL_ORDER, mLists, Server.Code.SUCCESS, "");
                        }
                    }


                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_GET_MATERIAL_ORDER, null, errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_GET_MATERIAL_ORDER, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void getSeachBrandList(Context context, HashMap<String, String> map) {
        HashMap<String, String> params = resolveJsonToPost(context, map);
        volleyManager.post(context, formatUrl("/housekeep/listSupportMeterialBrand"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {
                    @Override
                    public void onJsonArraySuccess(JSONArray jsonArray) {
                        super.onJsonArraySuccess(jsonArray);
                        if (jsonArray != null) {
                            ArrayList<SiteBean.Brand> mLists = new ArrayList<SiteBean.Brand>();
                            int arraylength = jsonArray.length();
                            for (int i = 0; i < arraylength; i++) {
                                JSONObject itemJson = jsonArray.optJSONObject(i);
                                SiteBean.Brand band = new SiteBean.Brand();
                                band.brandId = itemJson.optLong("brandId");
                                band.brandName = itemJson.optString("brandName");
                                band.storeId = itemJson.optLong("storeId");
                                band.storeName = itemJson.optString("storeName");
                                band.categoryId=itemJson.optLong("categoryId");
                                mLists.add(band);
                            }
                            postEvent(ResponseEvent.TYPE_SERVER_GET_SEARCH_BRAND_DATA, mLists, Server.Code.SUCCESS, "");
                        }
                    }


                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_GET_SEARCH_BRAND_DATA, null, errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_GET_SEARCH_BRAND_DATA, null, errorCode, errorMsg);
            }
        });

    }

    @Override
    public void loadSiteProgressData(Context context, HashMap<String, String> map) {
        HashMap<String, String> params = resolveJsonToPost(context, map);
        volleyManager.post(context, formatUrl("/housekeep/getLiveBuildingSite"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {
                    @Override
                    public void onSuccess(JSONObject json) {
                        super.onSuccess(json);
                        JSONArray progressJsonArray = json.optJSONArray("progress");
                        SiteBean.StateItem stateItem = new SiteBean.StateItem();

                        ArrayList<SiteBean.State> mList = new ArrayList<SiteBean.State>();
                        if (progressJsonArray != null) {
                            int jsonArraySize = progressJsonArray.length();
                            for (int i = 0; i < jsonArraySize; i++) {
                                SiteBean.State state = new SiteBean.State();
                                JSONObject stateJsonObject = progressJsonArray.optJSONObject(i);
                                state.level = stateJsonObject.optInt("progressStatus");
                                state.id = stateJsonObject.optInt("progressId");
                                state.name = stateJsonObject.optString("progressName");
                                state.creatTime = stateJsonObject.optLong("createTime");
                                mList.add(state);

                            }
                            stateItem.mStateList = mList;
                        }
                        JSONObject jsonObject = json.optJSONObject("buildingSite");
                        if (jsonObject != null) {
                            int currentProgress = jsonObject.optInt("progressId");
                            stateItem.curentProgressId = currentProgress;
                        }
                        postEvent(ResponseEvent.TYPE_SERVER_LOAD_SITE_STATE_DATA, stateItem, Server.Code.SUCCESS, "");
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_LOAD_SITE_STATE_DATA, null, errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_LOAD_SITE_STATE_DATA, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void upLoadMaterialData(final Context context, HashMap<String, String> map) {
        HashMap<String, String> params = resolveJsonToPost(context, map);
        volleyManager.post(context, formatUrl("/housekeep/addBuildingMaterial"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {
                    @Override
                    public void onSuccess() {
                        super.onSuccess();
                        postEvent(ResponseEvent.TYPE_SERVER_UPLOAD_METERRIAL, null, Server.Code.SUCCESS, "");
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_UPLOAD_METERRIAL, null, errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_UPLOAD_METERRIAL, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void updateBuildingSiteProgress2(Context context, HashMap<String, String> map) {
        HashMap<String, String> params = resolveJsonToPost(context, map);
        volleyManager.post(context, formatUrl("/housekeep/updateBuildingSiteProgress2"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {

                    @Override
                    public void onSuccess(JSONObject json) {
                        super.onSuccess(json);
                        postEvent(ResponseEvent.TYPE_SERVER_UPLOAD_BUILING_SITE_PROGRESS2, null, Server.Code.SUCCESS, "");
                    }

                    @Override
                    public void onSuccess() {
                        super.onSuccess();
                        postEvent(ResponseEvent.TYPE_SERVER_UPLOAD_BUILING_SITE_PROGRESS2, null, Server.Code.SUCCESS, "");
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_UPLOAD_BUILING_SITE_PROGRESS2, null, errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_UPLOAD_BUILING_SITE_PROGRESS2, null, errorCode, errorMsg);
            }
        });
    }
}
