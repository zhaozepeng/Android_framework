package com.android.libcore_ui.netapi;

import android.content.Context;

import com.android.libcore.net.netapi.BaseNetApi;
import com.android.libcore_ui.netapi.request.XMLRequest;

import org.xmlpull.v1.XmlPullParser;

import java.util.Map;

/**
 * Description: 对基础netapi类的完善，可以在request包下添加自定义的request，并且
 * 在这个类里添加相应的函数
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-09-15
 */
public class NetApi extends BaseNetApi{
    private volatile static NetApi instance;

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

    public void xmlRequest(Context context, String url, Map<String, String> params, OnNetCallback<XmlPullParser> callback){
        makeRequest(context, XMLRequest.class, url, params, callback);
    }
}
