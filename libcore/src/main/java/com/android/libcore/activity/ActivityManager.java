package com.android.libcore.activity;

import android.app.Activity;

import com.android.libcore.log.L;

import java.util.Stack;

/**
 * Description: 该类用栈来管理所有该应用的activity，进栈退栈等
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-07
 */
public final class ActivityManager {
    private static volatile  ActivityManager instance = null;
    private Stack<Activity> stack = null;

    private ActivityManager(){
        stack = new Stack<>();
    }

    public static ActivityManager getInstance(){
        if (instance == null){
            synchronized (ActivityManager.class){
                if (instance == null)
                    instance = new ActivityManager();
            }
        }
        return instance;
    }

    /**
     * 将activity加入到栈中
     * @param activity 需要加入到栈中的activity
     */
    public void addActivity(Activity activity){
        stack.add(activity);
    }

    /**
     * @return 栈顶的activity
     */
    public Activity getActivity(){
        if (!stack.isEmpty())
            return stack.peek();
        L.e("Activity 栈为空！！！");
        return null;
    }

    /**
     * 删除掉最上面一个的activity
     */
    public void removeActivity(){

    }
}
