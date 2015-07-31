package com.android.sample.test_utils;

import android.view.View;

import com.android.framework.R;
import com.android.libcore.Toast.T;
import com.android.libcore.utils.FileUtils;
import com.android.libcore_ui.activity.BaseActivity;

/**
 * Description: 测试FileUtils
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-31
 */
public class FileActivity extends BaseActivity implements View.OnClickListener{
    @Override
    protected void initView() {
        setContentViewSrc(R.layout.activity_test_file);
        findViewById(R.id.btn_test_file).setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        T.getInstance().showShort("请查看framework目录");
        FileUtils.createFileInTempDirectory("temp.temp");
        FileUtils.createFileInImageDirectory("a.png");
        FileUtils.createFileInVideoDirectory("a.mp4");
        FileUtils.createFileInVoiceDirectory("a.mp3");
        FileUtils.createFileInHtmlDirectory("a.html");
        FileUtils.getFileOrDirectorySize(FileUtils.getExternalStoragePath());
    }
}
