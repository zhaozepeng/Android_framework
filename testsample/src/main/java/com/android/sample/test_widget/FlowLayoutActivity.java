package com.android.sample.test_widget;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.framework.R;
import com.android.libcore.utils.CommonUtils;
import com.android.libcore_ui.activity.BaseActivity;
import com.android.libcore_ui.widget.FlowLayout;

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
        FlowLayout fl_content = (FlowLayout) findViewById(R.id.fl_content);
        for (int i=0; i<50; i++){
            TextView view = new TextView(this);
            view.setTextColor(getResources().getColor(android.R.color.black));
            view.setTextSize(20);
            if (i%2==0){
                view.setBackgroundColor(getResources().getColor(R.color.darkorchid));
            }else{
                view.setBackgroundColor(getResources().getColor(R.color.palegreen));
            }
            int width = (int) (CommonUtils.dp2px(30) + Math.random()*300);
            int height = (int) (CommonUtils.dp2px(30) + Math.random()*300);
            view.setText(CommonUtils.px2dp(width)+"dp");
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(width, height);
            view.setLayoutParams(lp);
            fl_content.addView(view);
        }
    }
}
