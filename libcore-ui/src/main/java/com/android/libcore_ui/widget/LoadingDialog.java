package com.android.libcore_ui.widget;

import android.app.Dialog;
import android.content.Context;

/**
 * Description: 应用加载dialog
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-16
 */
public class LoadingDialog extends Dialog{

    public LoadingDialog(Context context) {
        super(context);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }
}
