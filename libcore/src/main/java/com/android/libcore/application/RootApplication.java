package com.android.libcore.application;


import android.app.Application;
import android.content.Context;

import com.android.libcore.log.L;
import com.android.libcore.activity.ActivityManager;

import java.lang.ref.WeakReference;

/**
 * Description: {@linkplain Application}基类，
 * 应用的application应该继承自该基类
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-07
 */
public class RootApplication extends Application{

    /** 用来保存当前该Application的context */
    private static Context instance;
    /** 用来保存最新打开页面的context */
    private volatile static WeakReference<Context> instanceRef = null;
    /** 是否是调试模式 */
    public static final boolean DEBUG = true;

    public RootApplication(){
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    /**
     * 该函数用来返回一个context，一般情况下为当前activity的context，如果为空，
     * 就会调用{@linkplain ActivityManager#getActivity()}方法去获取栈顶context,
     * 但是如果activity没有调用 {@link #setInstanceRef(Context)}方法去设置context,
     * 就会使用整个Application的context，相当于{@link #getApplicationContext()},
     * 不推荐使用该方法，特别是耗时任务，因为会导致页面销毁时，任务无法回收，导致内存泄露和
     * 其他异常
     *
     * @return context上下文
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
                        L.w("请确保RootActivity调用setInstanceRef方法");
                    }
                }
            }
        }
        return instanceRef.get();
    }

    /**
     * 将{@link #instanceRef}设置为最新页面的context设置
     * @param context 最新页面的context
     */
    public static void setInstanceRef(Context context){
        instanceRef = new WeakReference<>(context);
    }
}
