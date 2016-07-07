package com.aiyiqi.decoration.lib.constants;

/**
 * Created by hubing on 16/3/17.
 */
public class Constants {

    public static final String TAG = "jia";
    public static int INVALID = -1;
    public static int SERVER_RESPONSE_SUCCESS = 0;
    public static final String ADD_IMAGE_PATH_SAMPLE = "add_image_path_sample";
    public static String PICKED_IMAGES = "picked_images";
    public static String PICKED_IMAGE = "picked_image";
    public static int PICK_IMAGE_REQ_CODE = 10001;
    public static String SELECTED_IMAGES = "seldcted_images";

    public static class Path {
        public static final String HOME = "17house";
        public static final String DISK_IMG_CACHE = "cache";
    }

    public static class Key {
        public static final String KEY_CHANNEL = "";
        public static final String KEY_LOGIN_IS_READY = "key_login_is_ready";
        public static final String KEY_UNREAD_COUNT = "key_unread_count";
        public static final String KEY_VENDOR_ID = "vendor_id";
        public static final String KEY_USER_AVATAR = "key_user_avatar";
        public static final String KEY_USER_LIFE_PIC = "key_user_life_pic";
        public static final String KEY_NICK_NAME = "key_nick_name";
        public static final String KEY_SIGNATURE = "key_signature";
        public static final String KEY_BOSS_ID = "key_boss_id";
        public static final String KEY_REAL_NAME = "key_real_name";
        public static final String KEY_IS_UPXG_TOkEN = "key_is_upxxg_token";
    }

    public static class IntentKey {
        public static final String PAY_MONEY = "pay_money";
        public static final String ORDER_ID = "order_id";
        public static final String SOURCE_ID = "sourceId";
        public static final String TARGET_ID = "targetId";
        public static final String CATGORY_ID = "catgory_id";
        public static final String QR_CODE = "qr_code";
        public static final String OWNER_NAME = "owner_name";
        public static final String OWNER_MOBILE = "owner_mobile";
        public static final String PLAN_PRICE = "plan_price";
        public static final String PLAN_DESC = "message";
        public static final String PLAN_IMAGES = "imgs";
        public static final String PRE_PRICE = "prePrice";
        public static final String BUILDING_ID = "buildingId";
        public static final String CHECKOUT_ID = "checkout_id";
        public static final String CHECKOUT_TYPE = "checkout_type";
        public static final String PAY_CHANNEL = "pay_channel";
        public static final String PAY_TYPE = "type";
        public static final String PAY_REMARK = "pay_remark";
        public static final String TRACK_ID = "track_id";
        public static final String UPDATE_TYPE = "updateType";
        public static final String TITLE = "title";
        public static final String ROLE_TYPE = "roleType";
        public static final String MODIFY_ORDER = "modify_order";
        public static final String BESPEAK_ENABLE = "bespeak_enable";
        public static final String HOUSER_ADDRESS = "orderHouseAddress";
        public static final String HOUSER_DOORPLATE = "orderHouseNum";
        public static final String HOUSER_AREA = "orderHouseArea";
        public static final String HOUSER_NEWHOUSE_STRING = "orderHouseOld";
        public static final String HOUSER_LAYOUT = "orderHouseType";
        public static final String HOUSER_MEASURE_TIME = "orderHouseMeasureTime";
        public static final String HOUSER_SOURCE = "orderHouseSource";
        public static final String HOUSER_MEASURE_TIME_VALUE = "orderHouseMeasureTime";
        public static final String REPLY_ID = "reply_id";
        public static final String REPLY_PARENT_ID = "reply_parent_id";
        public static final String INTENT_KEY_FINISH = "intent_key_finish";
        public static final String INTENT_KEY_PRE_ACTIVITY = "intent_key_pre_activity";
        public static final String INTENT_KEY_INPUT_ALREDY = "intent_key_input_already";
        public static final String INTENT_KEY_HEAD_TITLE = "intent_key_head_title";
        public static final String INTENT_KEY_POSITION = "intent_key_position";
        public static final String INTENT_KEY_PICS = "intent_key_pics";
        public static final String INTENT_KEY_TAB = "intent_key_tab";

