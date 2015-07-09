package com.android.sample.test_activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.android.framework.R;
import com.android.libcore_ui.activity.BaseActivity;

/**
 * Description: 测试activity功能主页
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-09
 */
public class ActivityTestHomePage extends BaseActivity implements View.OnClickListener{
    private Button btn_test_weakReference;
    private Button btn_test_broadcast;

    @Override
    protected void initView() {
        setContentViewSrc(R.layout.activity_test_activity_homepage);
        btn_test_weakReference = (Button) findViewById(R.id.btn_test_weakReference);
        btn_test_broadcast = (Button) findViewById(R.id.btn_test_broadcast);

        btn_test_weakReference.setOnClickListener(this);
        btn_test_broadcast.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, ActivityA.class));
    }
}
