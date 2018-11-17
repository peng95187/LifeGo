package com.lifegofood.jt3282.lifego.food.frame;

import android.support.v7.widget.RecyclerView;

/**
 * Created by jt3282 on 2018/10/23.
 */

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected boolean isScrolling = false;

    public void setScrolling(boolean scrolling) {
        isScrolling = scrolling;
    }
}
