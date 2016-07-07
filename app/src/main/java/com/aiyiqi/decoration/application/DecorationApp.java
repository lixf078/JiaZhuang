package com.aiyiqi.decoration.application;

import android.app.Application;

import net.wequick.small.Small;

/**
 * Created by lixufeng on 16/7/7.
 * Class desc
 */
public class DecorationApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Small.preSetUp(this);
    }
}
