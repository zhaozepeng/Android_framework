package com.android.libcore_ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.libcore.activity.RootFragment;

/**
 * Description: 基础fragment
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-27
 */
public abstract class BaseFragment extends RootFragment{

    /** fragment所依附的activity */
    protected BaseActivity activity;
    /** 整个activity的头部bar，如果某些activity需要改变bar样式，修改该view的子view即可 */
    public ViewGroup top_bar;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof BaseActivity) {
            this.activity = (BaseActivity) activity;
            top_bar = ((BaseActivity) activity).top_bar;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewContainer = setContentView(inflater, container);
        initView();
        initData();
        return viewContainer;
    }

    protected abstract View setContentView(LayoutInflater inflater, @Nullable ViewGroup container);

    protected abstract void initView();
    protected abstract void initData();

    /**
     * 根据id值获取view
     */
    protected View findViewById(int id){
        return viewContainer.findViewById(id);
    }

    /**
     * 设置该activity页面的标题
     */
    protected void setTitle(String title){
        activity.setTitle(title);
    }

    /**
     * 用来fragment和activity之间的通信
     * @param msg fragment发送给activity的消息
     */
    protected void sendMessageToActivity(Message msg){
        activity.onHandleMessageFromFragment(msg);
    }
}
