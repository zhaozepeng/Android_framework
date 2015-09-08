package com.android.libcore_ui.webview;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import android.widget.TextView;

import com.android.libcore.Toast.T;
import com.android.libcore.dialog.BaseDialog;
import com.android.libcore_ui.R;
import com.android.libcore_ui.activity.BaseFragment;
import com.android.libcore_ui.dialog.DialogFactory;

import java.lang.reflect.Method;

/**
 * Description: 最基本的webFragment，可以作为fragment嵌入activity的任何部分
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-08-03
 */
public class WebFragment extends BaseFragment{
    public static final String EXTRA_URL = "extra_url";

    protected WebView webView;
    protected FrameworkWebViewClient webViewClient = new FrameworkWebViewClient();
    protected FrameworkChromeClient chromeClient = new FrameworkChromeClient();

    protected String url;

    protected WebCallback callback;
    @Override
    protected View setContentView(LayoutInflater inflater, @Nullable ViewGroup container) {
        return new WebView(activity);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initView() {
        webView = (WebView) viewContainer;
        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(chromeClient);

        WebSettings settings = webView.getSettings();
        //设置网页大小自适应
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        //支持js
        settings.setJavaScriptEnabled(true);
    }

    @Override
    protected void initData() {
        Bundle data = getArguments();
        if (data != null)
            url = data.getString(EXTRA_URL);
        if (url != null)
            webView.loadUrl(url);
    }

    /**
     * 加载url
     */
    public void loadUrl(String url){
        webView.loadUrl(url);
    }

    /**
     * 能否返回上一个页面
     */
    public boolean canGoBack(){
        return webView.canGoBack();
    }

    /**
     * 返回上一个页面
     */
    public void goBack(){
        webView.goBack();
    }

    /**
     * 刷新webview
     */
    public void refresh(){
        webView.reload();
    }

    /**
     * 停止加载
     */
    public void stopLoading(){
        webView.stopLoading();
    }

    /** 是否是内部url，不需要跳转url */
    protected boolean handleUrlBeforeLoad(String url){
        return false;
    }
    protected void onPageStarted(WebView view, String url, Bitmap favicon){}
    protected void onPageFinished(WebView view, String url){}

    private class FrameworkWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!handleUrlBeforeLoad(url))
                view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (callback != null)
                callback.onPageStarted(url, favicon);
            WebFragment.this.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (callback != null)
                callback.onPageFinished(url);
            WebFragment.this.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            T.getInstance().showShort("errorCode:" + errorCode + " description:" + description + " failingUrl:" + failingUrl);
            super.onReceivedError(view, errorCode, description, failingUrl);
        }


    }

    protected void onProgressChanged(WebView view, int newProgress){}
    protected void onReceivedIcon(WebView view, Bitmap icon){}
    protected void onReceivedTitle(WebView view, String title){}

    private class FrameworkChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (callback != null)
                callback.onProgressChanged(newProgress);
            WebFragment.this.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            if (callback != null)
                callback.onReceivedIcon(icon);
            WebFragment.this.onReceivedIcon(view, icon);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            if (callback != null)
                callback.onReceivedTitle(title);
            WebFragment.this.onReceivedTitle(view, title);
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

    @Override
    public void onResume() {
        super.onResume();
        invokeMethod("onResume");
        webView.resumeTimers();
    }

    @Override
    public void onPause() {
        super.onPause();
        //当页面不可见时，停止内核所有的动作，省电，省流量等
        invokeMethod("onPause");
        //不仅仅针对当前的webview而是全局的全应用程序的webview，它会暂停所有webview的layout，parsing，javascript timer，降低CPU功耗。
        webView.pauseTimers();
    }

    protected void invokeMethod(String method){
        try {
            Method m = WebView.class.getMethod(method);
            m.setAccessible(true);
            m.invoke(webView);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 设置回调
     */
    public void setCallback(WebCallback callback) {
        this.callback = callback;
    }

    /** web页的回调 */
    public static class WebCallback{
        public void onPageStarted(String url, Bitmap favicon){}
        public void onPageFinished(String url){}
        public void onProgressChanged(int progress){}
        public void onReceivedIcon(Bitmap icon){}
        public void onReceivedTitle(String title){}
    }
}
