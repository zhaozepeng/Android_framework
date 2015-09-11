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
     * 一次性插入大量数据
     */
    public void insertInfos(String url, ArrayList<FileDownloadManager.DownloadInfo> infos){
        ArrayList<String> columns = DownloadDB.TABLES.DOWNLOAD.getTableColumns();
        ArrayList<HashMap<String, String>> lists = new ArrayList<>();
        for (FileDownloadManager.DownloadInfo info : infos){
            HashMap<String, String> map = new HashMap<>();
            map.put(columns.get(1), url);
            map.put(columns.get(2), info.startPos+"");
            map.put(columns.get(3), info.endPos+"");
            map.put(columns.get(4), info.completeSize+"");
            lists.add(map);
        }
        insertAll(lists, true);
    }

    /**
     * 更新该线程下载的完成度
     */
    public void updateInfos(String url, ArrayList<FileDownloadManager.DownloadInfo> infos){
        ArrayList<String> columns = DownloadDB.TABLES.DOWNLOAD.getTableColumns();
        HashMap<String, String> maps = new HashMap<>();

        initUpdateDB();
        if (db == null)
            return;
        try {
            db.beginTransaction();
            for (FileDownloadManager.DownloadInfo info : infos){
                maps.clear();
                maps.put(columns.get(4), info.completeSize+"");
                String whereClause = columns.get(0)+"=? and "+columns.get(1)+"=?";
                String[] whereArgs = new String[]{info.id+"", url};
                db.update(maps, whereClause, whereArgs);
            }
            db.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
            db.close();
        }
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

    /**
     * 删除该url的相关下载信息
     */
    public void deleteInfos(String url){
        ArrayList<String> columns = DownloadDB.TABLES.DOWNLOAD.getTableColumns();
        String selection = columns.get(1)+"=?";
        String[] selectionArgs = new String[]{url};
        delete(selection, selectionArgs);
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
