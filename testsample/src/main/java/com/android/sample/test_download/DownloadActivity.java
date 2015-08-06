package com.android.sample.test_download;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.framework.R;
import com.android.libcore.download.FileDownloadManager;
import com.android.libcore_ui.activity.BaseActivity;

/**
 * Description: 测试断点续传式下载
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-08-06
 */
public class DownloadActivity extends BaseActivity implements View.OnClickListener{
    ProgressBar pb_bar;
    TextView tv_progress;
    FileDownloadManager manager;

    @Override
    protected void initView() {
        setContentViewSrc(R.layout.activity_test_download);
        pb_bar = (ProgressBar) findViewById(R.id.pb_bar);
        tv_progress = (TextView) findViewById(R.id.tv_progress);
        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_stop).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        manager = new FileDownloadManager("http://125.91.249.24/ws.acgvideo.com/b/37/4194276-1.mp4?wsTime=1438884300&wsSecret=f92d8726706bcbe09cfd1ce27bc24055&wshc_tag=0&wsts_tag=55c331ae&wsid_tag=77894933&wsiphost=ipdbm", "a.mp4");
        manager.setListener(new FileDownloadManager.IDownloadProgressChangedListener() {
            @Override
            public void onProgressChanged(long completeSize, long totalSize) {
                pb_bar.setProgress((int) ((completeSize*1.0/totalSize*1.0)*100));
                tv_progress.setText(completeSize+"/"+totalSize);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:
                manager.start();
                break;
            case R.id.btn_stop:
                manager.stop();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        manager.stop();
        super.onDestroy();
    }
}
