package com.android.sample.test_db.db;

import android.database.sqlite.SQLiteDatabase;

import com.android.libcore.database.BaseDB;
import com.android.libcore.database.IBaseDBTable;
import com.android.libcore.log.L;

import java.util.ArrayList;

/**
 * Description: 学生类数据库
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-23
 */
public class StudentDB extends BaseDB{
    public StudentDB(IBaseDBTable table, boolean writable) {
        super(table, writable);
    }

    @Override
    protected String getDBName() {
        return "student.db";
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
            sql = "create table if not exists "+ TABLES.STUDENTINFO.getTableName()+"_" + getDBVersion() + " (";
            sql += TABLES.STUDENTINFO.getTableColumns().get(0)+" integer not null primary key autoincrement, ";
            sql += TABLES.STUDENTINFO.getTableColumns().get(1)+" varchar(40) not null default 'unknown', ";
            sql += TABLES.STUDENTINFO.getTableColumns().get(2)+" varchar(10) not null default 'male',";
            sql += TABLES.STUDENTINFO.getTableColumns().get(3)+" integer not null default '60'";
            sql += ")";
            db.execSQL(sql);
            sql = "create table if not exists "+TABLES.STUDENTGRADE.getTableName()+"_" + getDBVersion() + " (";
            sql += TABLES.STUDENTGRADE.getTableColumns().get(0)+" integer not null primary key autoincrement, ";
            sql += TABLES.STUDENTGRADE.getTableColumns().get(1)+" integer not null default '1', ";
            sql += TABLES.STUDENTGRADE.getTableColumns().get(2)+" integer not null default '60'";
            sql += ")";
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            L.e(getClass().getSimpleName() + " sql语句错误", e);
        } finally {
            db.endTransaction();
        }
    }

    @Override
    protected void onDBUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (newVersion){
            case 2:
                L.e("我要升级到2");
                break;
            default:
                break;
        }
    }

    /**
     * 将该数据库中所有的表使用枚举封装
     */
    public enum TABLES implements IBaseDBTable {
        STUDENTINFO("studentInfo"){
            @Override
            public ArrayList<String> getTableColumns() {
                ArrayList<String> columns = new ArrayList<>();
                columns.add("id");//键，不用插入，自增
                columns.add("name");//姓名，String类型
                columns.add("gender");//性别，String类型
                columns.add("weight");//体重，int类型
                return columns;
            }
        },
        STUDENTGRADE("studentGrade"){
            @Override
            public ArrayList<String> getTableColumns() {
                ArrayList<String> columns = new ArrayList<>();
                columns.add("id");//键，不用插入，自增
                columns.add("class");//年级，int类型
                columns.add("grade");//分数,int类型
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
