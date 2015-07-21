package com.android.libcore_ui.dbcache;

import com.android.libcore.database.BaseDBHelper;

/**
 * Description: 用来封装{@link CacheDB}的相关存取操作，存储的值会永久的
 * 存储在数据库中
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-20
 */
public class CacheDBHelper extends BaseDBHelper{
    private CacheDB db = null;

    private CacheDBHelper(){
    }

    public static void set(String key, String value){
        //TODO 增加cache的操作
    }
}
