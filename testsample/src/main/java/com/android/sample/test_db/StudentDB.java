package com.android.sample.test_db;

import android.database.sqlite.SQLiteDatabase;

import com.android.libcore.database.BaseDB;
import com.android.libcore.database.IBaseDBTableEnum;

/**
 * Description: 学生类数据库
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-23
 */
public class StudentDB extends BaseDB{
    public StudentDB(IBaseDBTableEnum table, boolean writable) {
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
