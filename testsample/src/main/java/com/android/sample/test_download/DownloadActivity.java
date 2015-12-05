package com.android.sample.test_download;

import android.os.Bundle;
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
    TextView tv_state;
    FileDownloadManager manager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    protected void initView() {
        setContentView(R.layout.activity_test_download);
        pb_bar = (ProgressBar) findViewById(R.id.pb_bar);
        tv_progress = (TextView) findViewById(R.id.tv_progress);
        tv_state = (TextView) findViewById(R.id.tv_state);
        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_stop).setOnClickListener(this);
        findViewById(R.id.btn_delete).setOnClickListener(this);
    }

    protected void initData() {
        String url = "http://gdown.baidu.com/data/wisegame/ce89b7ee349d6918/QQ_270.apk";
        manager = new FileDownloadManager(url, "QQ_270.apk");
        manager.setListener(new FileDownloadManager.IDownloadProgressChangedListener() {
            @Override
            public void onProgressChanged(long completeSize, long totalSize) {
                pb_bar.setProgress((int) ((completeSize*1.0/totalSize*1.0)*100));
                tv_progress.setText(completeSize+"/"+totalSize);
            }

            @Override
            public void onStateChanged(int state) {
                switch (state){
                    case FileDownloadManager.STATE_GETSIZE:
                        tv_state.setText("正在获取文件大小");
                        break;
                    case FileDownloadManager.STATE_STARTING:
                        tv_state.setText("开始下载");
                        break;
                    case FileDownloadManager.STATE_STOPING:
                        tv_state.setText("正在停止");
                        break;
                    case FileDownloadManager.STATE_STOPED:
                        tv_state.setText("停止成功");
                        break;
                    case FileDownloadManager.STATE_FINISH:
                        tv_state.setText("下载完成");
                        break;
                    case FileDownloadManager.STATE_DELETING:
                        tv_state.setText("正在删除");
                        break;
                    case FileDownloadManager.STATE_DELETE:
                        tv_state.setText("删除成功");
                        break;
                    case FileDownloadManager.STATE_NET_ERROR:
                        tv_state.setText("网络错误");
                        break;
                }
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
            case R.id.btn_delete:
                manager.delete();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        manager.stop();
        super.onDestroy();
    }
}
