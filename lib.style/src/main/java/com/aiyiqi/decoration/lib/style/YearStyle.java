package com.aiyiqi.decoration.lib.style;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.amo.wheelview.ArrayWheelAdapter;
import com.amo.wheelview.OnWheelChangedListener;
import com.amo.wheelview.WheelView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by 17house-llj on 16/6/20.
 */
public class YearStyle {
    private static ArrayList<String> dateList = new ArrayList<String>();

    public static final void showDateSelector(Context context, final int viewId, final int workYear, final OnSelectedDateLisntener lisntener) {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int offSet  =year-workYear;

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 找到dialog的布局文件
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.lib_style_year_time_layout, null);

        // 日
        final WheelView wv_yare = (WheelView) view.findViewById(R.id.years);
        wv_yare.setCyclic(false);


        String[] dayArray = getYearsList(year);
        wv_yare.setAdapter(new ArrayWheelAdapter<String>(dayArray, dayArray.length));
        wv_yare.setCurrentItem(offSet);

        // 时



        OnWheelChangedListener wheelListenerDay = new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
            }
        };
        wv_yare.addChangingListener(wheelListenerDay);

        // 根据屏幕密度来指定选择器字体的大小
        int textSize = 0;
        int currentTxtSize = 0;

        textSize = (int) context.getResources().getDimension(R.dimen.lib_style_text_size_normal);
        currentTxtSize = (int) context.getResources().getDimension(R.dimen.lib_style_text_size_big);
       wv_yare.TEXT_SIZE = textSize;
        wv_yare.CURRENT_ITEM_TEXT_SIZE = currentTxtSize;

        TextView btn_sure = (TextView) view.findViewById(R.id.btn_datetime_sure);
        TextView btn_cancel = (TextView) view
                .findViewById(R.id.btn_datetime_cancel);
        // 确定
        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

              String  year=  wv_yare.getAdapter().getItem(wv_yare.getCurrentItem());
                if (!TextUtils.isEmpty(year)){
                    long  num=Long.parseLong(year.replace("年",""));
                    if (lisntener != null) {
                        lisntener.onSelectedDate(viewId, year,num);
                    }
                }else {
                    if (lisntener != null) {
                        lisntener.onSelectedDate(viewId, year, 0);
                    }
                }

                dialog.dismiss();
            }
        });
        // 取消
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        // 设置dialog的布局,并显示
        dialog.setContentView(view);
        dialog.show();
    }



    private  static String[]  getYearsList(int year){
        ArrayList<String> mList =new ArrayList<>();
       for(int  i=0;i<50;i++) {
        StringBuilder stringBuilder = new StringBuilder("");
           stringBuilder.append(year-i).append("年");
           mList.add(stringBuilder.toString());
       }
        return  mList.toArray(new String[]{});
    }





    public static interface OnSelectedDateLisntener {
        public void onSelectedDate(int viewId, String formateVaue, long value);
    }
}