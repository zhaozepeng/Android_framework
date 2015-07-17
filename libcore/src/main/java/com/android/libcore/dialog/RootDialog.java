package com.android.libcore.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import java.util.ArrayList;

/**
 * Description: 最基础的dialog类，用来统一dialog的功能，该dialog可以添加多个按钮，按钮的顺序
 * 和添加按钮顺序一致，所以请按照需要显示的顺序要添加按钮
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-16
 */
public abstract class RootDialog extends Dialog{
    /** 用来标识确定按钮的回调id */
    public static final int POSITIVE_LISTENER = 0;
    /** 用来标识否定按钮的回调id */
    public static final int NEGATIVE_LISTENER = 1;
    /** 用来标识中间按钮的回调id */
    public static final int NEUTRAL_LISTENER = 2;
    /** 其他按钮的id */
    protected ArrayList<Integer> ids = new ArrayList<>();
    protected ButtonClickListener listener;

    public RootDialog(Context context) {
        super(context);
    }

    /**
     * 设置dialog的title
     * @param title 标题的文字
     */
    public abstract RootDialog setTitle(String title);

    /**
     * 设置dialog的title view
     * @param title view
     */
    public abstract RootDialog setTitle(View title);

    /**
     * 设置内容区域的文字
     * @param message 内容的文字
     */
    public abstract RootDialog setMessage(String message);

    /**
     * 内容区域的View
     * @param message view
     */
    public abstract RootDialog setMessage(View message);

    /**
     * 设置确定按钮的文字
     * @param positive 文字
     */
    public abstract RootDialog setPositiveButton(String positive);

    /**
     * 设置确定按钮的view
     * @param positive view
     */
    public abstract RootDialog setPositiveButton(View positive);

    /**
     * 设置取消按钮的文字
     * @param negative 文字
     */
    public abstract RootDialog setNegativeButton(String negative);

    /**
     * 设置取消按钮的view
     * @param negative view
     */
    public abstract RootDialog setNegativeButton(View negative);

    /**
     * 设置中性按钮的文字
     * @param neutral 文字
     */
    public abstract RootDialog setNeutralButton(String neutral);

    /**
     * 设置中性按钮的view
     * @param neutral view
     * @return
     */
    public abstract RootDialog setNeutralButton(View neutral);

    /**
     * 增加一个按钮
     * @param other 按钮的文字
     * @param other_listener 按钮的点击回调id
     */
    public abstract RootDialog addOtherButton(String other, int other_listener);

    /**
     * 增加一个按钮
     * @param other 按钮的view
     * @param other_listener 按钮的点击回调id
     */
    public abstract RootDialog addOtherButton(View other, int other_listener);

    /**
     * 设置dialog的按钮点击回调
     */
    public RootDialog setButtonClickListener(ButtonClickListener listener){
        this.listener = listener;
        return this;
    }

    /**
     * 设置dialog显示的位置
     * @param gravity Gravity类的变量
     */
    public abstract RootDialog setGravity(int gravity);

    /**
     * 设置dialog显示的具体位置
     * @param x x坐标
     * @param y y坐标
     */
    public abstract RootDialog setPosition(int x, int y);

    /**
     * 设置宽度，不使用默认
     * @param width 宽度
     */
    public abstract RootDialog setWidth(int width);

    /**
     * 设置高度，不使用默认
     * @param height 高度
     */
    public abstract RootDialog setHeight(int height);

    public interface ButtonClickListener{
        void onButtonClick(int button_id);
    }
}
