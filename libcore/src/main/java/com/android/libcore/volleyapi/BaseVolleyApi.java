package com.android.libcore.volleyapi;


import com.android.libcore.application.RootApplication;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Description: 封装的volley的请求
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-09-16
 */
public abstract class BaseVolleyApi {
    private static RequestQueue requestQueue;
    private static ImageLoader imageLoader;

    public static RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            synchronized (BaseVolleyApi.class){
                if (requestQueue == null)
                    requestQueue = Volley.newRequestQueue(RootApplication.getInstance());
            }
        }
        return requestQueue;
    }

    public static ImageLoader getImageLoader() {
        if (imageLoader == null) {
            synchronized (BaseVolleyApi.class) {
                if (imageLoader == null){
                    VolleyLruCache cache = new VolleyLruCache();
                    imageLoader = new ImageLoader(getRequestQueue(), cache);
                }
            }
        }
        return imageLoader;
    }
}
