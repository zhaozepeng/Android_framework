package com.android.libcore_ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.libcore.activity.RootActivity;
import com.android.libcore.log.L;
import com.android.libcore.utils.CommonUtils;
import com.android.libcore_ui.R;

/**
 * Description: 继承自{@link RootActivity}的基础activity，在这里进行页面界面的统一<br/><br/>
 *
 * 使用{@link #setContentView(int)}，{@link #setContentView(View)}和
 * {@link #setContentView(View, ViewGroup.LayoutParams)}来设置内容区域布局<br/><br/>
 *
 * 使用{@link #setOriginalContentView(int)}，{@link #setOriginalContentView(View)}和
 * {@link #setOriginalContentView(View, ViewGroup.LayoutParams)}来设置整体布局<br/><br/>
 *
 * 应用整体样式现在有status bar透明和底部navigation bar透明两种样式<br/>
 * 应用可以使用默认action bar样式，自定义top bar样式有ViewGroup和toolbar两种样式<br/><br/>
 *
 *
 * <ol>
 * <li>{@linkplain #mReceiver}用来在组件之间进行广播的接收</li>
 * <li>{@linkplain #setTitle(String)}用来设置页面标题</li>
 * <li>{@linkplain #addOptionsMenu(View)}用来在自定义top bar的情况下在右侧添加一个按钮</li>
 * <li>{@linkplain #onCreateOptionsMenu(Menu)}用来在使用toolbar的情况下操作toolbar</li>
 * <li>{@linkplain #addNavigationOnBottom(ViewGroup)}将一个和NavigationBar的高度一样的空白的view添加到viewGroup中</li>
 * <li>{@linkplain #onHandleMessageFromFragment(Message)}用来处理fragment传递过来的消息</li>
 * </ol>
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-08
 */
public abstract class BaseActivity extends RootActivity{

    /** 填充19版本以上SDK　status bar */
    protected View v_status_bar;
    /** 头部top bar容器 */
    protected FrameLayout fl_top_bar;
    /** 头部top bar */
    protected View top_bar;
    /** 默认action bar */
    protected ActionBar actionBar;
    /** 内容区域 */
    protected FrameLayout base_content;

    /** 底部navigation是否透明，如果使用的是Activity_translucent_navigation_bar style，
     * 变量则会变成true，调用addBlankOnBottom(View view)函数可以将一个空白的view添加到
     * 底部用以填充透明的navigation bar，防止覆盖内容区域 */
    protected boolean isNavigationTransparent;

