package com.android.libcore.download;

import com.android.libcore.database.BaseDBHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Description: 下载数据库帮助类
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-08-05
 */
public class DownloadDBHelper extends BaseDBHelper{

    /**
     * 插入一个线程的下载信息
     */
    public void insertInfo(String url, long startPos, long endPos, long completeSize){
        ArrayList<String> columns = DownloadDB.TABLES.DOWNLOAD.getTableColumns();
        HashMap<String, String> map = new HashMap<>();
        map.put(columns.get(1), url);
        map.put(columns.get(2), startPos+"");
        map.put(columns.get(3), endPos+"");
        map.put(columns.get(4), completeSize+"");
        insert(map, false);
    }

    /**
     * 将该url的下载信息全部删除，代表该文件已经下载完成
     */
    public void deleteInfo(String url){
        ArrayList<String> columns = DownloadDB.TABLES.DOWNLOAD.getTableColumns();
        String selection = columns.get(1)+"=?";
        String[] selectionArgs  = new String[]{url};
        delete(selection, selectionArgs);
    }

    /**
     * 更新该线程下载的完成度
     */
    public void updateInfo(int id, String url, long completeSize){
        ArrayList<String> columns = DownloadDB.TABLES.DOWNLOAD.getTableColumns();
        HashMap<String, String> maps = new HashMap<>();
        maps.put(columns.get(4), completeSize+"");
        String whereClause = columns.get(0)+"=? and "+columns.get(1)+"=?";
        String[] whereArgs = new String[]{id+"", url};
        update(maps, whereClause, whereArgs);
    }

    /**
     *　获取该文件下载进度信息
     */
    public ArrayList<HashMap<String, String>> getInfo(String url){
        ArrayList<String> columns = DownloadDB.TABLES.DOWNLOAD.getTableColumns();
        String selection = columns.get(1)+"=?";
        String[] selectionArgs = new String[]{url};
        return query(selection, selectionArgs, null, null, null, null);
    }

    @Override
    protected void initInsertDB() {
        db = new DownloadDB(DownloadDB.TABLES.DOWNLOAD, true);
    }

    @Override
    protected void initDeleteDB() {
        db = new DownloadDB(DownloadDB.TABLES.DOWNLOAD, true);
    }

    @Override
    protected void initUpdateDB() {
        db = new DownloadDB(DownloadDB.TABLES.DOWNLOAD, true);
    }

    @Override
    protected void initQueryDB() {
        db = new DownloadDB(DownloadDB.TABLES.DOWNLOAD, false);
    }
}
