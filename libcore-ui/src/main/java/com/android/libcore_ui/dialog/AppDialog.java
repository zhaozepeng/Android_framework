package com.android.libcore_ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.libcore.dialog.BaseDialog;
import com.android.libcore_ui.R;

/**
 * Description: 扩展dialog类，用来统一dialog的样式和实线dialog的功能，请在此处定义好一个应用
 * 的dialog样式，并且保证一个应用的都使用该dialog
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-16
 */
public class AppDialog extends BaseDialog implements View.OnClickListener{

    /** 标题 */
    private RelativeLayout rl_title;
    private TextView tv_title;

    /** 内容 */
    private RelativeLayout rl_message;
    private TextView tv_message;

    /** 底部按钮 */
    private LinearLayout ll_bottom_button;

    private LayoutInflater inflater;

    public AppDialog(Context context) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        inflater = LayoutInflater.from(getContext());
        //仿QQ的dialog样式
        setContentView(R.layout.dialog_base_layout);
        rl_title = (RelativeLayout) findViewById(R.id.rl_title);
        tv_title = (TextView) findViewById(R.id.tv_title);
        rl_message = (RelativeLayout) findViewById(R.id.rl_message);
        tv_message = (TextView) findViewById(R.id.tv_message);
        ll_bottom_button = (LinearLayout) findViewById(R.id.ll_bottom_button);
    }

    @Override
    public com.android.libcore.dialog.BaseDialog setTitle(String title) {
        tv_title.setText(title);
        return this;
    }

    @Override
    public com.android.libcore.dialog.BaseDialog setTitle(View title) {
        rl_title.removeView(tv_title);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rl_title.addView(title, params);
        return this;
    }

    @Override
    public com.android.libcore.dialog.BaseDialog setMessage(String message) {
        tv_message.setText(message);
        return this;
    }

    @Override
    public com.android.libcore.dialog.BaseDialog setMessage(View message) {
        rl_title.removeView(tv_title);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rl_title.addView(message, params);
        return this;
    }

    private LinearLayout generateLayout(String text){
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_item_button, null);
        View v_line = layout.findViewById(R.id.v_line);
        if (ll_bottom_button.getChildCount() == 0){
            layout.removeView(v_line);
        }
        TextView tv_text = (TextView) layout.findViewById(R.id.tv_text);
        tv_text.setText(text);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        layout.setLayoutParams(params);
        layout.setOnClickListener(this);
        return layout;
    }

    @Override
    public com.android.libcore.dialog.BaseDialog setPositiveButton(String positive) {
        LinearLayout layout = generateLayout(positive);
        layout.setTag(POSITIVE_LISTENER);
        ll_bottom_button.addView(layout);
        return this;
    }

    @Override
    public com.android.libcore.dialog.BaseDialog setPositiveButton(View positive) {
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        positive.setTag(POSITIVE_LISTENER);
        positive.setOnClickListener(this);
        ll_bottom_button.addView(positive, params);
        return this;
    }

    @Override
    public com.android.libcore.dialog.BaseDialog setNegativeButton(String negative) {
        LinearLayout layout = generateLayout(negative);
        layout.setTag(NEGATIVE_LISTENER);
        ll_bottom_button.addView(layout);
        return this;
    }

    @Override
    public com.android.libcore.dialog.BaseDialog setNegativeButton(View negative) {
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        negative.setTag(NEGATIVE_LISTENER);
        negative.setOnClickListener(this);
        ll_bottom_button.addView(negative, params);
        return this;
    }

    @Override
    public com.android.libcore.dialog.BaseDialog setNeutralButton(String neutral) {
        LinearLayout layout = generateLayout(neutral);
        layout.setTag(NEUTRAL_LISTENER);
        ll_bottom_button.addView(layout);
        return this;
    }

    @Override
    public com.android.libcore.dialog.BaseDialog setNeutralButton(View neutral) {
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        neutral.setTag(NEUTRAL_LISTENER);
        neutral.setOnClickListener(this);
        ll_bottom_button.addView(neutral, params);
        return this;
    }

    @Override
    public com.android.libcore.dialog.BaseDialog addOtherButton(String other, int other_listener) {
        if (!checkIllegalId(other_listener)){
            throw new IllegalArgumentException("按钮id重复");
        }
        LinearLayout layout = generateLayout(other);
        layout.setTag(other_listener);
        ids.add(other_listener);
        ll_bottom_button.addView(layout);
        return this;
    }

    @Override
    public com.android.libcore.dialog.BaseDialog addOtherButton(View other, int other_listener) {
        if (!checkIllegalId(other_listener)){
            throw new IllegalArgumentException("按钮id重复");
        }
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        other.setTag(NEUTRAL_LISTENER);
        other.setOnClickListener(this);
        ids.add(other_listener);
        ll_bottom_button.addView(other, params);
        return this;
    }

    @Override
    public com.android.libcore.dialog.BaseDialog setGravity(int gravity) {
        Window window = this.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = gravity;
        window.setAttributes(params);
        return this;
    }

    @Override
    public com.android.libcore.dialog.BaseDialog setPosition(int x, int y) {
        Window window = this.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        if (x != -1)
            params.x = x;
        if (y != -1)
            params.y = y;
        window.setAttributes(params);
        return this;
    }

    @Override
    public com.android.libcore.dialog.BaseDialog setWidth(int width) {
        Window window = this.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = width;
        window.setAttributes(params);
        return this;
    }

    @Override
    public com.android.libcore.dialog.BaseDialog setHeight(int height) {
        Window window = this.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.height = height;
        window.setAttributes(params);
        return this;
    }

    @Override
    public com.android.libcore.dialog.BaseDialog setAlpha(float alpha) {
        Window window = this.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.alpha = alpha;
        window.setAttributes(params);
        return null;
    }

    @Override
    public void onClick(View v) {
        this.dismiss();
        listener.onButtonClick((Integer) v.getTag());
    }
}
