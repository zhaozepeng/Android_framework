package com.android.libcore.netapi;

import android.app.Activity;
import android.content.Context;

import com.android.libcore.application.RootApplication;
import com.android.libcore.log.L;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.util.Map;

/**
 * Description: 所有网络访问基础类，使用了volley框架，并且进行了简单的基础
 * 封装，<Strong>所有进行网络访问的地方都使用该封装类</Strong>，方便以后更换网络访问框架，
 * 只需修改libcore，libcore-ui层相关类即可，这也是封装的唯一目的<br/>
 *
 * 不能在libcore，libcore-ui层的相关类之外直接使用volley框架内容
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-08
 */
public abstract class BaseNetApi {
    /** 网络访问requestQueue */
    private RequestQueue requestQueue;

    private RequestQueue getRequestQueue(int maxDiskCacheBytes){
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(RootApplication.getInstance(), maxDiskCacheBytes);
        return requestQueue;
    }

    protected RequestQueue getRequestQueue(){
        return getRequestQueue(-1);
    }

    /**
     * 回调接口
     */
    public interface OnNetCallback<T>{
        void onSuccess(T result);
        void onFail(NetError error);
    }

    private boolean checkIfExtendsRequest(Class clazz){
        while (clazz.getSuperclass() != null){
            clazz = clazz.getSuperclass();
            if (clazz == Request.class)
                return true;
        }
        return false;
    }

    /**
     * 网络请求
     */
    protected <T> void makeRequest(final Context context, Class<?> clazz, String url, final Map<String, String> params, final OnNetCallback<T> callback){
        //网络请求
        Request request = null;
        //失败回调
        Response.ErrorListener errorListener = null;
        //成功回调
        Response.Listener listener = null;
        //判空
        if (callback != null) {
            errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (context instanceof Activity && (((Activity)(context)).isFinishing())) {
                        L.i("activity finish, not callback");
                        return ;
                    }
                    NetError netError = new NetError();
                    netError.transferVolleyError(error);
                    callback.onFail(netError);
                }
            };
            listener = new Response.Listener<T>() {
                @Override
                public void onResponse(T response) {
                    if (context instanceof Activity && (((Activity)(context)).isFinishing())) {
                        L.i("activity finish, not callback");
                        return ;
                    }
                    callback.onSuccess(response);
                }
            };
        }

        //启动网络请求
        if (clazz == ImageRequest.class){
            throw new IllegalArgumentException("please use imageloader");
        }else if (checkIfExtendsRequest(clazz)) {
            try {
                Constructor constructor = clazz.getConstructor(int.class, String.class, Response.Listener.class,
                        Response.ErrorListener.class, Map.class);
                request = (Request) constructor.newInstance(Request.Method.GET, url, listener, errorListener, params);
            } catch (Exception e) {
                L.e("error reflect", e);
                return;
            }
        }else {
            throw new IllegalArgumentException("unsupported type");
        }

        //自定义超时时间，重试次数
//        request.setRetryPolicy(new DefaultRetryPolicy());
        getRequestQueue().add(request);
    }

    /**
     * 对{@linkplain StringRequest}的封装类
     */
    public static class StringRequestImpl extends StringRequest{
        private Map<String, String> params;

        public StringRequestImpl(int method, String url, Response.Listener<String> listener,
                                 Response.ErrorListener errorListener, Map<String, String> params) {
            super(method, url, listener, errorListener);
            this.params = params;
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return params;
        }
    }

    /**
     * 对{@linkplain JsonObjectRequest}的封装类
     */
    public static class JsonObjectRequestImpl extends JsonObjectRequest{
        private Map<String, String> params;

        public JsonObjectRequestImpl(int method, String url, Response.Listener<JSONObject> listener,
                                 Response.ErrorListener errorListener, Map<String, String> params) {
            super(method, url, listener, errorListener);
            this.params = params;
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return params;
        }
    }

    /**
     * 对{@linkplain JsonArrayRequest}的封装类
     */
    public static class JsonArrayRequestImpl extends JsonArrayRequest{
        private Map<String, String> params;

        public JsonArrayRequestImpl(int method, String url, Response.Listener<JSONArray> listener,
                                 Response.ErrorListener errorListener, Map<String, String> params) {
            super(method, url, listener, errorListener);
            this.params = params;
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return params;
        }
    }

    /**
     * string 请求
     * @param context 相关上下文
     * @param url　网络访问url
     * @param params 网络请求参数
     * @param callback　网络请求回调
     */
    public void stringRequest(Context context, String url, Map<String, String> params, OnNetCallback<String> callback){
        makeRequest(context, StringRequestImpl.class, url, params, callback);
    }

    /**
     * jsonObject 请求
     * @param context 相关上下文
     * @param url　网络访问url
     * @param params 网络请求参数
     * @param callback　网络请求回调
     */
    public void jsonObjectRequest(Context context, String url, Map<String, String> params, OnNetCallback<JSONObject> callback){
        makeRequest(context, JsonObjectRequestImpl.class, url, params, callback);
    }

    /**
     * jsonArray 请求
     * @param context 相关上下文
     * @param url　网络访问url
     * @param params 网络请求参数
     * @param callback　网络请求回调
     */
    public void jsonArrayRequest(Context context, String url, Map<String, String> params, OnNetCallback<JSONArray> callback){
        makeRequest(context, JsonArrayRequestImpl.class, url, params, callback);
    }
}
