package com.android.libcore.volley;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-10-20
 */
public class VolleyLruCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache{

    private static int getCacheSize(){
        return (int)(Runtime.getRuntime().maxMemory()/1024/8);
    }

    public VolleyLruCache() {
        this(getCacheSize());
    }

    private VolleyLruCache(int size){
        super(size);
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}
