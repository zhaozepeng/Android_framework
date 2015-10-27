package com.android.sample.test_netapi;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.framework.R;
import com.android.libcore.net.imageloader.ImageLoader;
import com.android.libcore.net.netapi.BaseNetApi;
import com.android.libcore.net.NetError;
import com.android.libcore_ui.activity.BaseActivity;
import com.android.libcore_ui.dialog.LoadingDialog;
import com.android.libcore_ui.net.NetApi;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.util.HashMap;

/**
 * Description: 测试网络请求
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-09-16
 */
public class NetActivity extends BaseActivity implements View.OnClickListener{

    private TextView tv_result;
    private ImageView iv_content;
    private LoadingDialog ld;

    @Override
    protected void initView() {
        setContentViewSrc(R.layout.activity_test_net);
        addNavigationOnBottom((ViewGroup) findViewById(R.id.ll_content));
        tv_result = (TextView) findViewById(R.id.tv_result);
        iv_content = (ImageView) findViewById(R.id.iv_content);

        findViewById(R.id.btn_string).setOnClickListener(this);
        findViewById(R.id.btn_jsonObject).setOnClickListener(this);
        findViewById(R.id.btn_jsonArray).setOnClickListener(this);
        findViewById(R.id.btn_xml).setOnClickListener(this);
        findViewById(R.id.btn_image).setOnClickListener(this);

        ld = new LoadingDialog(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        ld.show();
        switch (v.getId()){
            case R.id.btn_string:
                NetApi.getInstance().stringRequest(this, "https://www.baidu.com", new HashMap<String, String>(),
                        new BaseNetApi.OnNetCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        ld.dismiss();
                        tv_result.setText(result);
                    }

                    @Override
                    public void onFail(NetError error) {
                        ld.dismiss();
                        tv_result.setText(error.errorMessage);
                    }
                });
                break;
            case R.id.btn_jsonObject:
                NetApi.getInstance().jsonObjectRequest(this, "http://www.weather.com.cn/data/sk/101280601.html", new HashMap<String, String>(),
                        new BaseNetApi.OnNetCallback<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        ld.dismiss();
                        tv_result.setText(result.toString());
                    }

                    @Override
                    public void onFail(NetError error) {
                        ld.dismiss();
                        tv_result.setText(error.errorMessage);
                    }
                });
                break;
            case R.id.btn_jsonArray:
                ld.dismiss();
                break;
            case R.id.btn_xml:
                NetApi.getInstance().xmlRequest(this, "https://www.baidu.com/", new HashMap<String, String>(),
                        new BaseNetApi.OnNetCallback<XmlPullParser>() {
                    @Override
                    public void onSuccess(XmlPullParser result) {
                        ld.dismiss();
                        tv_result.setText(result.toString());
                    }

                    @Override
                    public void onFail(NetError error) {
                        ld.dismiss();
                        tv_result.setText(error.errorMessage);
                    }
                });
                break;
            case R.id.btn_image:
                ld.dismiss();
//                ImageLoader.getInstance().loadImage("http://www.baidu.com/img/bdlogo.png", iv_content);
//                ImageLoader.getInstance().loadImage("http://www.baidu.com/img/bdlogo.png", new ImageLoader.OnLoadCallBack() {
//                    @Override
//                    public void onLoadSuccess(Bitmap bitmap, String url) {
//                        iv_content.setImageBitmap(bitmap);
//                    }
//
//                    @Override
//                    public void onLoadFail(NetError error) {
//
//                    }
//                });
//                ImageLoader.getInstance().loadImage("http://www.baidu.com/img/bdlogo.png",
//                        iv_content.getMeasuredWidth(), iv_content.getMeasuredHeight(), new ImageLoader.OnLoadCallBack() {
//                            @Override
//                            public void onLoadSuccess(Bitmap bitmap, String url) {
//                                iv_content.setImageBitmap(bitmap);
//                            }
//
//                            @Override
//                            public void onLoadFail(NetError error) {
//
//                            }
//                        });
                ImageLoader.getInstance().loadImage("http://www.baidu.com/img/bdlogo.png", iv_content, R.mipmap.ic_refresh, R.mipmap.ic_refresh_close,
                                            iv_content.getMeasuredWidth(), iv_content.getMeasuredHeight());
                break;
        }
    }
}
