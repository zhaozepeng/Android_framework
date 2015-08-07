package com.android.libcore.download;

import android.os.Handler;
import android.os.Message;

import com.android.libcore.Toast.T;
import com.android.libcore.log.L;
import com.android.libcore.utils.FileUtils;

import org.apache.http.HttpResponse;

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

    /** 下载状态，正在获取文件大小 */
    public static final int STATE_GETSIZE = 1;
    /** 下载状态，开始下载 */
    public static final int STATE_STARTING = 2;
    /** 下载状态，正在停止 */
    public static final int STATE_STOPING = 3;
    /** 下载状态，停止成功 */
    public static final int STATE_STOPED = 4;
    /** 下载状态，下载完成 */
    public static final int STATE_FINISH = 5;

    /** 当前文件的下载状态，默认为停止成功，即为下载完成，且随时可以开始下载 */
    private static int currentState = STATE_STOPED;

    /** 下载一个文件所开启的线程数量 */
    private final int THREAD_NUM = 4;
    /** 下载一个文件的所有线程信息 */
    private ArrayList<DownloadInfo> infos;
    /** 开始下载线程 */
    private Thread startDownloadThread;
    /** 结束下载线程，用来检测下载线程的完成程度，更新状态 */
    private Thread stopDownloadThread;
    /** 下载一个文件的线程队列 */
    private ArrayList<DownloadThread> threads;
    /** 更新下载信息，更新界面线程 */
    private UpdateThread updateThread;
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
        checkFileFinish();
    }

    /**
     * 检测该文件是否已经下载完成
     */
    public boolean checkFileFinish(){
        if (isDownloadFinish || isFileDownloadFinish(helper.getInfo(url))){
            isDownloadFinish = true;
            progressChangeHandler.sendEmptyMessage(STATE_FINISH);
            return true;
        }
        return false;
    }

    /**
     * 开启下载
     */
    public void start(){
        if (checkFileFinish()){
            T.getInstance().showShort("文件已下载完成");
            return;
        }

        if (downloadState){
            T.getInstance().showShort("已经启动下载");
            return;
        }

        if (currentState == STATE_STOPING){
            T.getInstance().showShort("文件还未停止成功");
            return;
        }

        downloadState = true;

        //开启下载任务
        startDownload(helper.getInfo(url));
    }

    /**
     * 停止下载
     */
    public void stop(){
        downloadState = false;
        //停止更新界面线程，一定要保证最多只有一个更新线程在执行
        if (updateThread != null && updateThread.isAlive())
            updateThread.canRun = false;

        if (checkFileFinish()){
            T.getInstance().showShort("文件已下载完成");
            return;
        }

        if (currentState == STATE_STOPING){
            T.getInstance().showShort("正在停止，稍后...");
            return;
        }

        if (currentState == STATE_STOPED){
            T.getInstance().showShort("已停止");
            return;
        }

        stopDownload();
    }

    private void startDownload(final ArrayList<HashMap<String, String>> maps){
        startDownloadThread = new Thread(new Runnable() {
            @Override
            public void run() {
                //如果没有下载信息，则需要创建
                if (infos==null || infos.size()==0) {
                    if (maps == null || maps.size() == 0) {
                        createDownloadInfos();
                    } else {
                        revertDownloadInfos(maps);
                    }
                }

                //更新文件状态为正在下载
                progressChangeHandler.sendEmptyMessage(STATE_STARTING);

                //上次的线程完成之后才能开启新的下载线程开始下载
                threads.clear();
                for (DownloadInfo info : infos){
                    DownloadThread thread = new DownloadThread(info);
                    threads.add(thread);
                }
                L.e("准备开启下载线程");

                progressChangeHandler.sendEmptyMessage(-1);
            }
        });
        startDownloadThread.start();
    }

    private void stopDownload(){
        stopDownloadThread = new Thread(new Runnable() {
            @Override
            public void run() {
                //如果开始线程还未停止，比如用户摁完开始下载之后快速摁停止下载，
                // 这时状态更新为正在停止下载，直到开始线程完成
                boolean state = (startDownloadThread!=null&&startDownloadThread.isAlive());
                while (state){
                    L.e("开始线程还未结束");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progressChangeHandler.sendEmptyMessage(STATE_STOPING);
                    state = (startDownloadThread!=null&&startDownloadThread.isAlive());
                }

                //接着开始检测下载线程，确保下载线程要全部执行完成
                state = threads.size()>0;
                while(state){
                    state = false;
                    for (DownloadThread thread : threads){
                        if (thread.isAlive()){
                            state = true;
                            break;
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    L.e("下载线程还未结束");
                    //还有线程在执行，所以状态还为正在停止中
                    progressChangeHandler.sendEmptyMessage(STATE_STOPING);
                }

                //确保开始线程和下载线程都已经执行完成之后才能将状态修改为停止成功
                progressChangeHandler.sendEmptyMessage(STATE_STOPED);
            }
        });
        stopDownloadThread.start();
    }

    /**
     * 第一次下载文件，无下载记录，重新创建
     */
    private void createDownloadInfos(){
        try {
            //更新状态为正在获取文件大小
            progressChangeHandler.sendEmptyMessage(STATE_GETSIZE);
            URL url = new URL(FileDownloadManager.this.url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("GET");
            //添加这句话，要不然disconnect()会花费很多时间
            conn.setRequestMethod("HEAD");
            conn.setAllowUserInteraction(true);
            conn.connect();
            if (conn.getResponseCode()==200) {
                fileSize = conn.getContentLength();
                File file = FileUtils.checkAndCreateFile(path);
                // 本地访问文件
                RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
                accessFile.setLength(fileSize);
                accessFile.close();
            }
            long time = System.currentTimeMillis();
            conn.disconnect();
            L.e("get size time 333333= "+(System.currentTimeMillis()-time));
        } catch (Exception e) {
            e.printStackTrace();
            T.getInstance().showShort("获取文件长度发生错误");
            return;
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
            startPos -= 1;
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
        void onStateChanged(int state);
    }

    /**
     * 开启更新界面线程和开启下载线程
     */
    private void startThreads(){
        //准备开启线程
        updateThread = new UpdateThread();
        updateThread.start();
        for (Thread thread : threads)
            thread.start();
    }

    private void finishDownload(){
        currentState = STATE_FINISH;
        downloadState = false;
        isDownloadFinish = true;
        progressChangeHandler.sendEmptyMessage(STATE_FINISH);
        if (updateThread!=null && updateThread.isAlive())
            updateThread.canRun = false;
    }

    private static class ProgressChangeHandler extends Handler{
        private WeakReference<FileDownloadManager> activityWeakReference;

        public ProgressChangeHandler(FileDownloadManager manager){
            activityWeakReference = new WeakReference<>(manager);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == -1){
                L.e("开启线程");
                activityWeakReference.get().startThreads();
            }
            //下载进度更新
            else if (msg.what == 0) {
                if (activityWeakReference.get().getCompleteSize() >= activityWeakReference.get().fileSize) {
                    activityWeakReference.get().finishDownload();
                    T.getInstance().showShort("下载完成");
                }
                if (activityWeakReference.get().listener != null)
                    activityWeakReference.get().listener.onProgressChanged
                            (activityWeakReference.get().getCompleteSize(), activityWeakReference.get().fileSize);
            }
            //下载状态更新
            else {
                if (currentState != msg.what){
                    currentState = msg.what;
                    if (activityWeakReference.get().listener != null)
                        activityWeakReference.get().listener.onStateChanged(currentState);
                }
            }
        }
    }

    /**
     * 单独一个线程下载的信息
     */
    public class DownloadInfo {
        public int id;
        public long startPos;
        public long endPos;
        public volatile long completeSize;
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
            //TODO 文件超过１００Ｍ，会停止
            HttpURLConnection connection = null;
            RandomAccessFile randomAccessFile = null;
            InputStream is = null;
            try {
                URL url = new URL(FileDownloadManager.this.url);
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(2000);
                connection.setRequestMethod("GET");
                connection.setAllowUserInteraction(true);

                // 设置范围，格式为Range：bytes x-y;
                connection.setRequestProperty("Range", "bytes=" + (info.startPos + info.completeSize) + "-" + info.endPos);
                randomAccessFile = new RandomAccessFile(path, "rwd");
                randomAccessFile.seek(info.startPos + info.completeSize);
                // 将要下载的字节写到上次写的末尾
                is = connection.getInputStream();
                byte[] buffer = new byte[1024 * 8];
                int length;
                while ((length = is.read(buffer)) != -1) {
                    randomAccessFile.write(buffer, 0, length);
                    info.completeSize += length;
                    if (!downloadState) {
                        L.e("我到里面来了");
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    long time = System.currentTimeMillis();
                    //android 4.x disconnect或者close会耗费很长的时间，解决了很长时间，暂未找到方法，有的联系我
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

        public boolean canRun = true;
        @Override
        public void run() {
            try {
                while (canRun) {
                    // 更新数据库中的下载信息
                    helper.updateInfos(url, infos);
                    L.e("更新界面  " + getCompleteSize() + "   fileSize" + fileSize);
                    //更新界面
                    progressChangeHandler.sendEmptyMessage(0);
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
