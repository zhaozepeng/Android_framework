package com.android.sample.test_dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.framework.R;
import com.android.libcore.Toast.T;
import com.android.libcore.dialog.BaseDialog;
import com.android.libcore.utils.CommonUtils;
import com.android.libcore_ui.activity.BaseActivity;
import com.android.libcore_ui.dialog.AppDialog;
import com.android.libcore_ui.dialog.DialogCreator;
import com.android.libcore_ui.dialog.LoadingDialog;

/**
 * Description: 测试dialog
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-17
 */
public class DialogActivity extends BaseActivity implements View.OnClickListener{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    protected void initView() {
        setContentViewSrc(R.layout.activity_test_dialog);
        findViewById(R.id.btn_test_dialog_1).setOnClickListener(this);
        findViewById(R.id.btn_test_dialog_2).setOnClickListener(this);
        findViewById(R.id.btn_test_dialog_3).setOnClickListener(this);
        findViewById(R.id.btn_test_dialog_5).setOnClickListener(this);
        findViewById(R.id.btn_test_dialog_gravity).setOnClickListener(this);
        findViewById(R.id.btn_test_dialog_xy).setOnClickListener(this);
        findViewById(R.id.btn_test_dialog_width_height).setOnClickListener(this);
        findViewById(R.id.btn_test_dialog_alpha).setOnClickListener(this);
        findViewById(R.id.btn_test_loading_dialog).setOnClickListener(this);
    }

    protected void initData() {
        addNavigationOnBottom((ViewGroup) findViewById(R.id.ll_content));
    }

    @Override
    public void onClick(View v) {
        AppDialog dialog = null;
        switch (v.getId()){
            case R.id.btn_test_dialog_1:
                dialog = DialogCreator.createDialog(null, "测试1文字", "确定");
//                ImageView view = new ImageView(this);
//                view.setBackgroundResource(R.mipmap.app_icon);
//                EditText editText = new EditText(this);
//                Button button = new Button(this);
//                dialog = DialogCreator.createDialog(view, editText, button);
                break;
            case R.id.btn_test_dialog_2:
                Button button2 = new Button(this);
                dialog = DialogCreator.createDialog("测试1", "测试1文字", button2, "取消");
//                ImageView view = new ImageView(this);
//                view.setBackgroundResource(R.mipmap.app_icon);
//                EditText editText = new EditText(this);
//                Button button = new Button(this);
//                Button button1 = new Button(this);
//                dialog = DialogCreator.createDialog(view, editText, button, button1);
                break;
            case R.id.btn_test_dialog_3:
                dialog = DialogCreator.createDialog("测试1", "测试1文字", "确定", "取消", "中间");
                break;
            case R.id.btn_test_dialog_5:
                ImageView view = new ImageView(this);
                view.setBackgroundResource(R.mipmap.app_icon);
                EditText editText = new EditText(this);
                TextView button = new TextView(this);
                button.setText("444444444444444444444444");
                DialogCreator.OtherButton other4 = new DialogCreator.OtherButton(button, 4);
                Button button1 = new Button(this);
                DialogCreator.OtherButton other5 = new DialogCreator.OtherButton(button1, 5);
//                DialogCreator.OtherButton other4 = new DialogCreator.OtherButton("另外的4", 4);
//                DialogCreator.OtherButton other5 = new DialogCreator.OtherButton("另外的5", 5);
                dialog = DialogCreator.createDialog(view, editText, "确定", "取消", "中间", other4, other5);
                break;
            case R.id.btn_test_dialog_gravity:
                dialog = DialogCreator.createDialog("测试1", "测试1文字", "确定", "取消", "中间");
                dialog.setGravity(Gravity.TOP|Gravity.LEFT);
                break;
            case R.id.btn_test_dialog_xy:
                dialog = DialogCreator.createDialog("测试1", "测试1文字", "确定", "取消", "中间");
                dialog.setPosition(-100, -300);
                break;
            case R.id.btn_test_dialog_width_height:
                dialog = DialogCreator.createDialog("测试1", "测试1文字", "确定", "取消", "中间");
                dialog.setWidth(CommonUtils.dp2px(100));
                dialog.setHeight(CommonUtils.dp2px(200));
                break;
            case R.id.btn_test_dialog_alpha:
                dialog = DialogCreator.createDialog("测试1", "测试1文字", "确定", "取消", "中间");
                dialog.setAlpha(0.5f);
                break;
            case R.id.btn_test_loading_dialog:
                LoadingDialog loadingDialog = new LoadingDialog(this);
                loadingDialog.setLoadingText("正在显示loading dialog");
                loadingDialog.show();
                break;
            default:
                break;
        }
        if (dialog != null) {
            dialog.setOnButtonClickListener(new BaseDialog.ButtonClickListener() {
                @Override
                public void onButtonClick(int button_id) {
                    T.getInstance().showShort("您点击了第" + button_id + "个button");
                }
            });
            dialog.show();
        }
    }
}
