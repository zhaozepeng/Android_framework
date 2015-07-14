package com.android.libcore_ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.libcore_ui.R;

/**
 * Description: 底部弹出框的容器
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-07-14
 */
public class BottomBarGroupLinearLayout extends LinearLayout{
    private int groupId;
    private boolean hasSetGroupId = false;
    private LayoutInflater inflater;
    private GroupItemClickCallback callback;

    public BottomBarGroupLinearLayout(Context context) {
        super(context);
    }

    public BottomBarGroupLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /** 设置该项的groupId */
    public void setGroupId(int groupId){
        this.groupId = groupId;
        hasSetGroupId = true;
        inflater = LayoutInflater.from(getContext());
    }

    public void addItemToGroup(int itemId, String name){
        if (!hasSetGroupId){
            throw new IllegalArgumentException("set groupId first");
        }
        View view = inflater.inflate(R.layout.bottom_item_layout, null);
        TextView tv_item_name = (TextView) view.findViewById(R.id.tv_item_name);
        tv_item_name.setText(name);
        view.setTag(itemId);
        this.addView(view);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.callback(groupId, (Integer) v.getTag());
            }
        });

        //将最后一个view的底部分割线去除
        for (int i=0; i<getChildCount(); i++){
            getChildAt(i).findViewById(R.id.v_line).setVisibility(View.VISIBLE);
        }
        getChildAt(getChildCount()-1).findViewById(R.id.v_line).setVisibility(View.GONE);
    }

    public void setCallback(GroupItemClickCallback callback){
        this.callback = callback;
    }

    /**
     * 点击item之后的回调
     */
    public interface GroupItemClickCallback{
        public void callback(int groupId, int ItemId);
    }
}
