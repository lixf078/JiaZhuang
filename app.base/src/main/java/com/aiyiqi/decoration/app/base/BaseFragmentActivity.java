package com.aiyiqi.decoration.app.base;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.aiyiqi.lib.utils.ToastUtil;

/**
 * Created by hubing on 16/3/21.
 */
public class BaseFragmentActivity extends FragmentActivity {

    protected void resolveError(int errorCode,String errorMsg){
        if(errorCode == 3){
            Intent intent = new Intent();
            intent.setClassName(this,"com.aiyiqi.decoration.my.LoginActivity");
            startActivity(intent);
            finish();
        }else{
            ToastUtil.showToast(this, errorMsg);
        }
    }
}
