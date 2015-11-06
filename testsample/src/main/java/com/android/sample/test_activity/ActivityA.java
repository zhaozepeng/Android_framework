package com.android.sample.test_activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.framework.R;
import com.android.libcore.Toast.T;
import com.android.libcore.activity.ActivityManager;
import com.android.libcore.application.RootApplication;
import com.android.libcore_ui.activity.BaseActivity;

/**
 * Description: 测试类A
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-09
 */
public class ActivityA extends BaseActivity implements View.OnClickListener{
    private Button btn_go_to_activity;
    private Button btn_finish_top_activity;
    private Button btn_finish_first_B_activity;
    private Button btn_finish_all_B_activity;
    private Button btn_return_to_home_page;
    private Button btn_close;

    private TextView tv_all_info;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    protected void initView() {
        setContentViewSrc(R.layout.activity_test_activity_a);
        btn_go_to_activity = (Button) findViewById(R.id.btn_go_to_activity);
        btn_finish_top_activity = (Button) findViewById(R.id.btn_finish_top_activity);
        btn_finish_first_B_activity = (Button) findViewById(R.id.btn_finish_first_B_activity);
        btn_finish_all_B_activity = (Button) findViewById(R.id.btn_finish_all_B_activity);
        btn_return_to_home_page = (Button) findViewById(R.id.btn_return_to_home_page);
        btn_close = (Button) findViewById(R.id.btn_close);

        btn_go_to_activity.setOnClickListener(this);
        btn_finish_top_activity.setOnClickListener(this);
        btn_finish_first_B_activity.setOnClickListener(this);
        btn_finish_all_B_activity.setOnClickListener(this);
        btn_return_to_home_page.setOnClickListener(this);
        btn_close.setOnClickListener(this);

        tv_all_info = (TextView) findViewById(R.id.tv_all_info);

        addNavigationOnBottom((ViewGroup) findViewById(R.id.fl_navigation));
    }

    protected void initData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        showStackInfo();
    }

    private void showStackInfo(){
        T.getInstance().setGravity(Gravity.TOP).setyOffset(10).setyOffset(10).showShort(RootApplication.getInstance().toString());
        StringBuilder sb = new StringBuilder();
        sb.append("activityManager:   ").append(ActivityManager.getInstance().getStackInfo()).append("\n");
        tv_all_info.setText(sb.toString());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        //singleinstance
        super.onNewIntent(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_go_to_activity:
                startActivity(new Intent(ActivityA.this, ActivityB.class));
                break;
            case R.id.btn_finish_top_activity:
                ActivityManager.getInstance().finishActivity();
                break;
            case R.id.btn_finish_first_B_activity:
                ActivityManager.getInstance().finishLastActivity(ActivityB.class);
                break;
            case R.id.btn_finish_all_B_activity:
                ActivityManager.getInstance().finishAllActivity(ActivityB.class);
                break;
            case R.id.btn_return_to_home_page:
                ActivityManager.getInstance().finishAfterActivity(ActivityTestHomePage.class);
                break;
            case R.id.btn_close:
                ActivityManager.getInstance().finishAllActivityAndClose();
                break;
        }
        showStackInfo();
    }
}
