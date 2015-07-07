package com.android.libcore.log;

import android.text.TextUtils;
import android.util.Log;

import com.android.libcore.BuildConfig;
import com.android.libcore.application.RootApplication;

/**
 * Description: log打印类
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-07
 */
public final class L {
    private static boolean LOG_ENABLE = true;
    private static String LOG_TAG= "LOG_TAG";

    private L(){}

    static {
//        LOG_ENABLE = BuildConfig.DEBUG;
        LOG_ENABLE = RootApplication.DEBUG;
        LOG_TAG = RootApplication.getInstance().getPackageName();
    }

    /**
     * 打印基本信息
     * @param objects 需要打印出来的额外信息，每次换行
     */
    public static void i(String message, Object... objects){
        log(Log.INFO, message, null, objects);
    }

    /**
     * 打印警告
     * @param objects 需要打印出来的额外信息，每次换行
     */
    public static void w(String message, Object... objects){
        log(Log.INFO, message, null, objects);
    }

    /***
     * 打印异常
     * @param throwable 需要打印的异常信息
     */
    public static void e(Throwable throwable){
        log(Log.INFO, null, throwable);
    }

    /***
     * 打印异常
     */
    public static void e(String message, Object... objects){
        log(Log.INFO, message, null, objects);
    }

    /***
     * 打印异常
     * @param throwable 需要打印的异常信息
     * @param objects 需要打印出来的额外信息，每次换行
     */
    public static void e(String message, Throwable throwable, Object... objects){
        log(Log.INFO, message, throwable, objects);
    }

    private static void log(int priority, String message, Throwable throwable, Object... objects){
        if (!LOG_ENABLE)
            return;
        StringBuilder log = new StringBuilder();
        if (!TextUtils.isEmpty(message))
            log.append(message).append("\n");
        if (throwable != null)
            log.append(Log.getStackTraceString(throwable)).append("\n");
        for (Object object: objects)
            log.append(object.toString()).append("\n");
        Log.println(priority, LOG_TAG, log.toString());
    }
}
