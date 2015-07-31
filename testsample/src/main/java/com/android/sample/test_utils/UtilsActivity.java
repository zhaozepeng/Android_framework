package com.android.sample.test_utils;

import android.content.Intent;
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
    protected void initView() {
        setContentViewSrc(R.layout.activity_test_utils);
        findViewById(R.id.btn_test_file).setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_test_file:
                startActivity(new Intent(this, FileActivity.class));
                break;
        }
    }
}
