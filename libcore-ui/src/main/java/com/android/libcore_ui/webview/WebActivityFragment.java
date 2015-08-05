package com.android.libcore_ui.webview;

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

import java.util.ArrayList;

/**
 * Description: WebView显示fragment
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-27
 */
public class WebActivityFragment extends WebFragment{
    private ProgressBar pb_bar;
    private InnerFrameworkWebViewClient webViewClient = new InnerFrameworkWebViewClient();
    private InnerFrameworkChromeClient chromeClient = new InnerFrameworkChromeClient();

    /** 因为onReceivedTitle方法在goBack时不会调用，所以用一个list存储title */
    private ArrayList<String> titles = new ArrayList<>();
    @Override
    protected View setContentView(LayoutInflater inflater, @Nullable ViewGroup container) {
        return inflater.inflate(R.layout.fragment_web_layout, container, false);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initView() {
        pb_bar = (ProgressBar) findViewById(R.id.pb_bar);
        webView = (WebView) findViewById(R.id.wb_content);
        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(chromeClient);

        WebSettings settings = webView.getSettings();
        //设置网页大小自适应
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        //支持缩放
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);
        //隐藏缩放栏
        if (Build.VERSION.SDK_INT >= 11)
            settings.setDisplayZoomControls(false);
        //支持js
        settings.setJavaScriptEnabled(true);
    }

    @Override
    protected void initData() {
        if (url != null)
            webView.loadUrl(url);
    }

    private class InnerFrameworkWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //使用应用浏览器加载
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Message msg = Message.obtain();
            msg.what = 1;
            sendMessageToActivity(msg);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Message msg = Message.obtain();
            msg.what = 2;
            sendMessageToActivity(msg);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }

    private class InnerFrameworkChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100)
                pb_bar.setVisibility(View.GONE);
            else
                pb_bar.setVisibility(View.VISIBLE);
            pb_bar.setProgress(newProgress);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            BitmapDrawable drawable = new BitmapDrawable(getResources(), icon);
            drawable.setBounds(0, 0, CommonUtils.dp2px(20), CommonUtils.dp2px(20));
//            setTitle.setCompoundDrawables(drawable, null, null, null);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            titles.add(" "+title);
            setTitle(" " + title);
        }

        //js警告框
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            T.getInstance().showShort(message);
            result.confirm();
            return true;
        }

        //确认框
        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            DialogFactory.createDialog(null, message, getString(R.string.confirm), getString(R.string.cancel))
                    .setOnButtonClickListener(new BaseDialog.ButtonClickListener() {
                        @Override
                        public void onButtonClick(int button_id) {
                            if (button_id == 0){
                                result.confirm();
                            }else{
                                result.cancel();
                            }
                        }
                    })
                    .show();
            return true;
        }

        //提示框
        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
            final View v = View.inflate(getActivity(), R.layout.dialog_js_prompt_message_layout, null);
            ((TextView)(v.findViewById(R.id.tv_message))).setText(message);
            ((EditText)(v.findViewById(R.id.et_content))).setText(defaultValue);
            Dialog dialog = DialogFactory.createDialog(null, v, getString(R.string.confirm), getString(R.string.cancel))
                    .setOnButtonClickListener(new BaseDialog.ButtonClickListener() {
                        @Override
                        public void onButtonClick(int button_id) {
                            if (button_id == 0) {
                                result.confirm(((EditText) (v.findViewById(R.id.et_content))).getText().toString());
                            } else {
                                result.cancel();
                            }
                        }
                    });
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            return true;
        }
    }
}
