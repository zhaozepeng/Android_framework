package com.android.sample.test_widget;

import android.os.Bundle;

import com.android.framework.R;
import com.android.libcore_ui.activity.BaseActivity;

/**
 * Description: #TODO
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-11-14
 */
public class FlowLayoutActivity extends BaseActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewSrc(R.layout.activity_test_flow);
    }
}
