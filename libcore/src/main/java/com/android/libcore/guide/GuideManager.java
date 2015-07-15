package com.android.libcore.guide;

/**
 * Description: 蒙版管理器，用来管理页面的蒙版
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-15
 */
public class GuideManager {
    private static volatile GuideManager instance = null;

    private GuideManager(){}

    public GuideManager getInstance(){
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
     * 此函数用来传入蒙版的id，蒙版的显示顺序将会和参数的顺序一致
     * @param integers 需要展示蒙版的资源id，按照显示顺序传递即可
     */
    public void initMask(Integer... integers){

    }

    /**
     * 展示蒙版
     */
    public void showMask(){

    }
}
