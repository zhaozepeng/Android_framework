package com.android.libcore_ui.dbcache;

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
    private PermanentCacheDB db = null;
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
        table = "cache";
    }

    /**
     * 设置键值
     */
    public void set(String key, String value){

    }

    @Override
    protected long insert(HashMap<String, String> map, boolean replace) {
        long count = -1;
        try {
            db = new PermanentCacheDB(table, true);
            db.beginTransaction();
            count = db.insert(map, replace);
            db.setTransactionSuccessful();
        }catch (Exception e){
        }finally {
            db.endTransaction();
            db.close();
        }
        return count;
    }

    @Override
    protected long delete(String selection, String[] selectionArgs) {
        long count = -1;
        try {
            db = new PermanentCacheDB(table, true);
            db.beginTransaction();
            count = db.delete(selection, selectionArgs);
            db.setTransactionSuccessful();
        }catch (Exception e){
        }finally {
            db.endTransaction();
            db.close();
        }
        return count;
    }

    @Override
    protected long update(HashMap<String, String> maps, String whereClause, String[] whereArgs) {
        long count = -1;
        try {
            db = new PermanentCacheDB(table, true);
            db.beginTransaction();
            count = db.update(maps, whereClause, whereArgs);
            db.setTransactionSuccessful();
        }catch (Exception e){
        }finally {
            db.endTransaction();
            db.close();
        }
        return count;
    }

    @Override
    protected ArrayList<HashMap<String, String>> query(String selection, String[] selectionArgs, String groupBy, String having, String orderBy,
                                                       String limit) {
        ArrayList<HashMap<String, String>> result = null;
        try {
            db = new PermanentCacheDB(table, true);
            db.beginTransaction();
            result = db.query(selection, selectionArgs, groupBy, having, orderBy, limit);
            db.setTransactionSuccessful();
        }catch (Exception e){
        }finally {
            db.endTransaction();
            db.close();
        }
        return result;
    }
}
