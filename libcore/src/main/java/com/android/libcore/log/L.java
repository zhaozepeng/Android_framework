package com.android.libcore.log;

import android.text.TextUtils;
import android.util.Log;

import com.android.libcore.application.RootApplication;

import java.util.Locale;

/**
 * Description: log打印类
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-07
 */
public final class L {
    private static boolean LOG_ENABLE = true;
    public static String LOG_TAG = "LOG_TAG";

    private L(){}

    static {
        LOG_ENABLE = RootApplication.DEBUG;
        if (RootApplication.getInstance() == null) {
            LOG_TAG = "unknown";
        } else {
            LOG_TAG = RootApplication.getInstance().getPackageName();
        }
    }

    /**
     * 打印基本信息
     * @param args 需要打印出来的额外信息，每次换行
     */
    public static void i(String message, Object... args){
        log(Log.INFO, message, null, args);
    }

    /**
     * 打印警告
     * @param args 需要打印出来的额外信息，每次换行
     */
    public static void w(String message, Object... args){
        log(Log.WARN, message, null, args);
    }

    /***
     * 打印异常
     * @param throwable 需要打印的异常信息
     */
    public static void e(Throwable throwable){
        log(Log.ERROR, null, throwable);
    }

    /***
     * 打印异常
     */
    public static void e(String message, Object... args){
        log(Log.ERROR, message, null, args);
    }

    /***
     * 打印异常
     * @param throwable 需要打印的异常信息
     * @param args 需要打印出来的额外信息，每次换行
     */
    public static void e(String message, Throwable throwable, Object... args){
        log(Log.ERROR, message, throwable, args);
    }

    private static void log(int priority, String message, Throwable throwable, Object... args){
        if (!LOG_ENABLE)
            return;
        StringBuilder log = new StringBuilder();

        //获取调用者类，函数信息和行数信息
        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();
        String caller = "<unknown>";
        for (int i = 2; i < trace.length; i++) {
            Class<?> clazz = trace[i].getClass();
            if (!clazz.equals(L.class)) {
                String callingClass = trace[i].getClassName();
                callingClass = callingClass.substring(callingClass.lastIndexOf('.') + 1);
                callingClass = callingClass.substring(callingClass.lastIndexOf('$') + 1);
                caller = callingClass + "." + trace[i].getMethodName()
                        + "(line:" + trace[i].getLineNumber() +")";
                break;
            }
        }

        //将message附加到日志上
        if (!TextUtils.isEmpty(message))
            log.append(String.format(Locale.US, "[TID:%d] %s: %s",
                    Thread.currentThread().getId(), caller, message)).append("\n");

        //附加异常信息
        if (throwable != null)
            log.append(Log.getStackTraceString(throwable)).append("\n");

        //附加额外信息
        for (Object object: args)
            log.append(object.toString()).append("\n");

        //打印日志
        Log.println(priority, LOG_TAG, log.toString());
    }
}
