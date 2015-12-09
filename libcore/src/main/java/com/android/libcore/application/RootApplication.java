package com.android.libcore.application;


import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.libcore.cachemanager.CacheManager;
import com.android.libcore.log.L;
import com.android.libcore.activity.ActivityManager;
import com.android.libcore.utils.FileUtils;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Description: {@linkplain Application}基类，不要忘记在manifest文件中设置application的
 * android:name，应用的application应该继承自该基类
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-07
 */
public class RootApplication extends Application{

    /** 是否是调试模式，统一使用该全局的debug变量 */
    public static final boolean DEBUG = true;

    /** 用来保存当前该Application的context */
    private static Context instance;
    /** 用来保存最新打开页面的context */
    private volatile static WeakReference<Context> instanceRef = null;
    /** 该变量长久的存放于内存，用来存放一些在软件启动生命周期之内
     * 需要存放的变量和数据，但存放的数据量不宜过大，如果需要存放
     * 过大的数据，请在使用完之后，立马清除*/
    public static HashMap<String, Object> maps;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        maps = new HashMap<>();
        //设置默认崩溃处理，如需使用，不注释即可
//        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler());
    }


    /**
     * 该函数用来返回一个context，一般情况下为当前activity的context，如果为空，
     * 就会调用{@linkplain ActivityManager#getActivity()}方法去获取栈顶context,
     * 但是如果activity没有调用 {@link #setInstanceRef(Context)}方法去设置context,
     * 就会使用整个Application的context，相当于{@link #getApplicationContext()},
     * 不推荐使用该方法，特别是耗时任务，因为会导致页面销毁时，任务无法回收，导致内存泄露和
     * 其他异常
     *
     * @return context上下文，如果返回Null检测manifest文件是否设置了application的name
     */
    public static Context getInstance(){
        if (instanceRef == null || instanceRef.get() == null){
            synchronized (RootApplication.class) {
                if (instanceRef == null || instanceRef.get() == null) {
                    Context context = ActivityManager.getInstance().getActivity();
                    if (context != null)
                        instanceRef = new WeakReference<>(context);
                    else {
                        instanceRef = new WeakReference<>(instance);
                    }
                }
            }
        }
        return instanceRef.get();
    }

    /**
     * 将{@link #instanceRef}设置为最新页面的context
     * @param context 最新页面的context
     */
    public static void setInstanceRef(Context context){
        instanceRef = new WeakReference<>(context);
    }

    /**
     * 检测应用是否退出，并且在应用退出的时候做相关的处理
     */
    public static void checkApplicationDestroy(){
        //应用被关闭，删除需要删除的相关目录和文件
        if (ActivityManager.getInstance().getActivity() == null){
            CacheManager.removeTemporary();
            FileUtils.clearExternalStorageTemp();
        }
    }

    /**
     * 处理崩溃异常，并且在崩溃异常之后重启<br/>
     * <strong>Android对待所有传递给Context.startActivity()的 隐式intent好像它们至少包含
     * "android.intent.category.DEFAULT"（对应CATEGORY_DEFAULT常量）。
     * 因此，活动想要接收隐式intent必须要在intent过滤器中包含"android.intent.category.DEFAULT"</strong>
     */
    private class MyExceptionHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            //启动首页
            Intent intent = new Intent();
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.addCategory("com.android.framework.MAINPAGE");
            PendingIntent restartIntent = PendingIntent.getActivity(getInstance(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            //退出程序
            AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent);

            ActivityManager.getInstance().finishAllActivityWithoutClose();
        }
    }
}
