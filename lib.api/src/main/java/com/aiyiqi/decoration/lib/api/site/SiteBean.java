package com.aiyiqi.decoration.lib.api.site;

import com.aiyiqi.decoration.lib.api.PageInfo;
import com.aiyiqi.decoration.lib.api.Role;
import com.aiyiqi.decoration.lib.api.house.House;

import java.util.ArrayList;

/**
 * Created by hubing on 16/3/22.
 */
public class SiteBean {

    public static final class Item {
        public String orderId;
        public String siteId;
        public House house;
        public State state;
    }

    public final static class ItemList {
        public ArrayList<Item> items;
        public PageInfo pageInfo;
    }


    public static final class Material {
        public long tagId;
        public String tagName;
        public String materialName;
        public int parentId;
        public String parentName;
        public long updateTime;
        public long createTime;
        public int supportMeterial;
        public String pics;
        public Brand brand;
    }


    public static final class Brand {
        public long storeId;   //      合作店铺ID
        public String brandName;     //   品牌名
        public long brandId;         // 品牌ID
        public long categoryId;    // 类别ID
        public String storeName;

    }

    public final static class Detail { //详情
        public String buildingId;
        public String orderId;
        public House house;
        public ArrayList<Role> roleList;
        public ArrayList<State> states;
    }

    public final static class State { //状态
        public int id;
        public int level;
        public String levelName;
        public String name;
        public long creatTime;
    }

    public final static class  StateItem {//状态列表
        public  int curentProgressId;
        public   ArrayList<State>  mStateList;
    }
    public final static class TrackMsg {
        public Role publisher;//发布者
        public ArrayList<Role> atFriends;
        public String content;
        public long publishTime;
        public String trackId;
        public String buildingId;
        public ArrayList<String> trackImgs;
        public String shareUrl;
        public ArrayList<Comment> trackComments;
        public String avatar; //名称
        public String publicAdress;
    }


    public final static class Comment {
        public String id;
        public String replyId;
        public Role publisher;//发布者
        public String content;
        public long publishTime;
    }
}
