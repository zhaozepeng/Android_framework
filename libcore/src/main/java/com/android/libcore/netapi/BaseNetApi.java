package com.android.libcore.netapi;

import com.android.libcore.application.RootApplication;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Description: 所有网络访问基础类，使用了volley框架，并且进行了简单的基础
 * 封装，<font color=red><Strong>所有进行网络访问的地方都使用该封装类</Strong></font>，
 * 方便以后更换网络访问框架，只需修改libcore，libcore-ui层相关类即可，这也是封装的唯一目的<br/>
 *
 * 不能在libcore，libcore-ui层的相关类之外直接使用volley框架内容
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-08
 */
public abstract class BaseNetApi {
    /** 网络访问requestQueue */
    private RequestQueue requestQueue;

    protected RequestQueue getRequestQueue(int maxDiskCacheBytes){
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(RootApplication.getInstance(), maxDiskCacheBytes);
        return requestQueue;
    }

    protected RequestQueue getRequestQueue(){
        return getRequestQueue(-1);
    }
}
