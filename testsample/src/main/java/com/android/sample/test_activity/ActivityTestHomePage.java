package com.android.sample.test_activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.android.framework.R;
import com.android.libcore.Toast.T;
import com.android.libcore.activity.ActivityManager;
import com.android.libcore.application.RootApplication;
import com.android.libcore.log.L;
import com.android.libcore_ui.activity.BaseActivity;
import com.android.sample.HomeTestActivity;

/**
 * Description: 测试activity功能主页
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-09
 */
public class ActivityTestHomePage extends BaseActivity implements View.OnClickListener{
    private Button btn_test_weakReference;
    private Button btn_test_weakReference2;
    private Button btn_test_broadcast;
    private Button btn_test_bottom_popwindow;

    @Override
    protected void initView() {
        setContentViewSrc(R.layout.activity_test_activity_homepage);
        btn_test_weakReference = (Button) findViewById(R.id.btn_test_weakReference);
        btn_test_weakReference2 = (Button) findViewById(R.id.btn_test_weakReference2);
        btn_test_broadcast = (Button) findViewById(R.id.btn_test_broadcast);
        btn_test_bottom_popwindow = (Button) findViewById(R.id.btn_test_bottom_popwindow);

        btn_test_weakReference.setOnClickListener(this);
        btn_test_weakReference2.setOnClickListener(this);
        btn_test_broadcast.setOnClickListener(this);
        btn_test_bottom_popwindow.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        addItemToBottomPopWindow(0, 0, "0测试0");
        addItemToBottomPopWindow(0, 1, "0测试1");
        addItemToBottomPopWindow(0, 2, "0测试2");

        addItemToBottomPopWindow(1, 0, "1测试0");
        addItemToBottomPopWindow(1, 1, "1测试1");

        addItemToBottomPopWindow(2, 0, "2测试0");
        addItemToBottomPopWindow(2, 1, "2测试1");
        addItemToBottomPopWindow(2, 2, "2测试2");

        addItemToBottomPopWindow(3, 0, "3测试0");
        addItemToBottomPopWindow(3, 1, "3测试1");
        addItemToBottomPopWindow(3, 2, "3测试2");
        addItemToBottomPopWindow(3, 3, "3测试3");

        addItemToBottomPopWindow(4, 1, "4测试1");
        addItemToBottomPopWindow(4, 2, "4测试2");
        addItemToBottomPopWindow(4, 3, "4测试3");
        addItemToBottomPopWindow(4, 4, "4测试4");

        addItemToBottomPopWindow(5, 0, "5测试0");
        addItemToBottomPopWindow(5, 1, "5测试1");
        addItemToBottomPopWindow(5, 2, "5测试2");
        addItemToBottomPopWindow(5, 3, "5测试3");
        addItemToBottomPopWindow(5, 4, "5测试4");
        addItemToBottomPopWindow(5, 5, "5测试5");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_test_weakReference:
                startActivity(new Intent(this, ActivityA.class));
                break;
            case R.id.btn_test_weakReference2:
                T.getInstance().showShort(RootApplication.getInstance().toString());
                break;
            case R.id.btn_test_broadcast:
                sendBroadcast(new Intent(HomeTestActivity.ACTION));
                ActivityManager.getInstance().finishActivity();
                break;
            case R.id.btn_test_bottom_popwindow:
//                try {
                    removeItemFromBottomPopWindow(1, 1);
                    removeItemFromBottomPopWindow(2, 2);
                    removeItemFromBottomPopWindow(3, 3);
                    removeItemFromBottomPopWindow(4, 4);
                    removeItemFromBottomPopWindow(5, 5);
//                }catch (Exception e){
////                    try {
////                        removeItemFromBottomPopWindow(5, 0);
////                        removeItemFromBottomPopWindow(5, 1);
////                        removeItemFromBottomPopWindow(5, 2);
////                        removeItemFromBottomPopWindow(5, 3);
////                        removeItemFromBottomPopWindow(5, 4);
////                    }catch (Exception e1){
////                    }
//                }
                showBottomPopWindow();
                break;
        }
    }

    @Override
    protected void onItemClickCallback(int groupId, int itemId) {
        super.onItemClickCallback(groupId, itemId);
        T.getInstance().showShort("groupId:"+groupId+" ItemId:"+itemId);
    }
}
