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

    /**
     * 代回调的加载图片
     * @param url 图片url
     * @param width
     * @param height
     * @param listener
     */
    public void loadImage(String url, int width, int height, final onLoadCallBack listener){
        imageLoader.get(url, new com.android.volley.toolbox.ImageLoader.ImageListener() {
            @Override
            public void onResponse(com.android.volley.toolbox.ImageLoader.ImageContainer response, boolean isImmediate) {
                listener.onLoadSuccess(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                NetError netError = new NetError();
                netError.transferVolleyError(error);
                listener.onLoadFail(netError);
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
    public void loadImage(String url, final ImageView imageView, int defaultImageResId,
                          final int errorImageResId){
        com.android.volley.toolbox.ImageLoader.ImageListener listener =
                com.android.volley.toolbox.ImageLoader.getImageListener(imageView,
                defaultImageResId, errorImageResId);
        imageLoader.get(url, listener);
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
     * 加载图片回调
     */
    public interface onLoadCallBack{
        void onLoadSuccess(Bitmap bitmap);
        void onLoadFail(NetError error);
    }
}
