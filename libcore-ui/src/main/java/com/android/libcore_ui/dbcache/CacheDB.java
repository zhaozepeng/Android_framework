package com.android.libcore_ui.dbcache;

import android.database.sqlite.SQLiteDatabase;

import com.android.libcore.database.BaseDB;

/**
 * Description: 缓存{@link CacheDBHelper}的数据库表
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-20
 */
public class CacheDB extends BaseDB{

    public CacheDB(String table, boolean writable) {
        super(table, writable);
    }

    @Override
    protected String getDBName() {
        return null;
    }

    @Override
    protected int getDBVersion() {
        return 0;
    }

    @Override
    protected void onDBCreate(SQLiteDatabase db) {

    }

    @Override
    protected void onDBUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
