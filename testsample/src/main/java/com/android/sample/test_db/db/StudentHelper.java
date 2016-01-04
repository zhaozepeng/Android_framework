package com.android.sample.test_db.db;

import com.android.libcore.database.BaseDBHelper;

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
     * 数据库一个表的数据如果很多，也可以封装成一个实体类
     */
    public static class StudentInfo{
        public String name;
        public String gender;
        public double weight;
        //插入时该值无效
        public int id;
    }

    /**
     * 插入学生信息
     */
    public boolean insertStudentInfo(String name, String gender, int weight){
        mTable = StudentDB.TABLES.STUDENTINFO;
        HashMap<String, String> map = new HashMap<>();
        map.put(mTable.getTableColumns().get(1), name);
        map.put(mTable.getTableColumns().get(2), gender);
        map.put(mTable.getTableColumns().get(3), weight + "");
        if (insert(map, false) > 0)
            return true;
        return false;
    }

    public long insertStudentInfos(ArrayList<StudentInfo> infos){
        mTable = StudentDB.TABLES.STUDENTINFO;
        ArrayList<HashMap<String, String>> maps = new ArrayList<>();
        for (StudentInfo info : infos) {
            HashMap<String, String> map = new HashMap<>();
            map.put(mTable.getTableColumns().get(1), info.name);
            map.put(mTable.getTableColumns().get(2), info.gender);
            map.put(mTable.getTableColumns().get(3), info.weight + "");
            maps.add(map);
        }
        return insertAll(maps, false);
    }

    public boolean deleteStudentInfo(String name){
        mTable = StudentDB.TABLES.STUDENTINFO;
        String selection = mTable.getTableColumns().get(1) + "=?";
        String[] selectionArgs = new String[]{name};

        ArrayList<HashMap<String, String>> temp = query(selection, selectionArgs, null, null ,null, null);

        if (delete(selection, selectionArgs) > 0)
            return true;
        return false;
    }

    public boolean insertGrade(int Class, int grade){
        mTable = StudentDB.TABLES.STUDENTGRADE;
        HashMap<String, String> map = new HashMap<>();
        map.put(mTable.getTableColumns().get(1), Class+"");
        map.put(mTable.getTableColumns().get(2), grade+"");
        if (insert(map, false) > 0)
            return true;
        return false;
    }

    public ArrayList<StudentInfo> getStudentInfo(String name){
        mTable = StudentDB.TABLES.STUDENTINFO;
        String selection = mTable.getTableColumns().get(1)+"=?";
        String[] selectionArgs = new String[]{name};
        ArrayList<HashMap<String, String>> result = query(selection, selectionArgs, null, null, null, null);
        ArrayList<StudentInfo> studentInfos = null;
        if (result != null){
            studentInfos = new ArrayList<>();
            for (HashMap<String, String> temp : result){
                StudentInfo info = new StudentInfo();
                info.id = Integer.parseInt(temp.get(mTable.getTableColumns().get(0)));
                info.name = temp.get(mTable.getTableColumns().get(1));
                info.gender = temp.get(mTable.getTableColumns().get(2));
                info.weight = Double.parseDouble(temp.get(mTable.getTableColumns().get(3)));
                studentInfos.add(info);
            }
        }
        return studentInfos;
    }

    @Override
    protected void initInsertDB() {
        mDb = new StudentDB(mTable, true);
    }

    @Override
    protected void initDeleteDB() {
        mDb = new StudentDB(mTable, true);
    }

    @Override
    protected void initUpdateDB() {
        mDb = new StudentDB(mTable, true);
    }

    @Override
    protected void initQueryDB() {
        mDb = new StudentDB(mTable, false);
    }
}
