package com.android.libcore.download;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.libcore.Toast.T;
import com.android.libcore.log.L;
import com.android.libcore.utils.FileUtils;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Description: 单个文件下载，支持断点续传
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-08-05
 */
public class FileDownloadManager {
    /** 下载一个文件所开启的线程数量 */
    private final int THREAD_NUM = 1;
    /** 下载一个文件的所有线程信息 */
    private ArrayList<DownloadInfo> infos;
    /** 下载一个文件的线程 */
    private ArrayList<DownloadThread> threads;
    /** 数据库操作对象 */
    private DownloadDBHelper helper;
    /** 该文件下载的url */
    private String url;
    /** 该文件下载路径，默认为SD卡file目录 */
    private String path = FileUtils.getExternalStorageFilePath();
    /** 该文件下载的文件大小 */
    private long fileSize = 0;
    /** 该文件下载的完成度 */
    private long completeSize = 0;
    /** 通知更新进度handler */
    private ProgressChangeHandler progressChangeHandler;
    /** 文件下载进度更新 */
    private IDownloadProgressChangedListener listener;
    /** 文件的下载状态 */
    private boolean downloadState = false;
    /** 文件是否下载完成 */
    private boolean isDownloadFinish = false;

    /**
     * @param url　文件下载url
     * @param fileName 文件名
     */
    public FileDownloadManager(String url, String fileName){
        this(url, fileName, null);
    }

    /**
     * @param url 文件下载url
     * @param fileName 文件名
     * @param path 文件下载路径，不需要带文件名
     */
    public FileDownloadManager(String url, String fileName, String path){
        this.url = url;
        if (path != null)
            this.path = path;
        if (!this.path.substring(this.path.length()-1).equals("/")){
            this.path += "/";
        }
        this.path += fileName;
        helper = new DownloadDBHelper();
        infos = new ArrayList<>();
        threads = new ArrayList<>();
        progressChangeHandler = new ProgressChangeHandler(this);
    }

    /**
     * 开启下载
     */
    public void start(){
        if (downloadState){
            T.getInstance().showShort("已经启动下载");
            return;
        }
        ArrayList<HashMap<String, String>> maps = helper.getInfo(url);
        if (isDownloadFinish || isFileDownloadFinish(maps)){
            isDownloadFinish = true;
            T.getInstance().showShort("文件已下载完成");
        }
        downloadState = true;

        //开启下载任务
        startDownload(maps);
    }

    /**
     * 停止下载
     */
    public void stop(){
        downloadState = false;
        threads.clear();
    }

