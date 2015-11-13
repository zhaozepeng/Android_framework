package com.android.libcore_ui.web.webactivity;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.libcore.utils.CommonUtils;
import com.android.libcore_ui.R;
import com.android.libcore_ui.activity.BaseActivity;
import com.android.libcore_ui.web.WebFragment;

import java.util.ArrayList;

/**
 * Description: 应用基础的网页浏览activity
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-27
 */
public class WebActivity extends BaseActivity{

    public static final String EXTRA_URL = "extra_url";
    private WebActivityFragment webView;
    private ProgressBar pb_bar;
    private ImageView refresh;
    private boolean isLoading = false;
    /** 因为onReceivedTitle方法在goBack时不会调用，所以用一个list存储title */
    private ArrayList<String> titles = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    protected void initView() {
        setContentViewSrc(R.layout.activity_web_layout);
        webView = new WebActivityFragment();
        pb_bar = (ProgressBar) findViewById(R.id.pb_bar);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fl_content, webView);
        ft.commit();

        refresh = new ImageView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(CommonUtils.dp2px(30), CommonUtils.dp2px(30));
        params.setMargins(0, 0, CommonUtils.dp2px(10), 0);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        refresh.setLayoutParams(params);
        if (!isUseToolbar())
            addOptionsMenu(refresh);
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            ((ViewGroup) findViewById(R.id.fl_bottom_blank)).removeAllViews();
        }else{
            addNavigationOnBottom((ViewGroup) findViewById(R.id.fl_bottom_blank));
        }
    }

    protected void initData() {
        Bundle bundle = new Bundle();
        String url = getIntent().getStringExtra(EXTRA_URL);
        bundle.putString(WebFragment.EXTRA_URL, url);
        webView.setArguments(bundle);
        addNavigationOnBottom((ViewGroup) findViewById(R.id.fl_bottom_blank));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isUseToolbar()){
            getMenuInflater().inflate(R.menu.menu_webactivity_refresh, menu);
            MenuItem item = menu.findItem(R.id.menu_refresh);
            View view = MenuItemCompat.getActionView(item);
            ((ViewGroup) view).addView(refresh);
            return true;
        }
        else
            return super.onCreateOptionsMenu(menu);
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
        }else if (msg.what == 3){
            int newProgress = (int) msg.obj;
            if (newProgress == 100)
                pb_bar.setVisibility(View.GONE);
            else
                pb_bar.setVisibility(View.VISIBLE);
            pb_bar.setProgress(newProgress);
        }else if (msg.what == 4){
            Bitmap icon = (Bitmap) msg.obj;
            BitmapDrawable drawable = new BitmapDrawable(getResources(), icon);
            drawable.setBounds(0, 0, CommonUtils.dp2px(20), CommonUtils.dp2px(20));
//            setTitle.setCompoundDrawables(drawable, null, null, null);
        }else if (msg.what == 5){
            String title = (String) msg.obj;
            titles.add(" "+title);
            setTitle(" " + title);
        }
    }
}
