package com.wdh.filterviewdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wdh.filterviewdemo.R;
import com.wdh.filterviewdemo.model.TravelingEntity;
import com.wdh.filterviewdemo.view.GildeImageView.GlideImageView;

import java.util.List;

/**
 * Created by Administrator on 2017/9/21.
 */

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TravelingEntity> mList;
    private Context mContext;
    private boolean isNoData;
    private int mHeight;
    public static final int ONE_SCREEN_COUNT = 8; // 一次显示的个数，这个根据屏幕高度和各自的需求定，
    public static final int TYPE_ONE = 0; //无数据
    private static final int TYPE_TWO = 1; //有数据

    public MainAdapter(Context context, List<TravelingEntity> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getItemViewType(int position) {
        if (isNoData == true) {
            return MainAdapter.TYPE_ONE;
        }
        if (isNoData == false) {
            return MainAdapter.TYPE_TWO;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ONE:
                return new NoDataViewHolder(
                        LayoutInflater.from(mContext).inflate(R.layout.item_nodata_adapter, parent, false));
            case TYPE_TWO:

                return new onDataViewHolder(
                        LayoutInflater.from(mContext).inflate(R.layout.item_adapter, parent, false));
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemType = getItemViewType(position);
        switch (itemType) {
            case TYPE_ONE:
                ((NoDataViewHolder) holder).bind(mList, position);
                break;
            case TYPE_TWO:
                ((onDataViewHolder) holder).bind(mList, position);
                break;
            default:
                break;
        }
    }


    class onDataViewHolder extends BaseViewHolder<List<TravelingEntity>> {
        private GlideImageView iv;
        private TextView mTv1, mTv2;

        public onDataViewHolder(View itemView) {
            super(itemView);
            iv = (GlideImageView) itemView.findViewById(R.id.giv_image);
            mTv1 = (TextView) itemView.findViewById(R.id.tv_title);
            mTv2 = (TextView) itemView.findViewById(R.id.tv_rank);
        }

        @Override
        protected void bind(List<TravelingEntity> travelingEntities, int position) {
            try {
                TravelingEntity data = mList.get(position);
                String title = data.getFrom() + data.getTitle() + data.getType();
                mTv1.setText(title);
                mTv2.setText(String.format("排名:%c", data.getRank()));
                iv.loadNetImage(data.getImage_url(), R.color.font_black_6);
            } catch (Exception e) {

            }
        }
    }

    class NoDataViewHolder extends BaseViewHolder<List<TravelingEntity>> {

        public NoDataViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void bind(List<TravelingEntity> travelingEntities, int i) {
            try {

            } catch (Exception e) {

            }
        }
    }


    // 设置数据
    public void setData(List<TravelingEntity> list) {
        clearAll();

        isNoData = false;
        if (list.size() == 1 && list.get(0).isNoData()) {
            // 暂无数据布局
            isNoData = list.get(0).isNoData();
            mHeight = list.get(0).getHeight();
        } else {
            // 添加空数据
            // if (list.size() < ONE_SCREEN_COUNT) {
            //addALL(createEmptyList(ONE_SCREEN_COUNT - list.size()));
            //  }
        }
        addALL(list);
        notifyDataSetChanged();
    }

//    // 创建不满一屏的空数据
//    public List<TravelingEntity> createEmptyList(int size) {
//        List<TravelingEntity> emptyList = new ArrayList<>();
//        if (size <= 0) return emptyList;
//        for (int i = 0; i < size; i++) {
//            emptyList.add(new TravelingEntity());
//        }
//        return emptyList;
//    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }


    public void clearAll() {
        mList.clear();
    }

    public void addALL(List<TravelingEntity> list) {
        if (list == null || list.size() == 0) return;
        mList.addAll(list);
    }
}
