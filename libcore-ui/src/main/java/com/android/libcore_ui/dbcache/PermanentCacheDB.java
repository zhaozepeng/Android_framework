package com.android.libcore_ui.dbcache;

import android.database.sqlite.SQLiteDatabase;

import com.android.libcore.database.BaseDB;

/**
 * Description: 缓存{@link PermanentCacheDBHelper}的数据库表
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-20
 */
public class PermanentCacheDB extends BaseDB{

    public PermanentCacheDB(String table, boolean writable) {
        super(table, writable);
    }

    @Override
    protected String getDBName() {
        return "cache";
    }

    @Override
    protected int getDBVersion() {
        return 1;
    }

    @Override
    protected void onDBCreate(SQLiteDatabase db) {
        try {
            db.beginTransaction();
            String sql;
            sql = "create table cache_" + getDBVersion() + " (";
            sql += "key varchar(40) not null primary key default '', ";
            sql += "value varchar(4000) not null default ''";
            sql += ")";
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();

        }
    }

    @Override
    protected void onDBUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 1:
                break;
            default:
                break;
        }
    }
}
