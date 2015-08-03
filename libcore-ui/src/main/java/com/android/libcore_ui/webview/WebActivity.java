package com.android.libcore_ui.webview;

import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.libcore.utils.CommonUtils;
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
    private ImageView refresh;
    private boolean isLoading = false;

    @Override
    protected void initView() {
        setContentViewSrc(R.layout.activity_web_layout);
        webView = new WebFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fl_content, webView);
        ft.commit();

        refresh = new ImageView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(CommonUtils.dp2px(30), CommonUtils.dp2px(30));
        params.setMargins(0, 0, CommonUtils.dp2px(10), 0);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        refresh.setLayoutParams(params);
        addOptionsMenuView(refresh);
        refresh.setBackgroundResource(R.mipmap.ic_refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoading)
                    webView.stopLoading();
                else
                    webView.refresh();
            }
        });
    }

    @Override
    protected void initData() {
        String url = getIntent().getStringExtra(EXTRA_URL);
        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack())
            webView.goBack();
        else
            super.onBackPressed();
    }

    @Override
    protected void onHandleMessageFromFragment(Message msg) {
        if (msg.what == 1){
            isLoading = true;
            refresh.setBackgroundResource(R.mipmap.ic_refresh_close);
        }else if (msg.what == 2){
            isLoading = false;
            refresh.setBackgroundResource(R.mipmap.ic_refresh);
        }
    }
}
