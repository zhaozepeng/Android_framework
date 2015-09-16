package com.android.libcore_ui.netapi;

import com.android.libcore.netapi.BaseNetApi;
import com.android.volley.Request;
import com.android.volley.Response;

import java.util.Map;

/**
 * Description: 对基础netapi类的完善，可以在request包下添加自定义的request，并且
 * 在这个类里添加相应的函数
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-09-15
 */
public class NetApi extends BaseNetApi{
    @Override
    protected boolean handleOtherRequest(Class<?> clazz, String url, final Map<String, String> params, Response.Listener listener, Response.ErrorListener
            errorListener, Request request) {
        return false;
    }
}
