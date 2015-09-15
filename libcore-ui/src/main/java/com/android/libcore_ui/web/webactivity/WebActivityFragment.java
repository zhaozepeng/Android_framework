package com.android.libcore_ui.web.webactivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.android.libcore_ui.R;
import com.android.libcore_ui.web.WebFragment;

import java.util.List;

/**
 * Description: 用于在{@link WebActivity}中显示的webfragment
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-27
 */
public class WebActivityFragment extends WebFragment {

    @Override
    protected void initView() {
        super.initView();
        WebSettings settings = webView.getSettings();
        //支持缩放
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);
        //隐藏缩放栏
        if (Build.VERSION.SDK_INT >= 11)
            settings.setDisplayZoomControls(false);
    }

    @Override
    protected boolean handleUrlBeforeLoad(String url) {
        try {
            PackageManager pm = getActivity().getPackageManager();
            // 以下固定写法
            final Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            List<ResolveInfo> infos = pm.queryIntentActivities(intent, PackageManager.GET_RESOLVED_FILTER);
            //如果除了浏览器之外还有应用能够打开该链接，则打开一个选择器
            if (infos.size() >= 2) {
                startActivity(intent);
            }
        } catch (Exception e) {
            // 防止没有安装的情况
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPageStarted(WebView view, String url, Bitmap favicon) {
        Message msg = Message.obtain();
        msg.what = 1;
        sendMessageToActivity(msg);
    }

    @Override
    protected void onPageFinished(WebView view, String url) {
        Message msg = Message.obtain();
        msg.what = 2;
        sendMessageToActivity(msg);
    }

    @Override
    protected void onProgressChanged(WebView view, int newProgress) {
        Message msg = Message.obtain();
        msg.what = 3;
        msg.obj = newProgress;
        sendMessageToActivity(msg);
    }

    @Override
    protected void onReceivedIcon(WebView view, Bitmap icon) {
        Message msg = Message.obtain();
        msg.what = 4;
        msg.obj = icon;
        sendMessageToActivity(msg);
    }

    @Override
    protected void onReceivedTitle(WebView view, String title) {
        Message msg = Message.obtain();
        msg.what = 5;
        msg.obj = title;
        sendMessageToActivity(msg);
    }
}
