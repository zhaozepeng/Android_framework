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

        if (params == null){
            try {
                Constructor constructor = clazz.getConstructor(int.class, String.class, Response.Listener.class, Response.ErrorListener.class);
                request = (Request) constructor.newInstance(Request.Method.GET, url, listener, errorListener);
            } catch (Exception e) {
                L.e("error reflect", e);
                return;
            }
        }
        //TODO 能否在runtime利用reflect覆盖一个类的方法？如果可实现此函数将大量简化
        //https://www.google.fr/?gfe_rd=cr&ei=pQf4VdKAAcHC8AeUoK_oDg&gws_rd=ssl#q=java+reflection+override+method+at+runtime

        //启动网络请求
        if ((request = handleOtherRequest(clazz, url, params, listener, errorListener)) != null){
        } else if (clazz == StringRequest.class){
            if (params != null)
                request = new StringRequest(Request.Method.POST, url, listener, errorListener){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        return params;
                    }
                };
        }else if (clazz == JsonObjectRequest.class){
            if (params != null)
                request = new JsonObjectRequest(Request.Method.POST, url, listener, errorListener){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        return params;
                    }
                };
        }else if (clazz == JsonArrayRequest.class){
            if (params != null)
                request = new JsonArrayRequest(Request.Method.POST, url, listener, errorListener){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        return params;
                    }
                };
        }else if (clazz == ImageRequest.class){
            throw new IllegalArgumentException("please use imageloader");
        }else{
            throw new IllegalArgumentException("unsupported type");
        }

        //自定义超时时间，重试次数
//        request.setRetryPolicy(new DefaultRetryPolicy());
        getRequestQueue().add(request);
    }

    /**
     * 用来在子类复写，用来处理在子module创建{@link Request}的子类request，处理方式按照
     * <code>
     *     if (clazz == A.class){
     *          if (params != null)
     *               request = new A(Request.Method.POST, url, listener, errorListener){
     *                   @Override
     *                   protected Map<String, String> getParams() throws AuthFailureError {
     *                       return params;
     *                   }
     *               };
     *           return true;
     *       }
     * </code>
     * @param clazz 网络访问类型
     * @param url　网络请求url
     * @param params 网络访问所带参数
     * @param listener　成功回调
     * @param errorListener　错误回调
     * @return request
     */
    protected abstract Request handleOtherRequest(Class<?> clazz, String url, final Map<String, String> params, Response.Listener listener,
                                                  Response.ErrorListener errorListener);

    /**
     * string 请求
     * @param context 相关上下文
     * @param url　网络访问url
     * @param params 网络请求参数
     * @param callback　网络请求回调
     */
    public void stringRequest(Context context, String url, Map<String, String> params, OnNetCallback<String> callback){
        makeRequest(context, StringRequest.class, url, params, callback);
    }

    /**
     * jsonObject 请求
     * @param context 相关上下文
     * @param url　网络访问url
     * @param params 网络请求参数
     * @param callback　网络请求回调
     */
    public void jsonObjectRequest(Context context, String url, Map<String, String> params, OnNetCallback<JSONObject> callback){
        makeRequest(context, JsonObjectRequest.class, url, params, callback);
    }

    /**
     * jsonArray 请求
     * @param context 相关上下文
     * @param url　网络访问url
     * @param params 网络请求参数
     * @param callback　网络请求回调
     */
    public void jsonArrayRequest(Context context, String url, Map<String, String> params, OnNetCallback<JSONArray> callback){
        makeRequest(context, JsonArrayRequest.class, url, params, callback);
    }
}
