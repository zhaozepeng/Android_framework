package com.android.sample.test_imageloader;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.framework.R;
import com.android.libcore.net.NetError;
import com.android.libcore.net.imageloader.ImageLoader;
import com.android.libcore.utils.CommonUtils;
import com.android.libcore_ui.activity.BaseActivity;

/**
 * Description: 测试图片加载类
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-10-27
 */
public class ImageActivity extends BaseActivity{
    private GridView gv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    protected void initView() {
        setContentView(R.layout.activity_test_imageloader);
        gv_content = (GridView) findViewById(R.id.gv_content);
    }

    protected void initData() {
        gv_content.setAdapter(new GridAdapter());
    }

    public class GridAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return PicUrl.pics.length;
        }

        @Override
        public Object getItem(int position) {
            return PicUrl.pics[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new ImageView(ImageActivity.this);
                convertView.setLayoutParams(new AbsListView.LayoutParams(CommonUtils.getScreenWidth()/3, CommonUtils.getScreenWidth()/3));
            }
            final ImageView imageView = (ImageView) convertView;
            imageView.setTag(PicUrl.pics[position]);
            ImageLoader.getInstance().loadImage(PicUrl.pics[position], CommonUtils.getScreenWidth() / 3, CommonUtils.getScreenWidth() / 3,
                    new ImageLoader.OnLoadCallBack() {
                        @Override
                        public void onLoadSuccess(Bitmap bitmap, String url) {
                            if (!imageView.getTag().equals(url))
                                return;
                            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                            imageView.setBackgroundDrawable(bitmapDrawable);
                        }

                        @Override
                        public void onLoadFail(NetError error) {

                        }
                    });
            return convertView;
        }
    }
}
