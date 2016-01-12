package com.android.libcore.download;

import android.database.sqlite.SQLiteDatabase;

import com.android.libcore.database.BaseDB;
import com.android.libcore.database.IBaseDBTable;
import com.android.libcore.log.L;

import java.util.ArrayList;

/**
 * Description: 保存下载信息的数据库
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-08-05
 */
public class DownloadDB extends BaseDB{

    public DownloadDB(IBaseDBTable table, boolean writable) {
        super(table, writable);
    }

    @Override
    protected String getDBName() {
        return "download.db";
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
            sql = "create table if not exists "+TABLES.DOWNLOAD.getTableName()+"_" + getDBVersion() + " (";
            sql += TABLES.DOWNLOAD.getTableColumns().get(0)+" integer not null, ";
            sql += TABLES.DOWNLOAD.getTableColumns().get(1)+" varchar(4000) not null default '', ";
            sql += TABLES.DOWNLOAD.getTableColumns().get(2)+" varchar(50) not null default '0', ";
            sql += TABLES.DOWNLOAD.getTableColumns().get(3)+" varchar(50) not null default '0', ";
            sql += TABLES.DOWNLOAD.getTableColumns().get(4)+" varchar(50) not null default '0'";
            sql += ")";
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            L.e(getClass().getSimpleName()+" sql语句错误", e);
        } finally {
            db.endTransaction();
        }
    }

    @Override
    protected void onDBUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 将该数据库中所有的表使用枚举封装
     */
    public enum TABLES implements IBaseDBTable {
        DOWNLOAD("download"){
            @Override
            public ArrayList<String> getTableColumns() {
                ArrayList<String> columns = new ArrayList<>();
                columns.add("id");//id
                columns.add("url");//下载url
                columns.add("start_pos");//该线程开始位置
                columns.add("end_pos");//该线程结束位置
                columns.add("complete_size");//线程当前完成的数
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
