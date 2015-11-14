package com.android.libcore_ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.android.libcore_ui.R;

/**
 * Description: 水平流式布局
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-11-14
 */
public class FlowLayout extends ViewGroup{
    //默认间隙
    int verticalSpacing = 10;
    int horizontalSpacing = 10;

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrAndSetSpacing(attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrAndSetSpacing(attrs);
    }

    private void getAttrAndSetSpacing(AttributeSet attrs){
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.FlowLayout);

        verticalSpacing = typedArray.getInt(R.styleable.FlowLayout_verticalSpacing, 10);
        horizontalSpacing = typedArray.getInt(R.styleable.FlowLayout_horizontalSpacing, 10);

        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getChildCount() <= 0){
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        int paddingTop = getPaddingTop();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = 0;

        int childWidth;
        int childHeight;

        //该行的子view序号，递增
        int indexOfLine = 0;
        //该行最大子view高度
        int maxChildHeight = 0;
        //剩余宽度
        int lastWidth = width - paddingLeft - paddingRight;

        int x = paddingLeft;
        int y = paddingTop;

        int childSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        for (int i=0; i<getChildCount(); i++){
            View child = getChildAt(i);
            child.measure(childSpec, childSpec);

            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            lp.setXY(x, y);

            childWidth = child.getMeasuredWidth();
            childHeight = child.getMeasuredHeight();

            //计算出这一行子view中高度最大的view
            maxChildHeight = maxChildHeight>childHeight ? maxChildHeight:childHeight;

            lastWidth = lastWidth - childWidth - horizontalSpacing;

            //需要换行
            if (lastWidth < 0){
                //如果第一个子view的宽度已经超过容器宽度
                if (indexOfLine == 0){
                    throw new ChildWidthTooLongException("first child's width too long");
                }

                indexOfLine = 0;
                lastWidth = width - paddingLeft - paddingRight;
                height += maxChildHeight + verticalSpacing;
                x = paddingLeft;
                y += maxChildHeight + verticalSpacing;
            }
            //不需要换行
            else{
                indexOfLine ++;
                x += childWidth + horizontalSpacing;
            }
        }
        height -= verticalSpacing;
        height = height + paddingBottom + paddingTop;
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            child.layout(lp.x, lp.y, lp.x + child.getMeasuredWidth(), lp.y + child.getMeasuredHeight());
        }
    }

    public static class LayoutParams extends ViewGroup.LayoutParams{
        public int x;
        public int y;

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public void setXY(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    public static class ChildWidthTooLongException extends RuntimeException{
        public ChildWidthTooLongException(String message){
            super(message);
        }
    }
}
