package com.android.sample.test_netapi;

import android.view.ViewGroup;

import com.android.framework.R;
import com.android.libcore_ui.activity.BaseActivity;

/**
 * Description: 测试网络请求
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-09-16
 */
public class NetActivity extends BaseActivity{
    @Override
    protected void initView() {
        setContentViewSrc(R.layout.activity_test_net);
        addNavigationOnBottom((ViewGroup) findViewById(R.id.ll_content));
    }

    @Override
    protected void initData() {

    }
}
