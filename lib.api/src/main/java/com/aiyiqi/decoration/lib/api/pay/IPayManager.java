package com.aiyiqi.decoration.lib.api.pay;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by hubing on 16/3/23.
 */
public interface IPayManager  {

    public void pay(Context context, HashMap<String, String> map);
    public void queyPaymentStatus(Context context, HashMap<String, String> map);
    public void queyPayment(Context context, HashMap<String, String> map);
    public void checkout(Context context, HashMap<String, String> map);
}
