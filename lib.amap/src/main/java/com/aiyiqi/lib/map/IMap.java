package com.aiyiqi.lib.map;

import android.content.Context;

import com.aiyiqi.lib.map.bean.AmapBean;

/**
 * Created by hubing on 16/3/17.
 */
public interface IMap {

    public void searchAddressByKeyword(Context context, String keyword, String cityCode);

    public void updateAddress(Context context, String cityCode);

    public void searchAddressByPosition(Context context, AmapBean amapBean);

    public void getCurrentLocation(Context context);

    public interface CustomLocationListener{
        public void onLocationChanged(AmapBean mapBean);
    }

}
