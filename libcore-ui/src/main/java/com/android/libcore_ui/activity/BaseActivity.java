package com.android.libcore_ui.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.libcore.activity.RootActivity;
import com.android.libcore.log.L;
import com.android.libcore_ui.R;

/**
 * Description: 继承自{@link RootActivity}的基础activity，在这里进行页面界面
 * 的统一,在这里我将样式定位一个仿actionbar的样式，但是为了方便使用和扩展，所以将会使用一个
 * {@link RelativeLayout}做一个actionbar
 *
 *
 * <ul>
 * <li>{@linkplain #receiver}用来在组件之间进行广播的接收</li>
 * <li>{@linkplain #initView()}用来初始化该activity的view，第一步调用{@link #setContentViewSrc(Object)}
 * 进行设置布局，参数为layout的id或者view{@link #findViewById(int)}等等操作</li>
 * <li>{@linkplain #initData()}用来初始化该activity的data</li>
 * </ul>
 * <Strong>{@linkplain #initView()}和{@linkplain #initData()}需要子类实现</Strong>
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-08
 */
public abstract class BaseActivity extends RootActivity{

    /** 头部bar，如果某些activity需要改变bar样式，修改该view的子view即可 */
    protected ViewGroup ll_top_content;
    /** 返回按钮 */
    protected RelativeLayout rl_back;
    /** 该页面标题 */
    protected TextView tv_title;
    /** 右侧添加按钮区域 */
    protected RelativeLayout rl_top_extra_content;
    /** 内容区域 */
    protected FrameLayout content;
    /** 底部popWindow */
    protected LinearLayout ll_bottom_popWindow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_base_layout);
        ll_top_content = (ViewGroup) findViewById(R.id.ll_top_content);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        rl_top_extra_content = (RelativeLayout) findViewById(R.id.rl_top_extra_content);
        content = (FrameLayout) findViewById(R.id.content);
        ll_bottom_popWindow = (LinearLayout) findViewById(R.id.ll_bottom_popWindow);

        /** 通过 android::label 设置的标题 */
        if (!TextUtils.isEmpty(getTitle()))
            tv_title.setText(getTitle());
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
        initData();
    }

    /** <Strong><font color=red>第一步</font></Strong>调用{@link #setContentViewSrc(Object)}进行布局的设置 */
    protected abstract void initView();
    protected abstract void initData();

    /**
     * 设置标题
     */
    protected void setTitle(String title){
        tv_title.setText(title);
    }

    /**
     * 用来设置该页面需要显示的内容，不包括topbar
     * @param object
     */
    protected void setContentViewSrc(Object object){
        if (object instanceof Integer){
            LayoutInflater inflater = LayoutInflater.from(this);
            View v = inflater.inflate((Integer)object, null);
            content.addView(v);
        }else if(object instanceof View){
            content.addView((View)object);
        }else{
            L.e("参数只能为id或者view");
        }
    }

    /**
     * 通过添加item到底部bar来创建一系列的选项
     * @param groupId
     * @param itemId
     * @param name
     */
    protected void addItemToBottomPopWindow(int groupId, int itemId, String name){
    }
}
