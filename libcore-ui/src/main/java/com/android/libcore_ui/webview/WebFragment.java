package com.android.libcore_ui.webview;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.Spanned;
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
import com.android.libcore_ui.R;
import com.android.libcore_ui.activity.BaseFragment;
import com.android.libcore_ui.dialog.DialogFactory;
import com.android.libcore_ui.dialog.LoadingDialog;

import java.lang.reflect.Method;

/**
 * Description: WebView显示fragment
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-27
 */
public class WebFragment extends BaseFragment{

    private ProgressBar pb_bar;
    public WebView webView;
    private FrameworkWebViewClient webViewClient = new FrameworkWebViewClient();
    private FrameworkChromeClient chromeClient = new FrameworkChromeClient();

    private LoadingDialog dialog;
    @Override
    protected View setContentView(LayoutInflater inflater, @Nullable ViewGroup container) {
        return null;
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
        //支持js
        settings.setJavaScriptEnabled(true);

        dialog = new LoadingDialog(getActivity());
    }

    @Override
    protected void initData() {
    }

    /**
     * 加载url
     */
    public void loadUrl(String url){
        webView.loadUrl(url);
    }

    private class FrameworkWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //使用应用浏览器加载
            webView.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            dialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            dialog.dismiss();
        }
    }

    private class FrameworkChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            pb_bar.setProgress(newProgress);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            BitmapDrawable drawable = new BitmapDrawable(getResources(), icon);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            tv_title.setCompoundDrawables(drawable, null, null, null);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            tv_title.setText(title);
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
}
