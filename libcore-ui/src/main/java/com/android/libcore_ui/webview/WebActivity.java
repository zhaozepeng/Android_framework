package com.android.libcore_ui.webview;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.android.libcore_ui.R;
import com.android.libcore_ui.activity.BaseActivity;

/**
 * Description: 应用基础的网页浏览activity
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-27
 */
public class WebActivity extends BaseActivity{

    public static final String EXTRA_URL = "extra_url";
    private WebFragment webView;

    @Override
    protected void initView() {
        setContentViewSrc(R.layout.activity_web_layout);
        webView = new WebFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fl_content, webView);
        ft.commit();
    }

    @Override
    protected void initData() {
        String url = getIntent().getStringExtra(EXTRA_URL);
        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (webView.webView.canGoBack())
            webView.webView.goBack();
        else
            super.onBackPressed();
    }
}
