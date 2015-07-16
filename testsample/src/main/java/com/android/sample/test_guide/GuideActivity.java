package com.android.sample.test_guide;

import android.view.View;

import com.android.framework.R;
import com.android.libcore.guide.GuideManager;
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
        findViewById(R.id.btn_test_guide_full_screen).setOnClickListener(this);
        findViewById(R.id.btn_test_guide_content).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        GuideManager.getInstance().initMask(R.layout.guide_test_1, R.layout.guide_test_2, R.layout.guide_test_3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_test_guide_full_screen:
                GuideManager.getInstance().showMaskFullScreen();
                break;
            case R.id.btn_test_guide_content:
                GuideManager.getInstance().showMaskInContent();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        //记住在页面蒙版使用完成之后，一定要清空蒙版的内容
        GuideManager.getInstance().clearMask();
        super.onDestroy();
    }
}
