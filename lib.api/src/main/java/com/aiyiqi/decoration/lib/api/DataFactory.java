package com.aiyiqi.decoration.lib.api;

import android.content.Context;

import com.aiyiqi.decoration.lib.api.order.IOrderManager;
import com.aiyiqi.decoration.lib.api.order.OrderManager;
import com.aiyiqi.decoration.lib.api.pay.IPayManager;
import com.aiyiqi.decoration.lib.api.pay.PayManager;
import com.aiyiqi.decoration.lib.api.site.ISiteManager;
import com.aiyiqi.decoration.lib.api.site.SiteManager;
import com.aiyiqi.decoration.lib.api.user.IUserManager;
import com.aiyiqi.decoration.lib.api.user.UserManager;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by hubing on 16/3/17.
 */
public class DataFactory  implements IUserManager,IOrderManager,ISiteManager,IPayManager{
    private static Object objClock = new Object();
    private static DataFactory mInstance;
    private IUserManager userManager;
    private IOrderManager orderManager;
    private ISiteManager siteManager;
    private IPayManager payManager;
    private DataFactory(Context context){
        userManager = UserManager.getInstance(context);
        orderManager = OrderManager.getInstance(context);
        siteManager = SiteManager.getInstance(context);
        payManager = PayManager.getInstance(context);

    };
    public static DataFactory getInstance(Context context){
        synchronized (objClock){
            if(mInstance == null){
                mInstance = new DataFactory(context);
            }
            return mInstance;
        }
    }

    @Override
    public void listOrderNum(Context context) {
        orderManager.listOrderNum(context);
    }

    @Override
    public void listOrder(Context context, HashMap<String,String> map) {
        orderManager.listOrder(context, map);
    }

    @Override
    public void getOrderInfo(Context context, HashMap<String,String> map) {
        orderManager.getOrderInfo(context, map);
    }

    @Override
    public void editOrderStatus(Context context,HashMap<String,String> map) {
        orderManager.editOrderStatus(context, map);
    }

    @Override
    public void addOrderContract(Context context, HashMap<String,String> map) {
        orderManager.addOrderContract(context, map);
    }

    @Override
    public void addOrderScheme(Context context, HashMap<String,String> map) {
        orderManager.addOrderScheme(context, map);
    }

    @Override
    public void getOrderContract(Context context, HashMap<String, String> map) {
        orderManager.getOrderContract(context, map);
    }

    @Override
    public void addOrder(Context context, HashMap<String,String> map) {
        orderManager.addOrder(context, map);
    }

    @Override
    public void saveOrderHouse(Context context, HashMap<String,String> map) {
        orderManager.saveOrderHouse(context, map);
    }

    @Override
    public void listVendor(Context context, HashMap<String,String> map) {
        orderManager.listVendor(context, map);
    }

    @Override
    public void editOrderUnder(Context context, HashMap<String,String> map) {
        orderManager.editOrderUnder(context, map);
    }

    @Override
    public void editUserDetail(Context context, HashMap<String, String> map) {
        orderManager.editUserDetail(context, map);
    }

    @Override
    public void addRecord(Context context, HashMap<String, String> map) {
        orderManager.addRecord(context, map);
    }

    @Override
    public void listVendorBySite(Context context, HashMap<String, String> map) {
        orderManager.listVendorBySite(context,map);
    }

    @Override
    public void listPackageCategory(Context context, HashMap<String, String> map) {
        orderManager.listPackageCategory(context,map);
    }

    @Override
    public void getOrderSource(Context context, HashMap<String, String> map) {
        orderManager.getOrderSource(context,map);
    }

    @Override
    public void getDecStyleTabData(Context context, HashMap<String, String> map) {
        orderManager.getDecStyleTabData(context,map);
    }

    @Override
    public void creatOrder(Context context, HashMap<String, String> map) {
        orderManager.creatOrder(context,map);
    }

    @Override
    public void editOrderHouse(Context context, HashMap<String, String> map) {
        orderManager.editOrderHouse(context,map);
    }

    @Override
    public void editBuildingOrderChannel(Context context, HashMap<String, String> map) {
        orderManager.editBuildingOrderChannel(context,map);
    }

    @Override
    public void editBuildingOrderDate(Context context, HashMap<String, String> map) {
        orderManager.editBuildingOrderDate(context,map);
    }

