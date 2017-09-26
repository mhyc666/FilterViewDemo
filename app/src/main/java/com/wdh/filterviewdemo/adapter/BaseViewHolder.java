package com.wdh.filterviewdemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;


public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);

    }

    protected abstract void bind(T t,int i);
}
