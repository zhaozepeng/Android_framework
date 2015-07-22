package com.android.libcore.database;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Description: 所有数据库操作的操作封装类，其他类只能通过helper的子类进行数据库的
 * 操作，不能直接操作数据库，每一个数据库对应一个数据库helper，这样就能够保证数据库访问
 * 的统一性，以便以后的数据库修改
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-21
 */
public abstract class BaseDBHelper {
    protected String table;
    protected BaseDB db;

    protected long insert(HashMap<String, String> map, boolean replace){
        if (db == null)
            return 0;
        long count = -1;
        try {
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

    protected long delete(String selection, String[] selectionArgs){
        if (db == null)
            return 0;
        long count = -1;
        try {
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

    protected long update(HashMap<String, String> maps, String whereClause, String[] whereArgs){
        if (db == null)
            return 0;
        long count = -1;
        try {
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

    protected ArrayList<HashMap<String, String>> query(String selection, String[] selectionArgs,
                                                                String groupBy, String having, String orderBy, String limit){
        if (db == null)
            return null;
        ArrayList<HashMap<String, String>> result = null;
        try {
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