        public static final String INTENT_KEY_BRAND_ID = "intent_key_tabs_id";
        public static final String INTENT_KEY_NEW_HOUSE = "intent_key_new_house";
        public static final String INTENT_KEY_AREA = "intent_key_area";
        public static final String INTENT_KEY_DOOR_PLATE = "intent_key_door_plate";
        public static final String INTENT_KEY_COMMUNITY = "intent_key_community";
        public static final String INTENT_KEY_ADRESS = "intent_key_adress";
        public static final String INTENT_KEY_MEASURE_TIME = "intent_key_measure_time";
        public static final String INTENT_KEY_SOURCEA = "intent_key_source";
        public static final String INTENT_KEY_LNG = "intent_key_lng";//经度
        public static final String INTENT_KEY_LAT = "intent_key_lat";//纬度
        public static final String INTENT_KEY_LAYOUT = "intent_key_layout";
        public static final String INTENT_KEY_HOUING = "intent_key_housing";
        public static final String INTENT_KEY_STRING_HOUSING = "intent_key_string_housing";
        public static final String INTENT_KEY_OWENER_NEED = "intent_key_owener_need";
        public static final String INTENT_KEY_FIRST_CHANNEL = "intent_key_frist_channel";
        public static final String INTENT_KEY_SECOND_CHANNEL = "intent_key_second_channel";
        public static final String INTENT_KEY_STORE_ID = "intent_key_store_id";
        public static final String INTENT_KEY_CATGORY_ID="intnt_key_catgory_id";
        public static final String INTENT_KEY_ChOOSE_TYPE = "intent_key_choose_type";
        public static final String INTENT_KEY_PROVINCE="intent_key_province";
        public static final String INTENT_KEY_WORK_YEAR="intent_key_work_year";
        public static final String INTENT_KEY_GOOD_AT="intent_key_good_at";
        public static final String INTENT_KEY_STYLE="intent_key_style";
    }


    public static final class Time {
        public static final int SECOND = 1000;
        public static final int MINUTE = 1000 * 60;
        public static final int HOUR = MINUTE * 60;
        public static final int DAY = HOUR * 24;
        public static final int YEAR = DAY * 365;
    }

    public static final class ActReqCode {
        public static final int TYPE_CLIENT_EDIT_HOUSE = 1; // 修改房屋信息
        public static final int TYPE_CLIENT_EDIT_HOUSE_OWNER = 2; // 修改订单用户信息
        public static final int TYPE_CLIENT_EDIT_HOUSE_DESIGNER = 3; // 修改订单设计师信息
        public static final int TYPE_CLIENT_EDIT_PLAN_PRICE = 4; // 修改订单方案信息

        public static final int TYPE_CLIENT_EDIT_LISTENER = 100; // 修改监理
        public static final int TYPE_CLIENT_EDIT_LEADER = 101; // 修改工长信息
        public static final int TYPE_CLIENT_EDIT_SECRETARY = 102; // 修改秘书信息
        public static final int TYPE_CLIENT_EDIT_ADVISER = 103; // 修改顾问信息
        public static final int TYPE_UPLOAD_HEAD_PIC = 104;//上传用户头像
        public static final int TYPE_UPLOAD_LIFE_PIC = 105;//上传用户生活照
        public static final int TYPE_RC_CROP_PHOTO = 106; //用户剪切图片
        public static final int TYPE_RC_DECORATION = 107;//风格
        public static final int TYPE_RC_SITE_BRAND = 108;//品牌
        public static final int TYPE_RC_SITE_ORDER = 109;
        public static final int TYPE_RC_MY_ADRESS = 110;

    }

    public static final class Action {
        public static final String ACTION_PAY_QUERY_RESULT = "com.aiyiqi.decoration.lib.constants.ACTION_PAY_QUERY_RESULT";
    }

    public static final class Network {
        public static final int NETWORK_UNKNOWN = 0;
        public static final int NETWORK_WIFI = 1;
        public static final int NETWORK_2_G = 2;
        public static final int NETWORK_3_G = 3;
        public static final int NETWORK_4_G = 4;
    }
}
