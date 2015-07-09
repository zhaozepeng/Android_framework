package com.android.libcore.Toast;

import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.android.libcore.application.RootApplication;

/**
 * Description: Toast类<br/>
 * 如果需要设置gravity，请使用{@link #getInstance()}.{@link #setGravity(int)}方法进行设置<br/>
 * 如果需要额外设置{@link #xOffset}或者{@link #yOffset}请调用对应{@link #setxOffset(int)}和
 * {@link #setyOffset(int)}即可，用法和gravity一致
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-09
 */
public class T {

    private volatile static T instance;
    private int gravity = -1;
    private int xOffset = 0;
    private int yOffset = 0;
    private Toast temp;

    public static T getInstance() {
        if (instance == null){
            synchronized (T.class){
                if (instance == null){
                    instance = new T();
                }
            }
        }
        return instance;
    }

    private T(){
    }

    /**
     * 设置该toast的显示位置，只对该toast有效
     */
    public T setGravity(int gravity){
        this.gravity = gravity;
        return getInstance();
    }

    /**
     * 请在{@link #setGravity(int)}调用之后调用，只对该toast有效
     */
    public T setxOffset(int xOffset) {
        this.xOffset = xOffset;
        return getInstance();
    }

    /**
     * 请在{@link #setGravity(int)}调用之后调用，只对该toast有效
     */
    public T setyOffset(int yOffset) {
        this.yOffset = yOffset;
        return getInstance();
    }

    public void showShort(String message){
        showShort(message, null);
    }

    /**
     * 需要在toast中显示的v
     */
    public void showShort(String message, View v){
        //防止一堆toast的显示堆积
        if (temp != null)
            temp.cancel();
        temp = Toast.makeText(RootApplication.getInstance(), message, Toast.LENGTH_SHORT);
        if (gravity != -1)
            temp.setGravity(gravity, xOffset, yOffset);
        if (v != null){
            temp.setView(v);
        }
        temp.show();
        reset();
    }

    public void showLong(String message){
        showLong(message, null);
    }

    /**
     * 需要在toast中显示的v
     */
    public void showLong(String message, View v){
        //防止一堆toast的显示堆积
        if (temp != null)
            temp.cancel();
        temp = Toast.makeText(RootApplication.getInstance(), message, Toast.LENGTH_LONG);
        if (gravity != -1)
            temp.setGravity(gravity, xOffset, yOffset);
        if (v != null){
            temp.setView(v);
        }
        temp.show();
        reset();
    }

    private void reset(){
        gravity = -1;
        xOffset = 0;
        yOffset = 0;
    }
}
