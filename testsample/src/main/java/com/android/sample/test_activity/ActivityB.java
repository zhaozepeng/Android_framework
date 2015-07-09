package com.android.sample.test_activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.framework.R;
import com.android.libcore.activity.ActivityManager;
import com.android.libcore.application.RootApplication;
import com.android.libcore_ui.activity.BaseActivity;

/**
 * Description: 测试类B
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-09
 */
public class ActivityB extends BaseActivity{

    private Button btn_go_to_activity;
    private TextView tv_all_info;

    @Override
    protected void initView() {
        setContentViewSrc(R.layout.activity_test_activity_b);
        btn_go_to_activity = (Button) findViewById(R.id.btn_go_to_activity);
        btn_go_to_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityB.this, ActivityA.class));
            }
        });
        tv_all_info = (TextView) findViewById(R.id.tv_all_info);
    }

    @Override
    protected void initData() {
        StringBuilder sb = new StringBuilder();
        sb.append(RootApplication.getInstance().toString()).append("\n");
        sb.append(ActivityManager.getInstance().getStackInfo()).append("\n");
        tv_all_info.setText(sb.toString());
    }
}
