package com.android.libcore_ui.dialog;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.libcore.log.L;
import com.android.libcore.utils.CommonUtils;
import com.android.libcore_ui.R;

import java.util.ArrayList;

/**
 * Description: 应用加载dialog
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-16
 */
public class LoadingDialog extends Dialog{

    private ImageView iv_loading_image;
    private TextView tv_loading_text;
    private TextView tv_dot_1;
    private TextView tv_dot_2;
    private TextView tv_dot_3;
    private ArrayList<TextView> tv_dots = new ArrayList<>();
    private int currentIndex = 0;
    private Animator upAnimator;
    private Animator downAnimator;

    public LoadingDialog(Context context) {
        this(context, R.style.theme_dialog);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        setContentView(R.layout.loading_dialog_base_layout);
        iv_loading_image = (ImageView) findViewById(R.id.iv_loading_image);
        tv_loading_text = (TextView) findViewById(R.id.tv_loading_text);
        tv_dot_1 = (TextView) findViewById(R.id.tv_dot_1);
        tv_dot_2 = (TextView) findViewById(R.id.tv_dot_2);
        tv_dot_3 = (TextView) findViewById(R.id.tv_dot_3);
        tv_dots.add(tv_dot_1);
        tv_dots.add(tv_dot_2);
        tv_dots.add(tv_dot_3);
        initView();
    }

    /**
     * 初始化显示动画
     */
    private void initView(){
        RotateAnimation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(1500);
        animation.setRepeatCount(-1);
        iv_loading_image.setAnimation(animation);
        animation.startNow();

        if (Build.VERSION.SDK_INT >= 11) {
            final AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
            downAnimator = null;
            upAnimator = ObjectAnimator.ofFloat(tv_dot_1, "topMargin", 0, 1);
            ((ObjectAnimator)upAnimator).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    TextView view = tv_dots.get(currentIndex % 3);
                    float value = (float) animation.getAnimatedValue();
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
                    params.topMargin = -(int) (CommonUtils.dp2px(10) * interpolator.getInterpolation(value));
                    view.setLayoutParams(params);
                    view.invalidate();
                    L.e("up animation " + params.topMargin);
                    if (value == 1.0f) {
                        L.e("down animation start");
                        downAnimator.start();
                    }
                }
            });
            upAnimator.setDuration(500);
            downAnimator = ObjectAnimator.ofFloat(tv_dot_1, "topMargin", 1, 0);
            ((ObjectAnimator)downAnimator).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    TextView view = tv_dots.get(currentIndex % 3);
                    float value = (float) animation.getAnimatedValue();
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
                    params.topMargin = -(int) (CommonUtils.dp2px(10) * interpolator.getInterpolation(value));
                    view.setLayoutParams(params);
                    view.invalidate();
                    L.e("down animation " + value);
                    if (value == 0.0f) {
                        currentIndex++;
                        L.e("up animation start");
                        upAnimator.start();
                    }
                }
            });
            downAnimator.setDuration(500);
            upAnimator.start();
            this.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    ((ObjectAnimator) upAnimator).removeAllUpdateListeners();
                    ((ObjectAnimator) downAnimator).removeAllUpdateListeners();
                    upAnimator.end();
                    upAnimator = null;
                    downAnimator.end();
                    downAnimator = null;
                }
            });
        }
    }

    /**
     * 设置当前所显示的字
     */
    public void setLoadingText(String text){
        tv_loading_text.setText(text);
    }
}
