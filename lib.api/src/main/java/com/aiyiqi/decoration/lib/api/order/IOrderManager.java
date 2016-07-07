package com.aiyiqi.decoration.lib.api.order;

import android.content.Context;
import android.nfc.cardemulation.HostApduService;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by hubing on 16/3/22.
 */
public interface IOrderManager {

    public void listOrderNum(Context context);
    public void listOrder(Context context, HashMap<String, String> map);
    public void getOrderInfo(Context context, HashMap<String, String> map);
    public void editOrderStatus(Context context, HashMap<String, String> map);//String orderId,String sourceId,String targetId,String message
    public void addOrderContract(Context context, HashMap<String, String> map);//String orderId,String contractPrice,String message,String firstPrice,String endPrice,String designUrls
    public void addOrderScheme(Context context, HashMap<String, String> map);//String orderId,String contractPrice,String message,String firstPrice,String endPrice,String designUrls
    public void getOrderContract(Context context, HashMap<String, String> map);//String orderId
    public void addOrder(Context context, HashMap<String, String> map);//String name,String mobile
    public void saveOrderHouse(Context context, HashMap<String, String> map);
    public void listVendor(Context context, HashMap<String, String> map);
    public void editOrderUnder(Context context, HashMap<String, String> map);//String orderId,String vendorId
    public void editUserDetail(Context context, HashMap<String, String> map);
    public void addRecord(Context context, HashMap<String, String> map);
    public void listVendorBySite(Context context, HashMap<String, String> map);
    public void listPackageCategory(Context context, HashMap<String, String> map);
    public void getOrderSource(Context context, HashMap<String, String> map);
    public void  getDecStyleTabData(Context context, HashMap<String, String> map);
    public void  creatOrder(Context context, HashMap<String, String> map);
    public void  editOrderHouse(Context context, HashMap<String, String> map);
    public void  editBuildingOrderChannel(Context context, HashMap<String, String> map);
    public void editBuildingOrderDate(Context context, HashMap<String, String> map);

}
