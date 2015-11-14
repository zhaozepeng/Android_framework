package com.android.sample.test_widget;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.framework.R;
import com.android.libcore.utils.CommonUtils;
import com.android.libcore_ui.activity.BaseActivity;
import com.android.libcore_ui.widget.GridLinearLayout;

/**
 * Description: #TODO
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-11-14
 */
public class GridLinearLayoutActivity extends BaseActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewSrc(R.layout.activity_test_grid);
        GridLinearLayout gll_content = (GridLinearLayout) findViewById(R.id.gll_content);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(CommonUtils.dp2px(100), CommonUtils.dp2px(100));
        lp.gravity = Gravity.CENTER;

        for (int i=2; i<10; i++){
            TextView textView = new TextView(this);
            textView.setLayoutParams(lp);
            textView.setText(i + "");
            textView.setTextSize(20);
            gll_content.addView(textView);
        }
    }
}
