package com.wdh.filterviewdemo.adapter;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wdh.filterviewdemo.R;
import com.wdh.filterviewdemo.model.FilterTwoEntity;
import java.util.List;

public class FilterLeftAdapter extends RecyclerView.Adapter<FilterLeftAdapter.MyViewHolder>{
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<FilterTwoEntity> list;
    private OnItemClickListener onItemClickListener;
    public FilterLeftAdapter(Context c,List<FilterTwoEntity> data){
        this.mContext=c;
        this.list=data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }
    public void setSelectedEntity(FilterTwoEntity filterEntity) {
        for (FilterTwoEntity entity : list) {
            entity.setSelected(filterEntity != null && entity.getType().equals(filterEntity.getType()));
        }
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_filter_left, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        FilterTwoEntity entity = list.get(position);

        holder.mTv.setText(entity.getType());
        if (entity.isSelected()) {
            holder.mTv.setTextColor(ContextCompat.getColor(mContext,R.color.primary));
            holder.mLl.setBackgroundColor(ContextCompat.getColor(mContext,R.color.white));
        } else {
            holder.mTv.setTextColor(ContextCompat.getColor(mContext,R.color.font_black_2));
            holder.mLl.setBackgroundColor(ContextCompat.getColor(mContext,R.color.font_black_6));
        }

        holder.mLl.setOnClickListener(new View.OnClickListener() {
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
        private TextView mTv;
        private LinearLayout mLl;
        public MyViewHolder(View itemView) {
            super(itemView);
            mTv= (TextView) itemView.findViewById(R.id.tv_title);
            mLl= (LinearLayout) itemView.findViewById(R.id.ll_root_view);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int i);
    }
}
