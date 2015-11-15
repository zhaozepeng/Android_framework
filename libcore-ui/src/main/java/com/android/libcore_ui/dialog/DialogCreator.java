package com.android.libcore_ui.dialog;

import android.view.View;

import com.android.libcore_ui.application.BaseApplication;

import java.util.ArrayList;

/**
 * Description: dialog的生成类，用来获取所需要基本常用的dialog
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-16
 */
public class DialogCreator {

    /**
     * 创建dialog函数
     * @param title 只能为string和view
     * @param message 只能为string和view
     * @param positive 只能为string和view
     */
    public static AppDialog createDialog(Object title, Object message, Object positive){
        return createDialog(title, message, positive, null);
    }

    /**
     * 创建dialog函数
     * @param title 只能为string和view
     * @param message 只能为string和view
     * @param positive 只能为string和view
     * @param negative 只能为string和view
     */
    public static AppDialog createDialog(Object title, Object message, Object positive, Object negative){
        return createDialog(title, message, positive, negative, null);
    }

    /**
     * 创建dialog函数
     * @param title 只能为string和view
     * @param message 只能为string和view
     * @param positive 只能为string和view
     * @param negative 只能为string和view
     * @param neutral 只能为string和view
     */
    public static AppDialog createDialog(Object title, Object message, Object positive, Object negative, Object neutral){
        return createDialog(title, message, positive, negative, neutral, null);
    }

    /**
     * 创建dialog函数
     * @param title 只能为string和view
     * @param message 只能为string和view
     * @param positive 只能为string和view
     * @param negative 只能为string和view
     * @param neutral 只能为string和view
     * @param others 需要另外加上按钮的数据集合
     */
    public static AppDialog createDialog(Object title, Object message, Object positive, Object negative, Object neutral,
                                      ArrayList<OtherButton> others){
        AppDialog dialog = new AppDialog(BaseApplication.getInstance());
        if (title != null) {
            if (title instanceof String) {
                dialog.setTitle((String) title);
            } else if (title instanceof View) {
                dialog.setTitle((View) title);
            } else {
                throw new IllegalArgumentException("title 只能为string和view");
            }
        }

        if (message != null) {
            if (message instanceof String) {
                dialog.setMessage((String) message);
            } else if (message instanceof View) {
                dialog.setMessage((View) message);
            } else {
                throw new IllegalArgumentException("message 只能为string和view");
            }
        }

        if(positive != null) {
            if (positive instanceof String) {
                dialog.setPositiveButton((String) positive);
            } else if (positive instanceof View) {
                dialog.setPositiveButton((View) positive);
            } else {
                throw new IllegalArgumentException("positive 只能为string和view");
            }
        }

        if (negative != null) {
            if (negative instanceof String) {
                dialog.setNegativeButton((String) negative);
            } else if (negative instanceof View) {
                dialog.setNegativeButton((View) negative);
            } else {
                throw new IllegalArgumentException("negative 只能为string和view");
            }
        }

        if (neutral != null) {
            if (neutral instanceof String) {
                dialog.setNeutralButton((String) neutral);
            } else if (neutral instanceof View) {
                dialog.setNeutralButton((View) neutral);
            } else {
                throw new IllegalArgumentException("neutral 只能为string和view");
            }
        }

        if (others!=null && others.size() > 0){
            for (OtherButton temp : others){
                if (temp.other instanceof String){
                    dialog.addOtherButton((String)temp.other, temp.id);
                }else{
                    dialog.addOtherButton((View)temp.other, temp.id);
                }
            }
        }
        return dialog;
    }

    /**
     * 另外需要添加按钮的数据集合
     */
    public static class OtherButton{
        Object other;
        Integer id;

        /** 该类只提供该两种构造函数 */
        public OtherButton(String text, Integer id){
            other = text;
            this.id = id;
        }
        public OtherButton(View view, Integer id){
            other = view;
            this.id = id;
        }
    }
}
