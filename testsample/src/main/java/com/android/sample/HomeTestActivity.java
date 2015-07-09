package com.android.sample;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.android.framework.R;
import com.android.libcore_ui.BaseActivity;
import com.android.sample.test_activity.ActivityA;
import com.android.sample.test_activity.ActivityTestHomePage;

/**
 * Description: homepage
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-08
 */
public class HomeTestActivity extends BaseActivity implements View.OnClickListener{

    /** 测试activity */
    private Button btn_test_activity;

    @Override
    protected void initView() {
        setContentViewSrc(R.layout.activity_home_test);

        btn_test_activity = (Button) findViewById(R.id.btn_test_activity);
        btn_test_activity.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.btn_test_activity:
                intent.setClass(this, ActivityTestHomePage.class);
                break;
            default:
                return;
        }
        startActivity(intent);
    }
}
