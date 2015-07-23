package com.android.sample.test_db.db;

import com.android.libcore.database.BaseDBHelper;
import com.android.libcore_ui.permanentdbcache.PermanentCacheDB;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Description: 学生数据库辅助类
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-23
 */
public class StudentHelper extends BaseDBHelper{
    private volatile static StudentHelper instance;

    public static StudentHelper getInstance(){
        if (instance == null){
            synchronized (StudentHelper.class){
                if (instance == null){
                    instance = new StudentHelper();
                }
            }
        }
        return instance;
    }

    private StudentHelper(){
    }

    /**
     * 插入学生信息
     */
    public boolean insertStudentInfo(String name, String gender, int weight){
        table = StudentDB.TABLES.STUDENTINFO;
        HashMap<String, String> map = new HashMap<>();
        map.put(table.getTableColumns().get(1), name);
        map.put(table.getTableColumns().get(2), gender);
        map.put(table.getTableColumns().get(3), weight + "");
        if (insert(map, false) > 0)
            return true;
        return false;
    }

    public boolean deleteStudentInfo(String name){
        table = StudentDB.TABLES.STUDENTINFO;
        String selection = table.getTableColumns().get(1) + "=?";
        String[] selectionArgs = new String[]{name};

        ArrayList<HashMap<String, String>> temp = query(selection, selectionArgs, null, null ,null, null);

        if (delete(selection, selectionArgs) > 0)
            return true;
        return false;
    }

    @Override
    protected long insert(HashMap<String, String> map, boolean replace) {
        db = new StudentDB(table, true);
        return super.insert(map, replace);
    }

    @Override
    protected long delete(String selection, String[] selectionArgs) {
        db = new StudentDB(table, true);
        return super.delete(selection, selectionArgs);
    }

    @Override
    protected long update(HashMap<String, String> maps, String whereClause, String[] whereArgs) {
        db = new StudentDB(table, true);
        return super.update(maps, whereClause, whereArgs);
    }

    @Override
    protected ArrayList<HashMap<String, String>> query(String selection, String[] selectionArgs, String groupBy, String having, String orderBy,
                                                       String limit) {
        db = new StudentDB(table, false);
        return super.query(selection, selectionArgs, groupBy, having, orderBy, limit);
    }
}
