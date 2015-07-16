package com.android.libcore.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

/**
 * Description: 最基础的dialog类，用来统一dialog的功能
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
    public ArrayList<Integer> ids = new ArrayList<>();

    public RootDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public abstract RootDialog setTitle(String title);

    public abstract RootDialog setTitle(View title);

    public abstract RootDialog setMessage(String message);

    public abstract RootDialog setMessage(View message);

    public abstract RootDialog setPositiveButton(String positive);

    public abstract RootDialog setPositiveButton(View positive);

    public abstract RootDialog setNegativeButton(String negative);

    public abstract RootDialog setNegativeButton(View negative);

    public abstract RootDialog setNeutralButton(String neutral);

    public abstract RootDialog setNeutralButton(View neutral);

    public abstract RootDialog addOtherButton(String other, int other_listener);

    public abstract RootDialog addOtherButton(View other, int other_listener);

    public abstract RootDialog setButtonClickListener(ButtonClickListener listener);

    public abstract RootDialog setGravity(int gravity);

    public abstract RootDialog setPosition(int x, int y);

    public abstract RootDialog setWidth(int width);

    public abstract RootDialog setHeight(int height);

    public interface ButtonClickListener{
        void onButtonClick(int button_id);
    }
}
