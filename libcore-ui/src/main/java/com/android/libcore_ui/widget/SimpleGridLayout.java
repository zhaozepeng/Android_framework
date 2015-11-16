package com.android.libcore_ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.android.libcore_ui.R;


/**
 * Description: 自定义的网格布局，每个子view的高度和宽度一致，布局以第一个子view的高度为基础
 * 进行父控件的高度测量，以第一个子view的宽度进行所有view的位置控制
 *
 * @author  zzp
 * @since  2015-07-02
 */
public class SimpleGridLayout extends ViewGroup{

    private int NUM_PER_LINE = 3;
    private int verticalSpacing = 10;

    public SimpleGridLayout(Context context) {
        super(context);
    }

    public SimpleGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SimpleGridLayout);
        //默认为3行
        NUM_PER_LINE = array.getInteger(R.styleable.SimpleGridLayout_columns, 3);
        if (NUM_PER_LINE <= 1)
            throw new IllegalArgumentException("columns must greater than 1");
        verticalSpacing = array.getDimensionPixelSize(R.styleable.SimpleGridLayout_verticalItemSpacing, 10);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getChildCount() == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int childHeight = (getChildAt(0).getLayoutParams()).height;
        int childWidth = (getChildAt(0).getLayoutParams()).width;

        if (childHeight <= 0 || childWidth <= 0){
            getChildAt(0).measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            childHeight = getChildAt(0).getMeasuredHeight();
            childWidth = getChildAt(0).getMeasuredHeight();
        }

        int lines = (int)Math.ceil(((getChildCount()*1.0) / (NUM_PER_LINE *1.0)));
        int height = lines * (verticalSpacing + childHeight) + paddingBottom + paddingTop - verticalSpacing;
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(heightMeasureSpec));
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);

        for (int i=0; i<getChildCount(); i++){
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
            getChildAt(i).measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() == 0) {
            return;
        }

        int paddingTop = getPaddingTop();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();

        l += paddingLeft;
        r -= paddingRight;

        int childHeight = (getChildAt(0).getLayoutParams()).height;
        if (childHeight <= 0){
            childHeight = getChildAt(0).getMeasuredHeight();
        }
        int childWidth = (getChildAt(0).getLayoutParams()).width;
        if (childWidth <= 0){
            childWidth = getChildAt(0).getMeasuredWidth();
        }

        for (int i=0; i<getChildCount(); i++){
            int position = i% NUM_PER_LINE;
            int xPos = (position)*((((r-l)-childWidth*NUM_PER_LINE)/(NUM_PER_LINE -1))+childWidth);
            int yPos= (i/NUM_PER_LINE)*(childHeight+verticalSpacing)+paddingTop;
            getChildAt(i).layout(xPos, yPos, xPos+childWidth, yPos+childHeight);
        }
    }
}
