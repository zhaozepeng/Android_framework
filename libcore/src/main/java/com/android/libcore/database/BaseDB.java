package com.android.libcore.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.libcore.application.RootApplication;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Description: 基本数据库类，所有的数据库类都应该继承自该类<br/>
 * 应该注意的地方是：如果新版本修改了数据库，请在增加version版本号，并且在
 * {@link #onDBUpgrade(SQLiteDatabase, int, int)}函数里增加相应的版本升级工作，
 * 如果新版本的数据库与旧版本的数据库不兼容（比如用户卸载新版本，安装旧版本，数据未清除），
 * 请在{@link #onDBDowngrade(SQLiteDatabase, int, int)}里面进行版本的降级工作<br/>
 *
 * <strong>注意：表的创建请在名字后面加上版本，如版本为1的cache表：cache_1</strong>
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-20
 */
public abstract class BaseDB {

    /** 数据库锁 */
    private Byte[] lock = new Byte[0];

    /**
     * 获取数据库的名字
     */
    protected abstract String getDBName();

    /**
     * 获取数据库的版本
     */
    protected abstract int getDBVersion();

    /**
     * 数据库的创建
     */
    protected abstract void onDBCreate(SQLiteDatabase db);

    /**
     * 如果应用程序版本号大于本地版本库，会产生升级动作
     */
    protected abstract void onDBUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

    /**
     * 如果应用程序版本号小于本地版本库，会产生降级动作（如用户安装了以前版本的应用包）
     */
    protected abstract void onDBDowngrade(SQLiteDatabase db, int oldVersion, int newVersion);

    /**
     * 数据库封装类
     */
    private class DataBaseHelper extends SQLiteOpenHelper{

        public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(RootApplication.getInstance(), getDBName(), null, getDBVersion());
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            onDBCreate(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion < newVersion){
                removeNewTables(db, oldVersion+1);
            }
            onDBUpgrade(db, oldVersion, newVersion);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onDBDowngrade(db, oldVersion, newVersion);
        }

        /**
         * 获取该数据库中的所有表
         */
        private ArrayList<String> getTables(SQLiteDatabase db){
            ArrayList<String> tables = new ArrayList<>();
            Cursor cursor = db.rawQuery("select * from sqlite_master where type = 'table'", null);
            while (cursor.moveToNext()){
                String[] cols = cursor.getColumnNames();
                for (int i = 0; i < cols.length; i++) {
                    if (cols[i].equals("tbl_name")){
                        String name = cursor.getString(i);
                        //排除sqlite_master表
                        if (name.length() < 7 || (!name.substring(0, 7).equals("sqlite_"))) {
                            tables.add(name);
                        }
                    }
                }
            }
            return tables;
        }

        /**
         * 删除所有异常新版本库，保证当前数据的表都为旧版本
         * @param version 需要删除的版本
         */
        private void removeNewTables(SQLiteDatabase db, int version){
            ArrayList<String> tables = getTables(db);
            String suffix = "_"+version;
            for (String table : tables){
                if (table.substring(table.length()-suffix.length()).equals(suffix)){

                }
            }
        }
    }
}
