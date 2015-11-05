package com.android.sample.test_utils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.framework.R;
import com.android.libcore_ui.activity.BaseActivity;

/**
 * Description: utils测试类
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-31
 */
public class UtilsActivity extends BaseActivity implements View.OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    protected void initView() {
        setContentViewSrc(R.layout.activity_test_utils);
        findViewById(R.id.btn_test_file).setOnClickListener(this);
        findViewById(R.id.btn_test_image).setOnClickListener(this);
        findViewById(R.id.btn_test_common).setOnClickListener(this);
    }

    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_test_file:
                startActivity(new Intent(this, FileActivity.class));
                break;
            case R.id.btn_test_image:
                startActivity(new Intent(this, ImageActivity.class));
                break;
            case R.id.btn_test_common:
                startActivity(new Intent(this, CommonActivity.class));
                break;
        }
    }
}
