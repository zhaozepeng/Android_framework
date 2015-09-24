package com.android.libcore.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.libcore.application.RootApplication;
import com.android.libcore.log.L;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Description: 基本数据库类，所有的数据库类都应该继承自该类<br/>
 * 应该注意的地方是：如果新版本修改了数据库，请在增加version版本号，并且在
 * {@link #onDBUpgrade(SQLiteDatabase, int, int)}函数里增加相应的版本升级工作<br/>
 *
 * 每次数据库的操作请加上事务，开始操作时请调用{@link #beginTransaction()}，如果操作成功调用
 * {@link #setTransactionSuccessful()}，操作完成调用{@link #endTransaction()}，还有一定
 * 要记住最后调用{@link #close()}方法关闭数据库，使用try-catch-finally结构操作<br/>
 *
 * <strong>注意：表的创建请在名字后面加上版本，如版本为1的cache表名为<em>cache_1</em></strong>
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-20
 */
public abstract class BaseDB {

    /** 数据库锁 */
    private Byte[] lock = new Byte[0];
    /** 打开数据库超时时间 */
    private final int TIME_OUT = 30*1000;
    /** 当前操作的表 */
    private String table;

    private DataBaseHelper helper;
    protected SQLiteDatabase db;

    public BaseDB(IBaseDBTable table, boolean writable){
        this.table = table.getTableName()+"_"+getDBVersion();
        helper = new DataBaseHelper();
        if (writable)
            db = helper.getWritableDatabase();
        else
            db = helper.getReadableDatabase();
    }

    /**
     * 获取数据库的名字
     */
    protected abstract String getDBName();

    /**
     * 获取数据库的版本，注意该版本只增不减
     */
    protected abstract int getDBVersion();

    /**
     * 数据库的创建
     */
    protected abstract void onDBCreate(SQLiteDatabase db);

    /**
     * 如果应用程序版本号大于本地版本库，会产生升级动作，该函数只需要处理数据库表的变更即可，不需要进行名字的更改，
     * 注意该函数使用的是逐步升级版本的操作，比如本地数据库版本为1，升级到3版本，会迭代先升级到2版本，再从2版本升
     * 级到3版本，<strong>升级时的版本号仍然为旧版本号</strong>，所以该函数要详细写清楚每个版本之间的差异，
     * 并且处理每个版本之间的升级任务，该升级操作会在数据库的第一个操作检测到数据库版本不一致时进行升级
     */
    protected abstract void onDBUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

    /**
     * 开始事务
     */
    public void beginTransaction(){
        if (db != null && !db.inTransaction()){
            try {
                db.beginTransaction();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 事务成功
     */
    public void setTransactionSuccessful(){
        if (db != null && db.inTransaction()){
            try {
                db.setTransactionSuccessful();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 结束事务
     */
    public void endTransaction(){
        if (db != null && db.inTransaction()){
            try {
                db.endTransaction();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭数据库
     */
    public void close(){
        if (db != null && db.isOpen())
            db.close();
    }

    /**
     * 获取表中所有的条目
     */
    public int getCount(){
        return getCount(null, null);
    }

    /**
     * 获取表中符合该条件的表的条目数
     */
    public int getCount(String selection, String[] selectionArgs){
        int count = 0;
        Cursor cursor = null;
        try {
            cursor = db.query(table, new String[]{"count(*)"}, selection, selectionArgs, null, null, null);
            if (cursor.moveToNext()){
                count = cursor.getInt(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null){
                try {
                    cursor.close();
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            }
        }
        return count;
    }

    /**
     * 增
     */
    public long insert(HashMap<String, String> map, boolean replace){
        getCount();
        long count = 0;
        synchronized (lock){
            try {
                if (!replace)
                    count = db.insert(table, null, parseHashMapToContentValues(map));
                else
                    count = db.replace(table, null, parseHashMapToContentValues(map));
            }catch (Exception e){
                e.printStackTrace();
                count = -1;
            }
        }
        return count;
    }

    /**
     * 删
     */
    public long delete(String selection, String[] selectionArgs){
        long count = 0;
        synchronized (lock){
            try {
                count = db.delete(table, selection, selectionArgs);
            }catch (Exception e){
                e.printStackTrace();
                count = -1;
            }
        }
        return count;
    }

    /**
     * 改<br/>
     * 使用范例：如果需要将带"shangh"开头的城市的人数字段都变成0，函数就可以这么调用:
     * update(hashmap({"people", "0"}), "city like ?", "new String[]{"shangh"}")
     */
    public long update(HashMap<String, String> maps, String whereClause, String[] whereArgs){
        long count = 0;
        synchronized (lock) {
            try {
                count = db.update(table, parseHashMapToContentValues(maps), whereClause, whereArgs);
            }catch (Exception e){
                e.printStackTrace();
                count = -1;
            }
        }
        return count;
    }

    /**
     * 查
     */
    public ArrayList<HashMap<String, String>> query(){
       return query(null, null);
    }

    /**
     * 查
     */
    public ArrayList<HashMap<String, String>> query(String selection, String[] selectionArgs){
        return query(selection, selectionArgs, null, null, null, null);
    }

    /**
     * 查<br/>
     * 使用范例:query("city like ? or city=?", new String[]{"shangh", "beijing"}, "district", "sum(people)>10", "GDP", "1000")，
     * 作用为查找城市中像"shangh"和等于beijing的城市，并且按照district排序，统计所有区总人数大于10万，并且区之间按照GDP排序，显示前1000条信息，该函数的
     * 使用与正常的sql语句一样
     */
    public ArrayList<HashMap<String, String>> query(String selection, String[] selectionArgs,
                                                    String groupBy, String having, String orderBy, String limit){
        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        synchronized (lock){
            try {
                Cursor cursor = db.query(table, null, selection, selectionArgs, groupBy, having, orderBy, limit);
                int length = cursor.getColumnCount();
                while (cursor.moveToNext()){
                    HashMap<String, String> value = new HashMap<>();
                    for (int i=0; i<length; i++){
                        value.put(cursor.getColumnName(i), cursor.getString(i));
                    }
                    result.add(value);
                }
                if (result.size() == 0)
                    result = null;
            }catch (Exception e){
                e.printStackTrace();
                result = null;
            }
        }
        return result;
    }

    private ContentValues parseHashMapToContentValues(HashMap<String, String> map){
        ContentValues values = new ContentValues();
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, String> entry = iterator.next();
            values.put(entry.getKey(), entry.getValue());
        }
        return values;
    }

    /**
     * 数据库封装类
     */
    private class DataBaseHelper extends SQLiteOpenHelper{

        public DataBaseHelper() {
            super(RootApplication.getInstance(), getDBName(), null, getDBVersion());
        }

        @Override
        public SQLiteDatabase getReadableDatabase() {
            SQLiteDatabase db = null;
            synchronized (lock){
                boolean retry = false;
                long time = System.currentTimeMillis();
                do {
                    retry = false;
                    try {
                        db = super.getReadableDatabase();
                    }catch (Exception e){
                        L.e(BaseDB.class.getSimpleName(), e);
                        retry = true;
                        //休眠一个随机时间防止线程并发
                        try {
                            Thread.sleep((long) (3*1000+Math.random()*1000));
                        }catch (InterruptedException e1){
                            L.e(BaseDB.class.getSimpleName(), e1);
                        }
                    }
                }while (retry && ((System.currentTimeMillis()-time)<TIME_OUT));
            }
            return db;
        }

        @Override
        public SQLiteDatabase getWritableDatabase() {
            SQLiteDatabase db = null;
            synchronized (lock){
                boolean retry = false;
                long time = System.currentTimeMillis();
                do {
                    retry = false;
                    try {
                        db = super.getWritableDatabase();
                    }catch (Exception e){
                        L.e(BaseDB.class.getSimpleName(), e);
                        retry = true;
                        //休眠一个随机时间防止线程并发
                        try {
                            Thread.sleep((long) (3*1000+Math.random()*1000));
                        }catch (InterruptedException e1){
                            L.e(BaseDB.class.getSimpleName(), e1);
                        }
                    }
                }while (retry && ((System.currentTimeMillis()-time)<TIME_OUT));
            }
            return db;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            onDBCreate(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion < newVersion){
                //删除掉数据库中的异常新版本，防止升级出错
                removeNewTables(db, oldVersion+1);
                //升级版本
                onDBUpgrade(db, oldVersion, oldVersion+1);
                //等待升级任务完成之后再去修改版本号
                renameOldTables(db, oldVersion, oldVersion+1);
                oldVersion ++;
                //递归的版本升级
                onUpgrade(db, oldVersion, newVersion);
                //最后应该将数据库中的旧表清除
                if (oldVersion == newVersion)
                    clearOldTables(db, newVersion);
            }
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
                    db.rawQuery("drop table if exists " + table, null);
                }
            }
        }

        /**
         * 将旧版本的表名后面的版本号修改成新版本号
         */
        private void renameOldTables(SQLiteDatabase db, int oldVersion, int newVersion){
            String oldSuffix = "_"+oldVersion;
            String newSuffix = "_"+newVersion;
            ArrayList<String> tables = getTables(db);
            for (String table : tables){
                if (table.substring(table.length()-oldSuffix.length()).equals(oldSuffix)){
                    db.execSQL("alter table " + table + " rename to " + table.replace(oldSuffix, newSuffix));
                }
            }
        }

        /**
         * 清除所有的旧表
         */
        private void clearOldTables(SQLiteDatabase db, int newVersion){
            ArrayList<String> tables = getTables(db);
            String suffix = "_"+newVersion;
            for (String table : tables){
                //如果数据库中存在不是新版本的表，删除
                if (!table.substring(table.length()-suffix.length()).equals(suffix)){
                    db.rawQuery("drop table if exists " + table, null);
                }
            }
        }
    }
}
