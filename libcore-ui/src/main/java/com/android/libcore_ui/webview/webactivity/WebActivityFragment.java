package com.android.libcore_ui.webview.webactivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.libcore.Toast.T;
import com.android.libcore.dialog.BaseDialog;
import com.android.libcore.utils.CommonUtils;
import com.android.libcore_ui.R;
import com.android.libcore_ui.dialog.DialogFactory;
import com.android.libcore_ui.webview.WebFragment;

import java.util.ArrayList;

/**
 * Description: WebView显示fragment
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-27
 */
public class WebActivityFragment extends WebFragment {
    private ProgressBar pb_bar;

    /** 因为onReceivedTitle方法在goBack时不会调用，所以用一个list存储title */
    private ArrayList<String> titles = new ArrayList<>();
    @Override
    protected View setContentView(LayoutInflater inflater, @Nullable ViewGroup container) {
        return inflater.inflate(R.layout.fragment_web_layout, container, false);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initView() {
        super.initView();
        pb_bar = (ProgressBar) findViewById(R.id.pb_bar);
        webView = (WebView) findViewById(R.id.wb_content);

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
        if (newProgress == 100)
            pb_bar.setVisibility(View.GONE);
        else
            pb_bar.setVisibility(View.VISIBLE);
        pb_bar.setProgress(newProgress);
    }

    @Override
    protected void onReceivedIcon(WebView view, Bitmap icon) {
        BitmapDrawable drawable = new BitmapDrawable(getResources(), icon);
        drawable.setBounds(0, 0, CommonUtils.dp2px(20), CommonUtils.dp2px(20));
//            setTitle.setCompoundDrawables(drawable, null, null, null);
    }

    @Override
    protected void onReceivedTitle(WebView view, String title) {
        titles.add(" "+title);
        setTitle(" " + title);
    }
}
