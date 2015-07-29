package com.android.libcore.utils;

import android.os.Environment;

import com.android.libcore.application.RootApplication;

import java.io.File;

/**
 * Description: 文件相关的操作
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-22
 */
public class FileUtils {
    /** 外部SD卡根目录下的文件夹名 */
    private static final String EXTERNALSTORAGEPATH = "/FrameWork";

    /**
     * 外部最好不要使用外部目录的根目录进行操作，建立一个子目录去处理
     */
    public static String getExternalStoragePath(){
        String path = null;
        //需要检测外部SD卡的挂载状态
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = checkAndCreateChildDirectory(Environment.getExternalStorageDirectory().getPath()
                    + EXTERNALSTORAGEPATH);
        }
        if (path == null){
            //如果外部SD卡不可用，使用"/data/data/com.android.framework/files/"目录
            path = RootApplication.getInstance().getFilesDir().getPath();
        }
        return path;
    }

    /**
     * 获取外部临时文件目录
     */
    public static String getExternalStorageTempPath(){
        ExternalStorageType type = ExternalStorageType.TEMP;
        return checkAndCreateChildDirectory(type.getFilePath(getExternalStoragePath()));
    }

    /**
     * 获取外部图片文件目录
     */
    public static String getExternalStorageImagePath(){
        ExternalStorageType type = ExternalStorageType.IMAGE;
        return checkAndCreateChildDirectory(type.getFilePath(getExternalStoragePath()));
    }

    /**
     * 获取外部网页文件目录
     */
    public static String getExternalStorageHtmlPath(){
        ExternalStorageType type = ExternalStorageType.HTML;
        return checkAndCreateChildDirectory(type.getFilePath(getExternalStoragePath()));
    }

    /**
     * 创建主目录下子目录
     */
    private static String checkAndCreateChildDirectory(String path){
        File file = new File(path);
        if (!file.exists())
            file.mkdirs();
        if (!file.exists())
            return null;
        return path+"/";
    }

    /**
     * 所有在外部存储目录下的子目录都需要在此定义文件夹名
     */
    public enum ExternalStorageType{
        TEMP("temp"),IMAGE("image"),HTML("html");

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