    /**
     * 数据库中没有以前的下载信息，所以新建下载信息
     */
    private void startDownload(final ArrayList<HashMap<String, String>> maps){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (maps==null || maps.size()==0){
                    createDownloadInfos();
                }else{
                    revertDownloadInfos(maps);
                }

                //开启线程开始下载
                for (DownloadInfo info : infos){
                    DownloadThread thread = new DownloadThread(info);
                    threads.add(thread);
                    thread.start();
                }
                new UpdateThread().start();

            }
        }).start();
    }

    /**
     * 第一次下载文件，无下载记录，重新创建
     */
    private void createDownloadInfos(){
        try {
            URL url = new URL(FileDownloadManager.this.url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            fileSize = connection.getContentLength();
            File file = FileUtils.checkAndCreateFile(path);
            // 本地访问文件
            RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
            accessFile.setLength(fileSize);
            accessFile.close();
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //开始计算每个线程下载的字节范围
        long startPos = 0;
        //最后一个线程所下载的字节数一定要小于等于前面的线程保证文件完整性
        long perSize = (long) Math.ceil((fileSize*1.0) / (THREAD_NUM*1.0));
        for (int i=0; i<THREAD_NUM; i++){
            DownloadInfo info = new DownloadInfo();
            info.id = i;
            info.startPos = startPos;
            startPos += perSize;
            if (startPos >= fileSize)
                startPos = fileSize-1;
            info.endPos = startPos;
            info.completeSize = 0;
            //下一个任务的开始位置要＋１
            startPos ++;
            infos.add(info);
            helper.insertInfos(url, infos);
        }
    }

    /**
     * 恢复以前的下载信息
     */
    private void revertDownloadInfos(ArrayList<HashMap<String, String>> maps){
        ArrayList<String> columns = DownloadDB.TABLES.DOWNLOAD.getTableColumns();
        for (HashMap<String, String> map : maps){
            DownloadInfo info = new DownloadInfo();
            info.id = Integer.parseInt(map.get(columns.get(0)));
            info.startPos = Long.parseLong(map.get(columns.get(2)));
            info.endPos = Long.parseLong(map.get(columns.get(3)));
            info.completeSize = Long.parseLong(map.get(columns.get(4)));
            completeSize += info.completeSize;
            fileSize = fileSize>info.endPos ? fileSize:info.endPos;
            infos.add(info);
        }
    }

    /**
     * 获取该文件的总下载字节数
     */
    private long getCompleteSize(){
        completeSize = 0;
        for (DownloadThread thread : threads)
            completeSize += thread.getCompleteSize();
        return completeSize;
    }

    /**
     * 检测该文件是否下载完成
     */
    private boolean isFileDownloadFinish(ArrayList<HashMap<String, String>> maps){
        boolean result = true;
        if (maps==null || maps.size() == 0){
            return false;
        }
        ArrayList<String> columns = DownloadDB.TABLES.DOWNLOAD.getTableColumns();
        for (HashMap<String, String> map : maps){
            //如果完成字节数不足end-start，代表该线程未完成，所以需要继续下载
            if (Long.parseLong(map.get(columns.get(4))) <
                    (Long.parseLong(map.get(columns.get(3)))- Long.parseLong(map.get(columns.get(2))))){
                result = false;
                break;
            }
        }
        return result;
    }

    public void setListener(IDownloadProgressChangedListener listener){
        this.listener = listener;
    }

    /**
     * 下载进度更新接口
     */
    public interface IDownloadProgressChangedListener{
        void onProgressChanged(long completeSize, long totalSize);
    }

    private static class ProgressChangeHandler extends Handler{
        private WeakReference<FileDownloadManager> activityWeakReference;

        public ProgressChangeHandler(FileDownloadManager manager){
            activityWeakReference = new WeakReference<>(manager);
        }

        @Override
        public void handleMessage(Message msg) {
            if (activityWeakReference.get().getCompleteSize() >= activityWeakReference.get().fileSize) {
                activityWeakReference.get().downloadState = false;
                activityWeakReference.get().isDownloadFinish = true;
                T.getInstance().showShort("下载完成");
            }
            if (activityWeakReference.get().listener != null)
                activityWeakReference.get().listener.onProgressChanged
                        (activityWeakReference.get().getCompleteSize(), activityWeakReference.get().fileSize);
        }
    }

    /**
     * 单独一个线程下载的信息
     */
    public class DownloadInfo {
        public int id;
        public long startPos;
        public long endPos;
        public long completeSize;
    }

    /**
     * 下载线程
     */
    private class DownloadThread extends Thread{
        private DownloadInfo info;

        public DownloadThread(DownloadInfo info){
            this.info = info;
        }

        public long getCompleteSize(){
            return info.completeSize;
        }

        @Override
        public void run() {
            L.e("1");
            HttpURLConnection connection = null;
            RandomAccessFile randomAccessFile = null;
            InputStream is = null;
            try {
                L.e("2");
                URL url = new URL(FileDownloadManager.this.url);
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("GET");
                // 设置范围，格式为Range：bytes x-y;
                connection.setRequestProperty("Range", "bytes="+(info.startPos + info.completeSize) + "-" + info.endPos);

                L.e("3");
                randomAccessFile = new RandomAccessFile(path, "rwd");
                randomAccessFile.seek(info.startPos + info.completeSize);
                // 将要下载的字节写到上次写的末尾
                is = connection.getInputStream();
                byte[] buffer = new byte[1024 * 8];
                L.e("4");
                int length;
                L.e("5");
                while ((length = is.read(buffer)) != -1) {
                    L.e("正在下载");
                    randomAccessFile.write(buffer, 0, length);
                    info.completeSize += length;
                    if (!downloadState)
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                    randomAccessFile.close();
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 更新数据库和界面线程
     */
    private class UpdateThread extends Thread{
        @Override
        public void run() {
            try {
                while (downloadState) {
                    // 更新数据库中的下载信息
                    helper.updateInfos(url, infos);
                    //更新界面
                    progressChangeHandler.sendMessage(Message.obtain());
                    L.e("更新界面  "+getCompleteSize());
                    //每隔１秒操作数据库和更新界面，防止频繁的更新
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
