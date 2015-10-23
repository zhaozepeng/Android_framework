package com.android.libcore.net.imageloader;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.libcore.application.RootApplication;
import com.android.libcore.log.L;
import com.android.libcore.net.NetError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.lang.reflect.Field;

/**
 * Description: 图片加载类
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-10-21
 */
public class ImageLoader {
    /** 最大的图片缓存大小 */
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

    /** 通过反射获取imageview的大小 */
    private int getImageViewFieldValue(Object object, String fieldName) {
        int value = 0;
        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = (Integer) field.get(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
                value = fieldValue;
            }
        } catch (Exception e) {
            L.e(e);
        }
        return value;
    }

    /**
     * 加载图片
     * @param url 图片url
     * @param imageView 需要加载图片的视图
     */
    public void loadImage(String url, final ImageView imageView){
        loadImage(url, imageView, null);
    }

    /**
     * 只带回调的图片加载
     * @param url 图片url
     * @param listener 图片加载回调
     */
    public void loadImage(String url, final onLoadCallBack listener){
        loadImage(url, 0, 0, listener);
    }

    /**
     * 带回调的加载图片
     * @param url 图片url
     * @param width 需要加载的图片宽
     * @param height 需要加载的图片高
     * @param listener 加载图片完成回调
     */
    public void loadImage(String url, int width, int height, final onLoadCallBack listener){
        loadImage(url, null, width, height, listener);
    }

    /**
     * 带回调的加载图片
     * @param url 图片url
     * @param imageView 需要加载图片的视图
     * @param listener 加载图片的回调
     */
    public void loadImage(String url, final ImageView imageView, final onLoadCallBack listener){
        int width = getImageViewFieldValue(imageView, "mMaxWidth");
        int height = getImageViewFieldValue(imageView, "mMaxHeight");

        loadImage(url, imageView, width, height, listener);
    }

    /**
     * 加载图片
     * @param url 图片url
     * @param imageView 需要加载图片的视图
     * @param width 需要加载视图的宽
     * @param height 需要加载视图的高
     * @param listener 加载图片回调
     */
    public void loadImage(String url, final ImageView imageView, int width, int height, final onLoadCallBack listener){
        imageLoader.get(url, new com.android.volley.toolbox.ImageLoader.ImageListener() {
            @Override
            public void onResponse(com.android.volley.toolbox.ImageLoader.ImageContainer response, boolean isImmediate) {
                if (imageView != null)
                    imageView.setImageBitmap(response.getBitmap());
                if (listener != null)
                    listener.onLoadSuccess(response.getBitmap(), response.getRequestUrl());
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                if (listener != null) {
                    NetError netError = new NetError();
                    netError.transferVolleyError(error);
                    listener.onLoadFail(netError);
                }
            }
        }, width, height);
    }

    /**
     * 加载图片
     * @param url 图片url
     * @param imageView 需要加载该图片的url
     * @param defaultImageResId 加载图片时的默认资源id
     * @param errorImageResId 加载图片失败时显示的图片资源id
     */
    public void loadImage(String url, final ImageView imageView, int defaultImageResId, int errorImageResId){
        int width = getImageViewFieldValue(imageView, "mMaxWidth");
        int height = getImageViewFieldValue(imageView, "mMaxHeight");

        loadImage(url, imageView, defaultImageResId, errorImageResId, width, height);
    }

    /**
     * 加载图片
     * @param url 图片url
     * @param imageView 需要加载该图片的url
     * @param defaultImageResId 加载图片时的默认资源id
     * @param errorImageResId 加载图片失败时显示的图片资源id
     * @param width 加载图片的宽度
     * @param height 加载图片的高度
     */
    public void loadImage(String url, final ImageView imageView, int defaultImageResId, int errorImageResId,
                          int width, int height){
        com.android.volley.toolbox.ImageLoader.ImageListener listener =
                com.android.volley.toolbox.ImageLoader.getImageListener(imageView,
                        defaultImageResId, errorImageResId);
        imageLoader.get(url, listener, width, height);
    }

    /**
     * 加载图片回调
     */
    public interface onLoadCallBack{
        void onLoadSuccess(Bitmap bitmap, String url);
        void onLoadFail(NetError error);
    }
}
