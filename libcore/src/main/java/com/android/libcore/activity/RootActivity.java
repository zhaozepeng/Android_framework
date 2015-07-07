package com.android.libcore.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.android.libcore.application.RootApplication;

/**
 * Description: 所有基础{@linkplain Activity}的基类，所有的Activity应该
 * 继承自该基类，以便进行context的管理、页面的管理等
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-07
 */
public abstract class RootActivity extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        RootApplication.setInstanceRef(this);
    }


}
