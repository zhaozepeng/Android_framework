package com.android.sample.test_utils;

import android.os.Bundle;
import android.widget.TextView;

import com.android.framework.R;
import com.android.libcore.utils.CommonUtils;
import com.android.libcore_ui.activity.BaseActivity;

/**
 * Description: 测试{@link com.android.libcore.utils.CommonUtils}类
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-09-04
 */
public class CommonActivity extends BaseActivity{
    TextView getScreenWidth;
    TextView getScreenHeight;
    TextView isNetworkWifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    protected void initView() {
        setContentView(R.layout.activity_test_common);
        getScreenWidth = $(R.id.getScreenWidth);
        getScreenHeight = $(R.id.getScreenHeight);
        isNetworkWifi = $(R.id.isNetworkWifi);
        getScreenWidth.setText(CommonUtils.getScreenWidth()+"");
        getScreenHeight.setText(CommonUtils.getScreenHeight()+"");
        isNetworkWifi.setText(CommonUtils.isNetworkWifi()+"");
    }

    protected void initData() {

    }
}
