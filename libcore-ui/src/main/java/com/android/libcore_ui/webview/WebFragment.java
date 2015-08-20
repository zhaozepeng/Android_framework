package com.android.libcore_ui.webview;

import android.annotation.SuppressLint;
import android.app.Dialog;
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

    private class FrameworkWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //使用应用浏览器加载
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }

    private class FrameworkChromeClient extends WebChromeClient {

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
}
