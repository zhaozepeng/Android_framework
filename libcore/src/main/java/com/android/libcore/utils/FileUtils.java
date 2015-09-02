package com.android.libcore.utils;

import android.os.Environment;

import com.android.libcore.application.RootApplication;
import com.android.libcore.log.L;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Description: 文件相关的操作，如果需要在SD卡主目录下建立子目录，请在{@link ExternalStorageType}
 * 枚举下建立相同的变量，带上目录名字即可，<strong>所有创建之后的目录末尾自带"/"文件分隔符</strong><br/>
 * <strong>image,voice,video目录下会有.nomedia文件来屏蔽系统扫描</strong>
 *
 * <ul>
 *     <li>{@link #checkAndCreateFile(String)}根据path创建文件</li>
 *     <li>{@link #checkAndCreateChildDirectory(String)}根据path创建子目录,自动会在末尾添加"/"文件分隔符</li>
 *
 *     <li>{@link #getExternalStoragePath()}获取SD卡根目录，不要在此进行操作以防污染主目录</li>
 *     <li>{@link #getExternalStorageTempPath()}获取SD卡主目录下缓存目录</li>
 *     <li>{@link #createFileInImageDirectory(String)}在temp目录下创建文件并返回</li>
 *     <li>{@link #clearExternalStorageTemp()}应用退出之后删除temp目录</li>
 *     <li>{@link #getExternalStorageFilePath()} 获取SD卡主目录下文件目录</li>
 *     <li>{@link #createFileInFileDirectory(String)} 在file目录下创建文件并返回</li>
 *     <li>{@link #getExternalStorageImagePath()}获取SD卡主目录下图片目录</li>
 *     <li>{@link #createFileInImageDirectory(String)}在image目录下创建文件并返回</li>
 *     <li>{@link #getExternalStorageVoicePath()}获取SD卡主目录下声音目录</li>
 *     <li>{@link #createFileInVoiceDirectory(String)}在voice目录下创建文件并返回</li>
 *     <li>{@link #getExternalStorageVideoPath()}获取SD卡主目录下视频目录</li>
 *     <li>{@link #createFileInVideoDirectory(String)}在video目录下创建文件并返回</li>
 *     <li>{@link #getExternalStorageHtmlPath()}获取SD卡主目录下网页目录</li>
 *     <li>{@link #createFileInHtmlDirectory(String)}在html目录下创建文件并返回</li>
 *
 *     <li>{@link #getFileOrDirectorySize(String)}获取目录或者文件的大小，单位KB</li>
 * </ul>
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-22
 */
public class FileUtils {
    /** 外部SD卡根目录下的文件夹名 */
    private static final String EXTERNAL_STORAGE_PATH = "/FrameWork";
    /** 当temp目录大小超过该值的时候，清空该目录，单位为KB */
    private static final long K_BYTES_TO_DELETE = 10 * 1024;

    /**
     * 检测并且创建文件
     */
    public static File checkAndCreateFile(String path){
        File file = new File(path);
        if (!file.exists())
            try {
                file.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
        return file;
    }

    /**
     * 创建主目录下子目录，自动会在末尾添加"/"文件分隔符
     */
    public static String checkAndCreateChildDirectory(String path){
        File file = new File(path);
        if (!file.exists())
            file.mkdirs();
        if (!file.exists())
            return null;
        return path+"/";
    }

    /**
     * 外部最好不要使用外部目录的根目录进行操作，建立一个子目录去处理
     */
    public static String getExternalStoragePath(){
        String path = null;
        //需要检测外部SD卡的挂载状态
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = checkAndCreateChildDirectory(Environment.getExternalStorageDirectory().getPath() + EXTERNAL_STORAGE_PATH);
        }
        if (path == null){
            //如果外部SD卡不可用，使用"/data/data/com.android.framework/files/"目录
            path = RootApplication.getInstance().getFilesDir().getPath();
        }
        return path;
    }

    /**
     * 获取外部临时文件目录，过大会自动删除
     */
    public static String getExternalStorageTempPath(){
        ExternalStorageType type = ExternalStorageType.TEMP;
        return checkAndCreateChildDirectory(type.getFilePath(getExternalStoragePath()));
    }

    /**
     * 在外部{@link #getExternalStorageTempPath()}目录下创建文件，
     */
    public static File createFileInTempDirectory(String filename){
        return checkAndCreateFile(getExternalStorageTempPath() + filename);
    }

    /**
     * 删除外部临时文件目录
     */
    public static void clearExternalStorageTemp(){
        L.i("application close clear temp directory");
        if (getFileOrDirectorySize(getExternalStorageTempPath()) >= K_BYTES_TO_DELETE) {
            File file = new File(getExternalStorageTempPath());
            File[] files = file.listFiles();
            for (File temp : files)
                temp.delete();
        }
    }

    /**
     * 获取外部文件目录，过大会自动删除
     */
    public static String getExternalStorageFilePath(){
        ExternalStorageType type = ExternalStorageType.FILE;
        return checkAndCreateChildDirectory(type.getFilePath(getExternalStoragePath()));
    }

    /**
     * 在外部{@link #getExternalStorageFilePath()}目录下创建文件，
     */
    public static File createFileInFileDirectory(String filename){
        return checkAndCreateFile(getExternalStorageTempPath() + filename);
    }

    /**
     * 获取外部图片文件目录，在该目录下会创建.nomedia文件防止系统扫描
     */
    public static String getExternalStorageImagePath(){
        ExternalStorageType type = ExternalStorageType.IMAGE;
        String path = type.getFilePath(getExternalStoragePath());
        String result = checkAndCreateChildDirectory(path);
        checkAndCreateNoMedia(path);
        return result;
    }

    /**
     * 在外部{@link #getExternalStorageImagePath()}目录下创建文件，
     */
    public static File createFileInImageDirectory(String filename){
        return checkAndCreateFile(getExternalStorageImagePath() + filename);
    }

    /**
     * 获取外部声音文件目录，在该目录下会创建.nomedia文件防止系统扫描
     */
    public static String getExternalStorageVoicePath(){
        ExternalStorageType type = ExternalStorageType.VOICE;
        String path = type.getFilePath(getExternalStoragePath());
        String result = checkAndCreateChildDirectory(path);
        checkAndCreateNoMedia(path);
        return result;
    }

    /**
     * 在外部{@link #getExternalStorageVoicePath()}目录下创建文件，
     */
    public static File createFileInVoiceDirectory(String filename){
        return checkAndCreateFile(getExternalStorageVoicePath() + filename);
    }

    /**
     * 获取外部视频文件目录，在该目录下会创建.nomedia文件防止系统扫描
     */
    public static String getExternalStorageVideoPath(){
        ExternalStorageType type = ExternalStorageType.VIDEO;
        String path = type.getFilePath(getExternalStoragePath());
        String result = checkAndCreateChildDirectory(path);
        checkAndCreateNoMedia(path);
        return result;
    }

    /**
     * 在外部{@link #getExternalStorageVoicePath()}目录下创建文件，
     */
    public static File createFileInVideoDirectory(String filename){
        return checkAndCreateFile(getExternalStorageVideoPath() + filename);
    }

    /**
     * 获取外部网页文件目录
     */
    public static String getExternalStorageHtmlPath(){
        ExternalStorageType type = ExternalStorageType.HTML;
        return checkAndCreateChildDirectory(type.getFilePath(getExternalStoragePath()));
    }

    /**
     * 在外部{@link #getExternalStorageHtmlPath()}目录下创建文件，
     */
    public static File createFileInHtmlDirectory(String filename){
        return checkAndCreateFile(getExternalStorageHtmlPath() + filename);
    }

    /**
     * 获取文件或者目录大小，单位为取整的KB
     */
    public static long getFileOrDirectorySize(String path){
        long size = 0;
        if (path == null)
            return size;
        File file = new File(path);
        if (!file.exists())
            return size;
        if (file.isDirectory()){
            File[] files = file.listFiles();
            for (File temp : files){
                size += getFileSize(temp);
            }
        }else{
            size = getFileSize(file);
        }
        return size/8/1024;
    }

    /**
     * 检测该目录下是否有nomedia文件，如果没有就创建
     */
    private static void checkAndCreateNoMedia(String path){
        checkAndCreateFile(path + "/.nomedia");
    }

    /**
     * @return 返回文件大小，单位为byte
     */
    private static long getFileSize(File file){
        long size = 0;
        if (file.exists()){
            try {
                FileInputStream fis = new FileInputStream(file);
                size = fis.available();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return size;
    }

    /**
     * 所有在外部存储目录下的子目录都需要在此定义文件夹名
     */
    public enum ExternalStorageType{
        TEMP("temp"),FILE("file"),IMAGE("image"),VOICE("voice"),VIDEO("video"),HTML("html");

        private String typeName;
        ExternalStorageType(String typeName){
            this.typeName = typeName;
        }

        public String getFilePath(String parentPath) {
            String path = parentPath;
            if (!(parentPath.charAt(parentPath.length()-1)=='/')){
                path += "/";
            }
            return path+typeName;
        }
    }
}
