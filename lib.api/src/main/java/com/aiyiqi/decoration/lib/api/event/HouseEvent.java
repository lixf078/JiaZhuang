package com.aiyiqi.decoration.lib.api.event;

import com.aiyiqi.decoration.lib.api.house.House;
import com.aiyiqi.decoration.lib.api.order.OrderBean;
import com.aiyiqi.lib.base.BaseEvent;

/**
 * Created by hubing on 16/3/20.
 */
public class HouseEvent extends BaseEvent {
    public static final int TYPE_HOUSE_ADDRESS = 0;
    public static final int TYPE_HOUSE_LAYOUT = 1;
    public static final int TYPE_HOUSE_SOURCE = 2;
    public static final int TYPE_HOUSE_CATEGORY = 3;
    public static final int TYPE_HOUSE_AREA = 4;
    public static final int TYPE_HOUSE_NUM= 5;

    public House.Address address;
    public House.Layout layout;
    public House.ChildSource source;
    public House.ChildSource childSource;
    public House.Category category;

    public String doorplate; // 门牌号
    public String area;
    public int type; // 新房，旧房
    public int housing;// 是否期房，是，否

}
