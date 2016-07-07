package com.aiyiqi.decoration.lib.style;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.aiyiqi.decoration.lib.constants.Constants;
import com.aiyiqi.lib.utils.Logger;
import com.aiyiqi.lib.utils.TimeUtil;
import com.amo.wheelview.ArrayWheelAdapter;
import com.amo.wheelview.OnWheelChangedListener;
import com.amo.wheelview.WheelView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


/**
 * Created by hubing on 16/3/24.
 */
public class DateStyle {

    private static ArrayList<String> dateList = new ArrayList<String>();

    public static final void showDateSelector(Context context,final int viewId,final int offsetDay,final OnSelectedDateLisntener lisntener){

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
        String[] months_little = { "4", "6", "9", "11" };

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 找到dialog的布局文件
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.lib_style_time_layout, null);

        // 日
        final WheelView wv_day = (WheelView) view.findViewById(R.id.day);
        wv_day.setCyclic(false);

        String[] dayArray = getDayList(year,month+1,day,offsetDay);
        wv_day.setAdapter(new ArrayWheelAdapter<String>(dayArray,dayArray.length));
        wv_day.setCurrentItem(0);

        // 时
        final WheelView wv_hours = (WheelView) view.findViewById(R.id.hour);
        String[] hours = getHourList();
        wv_hours.setAdapter(new ArrayWheelAdapter<String>(hours, hours.length));
        wv_hours.setCyclic(false);
        wv_hours.setCurrentItem(hour);

        // 分
        final WheelView wv_mins = (WheelView) view.findViewById(R.id.mins);
        String[] fens = getFenList(wv_mins);
        wv_mins.setAdapter(new ArrayWheelAdapter<String>(fens, fens.length));
        wv_mins.setCyclic(false);
        wv_mins.setCurrentItem(minute);


        OnWheelChangedListener wheelListenerDay = new OnWheelChangedListener(){
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
            }
        };
        wv_day.addChangingListener(wheelListenerDay);

        // 根据屏幕密度来指定选择器字体的大小
        int textSize = 0;
        int currentTxtSize = 0;

        textSize = (int) context.getResources().getDimension(R.dimen.lib_style_text_size_normal);
        currentTxtSize = (int) context.getResources().getDimension(R.dimen.lib_style_text_size_big);
        wv_day.TEXT_SIZE = textSize;
        wv_hours.TEXT_SIZE = textSize;
        wv_mins.TEXT_SIZE = textSize;

        wv_day.CURRENT_ITEM_TEXT_SIZE = currentTxtSize;
        wv_hours.CURRENT_ITEM_TEXT_SIZE = currentTxtSize;
        wv_mins.CURRENT_ITEM_TEXT_SIZE = currentTxtSize;

        TextView btn_sure = (TextView) view.findViewById(R.id.btn_datetime_sure);
        TextView btn_cancel = (TextView) view
                .findViewById(R.id.btn_datetime_cancel);
        // 确定
        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                int second = calendar.get(Calendar.SECOND);
                Logger.e(Constants.TAG, "############################");
                long time = System.currentTimeMillis()+(23 - hour) * Constants.Time.HOUR + (59 - minute) * Constants.Time.MINUTE +(60 - second) * Constants.Time.SECOND;
                int dayItem = wv_day.getCurrentItem();
                int hourItem = wv_hours.getCurrentItem();
                int fenItem = wv_mins.getCurrentItem();
                long dayTime = (Long.valueOf(dayItem) * Constants.Time.DAY);
                long houreTime = Long.valueOf(hourItem) * Constants.Time.HOUR;
                long minuteTime = Long.valueOf(fenItem) * Constants.Time.MINUTE;
                long selectedTime = time + dayTime + houreTime + minuteTime;
                Logger.e(Constants.TAG, "setOnClickListener >> selectedTime : " + TimeUtil.formatTimeToYMDHHmm(selectedTime));
                Logger.e(Constants.TAG, "############################");

                if(lisntener!=null){
                    lisntener.onSelectedDate(viewId, TimeUtil.formatTimeToYMDHHmm(selectedTime),selectedTime);
                }
                dateList.clear();
                dialog.dismiss();
            }
        });
        // 取消
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dateList.clear();
                dialog.dismiss();
            }
        });
        // 设置dialog的布局,并显示
        dialog.setContentView(view);
        dialog.show();
    }

    private static ArrayList<String> getDataArrayList(int year,int month,int start,int end){
        ArrayList<String> strings = new ArrayList<String>();
        for(int i = start ; i <= end; i++){
            StringBuilder builder = new StringBuilder();
            int newYear = (month > 12) ? (year + 1) : year;
            int newMonth = (month > 12) ? 1 : (month);
            builder.append(String.valueOf(newYear));
            builder.append("年");
            builder.append(String.valueOf(newMonth));
            builder.append("月");
            builder.append(String.valueOf(i));
            builder.append("日");
            strings.add(builder.toString());
        }
        return strings;
    }


    private static String[] getDayList(int year,int month,int day,int offset){
        if(offset > 0){
            int dayOfMonth = getDayOfMonth(year,month);
            if(offset <= (dayOfMonth - day -1)){
                dateList.addAll(getDataArrayList(year, month, day +1 , offset+day));
            }else{
                dateList.addAll(getDataArrayList(year,month,day+1,dayOfMonth));
            }

            int leafDays = offset - dateList.size();
            getDayList((month+1) > 12 ? (year+1 ) : year,(month+1) > 12 ? 1 : (month +1 ),0,leafDays);

        }else {
            return dateList.toArray(new String[]{});
        }
        return dateList.toArray(new String[]{});
    }

    private static int getDayOfMonth(int year,int month){
        String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
        String[] months_little = { "4", "6", "9", "11" };
        final List<String> list_big = Arrays.asList(months_big);
        final List<String> list_little = Arrays.asList(months_little);

        if(month > 12){
            month = 1;
            year = year + 1 ;
        }

        if (list_big.contains(String.valueOf(month ))){
            return 31;
        }else if (list_little.contains(String.valueOf(month))) {
            return 30;
        }else{
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0){
                return 29;
            }else{
                return 28;
            }
        }
    }

    private static String[] getHourList(){
        ArrayList<String>  hourList = new ArrayList<String>();
        for(int i =  0; i <= 23; i++){
            StringBuilder builder = new StringBuilder();
            builder.append(String.format("%02d", i));
            builder.append("点");
            hourList.add(builder.toString());
        }
        return hourList.toArray(new String[]{});
    }

    private static String[] getFenList(WheelView wv){
        ArrayList<String>  fenList = new ArrayList<String>();
        for(int i=0;i<=59;i++){
            StringBuilder builder = new StringBuilder();
            builder.append(String.format("%02d", i));
            builder.append("分");
            fenList.add(builder.toString());
        }
        return fenList.toArray(new String[]{});
    }

    public  static interface OnSelectedDateLisntener{
        public void onSelectedDate(int viewId, String formateVaue, long value);
    }
}
