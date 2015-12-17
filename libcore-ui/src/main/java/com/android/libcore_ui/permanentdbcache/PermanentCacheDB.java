package com.android.libcore_ui.permanentdbcache;

import android.database.sqlite.SQLiteDatabase;

import com.android.libcore.database.BaseDB;
import com.android.libcore.database.IBaseDBTable;
import com.android.libcore.log.L;

import java.util.ArrayList;

/**
 * Description: 缓存{@link PermanentCacheDBHelper}的数据库表
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-20
 */
public class PermanentCacheDB extends BaseDB{

    public PermanentCacheDB(IBaseDBTable table, boolean writable) {
        super(table, writable);
    }

    @Override
    protected String getDBName() {
        return "permanentCache.db";
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
            sql = "create table if not exists "+ TABLES.CACHE.getTableName() +"_" + getDBVersion() + " (";
            sql += TABLES.CACHE.getTableColumns().get(0)+" varchar(40) not null primary key default '', ";
            sql += TABLES.CACHE.getTableColumns().get(1)+" varchar(4000) not null default ''";
            sql += ")";
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            L.e("sql语句错误", e);
        } finally {
            db.endTransaction();
        }
    }

    @Override
    protected void onDBUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (newVersion){
            case 2:
                break;
            default:
                break;
        }
    }

    /**
     * 将该数据库中所有的表使用枚举封装
     */
    public enum TABLES implements IBaseDBTable {
        CACHE("cache"){
            @Override
            public ArrayList<String> getTableColumns() {
                ArrayList<String> columns = new ArrayList<>();
                columns.add("key");//键
                columns.add("value");//值
                return columns;
            }
        };
        private String table_name;
        TABLES(String table_name){
            this.table_name = table_name;
        }

        @Override
        public String getTableName() {
            return table_name;
        }

        @Override
        public ArrayList<String> getTableColumns() {
            return null;
        }
    }
}
