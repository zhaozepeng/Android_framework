package com.android.sample.test_guide;

import android.view.View;

import com.android.framework.R;
import com.android.libcore_ui.activity.BaseActivity;

/**
 * Description: 蒙版测试activity
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-16
 */
public class GuideActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void initView() {
        setContentViewSrc(R.layout.activity_test_guide);
        findViewById(R.id.btn_test_guide).setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_test_guide:
                break;
            default:
                break;
        }
    }
}
