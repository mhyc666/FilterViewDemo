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

/**
 *
 * Created by Administrator on 2017/9/21.
 */

public class FilterOneAdapter extends RecyclerView.Adapter<FilterOneAdapter.MyViewHolder>{
    private List<FilterEntity> data;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    public FilterOneAdapter(Context context,List<FilterEntity> data){
        this.data=data;
        this.mContext=context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_one, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        FilterEntity entity = data.get(position);

        holder.mTv.setText(entity.getKey());
        if (entity.isSelected()) {
            holder.mTv.setTextColor(ContextCompat.getColor(mContext,R.color.primary));
        } else {
            holder.mTv.setTextColor(ContextCompat.getColor(mContext,R.color.font_black_3));
        }
        holder.mLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick(position);
                }
            }
        });

    }

    public void setSelectedEntity(FilterEntity filterEntity) {
        for (FilterEntity entity : data) {
            entity.setSelected(filterEntity != null && entity.getKey().equals(filterEntity.getKey()));
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mLL;
        private TextView mTv;
        private ImageView mIv;
        public MyViewHolder(View itemView) {
            super(itemView);
            mTv= (TextView) itemView.findViewById(R.id.tv_title);
            mIv= (ImageView) itemView.findViewById(R.id.iv_image);
            mLL= (LinearLayout) itemView.findViewById(R.id.ll);
        }
    }
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}
