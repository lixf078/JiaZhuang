package com.aiyiqi.decoration.lib.api.event;

import com.aiyiqi.lib.base.BaseEvent;

/**
 * Created by hubing on 16/3/23.
 */
public class ServerEvent extends BaseEvent {
    public static final int TYPE_SERVER_USER_LOGIN = 1000;//登录
    public static final int TYPE_SERVER_USER_LOGOUT = TYPE_SERVER_USER_LOGIN + 1;//登出
    public static final int TYPE_SERVER_ORDER_LIST_BY_TYPE = TYPE_SERVER_USER_LOGOUT + 1;//
    public static final int TYPE_SERVER_ORDER_TYPE_LIST = TYPE_SERVER_ORDER_LIST_BY_TYPE + 1;// 根据 type 获取订单列表
    public static final int TYPE_SERVER_ORDER_DETAIL = TYPE_SERVER_ORDER_TYPE_LIST + 1;// 根据 订单详情
    public static final int TYPE_SERVER_ORDER_ADD = TYPE_SERVER_ORDER_DETAIL + 1;//下订单
    public static final int TYPE_SERVER_ORDER_MODIFY_OWNER_INFO = TYPE_SERVER_ORDER_ADD + 1;//修改业主信息
    public static final int TYPE_SERVER_ORDER_CHANGE_STATE = TYPE_SERVER_ORDER_MODIFY_OWNER_INFO + 1;//增加跟进消息
    public static final int TYPE_SERVER_ORDER_ADD_REMARK = TYPE_SERVER_ORDER_CHANGE_STATE + 1;//增加备注

    public static final int TYPE_SERVER_SITE_ADD = TYPE_SERVER_ORDER_ADD_REMARK + 1;//创建工地
    public static final int TYPE_SERVER_SITE_LIST = TYPE_SERVER_SITE_ADD + 1;//工地列表
    public static final int TYPE_SERVER_SITE_DETAIL = TYPE_SERVER_SITE_LIST + 1;//工地基本信息
    public static final int TYPE_SERVER_SITE_TRACK_LIST = TYPE_SERVER_SITE_DETAIL + 1;//跟进列表

    public static final int TYPE_SERVER_SITE_MODIFY_TRACK_ROLE = TYPE_SERVER_SITE_TRACK_LIST + 1;//修改跟进人

    public static final int TYPE_SERVER_MY_MSG = TYPE_SERVER_SITE_MODIFY_TRACK_ROLE + 1;//我的消息列表
    public static final int TYPE_SERVER_MY_MSG_DETAIL = TYPE_SERVER_MY_MSG + 1;//at消息详情
    public static final int TYPE_SERVER_MY_MSG_NUMBER = TYPE_SERVER_MY_MSG_DETAIL + 1;//消息数
    public static final int TYPE_SERVER_MY_MODIYF_PD = TYPE_SERVER_MY_MSG_NUMBER + 1; // 修改 密码

    public static final int TYPE_SERVER_SITE_ADD_TRACK = TYPE_SERVER_MY_MODIYF_PD + 1;//增加跟进消息
    public static final int TYPE_SERVER_SITE_ADD_TRACK_REPLY = TYPE_SERVER_SITE_ADD_TRACK + 1;//回复跟进消息
    public static final int TYPE_SERVER_SITE_TRACK_READED = TYPE_SERVER_SITE_ADD_TRACK_REPLY + 1; // 标示跟进消息已读
    public static final int TYPE_SERVER_SITE_TRACK_DELETEED = TYPE_SERVER_SITE_TRACK_READED + 1; // 删除跟进
    public static final int TYPE_SERVER_FOLLOW_AT_FRIENDS = TYPE_SERVER_SITE_TRACK_DELETEED + 1;// 发跟进时，at人员列表
    public static final int TYPE_SERVER_FOLLOW_AT_ALL_FRIENDS = TYPE_SERVER_FOLLOW_AT_FRIENDS + 1;// 发跟进时，at人员列表

    public static final int TYPE_SERVER_PAY_GET_CHECKOUT_ID = TYPE_SERVER_FOLLOW_AT_ALL_FRIENDS + 1;// 获取checkoutId
    public static final int TYPE_SERVER_PAY_QR = TYPE_SERVER_PAY_GET_CHECKOUT_ID + 1;// 获取生成二维码的 信息
    public static final int TYPE_SERVER_PAY_QUERY_STATUS = TYPE_SERVER_PAY_QR + 1;// 从后台服务器查询支付状态
    public static final int TYPE_SERVER_PAY_QUERY_FROM_PLATFORM = TYPE_SERVER_PAY_QUERY_STATUS + 1;// 从微信后台查询支付状态


    public static final int TYPE_SERVER_PAY_ORDER_STATE_CHANGE = TYPE_SERVER_PAY_QUERY_FROM_PLATFORM + 1;// 订单状态更改

    public static final int TYPE_SERVER_PAY_ORDER_ADD_SCHEME = TYPE_SERVER_PAY_ORDER_STATE_CHANGE + 1;// 订单方案价格

