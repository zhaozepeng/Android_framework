package com.android.sample.test_webview;

import android.content.Intent;
import android.os.Environment;
import android.view.View;

import com.android.framework.R;
import com.android.libcore.log.L;
import com.android.libcore_ui.activity.BaseActivity;
import com.android.libcore_ui.webview.WebActivity;

import java.io.File;
import java.lang.reflect.Field;

/**
 * Description: 测试WebView
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-27
 */
public class WebViewActivity extends BaseActivity implements View.OnClickListener{
    @Override
    protected void initView() {
        setContentViewSrc(R.layout.activity_test_webview);
        findViewById(R.id.btn_webview).setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, WebActivity.class);
//        intent.putExtra(WebActivity.EXTRA_URL, "http://www.baidu.com");
        //加载assets文件
//        intent.putExtra(WebActivity.EXTRA_URL, "file:///android_asset/baidu.html");
        //加载SD卡文件
        L.e("getDatabasePath():"+getDatabasePath("student.db"));
        L.e("getCacheDir():"+getCacheDir());
        L.e("getFilesDir():" + getFilesDir());
        String[] strings = fileList();
        for (String path : strings){//为空
            L.e("fileList():---" + path);
        }
        L.e("getDir(\"zhao\"):" + getDir("zhao", MODE_PRIVATE));
//        L.e("getCodeCacheDir():" + getCodeCacheDir()); //java.lang.NoSuchMethodError
        L.e("getPackageCodePath():" + getPackageCodePath());
        L.e("getPackageResourcePath():" + getPackageResourcePath());
        L.e("getExternalFilesDir():" + getExternalFilesDir(null));
        File[] paths = getExternalFilesDirs(null);
        for (File path : paths){
            L.e("getExternalFilesDirs():---" + path.getPath());
        }
        L.e("getExternalCacheDir():" + getExternalCacheDir());
        paths = getExternalCacheDirs();
        for (File path : paths){
            L.e("getExternalCacheDirs():---" + path.getPath());
        }
        paths = getObbDirs();
        for (File path : paths){
            L.e("getObbDirs():---" + path.getPath());
        }
        L.e("Environment.getExternalStorageState():"+ Environment.getExternalStorageState());
        L.e("Environment.getExternalStorageDirectory():"+Environment.getExternalStorageDirectory());
        L.e("Environment.getDownloadCacheDirectory():"+Environment.getDownloadCacheDirectory());
        L.e("Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC):"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC));
        L.e("Environment.getRootDirectory():"+Environment.getRootDirectory());

        intent.putExtra(WebActivity.EXTRA_URL, "file:///sdcard/FrameWork/html/baidu.html");
        startActivity(intent);
    }
}