    @Override
    public void login(Context context, HashMap<String,String> map) {
        userManager.login(context, map);
    }

    @Override
    public void logout(Context context) {
        userManager.logout(context);
    }

    @Override
    public void editPswd(Context context, HashMap<String, String> map) {
        userManager.editPswd(context, map);
    }

    @Override
    public void upLoadMyInfor(Context context, HashMap<String, String> map) {
        userManager.upLoadMyInfor(context,map);
    }

    @Override
    public void getMyInfor(Context context, HashMap<String, String> map) {
        userManager.getMyInfor(context,map);
    }

    @Override
    public void listProvinces(Context context, HashMap<String, String> map) {
        userManager.listProvinces(context,map);
    }

    @Override
    public void getBuildingSiteVendor(Context context, HashMap<String, String> map) {
        userManager.getBuildingSiteVendor(context,map);
    }

    @Override
    public void upLoadXgToKen(Context context, HashMap<String, String> map) {
        userManager.upLoadXgToKen(context,map);
    }

    @Override
    public void addBuildingSite(Context context, HashMap<String,String> map) {
        siteManager.addBuildingSite(context, map);
    }

    @Override
    public void listBuildingSite(Context context, HashMap<String,String> map) {
        siteManager.listBuildingSite(context, map);
    }

    @Override
    public void listBuildingSiteTrack(Context context, HashMap<String,String> map) {
        siteManager.listBuildingSiteTrack(context, map);
    }

    @Override
    public void listMyRemind(Context context) {
        siteManager.listMyRemind(context);
    }

    @Override
    public void addBuildingSiteTrack(Context context, HashMap<String,String> map) {
        siteManager.addBuildingSiteTrack(context, map);
    }

    @Override
    public void addBuildingSiteTrackReply(Context context, HashMap<String, String> map) {
        siteManager.addBuildingSiteTrackReply(context, map);
    }

    @Override
    public void tagReadedBuildingSiteTrack(Context context, HashMap<String, String> map) {
        siteManager.tagReadedBuildingSiteTrack(context, map);
    }

    @Override
    public void deleteBuildingSiteTrack(Context context, HashMap<String, String> map) {
        siteManager.deleteBuildingSiteTrack(context, map);
    }

    @Override
    public void readBuildingSiteTrack(Context context, HashMap<String, String> map) {
        siteManager.readBuildingSiteTrack(context, map);
    }

    @Override
    public void countUnReaded(Context context, HashMap<String, String> map) {
        siteManager.countUnReaded(context, map);
    }

    @Override
    public void buildingMessage(Context context, HashMap<String, String> map) {
        siteManager.buildingMessage(context, map);
    }

    @Override
    public void ossToken(Context context, HashMap<String, String> map) {
        siteManager.ossToken(context, map);
    }

    @Override
    public void editBuildingSiteAllot(Context context, HashMap<String, String> map) {
        siteManager.editBuildingSiteAllot(context, map);
    }

    @Override
    public void getMaterialList(Context context, HashMap<String, String> map) {
        siteManager.getMaterialList(context,map);
    }

    @Override
    public void getSeachBrandList(Context context, HashMap<String, String> map) {
        siteManager.getSeachBrandList(context,map);
    }

    @Override
    public void loadSiteProgressData(Context context, HashMap<String, String> map) {
        siteManager.loadSiteProgressData(context,map);
    }

    @Override
    public void upLoadMaterialData(Context context, HashMap<String, String> map) {
        siteManager.upLoadMaterialData(context,map);
    }

    @Override
    public void updateBuildingSiteProgress2(Context context, HashMap<String, String> map) {
        siteManager.updateBuildingSiteProgress2(context,map);
    }

    @Override
    public void pay(Context context, HashMap<String, String> map) {
        payManager.pay(context, map);
    }

    @Override
    public void queyPaymentStatus(Context context, HashMap<String, String> map) {
        payManager.queyPaymentStatus(context, map);
    }

    @Override
    public void queyPayment(Context context, HashMap<String, String> map) {
        payManager.queyPayment(context, map);
    }

    @Override
    public void checkout(Context context, HashMap<String, String> map) {
        payManager.checkout(context, map);
    }

}
