package com.android.sample.test_webview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.android.framework.R;
import com.android.libcore_ui.activity.BaseActivity;
import com.android.libcore_ui.webview.WebFragment;
import com.android.libcore_ui.webview.webactivity.WebActivity;

/**
 * Description: 测试WebView
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-27
 */
public class WebViewActivity extends BaseActivity implements View.OnClickListener{
    @Override
    protected void initView() {
        setContentViewSrc(R.layout.activity_test_webview);
        findViewById(R.id.btn_webview).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        WebFragment webFragment = new WebFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fl_webfragment, webFragment);
        ft.commit();
        Bundle bundle = new Bundle();
        bundle.putString(WebFragment.EXTRA_URL, "file:///android_asset/1.html");
        webFragment.setArguments(bundle);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, WebActivity.class);
//        intent.putExtra(WebActivity.EXTRA_URL, "https://www.baidu.com");
        //加载assets文件
        intent.putExtra(WebActivity.EXTRA_URL, "file:///android_asset/baidu.html");
        //加载SD卡文件
//        intent.putExtra(WebActivity.EXTRA_URL, "file:///sdcard/FrameWork/html/baidu.html");
        startActivity(intent);
    }
}
