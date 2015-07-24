package com.android.libcore.cachemanager;

import android.content.Context;
import android.content.SharedPreferences;
import com.android.libcore.application.RootApplication;

import java.util.ArrayList;
import java.util.Set;

/**
 * Description: 使用SharedPreferences来保存对象，SharedPreference可以使用两种存储方式存储：
 * <ul>
 *     <li>
 *         一种是存储进临时SharedPreference中，{@link #setTemporary(String, Object)}，读取使用
 *         {@link #getTemporary(String, Class)}来读取固定类型或者{@link #getTemporarySet(String, Class)}
 *         函数来读取特定类型的集合，这种类型的存储会在应用退出之后全部删除
 *     </li>
 *     <li>
 *         一种是存储进永久的SharedPreference中，{@link #setPermanent(String, Object)}，读取使用
 *         {@link #getPermanent(String, Class)}来读取固定类型或者{@link #getPermanentSet(String, Class)}
 *         方法来读取特定类型集合，存储之后永久保存
 *     </li>
 * </ul>
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-23
 */
public class CacheManager {

    /** 临时SharedPreference，当退出应用之后会主动删除 */
    public static String TEMPORARY = "temporary";
    /** 永久SharedPreference，退出应用不会主动删除 */
    public static String PERMANENT = "permanent";

    public static void setTemporary(String key, Object value){
        SharedPreferences sp = RootApplication.getInstance().getSharedPreferences(TEMPORARY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (value instanceof Boolean){
            editor.putBoolean(key, (Boolean) value);
        }else if (value instanceof Float){
            editor.putFloat(key, (Float) value);
        }else if (value instanceof Integer){
            editor.putInt(key, (Integer) value);
        }else if (value instanceof Long){
            editor.putLong(key, (Long) value);
        }else if (value instanceof String){
            editor.putString(key, (String) value);
        }else if (value instanceof Set<?>){
            if ((((Set<?>)value).toArray())[0] instanceof String){
                editor.putStringSet(key, (Set<String>) value);
            }else{
                //如果以不是string集合的方式存储进SharedPreference则将其以"|"分割线的模式分割成
                //一个String子串存储，取时注意要以"|"分割成String集合进行对象的重组
                String putValue = "";
                Object[] objects = ((Set<?>)value).toArray();
                for (Object object : objects){
                    putValue += object.toString()+"|";
                }
                editor.putString(key, putValue);
            }
        }else{
            editor.putString(key, value.toString());
        }
    }

    public static <K>K getTemporary(String key, Class<K> clazz){

    }

    public static <K>ArrayList<K> getTemporarySet(String key, Class<K> clazz){

    }

    public static void setPermanent(String key, Object value){

    }

    public static <K>K getPermanent(String key, Class<K> clazz){

    }

    public static <K>ArrayList<K> getPermanentSet(String key, Class<K> clazz){

    }
}
