package com.android.libcore.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Description: 图片相关处理类
 *
 * <ol>
 *     <li>{@link #centerSquareScaleBitmap(Bitmap, int)}截取图片的正中部分</li>
 *     <li>{@link #toRoundCorner(Bitmap, int)}将图片截成圆角的方法</li>
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
            bitmap.recycle();
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
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
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
    }
}
