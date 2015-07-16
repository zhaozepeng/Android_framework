package com.android.libcore.guide;

import android.app.Activity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.libcore.application.RootApplication;
import com.android.libcore.log.L;

import java.util.ArrayList;

/**
 * Description: 蒙版管理器，用来管理页面的蒙版<br/>
 * 使用{@link #initMask(Integer...)}传入资源layout的id，调用{@link #showMaskFullScreen()}
 * 或者{@link #showMaskInContent()}显示蒙版，使用完蒙版之后需要调用{@link #clearMask()}清空蒙版，要不然会影响下次的使用<br/>
 * <strong>注意传入的layout文件中请在需要点击消失的地方加入一个id为<em>click_to_disappear</em>的View即可</strong>
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-15
 */
public class GuideManager {
    private static volatile GuideManager instance = null;
    private ArrayList<View> views;
    private int currentShowId;
    private View.OnClickListener listener;
    private boolean isShowFullScreen = true;

    private GuideManager() {
    }

    public static GuideManager getInstance() {
        if (instance == null) {
            synchronized (GuideManager.class) {
                if (instance == null) {
                    instance = new GuideManager();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化监听器
     */
    private void initListener() {
        listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将上一次的蒙版去除
                ((ViewGroup) (((Activity) (RootApplication.getInstance())).getWindow().getDecorView())).removeView(views.get(currentShowId));
                L.e("remove view" + views.get(currentShowId));

                currentShowId++;
                if (currentShowId < views.size()) {
                    if (isShowFullScreen) {
                            ((ViewGroup) (((Activity) (RootApplication.getInstance())).getWindow().getDecorView()))
                                    .addView(views.get(currentShowId), new ViewGroup.LayoutParams(ViewGroup
                                            .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    } else {
                        //获取activity_base_layout.xml里面的content
                        int base_content = RootApplication.getInstance().getResources().getIdentifier("base_content", "id",
                                RootApplication.getInstance().getPackageName());

                        //将其添加在主体界面的上部,覆盖主体窗口
                        ((ViewGroup) (((((Activity) (RootApplication.getInstance())).getWindow().getDecorView())).findViewById
                                (base_content))).addView(views.get(currentShowId), new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                                .MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        L.e("add view"+views.get(currentShowId));
                    }
                }
            }
        };
    }

    /**
     * 此函数用来传入蒙版的id，蒙版的显示顺序将会和参数的顺序一致
     *
     * @param layoutIds 需要展示蒙版的资源id，按照显示顺序传递即可
     */
    public void initMask(Integer... layoutIds) {
        LayoutInflater inflater = LayoutInflater.from(RootApplication.getInstance());
        try {
            //该处需要去根据名字找到该button的id
            int btnId = RootApplication.getInstance().getResources().getIdentifier("click_to_disappear", "id",
                    RootApplication.getInstance().getPackageName());
            if (btnId == 0) {
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
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //用来屏蔽底层view的点击事件
                    }
                });
                View btn = view.findViewById(btnId);
                if (btn == null) {
                    L.e("layout id为" + layoutId + "的layout中没有定义click_to_disappear按钮");
                    views = null;
                    return;
                }
                btn.setOnClickListener(listener);
                views.add(view);
            }
        } catch (InflateException e) {
            L.e("传入layout id 有误", e);
            views = null;
        }
    }

    /**
     * 使用完蒙版之后记得清空蒙版
     */
    public void clearMask() {
        views = null;
        listener = null;
        instance = null;
    }

    /**
     * 全屏展示蒙版，此蒙版将会覆盖top bar
     */
    public void showMaskFullScreen() {
        if (views == null) {
            L.e("请先初始化该蒙版");
            return;
        }
        isShowFullScreen = true;
        currentShowId = 0;
        //将其添加在主体界面的上部,覆盖主体窗口
        ((ViewGroup) (((Activity) (RootApplication.getInstance())).getWindow().getDecorView())).addView(views.get
                (currentShowId), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * 在内容区域内显示蒙版，不会覆盖top bar
     */
    public void showMaskInContent() {

        if (views == null) {
            L.e("请先初始化该蒙版");
            return;
        }
        isShowFullScreen = false;
        currentShowId = 0;

        //获取activity_base_layout.xml里面的content
        int base_content = RootApplication.getInstance().getResources().getIdentifier("base_content", "id",
                RootApplication.getInstance().getPackageName());

        //将其添加在主体界面的上部,覆盖主体窗口
        ((ViewGroup) (((((Activity) (RootApplication.getInstance())).getWindow().getDecorView())).findViewById(base_content)))
                .addView(views.get(currentShowId), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }
}
