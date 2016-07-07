package com.aiyiqi.decoration.lib.api.pay;

/**
 * Created by hubing on 16/3/18.
 */
public class Pay {

    public String checkoutId;
    public String result;

    public static final class Channel{
        public static final String WX = "weixin";
        public static final String ALIPAY = "alipay";
        public static final String POS = "pos";
        public static final String YIQIQIANBAO = "yiqiqianbao";
        public static final String ONLINE = "online";
        public static final String CASH = "cash";//现金支付
    }
    public static final class Type{
        public static final String JSAPI = "JSAPI";
        public static final String NATIVE = "NATIVE";
        public static final String APP = "APP";
        public static final String MICROPAY = "MICROPAY";
    }

    public static final class Source{
        public static final String ANDROID = "android";
        public static final String POS = "pos";
    }

    public static final class Checkout{
        public String result;
        public boolean isPayOffLine;
        public String payChannel;
    }


}
