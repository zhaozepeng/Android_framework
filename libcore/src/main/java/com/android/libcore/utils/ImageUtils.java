package com.android.libcore.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.android.libcore.Toast.T;
import com.android.libcore.application.RootApplication;
import com.android.libcore.log.L;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Description: 图片相关处理类
 *
 * <ol>
 *     <li>{@link #centerSquareScaleBitmap(Bitmap, int)}截取图片的正中部分</li>
 *     <li>{@link #toRoundCorner(Bitmap, int)}将图片截成圆角的方法</li>
 *     <li>{@link #saveBitmap(Bitmap, String, Runnable)}保存图片到制定路径下</li>
 *     <li>{@link #screenShot(Activity)}截取手机当前屏幕</li>
 *     <li>{@link #viewShot(View)}截取view的整个显示内容</li>
 * </ol>
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-31
 */
public class ImageUtils {
    /**
     * @param bitmap      原图
     * @param edgeLength  希望得到的正方形部分的边长
     * @return  截取正中部分后的位图。
     */
    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength){
        if(null == bitmap || edgeLength <= 0){
            return  null;
        }
        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();
        //从图中截取正中间的正方形部分。
        int xTopLeft = (widthOrg - edgeLength) / 2;
        int yTopLeft = (heightOrg - edgeLength) / 2;
        if(xTopLeft==0 && yTopLeft==0)
            return result;
        try{
            result = Bitmap.createBitmap(bitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
        }
        catch(OutOfMemoryError e){
            return result;
        }
        return result;
    }

    /**
     * 获取圆角位图的方法
     * @param bitmap 需要转化成圆角的位图
     * @param pixels 圆角的度数，数值越大，圆角越大
     * @return 处理后的圆角位图
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);
            final float roundPx = pixels;
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
            return output;
        }catch (OutOfMemoryError e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 截屏，只能截取当前屏幕显示的区域，不包含status bar
     */
    public static Bitmap screenShot(Activity activity){
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        // 获取屏幕长和高
        int width = CommonUtils.getScreenWidth();
        int height = CommonUtils.getScreenHeight();
        // 去掉标题栏
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    /**
     * view截图，webview和scrollview(scrollview需要传入子view)之类的view能够截取整个长度的bitmap，
     * 如果webview内容很多，view.draw(Canvas)方法会很耗时，在子进程中操作会有额外的问题，所以会暂时阻塞
     * UI主线程
     */
    public static Bitmap viewShot(final View view){
        if (view == null)
            return null;
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        if (view.getMeasuredWidth()<=0 || view.getMeasuredHeight()<=0) {
            int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            view.measure(measureSpec, measureSpec);
        }

        if (view.getMeasuredWidth()<=0 || view.getMeasuredHeight()<=0) {
            L.e("ImageUtils.viewShot size error");
            return null;
        }
        Bitmap bm;
        try {
            bm = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        }catch (OutOfMemoryError e){
            System.gc();
            try {
                bm = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            }catch (OutOfMemoryError ee){
                L.e("ImageUtils.viewShot error", ee);
                return null;
            }
        }
        Canvas bigCanvas = new Canvas(bm);
        Paint paint = new Paint();
        int iHeight = bm.getHeight();
        bigCanvas.drawBitmap(bm, 0, iHeight, paint);
        view.draw(bigCanvas);
        return bm;
    }

    /**
     * 保存bitmap到指定路径下
     * @param finish 保存完之后的回调，注意不在主线程执行
     */
    public static void saveBitmap(final Bitmap bitmap, final String filePath, final Runnable finish) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File file = new File(filePath);
                    if (!file.exists())
                        file.createNewFile();
                    else {
                        file.delete();
                        file.createNewFile();
                    }
                    //使用FileOutputStream防止OOM
                    FileOutputStream fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (finish != null)
                    finish.run();
            }
        }).start();
    }
}
