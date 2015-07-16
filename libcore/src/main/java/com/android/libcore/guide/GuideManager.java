package com.android.libcore.guide;

import android.app.Activity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.libcore.R;
import com.android.libcore.application.RootApplication;
import com.android.libcore.log.L;

import java.util.ArrayList;

/**
 * Description: 蒙版管理器，用来管理页面的蒙版<br/>
 * 使用{@link #initMask(Integer...)}传入资源layout的id，最后调用
 * {@link #showMask()}显示蒙版，注意传入的layout文件中请在需要点击消失
 * 的地方加入一个名字为<strong><em>click_to_disappear</em></strong>的View即可
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-15
 */
public class GuideManager{
    private static volatile GuideManager instance = null;
    private ArrayList<View> views;
    private int currentShowId;
    private View.OnClickListener listener;

    private GuideManager(){}

    public static GuideManager getInstance(){
        if (instance == null){
            synchronized (GuideManager.class){
                if (instance == null){
                    instance = new GuideManager();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化监听器
     */
    private void initListener(){
        listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentShowId ++;
                //将上一次的蒙版去除
                ((ViewGroup)(((Activity)(RootApplication.getInstance())).getWindow().getDecorView()))
                        .removeViewAt(0);
                if (currentShowId < views.size()){
                    ((ViewGroup)(((Activity)(RootApplication.getInstance())).getWindow().getDecorView()))
                            .addView(views.get(currentShowId), 0,
                                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.MATCH_PARENT));
                }
            }
        };
    }

    /**
     * 此函数用来传入蒙版的id，蒙版的显示顺序将会和参数的顺序一致
     * @param layoutIds 需要展示蒙版的资源id，按照显示顺序传递即可
     */
    public void initMask(Integer... layoutIds){
        LayoutInflater inflater = LayoutInflater.from(RootApplication.getInstance());
        try{
            int btnId = RootApplication.getInstance().getResources()
                    .getIdentifier("click_to_disappear", "id", RootApplication.getInstance().getPackageName());
            if (btnId == 0){
                L.e("请先定义id为click_to_disappear的按钮");
                views = null;
                return;
            }
            for (int layoutId : layoutIds) {
                if (views == null) {
                    views = new ArrayList<>();
                    currentShowId = 0;
                    initListener();
                }
                //虽然layoutId不在该子module里面定义，但是依然能够inflate，所以该处可行
                View view = inflater.inflate(layoutId, null);
                View btn = view.findViewById(btnId);
                if (btn == null){
                    L.e("layout id为"+layoutId+"的layout中没有定义click_to_disappear按钮");
                    views = null;
                    return;
                }
                btn.setOnClickListener(listener);
                views.add(view);
            }
        }catch (InflateException e){
            L.e("传入layout id 有误", e);
            views = null;
        }
    }

    /**
     * 使用完蒙版之后记得清空蒙版
     */
    public void clearMask(){
        views = null;
        listener = null;
        instance = null;
    }

    /**
     * 展示蒙版
     */
    public void showMask(){
        if (views == null){
            L.e("请先初始化该蒙版");
            return;
        }
        ((ViewGroup)(((Activity)(RootApplication.getInstance())).getWindow().getDecorView()))
                .addView(views.get(currentShowId), 0,
                        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));
    }
}
