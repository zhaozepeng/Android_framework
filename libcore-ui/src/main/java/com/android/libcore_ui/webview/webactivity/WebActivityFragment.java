package com.android.libcore_ui.webview.webactivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.android.libcore.utils.CommonUtils;
import com.android.libcore_ui.R;
import com.android.libcore_ui.webview.WebFragment;

import java.util.ArrayList;

/**
 * Description: 用于在{@link WebActivity}中显示的webfragment
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-27
 */
public class WebActivityFragment extends WebFragment {

    @Override
    protected void initView() {
        super.initView();
        WebSettings settings = webView.getSettings();
        //支持缩放
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);
        //隐藏缩放栏
        if (Build.VERSION.SDK_INT >= 11)
            settings.setDisplayZoomControls(false);
    }

    @Override
    protected boolean handleUrlBeforeLoad(String url) {
        return false;
    }

    @Override
    protected void onPageStarted(WebView view, String url, Bitmap favicon) {
        Message msg = Message.obtain();
        msg.what = 1;
        sendMessageToActivity(msg);
    }

    @Override
    protected void onPageFinished(WebView view, String url) {
        Message msg = Message.obtain();
        msg.what = 2;
        sendMessageToActivity(msg);
    }

    @Override
    protected void onProgressChanged(WebView view, int newProgress) {
        Message msg = Message.obtain();
        msg.what = 3;
        msg.obj = newProgress;
        sendMessageToActivity(msg);
    }

    @Override
    protected void onReceivedIcon(WebView view, Bitmap icon) {
        Message msg = Message.obtain();
        msg.what = 4;
        msg.obj = icon;
        sendMessageToActivity(msg);
    }

    @Override
    protected void onReceivedTitle(WebView view, String title) {
        Message msg = Message.obtain();
        msg.what = 5;
        msg.obj = title;
        sendMessageToActivity(msg);
    }
}
