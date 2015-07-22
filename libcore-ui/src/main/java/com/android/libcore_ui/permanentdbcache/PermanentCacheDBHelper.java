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
    private PermanentCacheDB.TABLES table;

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
        table = PermanentCacheDB.TABLES.CACHE;
    }

    /**
     * 设置键值，操作少，所以在UI线程
     * @return 删除成功返回true
     */
    public boolean set(String key, String value){
        ArrayList<String> columns = table.getTableColumns();
        HashMap<String, String> map = new HashMap<>();
        map.put(columns.get(0), key);
        map.put(columns.get(1), value);
        if (insert(map, true) > 0)
            return true;
        return false;
    }

    /**
     * 删除键，操作少，所以在UI线程
     * @return 删除成功返回true
     */
    public boolean del(String key){
        String selection = table.getTableColumns().get(0)+"=?";
        String[] selectionArgs = new String[]{key};
        if (delete(selection, selectionArgs) > 0)
            return true;
        return false;
    }

    /**
     * 清空该表的所有数据
     */
    public boolean clear(){
        if (delete("1=1", null) > 0)
            return true;
        return false;
    }

    /**
     * 根据键获取值，操作少，所以在UI线程
     * @return value
     */
    public String get(String key){
        String selection = table.getTableColumns().get(0)+"=?";
        String[] selectionArgs = new String[]{key};
        ArrayList<HashMap<String, String>> result = query(selection, selectionArgs, null, null, null, null);
        if (result != null){
            return result.get(0).get(table.getTableColumns().get(1));
        }
        return null;
    }

    @Override
    protected long insert(HashMap<String, String> map, boolean replace) {
        db = new PermanentCacheDB(table, true);
        return super.insert(map, replace);
    }

    @Override
    protected long delete(String selection, String[] selectionArgs) {
        db = new PermanentCacheDB(table, true);
        return super.delete(selection, selectionArgs);
    }

    @Override
    protected long update(HashMap<String, String> maps, String whereClause, String[] whereArgs) {
        return -1;
    }

    @Override
    protected ArrayList<HashMap<String, String>> query(String selection, String[] selectionArgs, String groupBy, String having, String orderBy,
                                                       String limit) {
        db = new PermanentCacheDB(table, false);
        return super.query(selection, selectionArgs, groupBy, having, orderBy, limit);
    }
}
