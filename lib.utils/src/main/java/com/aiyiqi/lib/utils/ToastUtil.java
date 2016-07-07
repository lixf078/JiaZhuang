package com.aiyiqi.lib.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by hubing on 16/3/16.
 */
public class ToastUtil {

    private ToastUtil(){}

    public static Toast showToast(Context context,String content){
        Toast toast=  Toast.makeText(context, content, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();

        return toast;

    }

    public static Toast showToast(Context context,int contentRes){
        Toast toast=  Toast.makeText(context, contentRes, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
        return  toast;
    }

}
