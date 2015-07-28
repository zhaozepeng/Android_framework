package com.android.sample;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.framework.R;
import com.android.libcore_ui.activity.BaseActivity;
import com.android.sample.test_activity.ActivityTestHomePage;
import com.android.sample.test_cache.CacheActivity;
import com.android.sample.test_db.DBActivity;
import com.android.sample.test_dialog.DialogActivity;
import com.android.sample.test_guide.GuideActivity;
import com.android.sample.test_webview.WebViewActivity;

/**
 * Description: homepage
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-08
 */
public class HomeTestActivity extends BaseActivity implements View.OnClickListener{

    public static final String ACTION = "action_from_activity_test_home_page";

    /** 测试activity */
    private Button btn_test_activity;
    /** 测试蒙版 */
    private Button btn_test_guide;
    /** 测试dialog */
    private Button btn_test_dialog;
    /** 测试数据库 */
    private Button btn_test_db;
    /** 测试cache */
    private Button btn_test_cache;
    /** 测试webview */
    private Button btn_test_webview;
    private TextView tv_info;

    @Override
    protected void initView() {
        setContentViewSrc(R.layout.activity_home_test);

        btn_test_activity = (Button) findViewById(R.id.btn_test_activity);
        btn_test_guide = (Button) findViewById(R.id.btn_test_guide);
        btn_test_dialog = (Button) findViewById(R.id.btn_test_dialog);
        btn_test_db = (Button) findViewById(R.id.btn_test_db);
        btn_test_cache = (Button) findViewById(R.id.btn_test_cache);
        btn_test_webview = (Button) findViewById(R.id.btn_test_webview);
        btn_test_activity.setOnClickListener(this);
        btn_test_guide.setOnClickListener(this);
        btn_test_dialog.setOnClickListener(this);
        btn_test_db.setOnClickListener(this);
        btn_test_cache.setOnClickListener(this);
        btn_test_webview.setOnClickListener(this);
        tv_info = (TextView) findViewById(R.id.tv_info);
        //http://stackoverflow.com/questions/2444040/naming-my-application-in-android
        setTitle("主页");
    }

    @Override
    protected void initData() {
        registerReceiver(ACTION);
    }

    @Override
    protected void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(ACTION)){
            tv_info.setText("有一个从activity_home_page来的广播");
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.btn_test_activity:
                intent.setClass(this, ActivityTestHomePage.class);
                break;
            case R.id.btn_test_guide:
                intent.setClass(this, GuideActivity.class);
                break;
            case R.id.btn_test_dialog:
                intent.setClass(this, DialogActivity.class);
                break;
            case R.id.btn_test_db:
                intent.setClass(this, DBActivity.class);
                break;
            case R.id.btn_test_cache:
                intent.setClass(this, CacheActivity.class);
                break;
            case R.id.btn_test_webview:
                intent.setClass(this, WebViewActivity.class);
                break;
            default:
                return;
        }
        startActivity(intent);
    }
}
