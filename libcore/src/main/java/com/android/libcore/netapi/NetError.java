package com.android.libcore.netapi;

import com.android.volley.VolleyError;

/**
 * Description: 网络访问错误error
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-09-15
 */
public class NetError extends Exception{
    public int errorCode;
    public String errorMessage;

    /**
     * 将volley的错误信息转换成通用的信息
     */
    public void transferVolleyError(VolleyError error){
        this.errorCode = error.networkResponse.statusCode;
        this.errorMessage = error.getMessage();
    }
}
