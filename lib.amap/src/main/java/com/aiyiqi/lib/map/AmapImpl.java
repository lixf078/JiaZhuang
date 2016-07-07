package com.aiyiqi.lib.map;

import android.content.Context;
import android.location.Location;


import com.aiyiqi.decoration.lib.constants.Constants;
import com.aiyiqi.lib.map.bean.AmapBean;
import com.aiyiqi.lib.map.event.AmapEvent;
import com.aiyiqi.lib.utils.Logger;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by hubing on 16/3/17.
 */
public class AmapImpl implements IMap{

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    /**
     * 地理位置
     */
    protected Location mLocation = null;

    @Override
    public void searchAddressByKeyword(Context context,String keyword,String cityCode) {
        Logger.e(Constants.TAG, "Logger >>  AmapImpl >> searchAddressByKeyword >> keyword : " + keyword);

       PoiSearch.Query query = new PoiSearch.Query(keyword,"","010");
        query.setPageSize(10);
        PoiSearch poiSearch = new PoiSearch(context,query);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                Logger.e(Constants.TAG,"Logger >>  AmapImpl >> onPoiSearched >> poiResult : "+poiResult);
                if(poiResult!=null){
                    ArrayList<PoiItem> poiItems = poiResult.getPois();
                    Logger.e(Constants.TAG,"Logger >>  AmapImpl >> onPoiSearched >> poiItems.isEmpty() : "+poiItems.isEmpty());

                    if(poiItems!=null && !poiItems.isEmpty()){
                        ArrayList<AmapBean> addressBeans = new ArrayList<AmapBean>();
                        for(PoiItem poiItem : poiItems){
                            AmapBean bean = new AmapBean();
                            bean.title = poiItem.getTitle();

                            StringBuilder builder = new StringBuilder();
                            builder.append(poiItem.getProvinceName());
                            builder.append(poiItem.getCityName());
                            builder.append(poiItem.getAdName());
                            builder.append(poiItem.getTitle());
                            bean.latitude = poiItem.getLatLonPoint().getLatitude();
                            bean.longitude = poiItem.getLatLonPoint().getLongitude();
                            bean.addressName = builder.toString();
                            Logger.e(Constants.TAG,"Logger >> onPoiSearched >> poiItem.getTitle() : "+poiItem.getTitle());
                            Logger.e(Constants.TAG,"Logger >> onPoiSearched >>builder.toString() : "+builder.toString()+" ; floor : "+ poiItem.getIndoorData().getFloorName());
                            addressBeans.add(bean);
                        }

                        AmapEvent event = new AmapEvent();
                        event.eventType = AmapEvent.TYPE_AMAP_SEARCH_LIST;
                        event.addressBeans = addressBeans;

                        EventBus.getDefault().post(event);
                    }
                }

            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
        poiSearch.searchPOIAsyn();

    }

    @Override
    public void updateAddress(Context context, String cityCode) {

    }

    @Override
    public void searchAddressByPosition(Context context, AmapBean amapBean) {

    }

    @Override
    public void getCurrentLocation(Context context) {

    }
}
