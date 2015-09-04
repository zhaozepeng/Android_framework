package com.android.sample.test_utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.android.framework.R;
import com.android.libcore.utils.ImageUtils;
import com.android.libcore_ui.activity.BaseActivity;

/**
 * Description: 测试{@link com.android.libcore.utils.ImageUtils}类
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-09-04
 */
public class ImageActivity extends BaseActivity{

    private ImageView centerSquareScaleBitmap;
    private ImageView toRoundCorner;
    private Bitmap bitmap;

    @Override
    protected void initView() {
        setContentViewSrc(R.layout.activity_test_image);
        centerSquareScaleBitmap = (ImageView) findViewById(R.id.centerSquareScaleBitmap);
        toRoundCorner = (ImageView) findViewById(R.id.toRoundCorner);
    }

    @Override
    protected void initData() {
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.test_image);
        centerSquareScaleBitmap.setImageBitmap(ImageUtils.centerSquareScaleBitmap(bitmap,
                bitmap.getWidth()>bitmap.getHeight()?bitmap.getHeight():bitmap.getWidth()));
        toRoundCorner.setImageBitmap(ImageUtils.toRoundCorner(bitmap, 120));
    }
}
