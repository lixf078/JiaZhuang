package com.aiyiqi.lib.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by hubing on 15/8/7.
 */
public class ShareUtil  implements  SharedPreferences.OnSharedPreferenceChangeListener{

    private static ShareUtil mInstance;
    private static Object objClock = new Object();
    private SharedPreferences sp;


    private On17SharedPreferenceChangeListener changeListener;
    private ShareUtil(Context context){
        sp = context.getSharedPreferences("17House_Jia",Context.MODE_MULTI_PROCESS);
    };

    public static ShareUtil getInstance(Context context){
        synchronized (objClock){
            if(mInstance==null){
                mInstance = new ShareUtil(context);
            }
            return mInstance;
        }
    }



    /**
     *
     * 保存 到 sharedPreference
     *
     * value 支持的类型 : int,long,boolean,String,Set<String>
     *
     * @param key
     * @param value
     */
    public void save(String key,Object value){
        if(value instanceof Integer){
            saveInt(key, (Integer) value);
        }else if(value instanceof Boolean){
            saveBoolean(key, (Boolean) value);
        }else if(value instanceof String){
            saveString(key, (String) value);
        }else if(value instanceof Long){
            saveLong(key, (Long) value);
        }else if(value instanceof Set){
            saveStringSet(key, (Set<String>) value);
        }else if(value instanceof Float){
            saveFloat(key, (Float) value);
        }else if(value == null){
            saveNull(key);
        }else{
            throw new IllegalArgumentException(" not support value class.");
        }

    }

    private void saveInt(String key,int value){
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key,value);
        editor.commit();
    }

    private void saveLong(String key,long value){
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    private void saveString(String key,String value){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private void saveBoolean(String key,boolean value){
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    private void saveStringSet(String key,Set<String> values){
        SharedPreferences.Editor editor = sp.edit();
        editor.putStringSet(key, values);
        editor.commit();
    }

    private void saveFloat(String key,Float value){
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    private void saveNull(String key){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, "");
        editor.commit();
    }


    public int getIntValue(String key,int defaultValue){
        return sp.getInt(key,defaultValue);
    }
    public long getLongValue(String key,long defaultValue){
        return sp.getLong(key, defaultValue);
    }

    public boolean getBooleanValue(String key,boolean defaultValue){
        return sp.getBoolean(key, defaultValue);
    }

    public String getStringValue(String key,String defaultValue){
        return sp.getString(key, defaultValue);
    }

    public Set<String> getSetValue(String key,Set<String> defaultValue){
        return sp.getStringSet(key, defaultValue);
    }
    public float getFloatValue(String key,float defaultValue){
        return sp.getFloat(key, defaultValue);
    }

    public void remove(String key){
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(changeListener!=null){
            changeListener.onSharedPreferenceChanged(sharedPreferences,key);
        }
    }

    public interface On17SharedPreferenceChangeListener{
        void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key);
    }
}
