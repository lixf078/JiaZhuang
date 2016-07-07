package com.aiyiqi.decoration.lib.api.event;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

/**
 * Created by lixufeng on 16/3/24.
 */
public class AppPostEvent {
    public static void postRequestEvent(int eventType,HashMap<String, String> paramMap){
        ResponseEvent event = new ResponseEvent();
        event.eventType = eventType;
        if(paramMap!=null){
            event.resultObj = paramMap;
        }
        EventBus.getDefault().post(event);
    }
}
