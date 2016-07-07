package com.aiyiqi.decoration.lib.api.order;

import com.aiyiqi.decoration.lib.api.PageInfo;
import com.aiyiqi.decoration.lib.api.Role;
import com.aiyiqi.decoration.lib.api.house.House;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hubing on 16/3/17.
 */
public class OrderBean {
    public final static class Sku {//商品信息
        public String id;
        public String name;
    }

    public final static class ItemList {
        public ArrayList<Item> items;
        public PageInfo pageInfo;
    }

    public final static class Item {//订单列表
        public String orderId;
        public Sku sku;
        public House house;
        public State state;
    }

    public final static class Remark { //评论
        public String id;
        public String orderId;
        public Role fromUser;
        public String content;
        public long publishDate;

    }

    public final static class Detail {//详情
        public Item item;
        public Role designer;
        public long perMoney;
        public long contractMoney;
        public ArrayList<Remark> remarks;
        public ArrayList<State> states;
        public long contractPrice;
        public long firstPrice;
        public long endPrice;
        public String styleName;
        public long bespeakPrice;
        public boolean bespeakEnable;
        public HashMap<String, Role> roleHashMap;
        public ArrayList<String> planImages;
        public String planDesc;
        public ArrayList<String> contractImages;
        public ArrayList<ReceivedMoney> receivedMoneyLists;
        public OrderBespeak orderBespeak;
    }

    public final static class OrderBespeak {
        public long orderId;
        public long measureTime;
        public long planTime;
    }

    public final static class ReceivedMoney {
        public String orderId;
        public int orderType; // 订金，首款，中间款等。。。
        public String orderTypeStr;
        public String moneyType; // 微信，支付宝，现金
        public long receivedMoney;
        public long publishTime;
    }

    public final static class State { //订单状态
        public int id;
        public String name;
        public int stage;
        public String orderId;
        public int number;

        // 1 已完成   2 处理中  3 将处理  4 被忽略
        public static final int LEVEL_COMPLETE = 1;
        public static final int LEVEL_DOING = 2;
        public static final int LEVEL_UNSTART = 3;
        public static final int LEVEL_IGNORE = 4;

        public static final String STATE_170 = "已完成";
        public static final String STATE_180 = "已取消";
        public static final String STATE_200 = "前期沟通";
        public static final String STATE_210 = "量房";
        public static final String STATE_220 = "碰方案";
        public static final String STATE_230 = "定合同款";
        public static final String STATE_240 = "首款";
        public static final String STATE_250 = "开工交底";
        public static final String STATE_260 = "尾款";
    }

}
