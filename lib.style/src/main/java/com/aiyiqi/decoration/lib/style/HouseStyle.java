package com.aiyiqi.decoration.lib.style;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.aiyiqi.decoration.lib.constants.Constants;
import com.aiyiqi.lib.utils.Logger;
import com.amo.wheelview.ArrayWheelAdapter;
import com.amo.wheelview.OnWheelChangedListener;
import com.amo.wheelview.WheelView;


/**
 * Created by lixufeng on 16/5/3.
 */
public class HouseStyle {

    private static String[] houseArray = new String[]{"一室", "二室", "三室", "四室", "五室", "六室", "七室", "八室", "九室", "十室"};
    private static String[] houseSpaceArray = new String[]{"0厅", "一厅", "二厅", "三厅", "四厅", "五厅", "六厅", "七厅", "八厅", "九厅", "十厅"};
    private static String[] cookRoomArray = new String[]{"0厨", "一厨", "二厨", "三厨", "四厨", "五厨", "六厨", "七厨", "八厨", "九厨", "十厨"};
    private static String[] toiletArray = new String[]{"0卫", "一卫", "二卫", "三卫", "四卫", "五卫", "六卫", "七卫", "八卫", "九卫", "十卫"};

    public static final void showHouseSelector(Context context,final int viewId,final OnSelectedLayoutListener listener){

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 找到dialog的布局文件
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.lib_style_house_layout, null);

        // 室
        final WheelView wv_house_room = (WheelView) view.findViewById(R.id.house_room);
        wv_house_room.setCyclic(false);

        wv_house_room.setAdapter(new ArrayWheelAdapter<String>(houseArray,houseArray.length));
        wv_house_room.setCurrentItem(1);

        // 厅
        final WheelView wv_house_space = (WheelView) view.findViewById(R.id.house_space);
        wv_house_space.setAdapter(new ArrayWheelAdapter<String>(houseSpaceArray, houseSpaceArray.length));
        wv_house_space.setCyclic(false);
        wv_house_space.setCurrentItem(1);

        // 厨
        final WheelView wv_cook_room = (WheelView) view.findViewById(R.id.house_cook_room);
        wv_cook_room.setAdapter(new ArrayWheelAdapter<String>(cookRoomArray, cookRoomArray.length));
        wv_cook_room.setCyclic(false);
        wv_cook_room.setCurrentItem(1);

        // 卫
        final WheelView wv_toilet_room = (WheelView) view.findViewById(R.id.house_toilet);
        wv_toilet_room.setAdapter(new ArrayWheelAdapter<String>(toiletArray, toiletArray.length));
        wv_toilet_room.setCyclic(false);
        wv_toilet_room.setCurrentItem(1);

        OnWheelChangedListener wheelListenerDay = new OnWheelChangedListener(){
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
            }
        };
        wv_house_room.addChangingListener(wheelListenerDay);

        // 根据屏幕密度来指定选择器字体的大小
        int textSize = 0;
        int currentTxtSize = 0;

        textSize = (int) context.getResources().getDimension(R.dimen.lib_style_text_size_normal);
        currentTxtSize = (int) context.getResources().getDimension(R.dimen.lib_style_text_size_big);
        wv_house_room.TEXT_SIZE = textSize;
        wv_house_space.TEXT_SIZE = textSize;
        wv_cook_room.TEXT_SIZE = textSize;
        wv_toilet_room.TEXT_SIZE = textSize;

        wv_house_room.CURRENT_ITEM_TEXT_SIZE = currentTxtSize;
        wv_house_space.CURRENT_ITEM_TEXT_SIZE = currentTxtSize;
        wv_cook_room.CURRENT_ITEM_TEXT_SIZE = currentTxtSize;
        wv_toilet_room.CURRENT_ITEM_TEXT_SIZE = currentTxtSize;

        TextView btn_sure = (TextView) view.findViewById(R.id.btn_layout_sure);
        TextView btn_cancel = (TextView) view
                .findViewById(R.id.btn_layout_cancel);
        // 确定
        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                int roomItem = wv_house_room.getCurrentItem();
                int spaceItem = wv_house_space.getCurrentItem();
                int cookRoomItem = wv_cook_room.getCurrentItem();
                int toiletItem = wv_toilet_room.getCurrentItem();
                String selectedLayout = houseArray[roomItem] + houseSpaceArray[spaceItem] + cookRoomArray[cookRoomItem] + toiletArray[toiletItem];
                Logger.e(Constants.TAG, "setOnClickListener >> selectedLayout : " + selectedLayout);

                if(listener!=null){
                    listener.onSelectedHouse(viewId, selectedLayout);
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

    public  static interface OnSelectedLayoutListener{
        public void onSelectedHouse(int viewId, String formatValue);
    }
}
