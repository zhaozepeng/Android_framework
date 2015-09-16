package com.android.libcore_ui.netapi;

import android.content.Context;

import com.android.libcore.netapi.BaseNetApi;
import com.android.libcore_ui.netapi.request.XMLRequest;
import com.android.volley.AuthFailureError;
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
    public volatile static NetApi instance;

    public static NetApi getInstance(){
        if (instance == null){
            synchronized (NetApi.class){
                if (instance == null){
                    instance = new NetApi();
                }
            }
        }
        return instance;
    }

    @Override
    protected Request handleOtherRequest(Class<?> clazz, String url, final Map<String, String> params, Response.Listener listener, Response.ErrorListener
            errorListener) {
        if (clazz == XMLRequest.class){
            Request request = new XMLRequest(Request.Method.POST, url, listener, errorListener){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return params;
                }
            };
            return request;
        }
        return null;
    }

    public void xmlRequest(Context context, String url, Map<String, String> params, OnNetCallback<String> callback){
        makeRequest(context, XMLRequest.class, url, params, callback);
    }
}
