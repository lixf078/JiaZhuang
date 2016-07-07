package com.aiyiqi.decoration.lib.api.user;

/**
 * Created by 17house-llj on 16/5/11.
 */
public class UserBean {
    public static final class UserInfo {
        public int vendorId;
        public String vendorName;
        public String realName;
        public String nickName;
        public String avatar;
        public String lifePhoto;
        public String signature;
        public String updateTime;
        public int provinceId;
        public String province;
        public int workYear;
        public int siteNum;
        public int sendMsgNum;
        public int applyFromShareNum;
        public String goodat; // 擅长风格
    }

    public static final class Province {
        public int provinceId;
        public String adCode;
        public String cityName;
        public String cityNamePinyin;
        public double longitude;
        public double latitude;

    }


}
