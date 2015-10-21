package com.android.libcore.imageloader;

import com.android.libcore.application.RootApplication;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Description: 图片加载类
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-10-21
 */
public class ImageLoader {
    private final int MAXDISKCACHEBYTES = 10 * 1024 *1024;
    private static volatile ImageLoader instance;
    private com.android.volley.toolbox.ImageLoader imageLoader;

    private ImageLoader(){
        RequestQueue requestQueue = Volley.newRequestQueue(RootApplication.getInstance(), MAXDISKCACHEBYTES);
        VolleyLruCache lruCache = new VolleyLruCache();
        imageLoader = new com.android.volley.toolbox.ImageLoader(requestQueue, lruCache);
    }

    public static ImageLoader getInstance(){
        if (instance == null){
            synchronized (ImageLoader.class){
                if (instance == null)
                    instance = new ImageLoader();
            }
        }
        return instance;
    }

    public void loadImage(){

    }
}