    public static final int TYPE_SERVER_PAY_ORDER_ADD_CONTRACT = TYPE_SERVER_PAY_ORDER_ADD_SCHEME + 1;// 订单方案价格

    public static final int TYPE_SERVER_PAY_ORDER_PLAN_PRICE = TYPE_SERVER_PAY_ORDER_ADD_CONTRACT + 1;// 订单方案价格

    public static final int TYPE_SERVER_PAY_FINISH = TYPE_SERVER_PAY_ORDER_PLAN_PRICE + 1;// 支付完成，返回给UI支付状态

    public static final int TYPE_SERVER_SITE_ADD_OWNER = TYPE_SERVER_PAY_FINISH + 1;//增加业主信息
    public static final int TYPE_SERVER_SITE_ADD_HOUSE = TYPE_SERVER_SITE_ADD_OWNER + 1;//增加房屋信息

    public static final int TYPE_SERVER_ORDER_ADD_HOUSE = TYPE_SERVER_SITE_ADD_HOUSE + 1;//增加房屋信息

    public static final int TYPE_SERVER_SITE_LIST_DESIGNER = TYPE_SERVER_ORDER_ADD_HOUSE + 1;//设计师列表信息
    public static final int TYPE_SERVER_SITE_ADD_DESIGNER = TYPE_SERVER_SITE_LIST_DESIGNER + 1;//指派设计师信息
    public static final int TYPE_SERVER_SITE_LOAD_AT_LIST = TYPE_SERVER_SITE_ADD_DESIGNER + 1;// 获取AT 好列表

    public static final int TYPE_SERVER_SITE_FETCH_OSSTOKEN = TYPE_SERVER_SITE_LOAD_AT_LIST + 1;// 获取阿里云token
    public static final int TYPE_SERVER_UPLOAD_FILE = TYPE_SERVER_SITE_FETCH_OSSTOKEN + 1;// 上传文件


    public static final int TYPE_SERVER_UPLOAD_MY_INFO = TYPE_SERVER_UPLOAD_FILE + 1;//上传个人信息
    public static final int TYPE_SERVER_GET_MY_INFO = TYPE_SERVER_UPLOAD_MY_INFO + 1;//得到个人信息
    public static final int TYPE_UPDATE_UMENG_INFO = TYPE_SERVER_GET_MY_INFO + 1;//得到个人信息
    public static final int TYPE_SERVER_LIST_PACKAGE_CATEGORY = TYPE_UPDATE_UMENG_INFO + 1;//获取套餐类别
    public static final int TYPE_SERVER_GET_ORDER_SOURCE = TYPE_SERVER_LIST_PACKAGE_CATEGORY + 1;//获取渠道来源
    public static final int TYPE_SERVER_GET_DEC_STYLE = TYPE_SERVER_GET_ORDER_SOURCE + 1;//获取装修风格
    public static final int TYPE_SERVER_GET_MATERIAL_ORDER = TYPE_SERVER_GET_DEC_STYLE + 1;//获取主材下单
    public static final int TYPE_SERVER_GET_SEARCH_BRAND_DATA = TYPE_SERVER_GET_MATERIAL_ORDER + 1; //搜索
    public static final int TYPE_SERVER_CREATE_ORDER = TYPE_SERVER_GET_SEARCH_BRAND_DATA + 1;//创建订单
    public static final int TYPE_SERVER_EDITE_HOUSE_INFO = TYPE_SERVER_CREATE_ORDER + 1;//修改房屋信息
    public static final int TYPE_SERVER_EDIT_CHANNEL_INFO = TYPE_SERVER_EDITE_HOUSE_INFO + 1;//修改渠道信息
    public static final int TYPE_SERVER_EDIT_BUILDING_ORDER_DATA = TYPE_SERVER_EDIT_CHANNEL_INFO + 1;//修改时间
    public static final int TYPE_SERVER_LOAD_SITE_STATE_DATA = TYPE_SERVER_EDIT_BUILDING_ORDER_DATA + 1;//获取工地状态
    public static final int TYPE_SERVER_LIST_PROVINCES = TYPE_SERVER_LOAD_SITE_STATE_DATA + 1;//得到省份列表
    public static final int TYPE_SERVER_LIST_GETBUILDINGSITEVENDO = TYPE_SERVER_LIST_PROVINCES + 1;//得到某个角色的详细信息
    public static final int TYPE_SERVER_UPLOAD_METERRIAL = TYPE_SERVER_LIST_GETBUILDINGSITEVENDO + 1;//上传主材下单信息
    public static final int TYPE_SERVER_UPLOAD_BUILING_SITE_PROGRESS2 = TYPE_SERVER_UPLOAD_METERRIAL + 1;//更新工地进度
    public static final int TYPE_SERVER_SITE_TRACK_LIST_BY_STATE = TYPE_SERVER_UPLOAD_BUILING_SITE_PROGRESS2 + 1;//根据状态获取跟进信息
    public static  final  int  TYPE_SERVER_SITE_UPLOAD_XG_TOKEN=TYPE_SERVER_SITE_TRACK_LIST_BY_STATE+1;//上传信token
}
