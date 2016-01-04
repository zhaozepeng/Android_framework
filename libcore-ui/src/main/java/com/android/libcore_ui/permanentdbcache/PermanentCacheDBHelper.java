package com.android.libcore_ui.permanentdbcache;

import com.android.libcore.database.BaseDBHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Description: 用来封装{@link PermanentCacheDB}的相关存取操作，存储的值会<strong>永久</strong>的
 * 存储在数据库中，所以该db只能用来存放和应用周期相关的信息
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-20
 */
public class PermanentCacheDBHelper extends BaseDBHelper{
    private volatile static PermanentCacheDBHelper instance;

    public static PermanentCacheDBHelper getInstance(){
        if (instance == null){
            synchronized (PermanentCacheDBHelper.class){
                if (instance == null){
                    instance = new PermanentCacheDBHelper();
                }
            }
        }
        return instance;
    }

    private PermanentCacheDBHelper(){
        mTable = PermanentCacheDB.TABLES.CACHE;
    }

    /**
     * 设置键值，操作少，所以在UI线程
     * @return 删除成功返回true
     */
    public boolean set(String key, String value){
        ArrayList<String> columns = mTable.getTableColumns();
        HashMap<String, String> map = new HashMap<>();
        map.put(columns.get(0), key);
        map.put(columns.get(1), value);
        return insert(map, true) > 0;
    }

    /**
     * 删除键，操作少，所以在UI线程
     * @return 删除成功返回true
     */
    public boolean del(String key){
        String selection = mTable.getTableColumns().get(0)+"=?";
        String[] selectionArgs = new String[]{key};
        return delete(selection, selectionArgs) > 0;
    }

    /**
     * 清空该表的所有数据
     */
    public boolean clear(){
        return delete("1=1", null) > 0;
    }

    /**
     * 根据键获取值，操作少，所以在UI线程
     * @return value
     */
    public String get(String key){
        String selection = mTable.getTableColumns().get(0)+"=?";
        String[] selectionArgs = new String[]{key};
        ArrayList<HashMap<String, String>> result = query(selection, selectionArgs, null, null, null, null);
        if (result != null){
            return result.get(0).get(mTable.getTableColumns().get(1));
        }
        return null;
    }

    @Override
    protected void initInsertDB() {
        mDb = new PermanentCacheDB(mTable, true);
    }

    @Override
    protected void initDeleteDB() {
        mDb = new PermanentCacheDB(mTable, true);
    }

    @Override
    protected void initUpdateDB() {
        mDb = null;
    }

    @Override
    protected void initQueryDB() {
        mDb = new PermanentCacheDB(mTable, false);
    }
}
