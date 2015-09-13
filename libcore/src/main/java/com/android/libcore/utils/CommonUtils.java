package com.android.libcore.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;

import com.android.libcore.application.RootApplication;
import com.android.libcore.log.L;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Description: 基础工具类，该类的所有函数如下所示(所有添加到该类的函数都应该在这标识出来，以防重复)
 * <ol>
 *     <li>{@link #dp2px(float)}用来将dp转换为px</li>
 *     <li>{@link #px2dp(float)}用来将px转换为dp</li>
 *     <li>{@link #getScreenWidth()}获取手机屏幕宽度</li>
 *     <li>{@link #getScreenHeight()}获取手机屏幕高度（有些手机会除去navigation bar高度）</li>
 *     <li>{@link #isNetworkAvailable()}用来判断网络是否可用</li>
 *     <li>{@link #isNetworkWifi()}用来判断网络是否是wifi</li>
 *     <li>{@link #hasNavigationBar()}判断手机是否会有navigation bar</li>
 *     <li>{@link #md5(String)}用来对字符串进行md5加密</li>
 *     <li>{@link #pathToUri(String)}手机图片路径path转uri</li>
 *     <li>{@link #uriToPath(Uri)}手机图片uri转路径path</li>
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

    public static int getScreenWidth(){
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)RootApplication.getInstance()).getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight(){
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)RootApplication.getInstance()).getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 判断当前网络是是否可用
     */
    public static boolean isNetworkAvailable(){
        ConnectivityManager cm = (ConnectivityManager) RootApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info!=null && info.isAvailable();
    }

    /**
     * 判断当前的网络是否是wifi
     */
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

    /**
     * md5加密
     */
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * 手机图片uri转path
     */
    public static String uriToPath(Uri imageUri) {
        String targetPath = "";
        if (!imageUri.toString().startsWith("file://")) {// file开头的路径不需要再进行转换
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = RootApplication.getInstance().getContentResolver().query(imageUri, filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                targetPath = cursor.getString(columnIndex);
                cursor.close();
            }
        } else {
            targetPath = imageUri.toString().substring(7, imageUri.toString().length());
        }
        return targetPath;
    }

    /**
     * 手机图片path转uri
     */
    public static Uri pathToUri(String path){
        Uri uri = null;
        if (path != null) {
            path = Uri.decode(path);
            ContentResolver cr = RootApplication.getInstance().getContentResolver();
            StringBuffer buff = new StringBuffer();
            buff.append("(").append(MediaStore.Images.ImageColumns.DATA)
                    .append("=").append("'" + path + "'").append(")");
            Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[] { MediaStore.Images.ImageColumns._ID },
                    buff.toString(), null, null);
            int index = 0;
            for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                index = cur.getInt(index);
            }
            if (index == 0) {
            } else {
                Uri uri_temp = Uri.parse("content://media/external/images/media/" + index);
                if (uri_temp != null) {
                    uri = uri_temp;
                }
            }
        }
        return uri;
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
