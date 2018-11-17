package com.lifegofood.jt3282.lifego.food.frame;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by jt3282 on 2018/1/1.
 */

class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    int mSpace;
    @Override
    public void getItemOffsets(Rect outRect,View view,RecyclerView parent,RecyclerView.State state){
        super.getItemOffsets(outRect,view,parent,state);
        outRect.left=mSpace;
        outRect.right=mSpace;
        outRect.bottom=mSpace;
        if(parent.getChildAdapterPosition(view)==0){
            outRect.top=mSpace;
        }
    }
    public SpaceItemDecoration(int i) {
        this.mSpace = i;
    }
}
