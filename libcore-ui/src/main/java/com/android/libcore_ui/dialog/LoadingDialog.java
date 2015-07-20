package com.android.libcore_ui.dialog;

import android.app.Dialog;
import android.content.Context;

import com.android.libcore_ui.R;

/**
 * Description: 应用加载dialog
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-16
 */
public class LoadingDialog extends Dialog{

    public LoadingDialog(Context context) {
        this(context, R.style.theme_dialog);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }
}
