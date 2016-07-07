package com.aiyiqi.lib.utils;

import android.text.TextUtils;

import com.aiyiqi.decoration.lib.constants.Constants;

import java.security.MessageDigest;

/**
 * Created by hubing on 15/12/14.
 */
public class DigestUtils {
    public static final String sha512Hex(String strText){
        if(TextUtils.isEmpty(strText)){
            return null;
        }
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.update(strText.getBytes());
            byte byteBuffer[] = messageDigest.digest();
            // 將 byte 轉換爲 string
            StringBuffer strHexString = new StringBuffer();
            for (int i = 0; i < byteBuffer.length; i++){
                String hex = Integer.toHexString(0xff & byteBuffer[i]);
                if (hex.length() == 1){
                    strHexString.append('0');
                }
                strHexString.append(hex);
            }

             return strHexString.toString();


        }catch (Exception e){
            Logger.e(Constants.TAG,"sha 512 error.",e);
        }
        return null;
    }
}
