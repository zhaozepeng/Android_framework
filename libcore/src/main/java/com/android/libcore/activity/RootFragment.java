package com.android.libcore.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Description: 所有基础{@linkplain Fragment}的基类<br/>
 * <a href=http://blog.csdn.net/u012403246/article/details/46371643>从今天开始抛弃Fragment吧</a><br/>
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-08
 */
public abstract class RootFragment extends Fragment{
    protected BroadcastReceiver mReceiver;
    protected View mViewContainer;
    private Boolean mIsNeedUnRegister = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                this.onReceive(context, intent);
            }
        };
    }

    protected void onReceive(Context context, Intent intent){}

    protected void registerReceiver(String action){
        IntentFilter filter = new IntentFilter(action);
        getActivity().registerReceiver(mReceiver, filter);
        mIsNeedUnRegister = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mIsNeedUnRegister)
            getActivity().unregisterReceiver(mReceiver);
    }
}
