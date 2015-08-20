package com.android.libcore.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;

import com.android.libcore.application.RootApplication;
import com.android.libcore.log.L;

import java.security.MessageDigest;

/**
 * Description: 基础工具类，该类的所有函数如下所示(所有添加到该类的函数都应该在这标识出来，以防重复)
 * <ol>
 *     <li>{@link #dp2px(float)}用来将dp转换为px</li>
 *     <li>{@link #px2dp(float)}用来将px转换为dp</li>
 *     <li>{@link #isNetworkAvailable()}用来判断网络是否可用</li>
 *     <li>{@link #isNetworkWifi()}用来判断网络是否是wifi</li>
 *     <li>{@link #hasNavigationBar()}判断手机是否会有navigation bar</li>
 *     <li>{@link #md5(String)}用来对字符串进行md5加密</li>
 * </ol>
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-14
 */
public class CommonUtils {

    public static int dp2px(float dp){
        final float scale = RootApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int px2dp(float px) {
        final float scale = RootApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static boolean isNetworkAvailable(){
        ConnectivityManager cm = (ConnectivityManager) RootApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info!=null && info.isAvailable();
    }

    public static boolean isNetworkWifi(){
        if (!isNetworkAvailable()){
            L.e("当前网络可用，请先调用isNetworkAvailable()函数");
        }
        ConnectivityManager cm = (ConnectivityManager) RootApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info.getType() == ConnectivityManager.TYPE_WIFI)
            return true;
        return false;
    }

    public static String md5(String s){
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 检查手机是否会有虚拟底部navigation bar
     */
    public static boolean hasNavigationBar(){
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);

        if(hasBackKey && hasHomeKey) {
            return false;
        }
        return true;
    }
}
