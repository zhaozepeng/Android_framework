package com.android.libcore.database;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Description: 所有数据库操作的操作封装类
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-21
 */
public abstract class BaseDBHelper {
    protected String table;

    protected abstract long insert(HashMap<String, String> map, boolean replace);

    protected abstract long delete(String selection, String[] selectionArgs);

    protected abstract long update(HashMap<String, String> maps, String whereClause, String[] whereArgs);

    protected abstract ArrayList<HashMap<String, String>> query(String selection, String[] selectionArgs,
                                                                String groupBy, String having, String orderBy, String limit);
}