    /**应用如果使用NoActionBar主题，则默认为使用toolbar，反之则使用系统actionbar */
    protected boolean useToolbar = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //如果系统的主题为Activity_translucent_navigation_bar，但是手机没有navigation bar，
        // 则将其设置回status bar主题，setTheme()调用一定要在onCreate()之前
        if (!CommonUtils.hasNavigationBar()
                && getApplicationInfo().theme==R.style.Activity_translucent_navigation_bar) {
            setTheme(R.style.Activity_translucent_status_bar);
        }
        super.onCreate(savedInstanceState);
        initLayout();
    }

    /**
     * 初始化布局
     */
    protected void initLayout(){
        setOriginalContentView(R.layout.activity_base_layout);
        base_content = (FrameLayout) findViewById(R.id.base_content);

        defineStyle();

        //如果系统没有指定action bar，则选用自定义action bar
        if (getSupportActionBar()==null)
            chooseTopBar();
        else{
            useToolbar = false;
            actionBar = getSupportActionBar();
        }
    }

    /**
     * 定义应用的基本样式
     */
    protected void defineStyle(){
        //SDK19版本以上才支持样式选择
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT  &&
            (getApplicationInfo().theme==R.style.Activity_translucent_status_bar ||
            getApplicationInfo().theme==R.style.Activity_translucent_navigation_bar)){

            v_status_bar = findViewById(R.id.v_status_bar);
            int id = getResources().getIdentifier("status_bar_height", "dimen", "android");
            v_status_bar.getLayoutParams().height = getResources().getDimensionPixelOffset(id);

            //21版本以下使用windowTranslucentStatus，使用自定义view填充status bar
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                v_status_bar.setBackgroundColor(ContextCompat.getColor(this, R.color.bar_color));
                v_status_bar.setVisibility(View.VISIBLE);
            }
            //21版本和以上，特殊处理
            else{
                if (getApplicationInfo().theme==R.style.Activity_translucent_navigation_bar){
//                    v_status_bar.setBackgroundColor(ContextCompat.getColor(this, R.color.bar_color));
//                    v_status_bar.setVisibility(View.VISIBLE);
                }
            }

            if (CommonUtils.hasNavigationBar()) {
                isNavigationTransparent = (getApplicationInfo().theme == R.style.Activity_translucent_navigation_bar);
            }
        }
    }

    /**
     * 选择应用top bar方式
     */
    protected void chooseTopBar(){
        fl_top_bar = (FrameLayout) findViewById(R.id.fl_top_bar);
        //添加top bar，现在有两种样式的top bar可以使用：一种是自定的viewGroup
        if (!useToolbar) {
            top_bar = View.inflate(this, R.layout.activity_top_bar_layout, null);
            View rl_back = top_bar.findViewById(R.id.rl_back);
            rl_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            TextView tv_title = (TextView) top_bar.findViewById(R.id.tv_title);
            // 通过 android::label 设置的标题
            if (!TextUtils.isEmpty(getTitle()))
                tv_title.setText(getTitle());
        }
        //一种是使用系统控件toolbar
        else{
            top_bar = View.inflate(this, R.layout.activity_top_toolbar_layout, null);
            Toolbar toolbar = (Toolbar) top_bar;
            setSupportActionBar(toolbar);
            toolbar.setTitleTextAppearance(this, R.style.toolbar_title_appearance);
            toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
            toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            // 通过 android::label 设置的标题
            if (!TextUtils.isEmpty(getTitle()))
                toolbar.setTitle(getTitle());
        }
        fl_top_bar.addView(top_bar);
    }

    /**
     * 设置内容区域布局
     */
    @Override
    public void setContentView(int layoutResID) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(layoutResID, null);
        base_content.addView(v);
    }

    /**
     * 省去类型转换
     */
    protected <T extends View> T $(int id){
        return (T) super.findViewById(id);
    }

    /**
     * 设置内容区域布局
     */
    @Override
    public void setContentView(View view) {
        base_content.addView(view);
    }

    /**
     * 设置内容区域布局
     */
    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        base_content.addView(view, params);
    }

    /**
     * 设置整体布局
     */
    protected void setOriginalContentView(int layoutResID){
        super.setContentView(layoutResID);
    }

    /**
     * 设置整体布局
     */
    protected void setOriginalContentView(View view){
        super.setContentView(view);
    }

    /**
     * 设置整体布局
     */
    protected void setOriginalContentView(View view, ViewGroup.LayoutParams params){
        super.setContentView(view, params);
    }

    /**
     * 设置标题
     */
    protected void setTitle(String title){
        if (!useToolbar) {
            if (actionBar == null)
                ((TextView) top_bar.findViewById(R.id.tv_title)).setText(title);
            else
                actionBar.setTitle(title);
        }else{
            ((Toolbar) top_bar).setTitle(title);
        }
    }

    /**
     * 将view添加进top bar右侧的相关区域中，适用于不使用toolbar样式
     */
    protected void addOptionsMenu(View view){
        if (useToolbar || actionBar!=null){
            L.e("该样式无法使用addOptionsMenuView，请使用onCreateOptionsMenu");
        }else{
            ((ViewGroup) top_bar.findViewById(R.id.rl_top_extra_content)).addView(view);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (useToolbar || actionBar!=null) {
            return super.onCreateOptionsMenu(menu);
        }else{
            L.e("该样式无法使用onCreateOptionsMenu，请使用addOptionsMenu");
            return false;
        }
    }

    /**
     * 将一个空白和navigation bar高度一致的view添加进传入的参数view中，保证传进来的view要在正确的位置
     */
    public void addNavigationOnBottom(ViewGroup view){
        if (CommonUtils.hasNavigationBar() && isNavigationTransparent) {
            int id = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            View navigationView = new View(this);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(id));
            navigationView.setLayoutParams(params);
            navigationView.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
            view.addView(navigationView);
        }
    }

    /**
     * 是否使用toolbar样式
     */
    public boolean isUseToolbar() {
        return useToolbar;
    }

    /**
     * 处理来自fragment的消息
     */
    protected void onHandleMessageFromFragment(Message msg){}
}
