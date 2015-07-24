package com.android.sample.test_cache;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.framework.R;
import com.android.libcore.cachemanager.CacheManager;
import com.android.libcore_ui.activity.BaseActivity;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashSet;

/**
 * Description: Cache测试类
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-24
 */
public class CacheActivity extends BaseActivity implements View.OnClickListener{
    private Button btn_set;
    private Button btn_get;
    private TextView tv_result;

    @Override
    protected void initView() {
        setContentViewSrc(R.layout.activity_test_cache);
        btn_set = (Button) findViewById(R.id.btn_set);
        btn_set.setOnClickListener(this);
        btn_get = (Button) findViewById(R.id.btn_get);
        btn_get.setOnClickListener(this);
        tv_result = (TextView) findViewById(R.id.tv_result);
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_set:
                CacheManager.setTemporary("boolean", false);
                CacheManager.setTemporary("float", 0.555f);
                CacheManager.setTemporary("int", 1);
                CacheManager.setTemporary("long", 100000L);
                CacheManager.setTemporary("string", "zhaozepeng");

                LinkedHashSet<String> list = new LinkedHashSet<>();
                list.add("1zhaozepeng");
                list.add("2zhaozepeng");
                CacheManager.setTemporary("String set", list);

                LinkedHashSet<TestModule> lists = new LinkedHashSet<>();
                TestModule module = new TestModule();
                module.a = 1;
                module.b = 2;
                module.c = 3;
                lists.add(module);
                module = new TestModule();
                module.a = 4;
                module.b = 5;
                module.c = 6;
                lists.add(module);
                module = new TestModule();
                module.a = 7;
                module.b = 8;
                module.c = 9;
                lists.add(module);
                CacheManager.setTemporary("module set", lists);
                break;
            case R.id.btn_get:
                tv_result.setText(CacheManager.getTemporary("boolean", Boolean.class, true) +"\n"
                        + CacheManager.getTemporary("float", Float.class, 0.66666f) + " \n"
                        + CacheManager.getTemporary("int", Integer.class, 5) + " \n"
                        + CacheManager.getTemporary("long", Long.class, 444444l) + " \n"
                        + CacheManager.getTemporary("string", String.class, "dddddd") + " \n"
                        + CacheManager.getTemporarySet("String set", String.class) +" \n"
                        + CacheManager.getTemporarySet("module set", TestModule.class) +" \n");
                break;
        }
    }

    public static class TestModule extends CacheManager.ParseObject{
        public int a;
        public float b;
        public double c;

        public TestModule() {
        }

        @Override
        public void stringParseObject(String value) {
            try {
                JSONObject object = new JSONObject(value);
                a = Integer.parseInt(object.getString("a"));
                b = Float.parseFloat(object.getString("b"));
                c = Double.parseDouble(object.getString("c"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            JSONObject object = new JSONObject();
            try {
                object.put("a", a);
                object.put("b", b);
                object.put("c", c);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return object.toString();
        }
    }
}
