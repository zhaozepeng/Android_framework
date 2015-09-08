package com.android.sample.test_utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.framework.R;
import com.android.libcore.Toast.T;
import com.android.libcore.utils.FileUtils;
import com.android.libcore.utils.ImageUtils;
import com.android.libcore_ui.activity.BaseActivity;
import com.android.libcore_ui.webview.WebFragment;
import com.android.sample.test_webview.TestWebFragment;

/**
 * Description: 测试{@link com.android.libcore.utils.ImageUtils}类
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-09-04
 */
public class ImageActivity extends BaseActivity implements View.OnClickListener{
    private WebView webView;

    private ImageView centerSquareScaleBitmap;
    private ImageView toRoundCorner;
    private Bitmap bitmap;

    @Override
    protected void initView() {
        setContentViewSrc(R.layout.activity_test_image);
        centerSquareScaleBitmap = (ImageView) findViewById(R.id.centerSquareScaleBitmap);
        toRoundCorner = (ImageView) findViewById(R.id.toRoundCorner);

        final TestWebFragment webFragment = new TestWebFragment();
        webFragment.setCallback(new WebFragment.WebCallback() {
            @Override
            public void onPageFinished(String url) {
                T.getInstance().showShort("加载完成");
                webView = webFragment.mWebView;
            }
        });
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fl_content, webFragment);
        ft.commit();
        Bundle bundle = new Bundle();
        bundle.putString(WebFragment.EXTRA_URL, "http://www.sohu.com");
        webFragment.setArguments(bundle);

        findViewById(R.id.btn_test_screenshot).setOnClickListener(this);
        findViewById(R.id.btn_test_webview).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.test_image);
        centerSquareScaleBitmap.setImageBitmap(ImageUtils.centerSquareScaleBitmap(bitmap,
                bitmap.getWidth()>bitmap.getHeight()?bitmap.getHeight():bitmap.getWidth()));
        toRoundCorner.setImageBitmap(ImageUtils.toRoundCorner(bitmap, 120));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_test_screenshot){
            ImageUtils.saveBitmap(ImageUtils.screenShot(this), FileUtils.getExternalStoragePath()+"screenshot.png");
            T.getInstance().showLong("图片位于"+FileUtils.getExternalStoragePath()+"screenshot.png");
        }else if (v.getId() == R.id.btn_test_webview){
            ImageUtils.saveBitmap(ImageUtils.viewShot(webView), FileUtils.getExternalStoragePath()+"webview.png");
            T.getInstance().showLong("图片位于" + FileUtils.getExternalStoragePath() + "webview.png");
        }
    }
}