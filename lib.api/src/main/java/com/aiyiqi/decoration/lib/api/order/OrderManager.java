package com.aiyiqi.decoration.lib.api.order;

import android.content.Context;
import android.text.TextUtils;

import com.aiyiqi.decoration.lib.api.BaseManager;
import com.aiyiqi.decoration.lib.api.PageInfo;
import com.aiyiqi.decoration.lib.api.Role;
import com.aiyiqi.decoration.lib.api.Server;
import com.aiyiqi.decoration.lib.api.VolleyManager;
import com.aiyiqi.decoration.lib.api.event.ResponseEvent;
import com.aiyiqi.decoration.lib.api.house.House;
import com.aiyiqi.decoration.lib.constants.Constants;
import com.aiyiqi.lib.utils.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hubing on 16/3/17.
 */
public class OrderManager extends BaseManager implements IOrderManager {
    private static Object objClock = new Object();
    private VolleyManager volleyManager;
    private static OrderManager mInstance;

    private OrderManager(Context context) {
        volleyManager = VolleyManager.getInstance(context);
    }

    ;

    public static OrderManager getInstance(Context context) {
        synchronized (objClock) {
            if (mInstance == null) {
                mInstance = new OrderManager(context);
            }
            return mInstance;
        }
    }

    @Override
    public void listOrderNum(Context context) {
        String token = getToken(context);
        if (!TextUtils.isEmpty(token)) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put(Params.PARAM_TOKEN, token);
            volleyManager.post(context, formatUrl("/housekeep/listOrderNum"), params, new VolleyManager.ServerResponse<String>() {
                @Override
                public void onSuccess(final String result) {
                    ResponseEvent event = new ResponseEvent();
                    event.eventType = ResponseEvent.TYPE_SERVER_ORDER_TYPE_LIST;
                    resolveJson(result, new OnRepSuccessListener() {
                        @Override
                        public void onJsonArraySuccess(JSONArray jsonArray) {
                            if (jsonArray != null) {
                                int size = jsonArray.length();
                                ArrayList<OrderBean.State> states = new ArrayList<OrderBean.State>();
                                try {
                                    for (int i = 0; i < size; i++) {
                                        OrderBean.State state = new OrderBean.State();
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        state.id = jsonObject.optInt("id");
                                        state.name = jsonObject.optString("name");
                                        state.number = jsonObject.optInt("num");
                                        states.add(state);
                                    }
                                } catch (JSONException e) {
                                    Logger.e(Constants.TAG, "resolve list order type error.", e);
                                }
                                postEvent(ResponseEvent.TYPE_SERVER_ORDER_TYPE_LIST, states, Server.Code.SUCCESS, "");
                            }
                        }

                        @Override
                        public void onFail(int errorCode, String errorMsg) {
                            postEvent(ResponseEvent.TYPE_SERVER_ORDER_TYPE_LIST, null, errorCode, errorMsg);
                        }
                    });
                }

                @Override
                public void onError(int errorCode, String errorMsg) {
                    postEvent(ResponseEvent.TYPE_SERVER_ORDER_TYPE_LIST, null, errorCode, errorMsg);
                }
            });
        }
    }

    @Override
    public void listOrder(Context context, HashMap<String, String> map) {//String groupId
        HashMap<String, String> params = resolveJsonToPost(context, map);
        volleyManager.post(context, formatUrl("/housekeep/listOrder"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {
                    @Override
                    public void onJsonArrayWidthPageSuccess(JSONArray jsonArray, JSONObject pageJson) {
                        super.onJsonArrayWidthPageSuccess(jsonArray, pageJson);

                        OrderBean.ItemList itemList = new OrderBean.ItemList();
                        int size = jsonArray.length();
                        ArrayList<OrderBean.Item> items = new ArrayList<OrderBean.Item>();
                        for (int i = 0; i < size; i++) {
                            try {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                OrderBean.Item item = new OrderBean.Item();
                                JSONObject skuObj = jsonObject.optJSONObject("productItem");
                                if (skuObj != null) {
                                    OrderBean.Sku sku = new OrderBean.Sku();
                                    sku.id = String.valueOf(skuObj.optLong("skuId"));
                                    sku.name = String.valueOf(skuObj.optString("itemName"));
                                    item.sku = sku;
                                }

                                JSONObject orderObj = jsonObject.optJSONObject("order");
                                if (orderObj != null) {
                                    item.orderId = String.valueOf(orderObj.optLong("orderId"));
                                    OrderBean.State state = new OrderBean.State();
                                    state.id = orderObj.optInt("orderStatusId");
                                    switch (state.id) {
                                        case 170: {
                                            state.name = OrderBean.State.STATE_170;
                                            break;
                                        }
                                        case 180: {
                                            state.name = OrderBean.State.STATE_180;
                                            break;
                                        }
                                        case 200: {
                                            state.name = OrderBean.State.STATE_200;
                                            break;
                                        }
                                        case 210: {
                                            state.name = OrderBean.State.STATE_210;
                                            break;
                                        }
                                        case 220: {
                                            state.name = OrderBean.State.STATE_220;
                                            break;
                                        }
                                        case 230: {
                                            state.name = OrderBean.State.STATE_230;
                                            break;
                                        }
                                        case 240: {
                                            state.name = OrderBean.State.STATE_240;
                                            break;
                                        }
                                        case 250: {
                                            state.name = OrderBean.State.STATE_250;
                                            break;
                                        }
                                        case 260: {
                                            state.name = OrderBean.State.STATE_260;
                                            break;
                                        }
                                    }
                                    item.state = state;
                                }

                                House house = new House();
                                House.Address address = new House.Address();
                                JSONObject houseJson = jsonObject.optJSONObject("orderHouse");
                                if (houseJson != null) {
                                    address.specialAddress = houseJson.optString("address");
                                    address.title = houseJson.optString("community");
                                    house.address = address;
                                    house.area = houseJson.optString("area");
                                    House.Source source = new House.Source();
                                    house.measuerDate = System.currentTimeMillis();
                                    source.name = houseJson.optString("source");
                                    house.source = source;
                                    House.Layout layout = new House.Layout();
                                    layout.name = houseJson.optString("layout");
                                    house.layout = layout;
                                    if (!TextUtils.isEmpty(houseJson.optString("newHouse"))) {
                                        house.type = Integer.valueOf(houseJson.optString("newHouse"));
                                    }
                                    house.doorplate = houseJson.optString("doorplate");
                                }

                                JSONObject userJson = jsonObject.optJSONObject("userDetail");
                                House.Owner owner = new House.Owner();
                                owner.name = userJson.optString("realName");
                                owner.phoneNum = userJson.optString("mobile");
                                house.owner = owner;
                                item.house = house;
                                items.add(item);


                                itemList.items = items;


                            } catch (JSONException e) {
                                Logger.e(Constants.TAG, "", e);
                            }
                        }

                        PageInfo pageInfo = new PageInfo();
                        if (pageJson != null) {
                            pageInfo.pageNo = pageJson.optInt("pageNo");
                            pageInfo.pageSize = pageJson.optInt("pageSize");
                            pageInfo.pageTotalNum = pageJson.optInt("pageTotalNum");
                        }
                        itemList.pageInfo = pageInfo;
                        postEvent(ResponseEvent.TYPE_SERVER_ORDER_LIST_BY_TYPE, itemList, Server.Code.SUCCESS, "");
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_ORDER_LIST_BY_TYPE, null, errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_ORDER_LIST_BY_TYPE, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void getOrderInfo(Context context, HashMap<String, String> map) {
        HashMap<String, String> params = resolveJsonToPost(context, map);
        volleyManager.post(context, formatUrl("/housekeep/getOrderInfo"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {
                    @Override
                    public void onSuccess(JSONObject json) {
                        if (json != null) {
                            OrderBean.Detail detail = new OrderBean.Detail();
                            ArrayList<OrderBean.State> states = new ArrayList<OrderBean.State>();
                            JSONArray stateArray = json.optJSONArray("orderAlterInfo");
                            if (stateArray != null) {
                                int size = stateArray.length();
                                for (int i = 0; i < size; i++) {
                                    try {
                                        JSONObject stateJson = stateArray.getJSONObject(i);
                                        if (stateJson != null) {
                                            OrderBean.State state = new OrderBean.State();
                                            state.id = stateJson.optInt("orderStatusId");
                                            state.name = stateJson.optString("statusName");
                                            state.stage = stateJson.optInt("stage");
                                            state.orderId = String.valueOf(stateJson.optLong("orderId"));
                                            states.add(state);
                                        }
                                    } catch (JSONException e) {
                                        Logger.e(Constants.TAG, "", e);
                                    }
                                }
                                detail.states = states;
                            }
                            OrderBean.OrderBespeak orderBespeak = new OrderBean.OrderBespeak();
                            JSONObject orderBespeakJsonObject = json.optJSONObject("orderBespeak");
                            if (orderBespeakJsonObject != null) {
                                orderBespeak.measureTime = orderBespeakJsonObject.optLong("measureTime");
                                orderBespeak.planTime = orderBespeakJsonObject.optLong("planTime");
                            }
                            detail.orderBespeak = orderBespeak;
                            JSONArray orderOperationRecord = json.optJSONArray("orderOperationRecord");
                            if (orderOperationRecord != null) {
                                //TODO 订单备注
                            }

                            OrderBean.Item item = new OrderBean.Item();

                            JSONObject orderObj = json.optJSONObject("order");
                            if (orderObj != null) {
                                item.orderId = String.valueOf(orderObj.optLong("orderId"));
                                OrderBean.State state = new OrderBean.State();
                                state.id = orderObj.optInt("orderStatusId");
                                switch (state.id) {
                                    case 170: {
                                        state.name = OrderBean.State.STATE_170;
                                        break;
                                    }
                                    case 180: {
                                        state.name = OrderBean.State.STATE_180;
                                        break;
                                    }
                                    case 200: {
                                        state.name = OrderBean.State.STATE_200;
                                        break;
                                    }
                                    case 210: {
                                        state.name = OrderBean.State.STATE_210;
                                        break;
                                    }
                                    case 220: {
                                        state.name = OrderBean.State.STATE_220;
                                        break;
                                    }
                                    case 230: {
                                        state.name = OrderBean.State.STATE_230;
                                        break;
                                    }
                                    case 240: {
                                        state.name = OrderBean.State.STATE_240;
                                        break;
                                    }
                                    case 250: {
                                        state.name = OrderBean.State.STATE_250;
                                        break;
                                    }
                                    case 260: {
                                        state.name = OrderBean.State.STATE_260;
                                        break;
                                    }
                                }

                                item.state = state;
                            }

                            House house = new House();
                            House.Address address = new House.Address();
                            House.Source source = new House.Source();
                            House.Layout layout = new House.Layout();
                            House.Category category = new House.Category();
                            House.ChildSource childSource = new House.ChildSource();
                            JSONObject channelJson = json.optJSONObject("channel");
                            if (channelJson != null) {
                                category.name = TextUtils.isEmpty(channelJson.optString("source")) ? "暂无" : channelJson.optString("source");
                                childSource.parentName = TextUtils.isEmpty(channelJson.optString("channelFirst")) ? "暂无" : channelJson.optString("channelFirst");
                                childSource.sourceName = TextUtils.isEmpty(channelJson.optString("channelSecond")) ? "暂无" : channelJson.optString("channelSecond");
                                house.childSource = childSource;
                                house.category = category;
                            }

                            JSONObject houseJson = json.optJSONObject("orderHouse");
                            if (houseJson != null) {
                                address.specialAddress = TextUtils.isEmpty(houseJson.optString("address")) ? "待定" : houseJson.optString("address");
                                address.title = TextUtils.isEmpty(houseJson.optString("community")) ? "待定" : houseJson.optString("community");

                                house.area = TextUtils.isEmpty(houseJson.optString("area")) ? "待定" : houseJson.optString("area") + "㎡";

                                if (houseJson.has("measureTime")) {
                                    house.measuerDate = houseJson.optLong("measureTime");
                                } else {
                                    house.measuerDate = Constants.INVALID;
                                }

//                                source.name = TextUtils.isEmpty(houseJson.optString("source")) ? "待定" : houseJson.optString("source");


                                layout.name = TextUtils.isEmpty(houseJson.optString("layout")) ? "待定" : houseJson.optString("layout");
                                house.housing = TextUtils.isEmpty(houseJson.optString("deliveryHouse")) ? "待定" : houseJson.optString("deliveryHouse");
                                if (!TextUtils.isEmpty(houseJson.optString("newHouse"))) {
                                    house.type = Integer.valueOf(houseJson.optString("newHouse"));
                                } else {
                                    house.type = Constants.INVALID;
                                }
                                house.doorplate = TextUtils.isEmpty(houseJson.optString("doorplate")) ? "待定" : houseJson.optString("doorplate");
                                house.ownerNeed = TextUtils.isEmpty(houseJson.optString("houseComment")) ? "暂无" : houseJson.optString("houseComment");
                            } else {
                                address.specialAddress = "待定";
                                address.title = "待定";
                                house.area = "待定";
                                house.measuerDate = Constants.INVALID;
                                source.name = "待定";
                                layout.name = "待定";
                                house.type = 1;
                                house.doorplate = "待定";
                                house.housing = "待定";

                            }

                            house.address = address;
                            house.layout = layout;
                            house.source = source;

                            JSONObject userJson = json.optJSONObject("userDetail");
                            House.Owner owner = new House.Owner();
                            owner.name = userJson.optString("realName");
                            owner.phoneNum = userJson.optString("mobile");
                            house.owner = owner;
                            item.house = house;

                            JSONObject designerJson = json.optJSONObject("designer");
                            if (designerJson != null) {
                                Role role = new Role();
                                role.name = designerJson.optString("vendorName");
                                role.id = String.valueOf(designerJson.optLong("vendorId"));
                                detail.designer = role;
                            }
                            detail.item = item;

                            // 合同内容
                            JSONObject contractJson = json.optJSONObject("orderContract");
                            if (contractJson != null) {
                                detail.contractPrice = contractJson.optLong("contractPrice");
                                detail.firstPrice = contractJson.optLong("firstPrice");
                                detail.endPrice = contractJson.optLong("endPrice");
                                String planDesc = contractJson.optString("message");
                                //获取装修风格
                                detail.styleName = contractJson.optString("style");
                                if (!TextUtils.isEmpty(planDesc)) {
                                    detail.planDesc = planDesc;
                                } else {
                                    detail.planDesc = "";
                                }
                            }


                            detail.bespeakPrice = json.optLong("bespeakPrice");
                            if (json.has("bespeakPriceStatus")) {
                                int status = json.optInt("bespeakPriceStatus");
                                if (status == 1) {
                                    detail.bespeakEnable = status == 1 ? true : false;
                                }
                            }
                            JSONArray roleArray = json.optJSONArray("allotList");
                            if (roleArray != null) {
                                HashMap<String, Role> roleMap = new HashMap<String, Role>();
                                int size = roleArray.length();
                                for (int i = 0; i < size; i++) {
                                    JSONObject jsonObject = roleArray.optJSONObject(i);
                                    if (jsonObject != null) {
                                        Role role = new Role();
                                        role.id = String.valueOf(jsonObject.optLong("vendorId"));
                                        role.name = jsonObject.optString("vendorName");
                                        role.typeId = String.valueOf(jsonObject.optLong("vendorRole"));
                                        roleMap.put(role.typeId, role);
                                    }
                                }
                                detail.roleHashMap = roleMap;
                            }

                            JSONArray remarkArray = json.optJSONArray("orderOperationRecord");
                            if (remarkArray != null) {
                                ArrayList<OrderBean.Remark> remarks = new ArrayList<OrderBean.Remark>();
                                int size = remarkArray.length();
                                for (int i = 0; i < size; i++) {
                                    JSONObject jsonObject = remarkArray.optJSONObject(i);
                                    if (jsonObject != null) {
                                        OrderBean.Remark remark = new OrderBean.Remark();
                                        remark.id = String.valueOf(jsonObject.optLong("recordId"));
                                        remark.orderId = jsonObject.optString("orderId");
                                        remark.publishDate = jsonObject.optLong("createTime");
                                        remark.content = jsonObject.optString("message");

                                        Role role = new Role();
                                        role.id = jsonObject.optString("userId");
                                        role.typeId = String.valueOf(jsonObject.optInt("userType"));
                                        remark.fromUser = role;
                                        remarks.add(remark);
                                    }
                                }
                                detail.remarks = remarks;
                            }


                            // 方案图
                            JSONArray planArray = json.optJSONArray("designUrls");
                            if (planArray != null) {
                                ArrayList<String> planImages = new ArrayList<String>();
                                int size = planArray.length();
                                for (int i = 0; i < size; i++) {
                                    String imgUrl = planArray.optString(i);
                                    if (!TextUtils.isEmpty(imgUrl)) {
                                        planImages.add(imgUrl);
                                    }
                                }
                                detail.planImages = planImages;
                                detail.contractImages = planImages;
                            }

                            // 订单支付列表
                            JSONArray receivedMoneyArray = json.optJSONArray("orderDetailList");
                            if (receivedMoneyArray != null) {
                                ArrayList<OrderBean.ReceivedMoney> receivedMoneyLists = new ArrayList<OrderBean.ReceivedMoney>();
                                int size = receivedMoneyArray.length();
                                for (int i = 0; i < size; i++) {
                                    JSONObject receivedMoneyJson = receivedMoneyArray.optJSONObject(i);
                                    OrderBean.ReceivedMoney receivedMoney = new OrderBean.ReceivedMoney();
                                    receivedMoney.moneyType = receivedMoneyJson.optString("payChannel");
                                    receivedMoney.orderType = receivedMoneyJson.optInt("orderType");
                                    switch (receivedMoney.orderType) {
                                        case 2: {
                                            receivedMoney.orderTypeStr = "预约款";
                                            break;
                                        }
                                        case 3: {
                                            receivedMoney.orderTypeStr = "首款";
                                            break;
                                        }
                                        case 6: {
                                            receivedMoney.orderTypeStr = "尾款";
                                            break;
                                        }
                                        case 7: {
                                            receivedMoney.orderTypeStr = "订金";
                                            break;
                                        }
                                        case 8: {
                                            receivedMoney.orderTypeStr = "中期款";
                                            break;
                                        }
                                        case 9: {
                                            receivedMoney.orderTypeStr = "增项款";
                                            break;
                                        }

                                    }
                                    receivedMoney.receivedMoney = receivedMoneyJson.optLong("purchasePrice");
                                    receivedMoney.publishTime = receivedMoneyJson.optLong("updateTime");
                                    receivedMoneyLists.add(receivedMoney);
                                }
                                detail.receivedMoneyLists = receivedMoneyLists;
                            }

                            postEvent(ResponseEvent.TYPE_SERVER_ORDER_DETAIL, detail, Server.Code.SUCCESS, "");
                        }
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_ORDER_DETAIL, null, errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_ORDER_DETAIL, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void editOrderStatus(Context context, final HashMap<String, String> map) {
        HashMap<String, String> params = resolveJsonToPost(context, map);
        volleyManager.post(context, formatUrl("/housekeep/editOrderStatus"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {

                    @Override
                    public void onSuccess() {
                        super.onSuccess();
                        postEvent(ResponseEvent.TYPE_SERVER_PAY_ORDER_STATE_CHANGE, map.get(Server.Param.ORDER_ID), Server.Code.SUCCESS, "");
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_PAY_ORDER_STATE_CHANGE, null, errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_PAY_ORDER_STATE_CHANGE, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void addOrderContract(Context context, final HashMap<String, String> map) {
        HashMap<String, String> params = resolveJsonToPost(context, map);
        volleyManager.post(context, formatUrl("/housekeep/addOrderContract"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {
                    @Override
                    public void onSuccess() {
                        super.onSuccess();
                        postEvent(ResponseEvent.TYPE_SERVER_PAY_ORDER_ADD_CONTRACT, map.get("orderId"), Server.Code.SUCCESS, "");
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_PAY_ORDER_ADD_CONTRACT, null, errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_PAY_ORDER_ADD_CONTRACT, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void getOrderContract(Context context, HashMap<String, String> map) {
        HashMap<String, String> params = resolveJsonToPost(context, map);
        volleyManager.post(context, formatUrl("housekeep/getOrderContract"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {
                    @Override
                    public void onSuccess(JSONObject json) {

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

    @Override
    public void addOrderScheme(Context context, final HashMap<String, String> map) {
        HashMap<String, String> params = resolveJsonToPost(context, map);
        volleyManager.post(context, formatUrl("/housekeep/addOrderScheme"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {
                    @Override
                    public void onSuccess() {
                        super.onSuccess();
                        postEvent(ResponseEvent.TYPE_SERVER_PAY_ORDER_ADD_SCHEME, map.get("orderId"), Server.Code.SUCCESS, "");
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_PAY_ORDER_ADD_SCHEME, null, errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_PAY_ORDER_ADD_SCHEME, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void addOrder(Context context, HashMap<String, String> map) {
        HashMap<String, String> params = resolveJsonToPost(context, map);
        volleyManager.post(context, formatUrl("/housekeep/addOrder"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {

                    @Override
                    public void onSuccessString(String result) {
                        super.onSuccessString(result);
                        if (!TextUtils.isEmpty(result)) {
                            postEvent(ResponseEvent.TYPE_SERVER_ORDER_ADD, result, Server.Code.SUCCESS, "");
                        }
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_ORDER_ADD, null, errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_ORDER_ADD, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void saveOrderHouse(Context context, HashMap<String, String> map) {
        String token = getToken(context);
        if (!TextUtils.isEmpty(token)) {
            HashMap<String, String> params = resolveJsonToPost(context, map);
            params.put(Params.PARAM_TOKEN, token);
            volleyManager.post(context, formatUrl("/housekeep/saveOrderHouse"), params, new VolleyManager.ServerResponse<String>() {
                @Override
                public void onSuccess(String result) {
                    resolveJson(result, new OnRepSuccessListener() {
                        @Override
                        public void onSuccess() {
                            super.onSuccess();
                            postEvent(ResponseEvent.TYPE_SERVER_ORDER_ADD_HOUSE, null, Server.Code.SUCCESS, "");
                        }

                        @Override
                        public void onFail(int errorCode, String errorMsg) {
                            postEvent(ResponseEvent.TYPE_SERVER_ORDER_ADD_HOUSE, null, errorCode, errorMsg);
                        }
                    });

                }

                @Override
                public void onError(int errorCode, String errorMsg) {
                    postEvent(ResponseEvent.TYPE_SERVER_ORDER_ADD_HOUSE, null, errorCode, errorMsg);
                }
            });

        }
    }

    @Override
    public void listVendor(Context context, HashMap<String, String> map) {
        HashMap<String, String> params = resolveJsonToPost(context, map);
        volleyManager.post(context, formatUrl("/housekeep/listVendor"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {
                    @Override
                    public void onJsonArraySuccess(JSONArray jsonArray) {
                        super.onJsonArraySuccess(jsonArray);

                        if (jsonArray != null) {
                            int size = jsonArray.length();
                            ArrayList<Role> roles = new ArrayList<Role>();
                            for (int i = 0; i < size; i++) {
                                try {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Role role = new Role();
                                    role.id = String.valueOf(jsonObject.optInt("vendorId"));
                                    role.icon = jsonObject.optString("avatar");
                                    role.name = jsonObject.optString("vendorName");
                                    roles.add(role);
                                } catch (JSONException e) {
                                    Logger.e(Constants.TAG, "", e);
                                }
                            }
                            postEvent(ResponseEvent.TYPE_SERVER_SITE_LIST_DESIGNER, roles, Server.Code.SUCCESS, "");
                        }
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_SITE_LIST_DESIGNER, null, errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_SITE_LIST_DESIGNER, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void editOrderUnder(Context context, HashMap<String, String> map) {
        HashMap<String, String> params = resolveJsonToPost(context, map);
        volleyManager.post(context, formatUrl("/housekeep/editOrderUnder"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {
                    @Override
                    public void onSuccess() {
                        super.onSuccess();
                        postEvent(ResponseEvent.TYPE_SERVER_SITE_ADD_DESIGNER, null, Server.Code.SUCCESS, "");
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_SITE_ADD_DESIGNER, null, errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_SITE_ADD_DESIGNER, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void editUserDetail(Context context, HashMap<String, String> map) {
        HashMap<String, String> params = resolveJsonToPost(context, map);
        volleyManager.post(context, formatUrl("/housekeep/editUserDetail"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {
                    @Override
                    public void onSuccess() {
                        super.onSuccess();
                        postEvent(ResponseEvent.TYPE_SERVER_ORDER_MODIFY_OWNER_INFO, null, Server.Code.SUCCESS, "");
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_ORDER_MODIFY_OWNER_INFO, null, errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_ORDER_MODIFY_OWNER_INFO, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void addRecord(Context context, HashMap<String, String> map) {
        HashMap<String, String> params = resolveJsonToPost(context, map);
        volleyManager.post(context, formatUrl("/housekeep/addRecord"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {
                    @Override
                    public void onSuccess() {
                        super.onSuccess();
                        postEvent(ResponseEvent.TYPE_SERVER_ORDER_ADD_REMARK, null, Server.Code.SUCCESS, "");
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_ORDER_ADD_REMARK, null, errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_ORDER_ADD_REMARK, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void listVendorBySite(Context context, HashMap<String, String> map) {
        HashMap<String, String> params = resolveJsonToPost(context, map);
        volleyManager.post(context, formatUrl("/housekeep/listVendorBySite"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {

                    @Override
                    public void onJsonArraySuccess(JSONArray jsonArray) {
                        super.onJsonArraySuccess(jsonArray);
                        if (jsonArray != null) {
                            int size = jsonArray.length();
                            ArrayList<Role> roles = new ArrayList<Role>();
                            for (int i = 0; i < size; i++) {
                                try {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Role role = new Role();
                                    role.id = String.valueOf(jsonObject.optInt("vendorId"));
                                    role.icon = jsonObject.optString("avatar");
                                    role.name = jsonObject.optString("vendorName");
                                    roles.add(role);
                                } catch (JSONException e) {
                                    Logger.e(Constants.TAG, "", e);
                                }
                            }
                            postEvent(ResponseEvent.TYPE_SERVER_FOLLOW_AT_FRIENDS, roles, Server.Code.SUCCESS, "");
                        }
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_FOLLOW_AT_FRIENDS, null, errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_FOLLOW_AT_FRIENDS, null, errorCode, errorMsg);
            }
        });
    }

    //获取套餐类别

    @Override
    public void listPackageCategory(Context context, HashMap<String, String> map) {

        HashMap<String, String> params = resolveJsonToPost(context, map);
        volleyManager.post(context, formatUrl("/housekeep/listPackageSource"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {

                    @Override
                    public void onJsonArraySuccess(JSONArray jsonArray) {
                        super.onJsonArraySuccess(jsonArray);
                        if (jsonArray != null) {
                            int size = jsonArray.length();
                            ArrayList<House.Category> categories = new ArrayList<>();
                            for (int i = 0; i < size; i++) {
                                try {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    House.Category category = new House.Category();
                                    category.id = jsonObject.optInt("packageSourceId");
                                    category.name = jsonObject.optString("sourceName");
                                    categories.add(category);
                                } catch (JSONException e) {
                                    Logger.e(Constants.TAG, "", e);
                                }
                            }
                            postEvent(ResponseEvent.TYPE_SERVER_LIST_PACKAGE_CATEGORY, categories, Server.Code.SUCCESS, "");
                        }
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_LIST_PACKAGE_CATEGORY, null, errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_LIST_PACKAGE_CATEGORY, null, errorCode, errorMsg);
            }
        });

    }

    //渠道来源

    @Override
    public void getOrderSource(Context context, HashMap<String, String> map) {

        HashMap<String, String> params = resolveJsonToPost(context, map);
        volleyManager.post(context, formatUrl("/housekeep/listChannelSource"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {
                    @Override
                    public void onSuccess(JSONObject json) {
                        super.onSuccess(json);
                        if (json != null) {
                            JSONArray jsonArray = json.optJSONArray("children");
                            if (jsonArray != null) {
                                int length = jsonArray.length();
                                ArrayList<House.Source> mParentSourceList = new ArrayList<House.Source>();
                                for (int i = 0; i < length; i++) {
                                    House.Source source = new House.Source();
                                    JSONObject parentObject = jsonArray.optJSONObject(i);
                                    source.channelSourceId = parentObject.optInt("channelSourceId");
                                    source.parentId = parentObject.optInt("parentId");
                                    source.name = parentObject.optString("sourceName");
                                    source.sources = new ArrayList<House.ChildSource>();
                                    JSONArray mChlidJsonArray = parentObject.optJSONArray("children");

                                    if (mChlidJsonArray != null) {
                                        int size = mChlidJsonArray.length();
                                        for (int j = 0; j < size; j++) {
                                            House.ChildSource childSource = new House.ChildSource();
                                            JSONObject childJsonObJect = mChlidJsonArray.optJSONObject(j);
                                            childSource.channelSourceId = childJsonObJect.optInt("channelSourceId");
                                            childSource.parentId = childJsonObJect.optInt("parentId");
                                            childSource.sourceName = childJsonObJect.optString("sourceName");
                                            childSource.parentName = source.name;
                                            source.sources.add(childSource);
                                        }
                                    }
                                    mParentSourceList.add(source);

                                }
                                postEvent(ResponseEvent.TYPE_SERVER_GET_ORDER_SOURCE, mParentSourceList, Server.Code.SUCCESS, "");

                            }
                        }


                    }


                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_GET_ORDER_SOURCE, null, errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_GET_ORDER_SOURCE, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void getDecStyleTabData(Context context, HashMap<String, String> map) {
        HashMap<String, String> params = resolveJsonToPost(context, map);
        volleyManager.post(context, formatUrl("/housekeep/listDecorateStyle"), params, new VolleyManager.ServerResponse<String>() {
            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {

                    @Override
                    public void onJsonArraySuccess(JSONArray jsonArray) {
                        super.onJsonArraySuccess(jsonArray);
                        if (jsonArray != null) {
                            int size = jsonArray.length();
                            ArrayList<House.Style> categories = new ArrayList<>();
                            for (int i = 0; i < size; i++) {
                                try {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    House.Style style = new House.Style();
                                    style.tagId = jsonObject.optInt("styleId");
                                    style.tagName = jsonObject.optString("styleName");
                                    categories.add(style);
                                } catch (JSONException e) {
                                    Logger.e(Constants.TAG, "", e);
                                }
                            }
                            postEvent(ResponseEvent.TYPE_SERVER_GET_DEC_STYLE, categories, Server.Code.SUCCESS, "");
                        }
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_GET_DEC_STYLE, null, errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_GET_DEC_STYLE, null, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void creatOrder(Context context, HashMap<String, String> map) {

        HashMap<String, String> params = resolveJsonToPost(context, map);
        volleyManager.post(context, formatUrl("/housekeep/createOrder"), params, new VolleyManager.ServerResponse<String>() {

            @Override
            public void onSuccess(String result) {
                resolveJson(result, new OnRepSuccessListener() {

                    @Override
                    public void onSuccess() {
                        super.onSuccess();
                    }

                    @Override
                    public void onSuccessString(String result) {
                        super.onSuccessString(result);
                        if (!TextUtils.isEmpty(result)) {
                            postEvent(ResponseEvent.TYPE_SERVER_CREATE_ORDER, result, Server.Code.SUCCESS, "");
                        }
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg) {
                        postEvent(ResponseEvent.TYPE_SERVER_CREATE_ORDER, null, errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                postEvent(ResponseEvent.TYPE_SERVER_CREATE_ORDER, null, errorCode, errorMsg);
            }
        });


    }

    @Override
    public void editOrderHouse(Context context, HashMap<String, String> map) {
        String token = getToken(context);
        if (!TextUtils.isEmpty(token)) {
            HashMap<String, String> params = resolveJsonToPost(context, map);
            params.put(Params.PARAM_TOKEN, token);
            volleyManager.post(context, formatUrl("/housekeep/editOrderHouse"), params, new VolleyManager.ServerResponse<String>() {
                @Override
                public void onSuccess(String result) {
                    resolveJson(result, new OnRepSuccessListener() {
                        @Override
                        public void onSuccess() {
                            super.onSuccess();
                            postEvent(ResponseEvent.TYPE_SERVER_EDITE_HOUSE_INFO, null, Server.Code.SUCCESS, "");
                        }

                        @Override
                        public void onFail(int errorCode, String errorMsg) {
                            postEvent(ResponseEvent.TYPE_SERVER_EDITE_HOUSE_INFO, null, errorCode, errorMsg);
                        }
                    });
                }

                @Override
                public void onError(int errorCode, String errorMsg) {
                    postEvent(ResponseEvent.TYPE_SERVER_EDITE_HOUSE_INFO, null, errorCode, errorMsg);
                }
            });

        }
    }

    @Override
    public void editBuildingOrderChannel(Context context, HashMap<String, String> map) {
        String token = getToken(context);
        if (!TextUtils.isEmpty(token)) {
            HashMap<String, String> params = resolveJsonToPost(context, map);
            params.put(Params.PARAM_TOKEN, token);
            volleyManager.post(context, formatUrl("/housekeep/editBuildingOrderChannel"), params, new VolleyManager.ServerResponse<String>() {
                @Override
                public void onSuccess(String result) {
                    resolveJson(result, new OnRepSuccessListener() {
                        @Override
                        public void onSuccess() {
                            super.onSuccess();
                            postEvent(ResponseEvent.TYPE_SERVER_EDIT_CHANNEL_INFO, null, Server.Code.SUCCESS, "");
                        }

                        @Override
                        public void onFail(int errorCode, String errorMsg) {
                            postEvent(ResponseEvent.TYPE_SERVER_EDIT_CHANNEL_INFO, null, errorCode, errorMsg);
                        }
                    });
                }

                @Override
                public void onError(int errorCode, String errorMsg) {
                    postEvent(ResponseEvent.TYPE_SERVER_EDIT_CHANNEL_INFO, null, errorCode, errorMsg);
                }
            });

        }
    }

    @Override
    public void editBuildingOrderDate(Context context, HashMap<String, String> map) {
        String token = getToken(context);
        if (!TextUtils.isEmpty(token)) {
            HashMap<String, String> params = resolveJsonToPost(context, map);
            params.put(Params.PARAM_TOKEN, token);
            volleyManager.post(context, formatUrl("/housekeep/editBuildingOrderDate"), params, new VolleyManager.ServerResponse<String>() {
                @Override
                public void onSuccess(String result) {
                    resolveJson(result, new OnRepSuccessListener() {
                        @Override
                        public void onSuccess() {
                            super.onSuccess();
                            postEvent(ResponseEvent.TYPE_SERVER_EDIT_BUILDING_ORDER_DATA, null, Server.Code.SUCCESS, "");
                        }

                        @Override
                        public void onFail(int errorCode, String errorMsg) {
                            postEvent(ResponseEvent.TYPE_SERVER_EDIT_BUILDING_ORDER_DATA, null, errorCode, errorMsg);
                        }
                    });
                }

                @Override
                public void onError(int errorCode, String errorMsg) {
                    postEvent(ResponseEvent.TYPE_SERVER_EDIT_BUILDING_ORDER_DATA, null, errorCode, errorMsg);
                }
            });

        }
    }


}
