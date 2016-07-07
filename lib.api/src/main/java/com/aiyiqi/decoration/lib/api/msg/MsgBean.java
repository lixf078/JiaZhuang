package com.aiyiqi.decoration.lib.api.msg;

import com.aiyiqi.decoration.lib.api.Role;
import com.aiyiqi.decoration.lib.api.site.SiteBean;

import java.util.ArrayList;

/**
 * Created by hubing on 16/3/24.
 */
public class MsgBean {

    public static final class AtMsg{
        public String trackId;
        public String ownerName;
        public String roleName;
        public long publistTime;
    }
    public final  static  class  SiteInforMsg {
        public Role publisher;//发布者
        public ArrayList<Role> atFriends;
        public String content;
        public long publishTime;
        public String trackId;
        public String buildingId;
        public ArrayList<String> trackImgs;
        public SiteBean.State state; //提醒的工地状态
    }
    public final static  class  CommentMsg{
        public String trackId;
        public String ownerName;
        public String roleName;
        public long publistTime;
    }
    public  final  static  class OrderMsg{
        public  String  trackId;
        public String ownerName;
        public String roleName;
        public long publistTime;
        public OrderState mOderState;

    }

    public final static class OrderState{ //订单状态
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
        public static final String STATE_220 = "出方案";
        public static final String STATE_230 = "定合同款";
        public static final String STATE_240 = "首款";
        public static final String STATE_250 = "开工交底";
        public static final String STATE_260 = "尾款";
    }

}
