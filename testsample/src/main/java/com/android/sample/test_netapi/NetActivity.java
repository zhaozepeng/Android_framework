package com.android.sample.test_netapi;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.framework.R;
import com.android.libcore.netapi.BaseNetApi;
import com.android.libcore.netapi.NetError;
import com.android.libcore_ui.activity.BaseActivity;
import com.android.libcore_ui.dialog.LoadingDialog;
import com.android.libcore_ui.netapi.NetApi;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: 测试网络请求
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-09-16
 */
public class NetActivity extends BaseActivity implements View.OnClickListener{

    private TextView tv_result;
    private LoadingDialog ld;

    @Override
    protected void initView() {
        setContentViewSrc(R.layout.activity_test_net);
        addNavigationOnBottom((ViewGroup) findViewById(R.id.ll_content));
        tv_result = (TextView) findViewById(R.id.tv_result);

        findViewById(R.id.btn_string).setOnClickListener(this);
        findViewById(R.id.btn_jsonObject).setOnClickListener(this);
        findViewById(R.id.btn_jsonArray).setOnClickListener(this);
        findViewById(R.id.btn_xml).setOnClickListener(this);

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
                NetApi.getInstance().stringRequest(this, "https://www.baidu.com", new HashMap<String, String>(), new BaseNetApi.OnNetCallback<String>() {
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
                NetApi.getInstance().jsonObjectRequest(this, "http://www.weather.com.cn/data/sk/101280601.html", new HashMap<String, String>(), new BaseNetApi.OnNetCallback<JSONObject>() {
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
                NetApi.getInstance().xmlRequest(this, "https://www.google.com", new HashMap<String, String>(), new BaseNetApi.OnNetCallback<String>() {
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
        }
    }
}
