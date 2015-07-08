package com.android.libcore.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.android.libcore.application.RootApplication;

/**
 * Description: 所有基础{@linkplain Activity}的基类，所有的Activity应该
 * 继承自该基类，以便进行context的管理、页面的管理等<br/>
 *
 * <li>{@linkplain #receiver}用来在组件之间进行广播的接收</li>
 * <li>{@linkplain #initView()}用来初始化该activity的view，findviewbyid等等操作</li>
 * <li>{@linkplain #initData()}用来初始化该activity的data</li>
 * <Strong>{@linkplain #initView()}和{@linkplain #initData()}需要子类实现</Strong>
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-07
 */
public abstract class RootActivity extends Activity{

    /** 用来在页面之间进行广播的传递 */
    private BroadcastReceiver receiver;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        RootApplication.setInstanceRef(this);
        ActivityManager.getInstance().addActivity(this);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                RootActivity.this.onReceive(context, intent);
            }
        };
        initView();
        initData();
    }

    protected abstract void initView();
    protected abstract void initData();

    /**
     * 用来在注册广播之后进行广播的接收处理
     * @param context
     * @param intent
     */
    protected void onReceive(Context context, Intent intent){}

    /**
     * 用来注册广播
     * @param action 需要注册广播的action
     */
    public void registerReceiver(String action){
        IntentFilter filter = new IntentFilter(action);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        ActivityManager.getInstance().removeActivity(this);
    }
}
