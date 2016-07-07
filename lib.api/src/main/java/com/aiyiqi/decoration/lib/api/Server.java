package com.aiyiqi.decoration.lib.api;

/**
 * Created by hubing on 16/3/23.
 */
public class Server {

    public static final String ERROR_SERVER_MSG = "数据异常，请稍后";
    private static boolean isOnLine;
    public static final int PAGE_SIZE = 10;

    public static final void setOnLine(boolean onLine) {
        isOnLine = onLine;
    }

    public static final String getServerUrl() {
        if (isOnLine) {
            return URL.ONLINE_SERVER_URL;
        }
        return URL.TEST_SERVER_URL;
    }

    public static final class Code {
        public static final int SUCCESS = 0;
        public static final int FAIL = 1;
        public static final String PAY_RESULT_SUCCESS = "pay_success";
        public static final String PAY_RESULT_FAILURE = "pay_failure";
    }

    public static final class URL {
        public static final String ONLINE_SERVER_URL = "http://hui.17house.com/svc/payment-facade";//"http://192.168.3.174:8088/payment-facade-0.0.1-local";//
        public static final String TEST_SERVER_URL =  "http://appdev.17house.com/svc/payment-facade";//"http://192.168.2.209:8088/payment-facade-0.0.1-local";http://appdev.17house.com/svc/payment-facade
    }

    public static final class Param {
        public static final String GROUP_ID = "groupId";
        public static final String ORDER_ID = "orderId";
        public static final String SOURCE_ID = "sourceId";
        public static final String TARGET_ID = "targetId";
        public static final String MESSAGE = "message";
        public static final String PRE_PRICE = "prePrice";
        public static final String CONTRACT_PRICE = "contractPrice";
        public static final String FIRST_PRICE = "firstPrice";
        public static final String END_PRICE = "endPrice";
        public static final String DESIGN_URLS = "designUrls";
        public static final String NAME = "name";
        public static final String MOBILE = "mobile";
        public static final String TYPE = "type";
        public static final String VENDOR_ID = "vendorId";
        public static final String ROLE_JSON = "roleJson";
        public static final String BUILDING_ID = "buildingId";
        public static final String AT_LIST_JSON = "atList";
        public static final String VENDOR_ACCOUNT = "vendorAccount";
        public static final String VENDOR_PSWD = "vendorPswd";
        public static final String CHECKOUT_ID = "checkoutId";
        public static final String CHECKOUT_TYPE = "checkoutType";
        public static final String COMMUNITY = "community";
        public static final String ADDRESS = "address";
        public static final String DOORPLATE = "doorplate";
        public static final String NEWHOUSE = "newHouse";
        public static final String LAYOUT = "layout";
        public static final String AREA = "area";
        public static final String SOURCE = "source";
        public static final String MEASURE_TIME = "measureTime";
        public final static String OLD_PASS_WORD = "oldPswd";
        public final static String NEW_PASS_WORD = "newPswd";
        public final static String CHANNEL = "channel";
        public final static String PAY_CHANNEL = "payChannel";
        public final static String ORDER_MONEY = "price";
        public final static String IS_PAY_OFFO_NLINE = "isPayOffLine";
        public final static String TRACKER_ID = "trackId";
        public final static String PAGE_SIZE = "pageSize";
        public final static String PAGE_NUM = "pageNo";
        public final static String PAY_RESULT_STATE = "pay_result_state";
        public final static String PAY_QUERY_RESULT_NUMBER = "pay_query_result_number";
        public static final String UPDATE_TYPE = "updateType";
        public static final String ALLOT_LIST = "allotList";
        public static final String UPLOAD_IMG_LIST = "imgSrc";
        public static final String UPLOAD_SIGNATURE = "signature";
        public static final String UPLOAD_AVATAR = "avatar";
        public static final String UPLOAD_LIFE_PHOTO = "lifePhoto";
        public static final String LNG = "lng";
        public static final String LAT = "lat";
        public static final String CHANNEL_FIRST = "channelFirst";
        public static final String CHANNEL_SECOND = "channelSecond";
        public static final String DELIVERY_HOUSE = "deliveryHouse";
        public static final String DATE = "date";
        public static final String HOUSE_COMMENT = "houseComment";
        public static final String PROVINCE = "provinceId";
        public static final String WORK_YEAR = "workYear";
        public static final String GOOD_AT = "goodAt";
        public static final String CATEROGY_ID = "caterogyId";
        public static final String LIKE_NAME = "likeName";
        public static final String MATERIALS = "materials";
        public  static  final  String  PROGRESS_ID=   "progressId";
        public  static  final  String  STYLE ="style";

    }

}
