package com.android.sample;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.framework.R;
import com.android.libcore_ui.activity.BaseActivity;
import com.android.sample.test_activity.ActivityTestHomePage;
import com.android.sample.test_guide.GuideActivity;

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
    private TextView tv_info;

    @Override
    protected void initView() {
        setContentViewSrc(R.layout.activity_home_test);

        btn_test_activity = (Button) findViewById(R.id.btn_test_activity);
        btn_test_guide = (Button) findViewById(R.id.btn_test_guide);
        btn_test_activity.setOnClickListener(this);
        btn_test_guide.setOnClickListener(this);
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
            default:
                return;
        }
        startActivity(intent);
    }
}
