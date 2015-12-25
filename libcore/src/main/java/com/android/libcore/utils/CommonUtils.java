package com.android.libcore.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
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
     * 用来将uri转变成path，兼容19版本以上
     * 原文地址http://stackoverflow.com/questions/19834842/android-gallery-on-kitkat-returns-different-uri-for-intent-action-get-content
     * @param uri The Uri to query.
     */
    public static String uriToPath(final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(RootApplication.getInstance(), uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(RootApplication.getInstance(), contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };
                return getDataColumn(RootApplication.getInstance(), contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(RootApplication.getInstance(), uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
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
     * 获取进程名
     */
    public static String getCurrentProcessName(Context context){
        String currentProcessName = "";
        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses())
        {
            if (processInfo.pid == pid)
            {
                currentProcessName = processInfo.processName;
                break;
            }
        }
        return currentProcessName;
    }

    /**
     * 制造崩溃
     */
    public static int makeCrash(){
        int a[] = new int[2];
        return a[3];
    }
}
