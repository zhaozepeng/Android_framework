package com.android.libcore_ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.android.libcore_ui.R;

/**
 * Description: 流式布局
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-11-14
 */
public class FlowLayout extends ViewGroup{
    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;
    //默认间隙
    int verticalSpacing = 10;
    int horizontalSpacing = 10;
    //布局方向
    int orientation = HORIZONTAL;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrValue(attrs);
    }

    private void getAttrValue(AttributeSet attrs){
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.FlowLayout);

        verticalSpacing = typedArray.getDimensionPixelSize(R.styleable.FlowLayout_verticalSpacing, 10);
        horizontalSpacing = typedArray.getDimensionPixelSize(R.styleable.FlowLayout_horizontalSpacing, 10);
        orientation = typedArray.getInt(R.styleable.FlowLayout_orientation, HORIZONTAL);

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

            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            childHeight = lp.height;
            childWidth = lp.width;

            if (childHeight <= 0 || childWidth <= 0){
                child.measure(childSpec, childSpec);
                childWidth = child.getMeasuredWidth();
                childHeight = child.getMeasuredHeight();
            }
            child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY));

            lastWidth = lastWidth - childWidth - horizontalSpacing;

            //需要换行
            if (lastWidth < 0){
                //如果第一个子view的宽度已经超过容器宽度
                if (indexOfLine == 0){
                    throw new ChildWidthTooLongException("the " + i + " child's width too long");
                }

                //重置所有变量
                indexOfLine = 0;
                lastWidth = width - paddingLeft - paddingRight;
                //高度换行
                height += maxChildHeight + verticalSpacing;
                //换行之后的第一行坐标
                x = paddingLeft;
                y += maxChildHeight + verticalSpacing;
                //将最大高度值置为第一个view的高度
                maxChildHeight = childHeight;
            }
            //不需要换行
            else{
                //计算出这一行子view中高度最大的view
                maxChildHeight = maxChildHeight>childHeight ? maxChildHeight:childHeight;
                indexOfLine ++;
            }
            lp.setXY(x, y);
            x += childWidth + horizontalSpacing;
        }
        height += maxChildHeight;
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

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
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
