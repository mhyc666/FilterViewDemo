package com.wdh.filterviewdemo.adapter;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wdh.filterviewdemo.R;
import com.wdh.filterviewdemo.model.FilterEntity;

import java.util.List;

public class FilterRightAdapter extends RecyclerView.Adapter<FilterRightAdapter.MyViewHolder> {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<FilterEntity> list;
    private OnItemClickListener onItemClickListener;

    public FilterRightAdapter(Context mContext, List<FilterEntity> data) {
        this.mContext = mContext;
        this.list = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setSelectedEntity(FilterEntity filterEntity) {
        for (FilterEntity entity : list) {
            entity.setSelected(filterEntity != null && entity.getKey().equals(filterEntity.getKey()));
        }
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_filter_one, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        FilterEntity entity = list.get(position);
        holder.tv.setText(entity.getKey());
        if (entity.isSelected()) {
            holder.tv.setTextColor(ContextCompat.getColor(mContext, R.color.primary));
        } else {
            holder.tv.setTextColor(ContextCompat.getColor(mContext, R.color.font_black_3));
        }
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv;
        private TextView tv;
        private LinearLayout ll;

        public MyViewHolder(View itemView) {
            super(itemView);
            ll= (LinearLayout) itemView.findViewById(R.id.ll);
            iv = (ImageView) itemView.findViewById(R.id.iv_image);
            tv = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int i);
    }
}
