package com.android.libcore_ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.android.libcore_ui.R;

/**
 * Description: 默认应用加载框，可自定义
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-11-03
 */
public class LoadingDialog extends Dialog{
    private TextView tv_loading_text;

    public LoadingDialog(Context context) {
        this(context, 0);
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, R.style.theme_dialog);
        setContentView(R.layout.loading_dialog_layout);
        tv_loading_text = (TextView) findViewById(R.id.tv_loading_text);
    }

    public void setLoadingText(String text){
        tv_loading_text.setText(text);
    }
}
