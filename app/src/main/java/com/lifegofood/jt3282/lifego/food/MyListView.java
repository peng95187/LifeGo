package com.lifegofood.jt3282.lifego.food;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by jt3282 on 2017/11/20.
 */

public class MyListView extends ListView {

    public MyListView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public MyListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //根據模式計算每個Child的高度和寬度
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
