package com.android.libcore_ui.dialog;

import android.content.Context;
import android.view.View;

import com.android.libcore.dialog.RootDialog;

/**
 * Description: 扩展dialog类，用来统一dialog的样式和实线dialog的功能，请在此处定义好一个应用
 * 的dialog样式，并且保证一个应用的都使用该dialog
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-16
 */
public class BaseDialog extends RootDialog{

    public BaseDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    public RootDialog setTitle(String title) {
        return this;
    }

    @Override
    public RootDialog setTitle(View title) {
        return this;
    }

    @Override
    public RootDialog setMessage(String message) {
        return this;
    }

    @Override
    public RootDialog setMessage(View message) {
        return this;
    }

    @Override
    public RootDialog setPositiveButton(String positive) {
        return this;
    }

    @Override
    public RootDialog setPositiveButton(View positive) {
        return this;
    }

    @Override
    public RootDialog setNegativeButton(String negative) {
        return this;
    }

    @Override
    public RootDialog setNegativeButton(View negative) {
        return this;
    }

    @Override
    public RootDialog setNeutralButton(String neutral) {
        return this;
    }

    @Override
    public RootDialog setNeutralButton(View neutral) {
        return this;
    }

    @Override
    public RootDialog addOtherButton(String other, int other_listener) {
        return this;
    }

    @Override
    public RootDialog addOtherButton(View other, int other_listener) {
        return this;
    }

    @Override
    public RootDialog setButtonClickListener(ButtonClickListener listener) {
        return this;
    }

    @Override
    public RootDialog setGravity(int gravity) {
        return this;
    }

    @Override
    public RootDialog setPosition(int x, int y) {
        return this;
    }

    @Override
    public RootDialog setWidth(int width) {
        return this;
    }

    @Override
    public RootDialog setHeight(int height) {
        return this;
    }
}
