package com.android.sample.test_widget;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.framework.R;
import com.android.libcore_ui.activity.BaseActivity;
import com.android.libcore_ui.widget.FlowLayout;
import com.android.libcore_ui.widget.GridLinearLayout;

/**
 * Description: 测试导入控件的activity
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-20
 */
public class WidgetActivity extends BaseActivity implements View.OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewSrc(R.layout.activity_test_widget);
        findViewById(R.id.btn_test_gridlayout).setOnClickListener(this);
        findViewById(R.id.btn_test_flowlayout).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_test_gridlayout){
            startActivity(new Intent(this, GridLinearLayout.class));
        }else if (v.getId() == R.id.btn_test_flowlayout){
            startActivity(new Intent(this, FlowLayoutActivity.class));
        }
    }
}
