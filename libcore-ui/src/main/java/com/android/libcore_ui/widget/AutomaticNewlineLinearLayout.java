package com.android.libcore_ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.android.libcore_ui.R;

/**
 * @author: zzp
 * @since: 2015-07-02
 * Description: 自动换行LinearLayout，请确保height和bottomMargin一致
 */
public class AutomaticNewlineLinearLayout extends LinearLayout{

    private int NUMS_PER_LINE = 3;

    public AutomaticNewlineLinearLayout(Context context) {
        super(context);
    }

    public AutomaticNewlineLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LinearLayout_Columns);
        //默认为3行
        NUMS_PER_LINE = array.getInteger(R.styleable.LinearLayout_Columns_columns, 3);
        if (NUMS_PER_LINE <= 1)
            throw new IllegalArgumentException("columns must greater than 1");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getChildCount() == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        int margin_bottom = ((LayoutParams)(getChildAt(0).getLayoutParams())).bottomMargin;
        int childHeight = ((LayoutParams)(getChildAt(0).getLayoutParams())).height;

        int lines = (int)Math.ceil(((getChildCount()*1.0) / (NUMS_PER_LINE*1.0)));
        int height = lines * (margin_bottom + childHeight);
        int heightChildSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(heightMeasureSpec));
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);

        int count = getChildCount();
        for (int i = 0; i < count ; i++){
            int childWidth = ((LayoutParams)(getChildAt(i).getLayoutParams())).width;
            int widthChildSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST);
            getChildAt(i).measure(widthChildSpec, heightChildSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() == 0) {
            super.onLayout(changed, l, t, r, b);
            return;
        }
        int childHeight = ((LayoutParams)(getChildAt(0).getLayoutParams())).height;
        int childHeightWithMargin = childHeight + ((LayoutParams)(getChildAt(0).getLayoutParams())).bottomMargin;

        for (int i=0; i<getChildCount(); i++){
            int childWidth = ((LayoutParams)(getChildAt(i).getLayoutParams())).width;
            int position = i%NUMS_PER_LINE;
            int xPos = (position)*((((r-l)-childWidth*NUMS_PER_LINE)/(NUMS_PER_LINE-1))+childWidth);
            int yPos= (i/NUMS_PER_LINE)* childHeightWithMargin;
            getChildAt(i).layout(xPos, yPos, xPos+childWidth, yPos+childHeight);
        }
    }
}
