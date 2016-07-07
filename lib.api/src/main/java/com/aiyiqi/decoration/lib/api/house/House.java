package com.aiyiqi.decoration.lib.api.house;

import java.util.ArrayList;

/**
 * Created by hubing on 16/3/22.
 */
public class House {
    public Address address;
    public String doorplate; // 门牌号
    public String area;
    public int type; // 新房，旧房
    public String  housing;// 是否期房，是，否
    public Layout layout;// 户型
    public String  ownerNeed;//业主需求
    public long measuerDate;
    public Source source;
    public Owner owner;
    public Category category;
    public Style style;
    public ChildSource childSource;


    public final static class Address {
        public String community;
        public String title;
        public String specialAddress;
        public double longitude;
        public double latitude;
    }

    public final static class Layout {//户型
        public int id;
        public String name;
    }

    //渠道来源
    public final static class Source {
        public int channelSourceId;
        public int parentId;
        public String name;
        public ArrayList<ChildSource> sources;
    }

    public final static class ChildSource {
        public int channelSourceId;
        public int parentId;
        public String sourceName;
        public String  parentName;
    }

    public final static class Category {
        public int id;
        public String name;
    }

    public final static class Owner {
        public String id;
        public String name;
        public String phoneNum;
        public String orderNeed;
    }

    public final static class Style {
        public String tagName;
        public int tagId;
        public boolean isSelected;
    }
}


