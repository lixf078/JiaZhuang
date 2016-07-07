package com.aiyiqi.lib.map.event;

import com.aiyiqi.lib.base.BaseEvent;
import com.aiyiqi.lib.map.bean.AmapBean;

import java.util.ArrayList;

/**
 * Created by hubing on 16/3/18.
 */
public class AmapEvent extends BaseEvent {
    public static final int TYPE_AMAP_SEARCH_LIST = 0;
    public static final int TYPE_AMAP_SELECTED_ITEM = 1;

    public ArrayList<AmapBean> addressBeans;
    public AmapBean selectedItemBean;

}
