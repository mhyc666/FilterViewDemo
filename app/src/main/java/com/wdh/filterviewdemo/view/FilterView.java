package com.wdh.filterviewdemo.view;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wdh.filterviewdemo.R;
import com.wdh.filterviewdemo.adapter.FilterLeftAdapter;
import com.wdh.filterviewdemo.adapter.FilterOneAdapter;
import com.wdh.filterviewdemo.adapter.FilterRightAdapter;
import com.wdh.filterviewdemo.model.FilterData;
import com.wdh.filterviewdemo.model.FilterEntity;
import com.wdh.filterviewdemo.model.FilterTwoEntity;
import com.wdh.filterviewdemo.util.RecyclerViewUtil;

/**
 * wdh
 * Created by Administrator on 2017/9/20.
 */

public class FilterView extends LinearLayout implements View.OnClickListener {
    private Context context;
    private LinearLayout[] ll;
    private TextView[] tv;
    private ImageView[] iv;
    private RecyclerView[] mRv;
    private View viewMaskBg;
    private FrameLayout mFrame;
    private int filterPosition = -1;
    private int lastFilterPosition = -1;
    private int panelHeight;
    private boolean isShowing = false;
    private FilterData filterData;
    private FilterLeftAdapter leftAdapter;
    private FilterRightAdapter rightAdapter;
    private FilterOneAdapter sortAdapter;
    private FilterTwoEntity leftSelectedCategoryEntity; // 被选择的分类项左侧数据
    private FilterEntity rightSelectedCategoryEntity; // 被选择的分类项右侧数据
    private FilterEntity selectedSortEntity; // 被选择的排序项
    public static final int POSITION_CATEGORY = 0; // 分类的位置
    public static final int POSITION_SORT = 1; // 排序的位置
    public static final int POSITION_STYLE = 2; // 图标的位置
    public static final int POSITION_FILTER = 3; // 价格的位置
    public static boolean flag = false;

    public FilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FilterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.view_filter_layout, this);
        initView(view);
        initListener();
    }

    private void initView(View view) {
        ll = new LinearLayout[5];
        ll[0] = (LinearLayout) view.findViewById(R.id.ll_fl);
        ll[1] = (LinearLayout) view.findViewById(R.id.ll_px);
        ll[2] = (LinearLayout) view.findViewById(R.id.ll_jg);
        ll[3] = (LinearLayout) view.findViewById(R.id.ll_content_view);
        ll[4] = (LinearLayout) view.findViewById(R.id.ll_head_layout);
        tv = new TextView[3];
        tv[0] = (TextView) view.findViewById(R.id.tv_fl);
        tv[1] = (TextView) view.findViewById(R.id.tv_px);
        tv[2] = (TextView) view.findViewById(R.id.tv_jg);
        iv = new ImageView[3];
        iv[0] = (ImageView) view.findViewById(R.id.iv_fl);
        iv[1] = (ImageView) view.findViewById(R.id.iv_px);
        iv[2] = (ImageView) view.findViewById(R.id.iv);
        mRv = new RecyclerView[2];
        mRv[0] = (RecyclerView) view.findViewById(R.id.rv_left);
        mRv[1] = (RecyclerView) view.findViewById(R.id.rv_right);
        viewMaskBg = view.findViewById(R.id.view_mask_bg);
        mFrame = (FrameLayout) view.findViewById(R.id.frame);

        viewMaskBg.setVisibility(GONE);
        ll[3].setVisibility(GONE);
    }

    private void initListener() {
        ll[0].setOnClickListener(this);
        ll[1].setOnClickListener(this);
        ll[2].setOnClickListener(this);
        iv[2].setOnClickListener(this);
        viewMaskBg.setOnClickListener(this);
        ll[3].setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_fl:
                filterPosition = 0;
                if (onFilterClickListener != null) {
                    onFilterClickListener.onFilterClick(filterPosition);
                }
                break;
            case R.id.ll_px:
                filterPosition = 1;
                if (onFilterClickListener != null) {
                    onFilterClickListener.onFilterClick(filterPosition);
                }
                break;
            case R.id.iv:   ////不需要显示view
                filterPosition = 2;
                if (flag) {
                    iv[2].setImageResource(R.mipmap.ic_launcher);
                } else {
                    iv[2].setImageResource(R.mipmap.ic_no_data);
                }
                if (onItemStyleClickListener != null) {
                    onItemStyleClickListener.onItemStyleClick(flag);
                }
                break;
            case R.id.ll_jg:  ////不需要显示view
                filterPosition = 3;
                tv[2].setTextColor(ContextCompat.getColor(context, R.color.primary));
                if (onItemFilterClickListener != null) {
                    selectedSortEntity = filterData.getSorts().get(1);
                    onItemFilterClickListener.onItemFilterClick(selectedSortEntity);
                }
                break;
            case R.id.view_mask_bg:
                hide();
                break;

//            case R.id.ll_head_layout:
//
//                break;
        }
    }

    // 设置筛选数据
    public void setFilterData(Context context, FilterData filterData) {
        this.context = context;
        this.filterData = filterData;
    }

    // 动画显示
    public void show(int position) {
        if (isShowing && lastFilterPosition == position) {
            hide();
            return;
        } else if (!isShowing) {
            viewMaskBg.setVisibility(VISIBLE);
            ll[3].setVisibility(VISIBLE);
            ll[3].getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ll[3].getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    panelHeight = ll[3].getHeight();
                    ObjectAnimator.ofFloat(ll[3], "translationY", -panelHeight, 0).setDuration(200).start();
                }
            });
        }
        isShowing = true;
        resetViewStatus();
        rotateArrowUp(position);
        rotateArrowDown(lastFilterPosition);
        lastFilterPosition = position;

        switch (position) {
            case POSITION_CATEGORY:
                tv[0].setTextColor(ContextCompat.getColor(context, R.color.primary));
                iv[0].setImageResource(R.mipmap.home_down_arrow_red);
                setCategoryAdapter();
                break;
            case POSITION_SORT:
                tv[1].setTextColor(ContextCompat.getColor(context, R.color.primary));
                iv[1].setImageResource(R.mipmap.home_down_arrow_red);
                setSortAdapter();
                break;
//            case POSITION_STYLE:   //不需要显示view
//                Log.i("toast", "iv");
//                break;
//            case POSITION_FILTER:
//
//                Log.i("toast", "哈哈哈");
//                break;
        }
    }

    private void setSortAdapter() {
        mRv[0].setVisibility(GONE);
        mRv[1].setVisibility(VISIBLE);
        sortAdapter = new FilterOneAdapter(context, filterData.getSorts());
        RecyclerViewUtil.initNoDecoration(context, mRv[1], sortAdapter);
        sortAdapter.setOnItemClickListener(new FilterOneAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                selectedSortEntity = filterData.getSorts().get(position);
                sortAdapter.setSelectedEntity(selectedSortEntity);
                if (onItemSortClickListener != null) {
                    onItemSortClickListener.onItemSortClick(selectedSortEntity);
                }
                hide();
            }
        });
    }

    private void setCategoryAdapter() {
        mRv[0].setVisibility(VISIBLE);
        mRv[1].setVisibility(VISIBLE);

        // 左边列表视图
        leftAdapter = new FilterLeftAdapter(context, filterData.getCategory());
        RecyclerViewUtil.initNoDecoration(context, mRv[0], leftAdapter);
        if (leftSelectedCategoryEntity == null) {
            leftSelectedCategoryEntity = filterData.getCategory().get(0);
        }
        leftAdapter.setSelectedEntity(leftSelectedCategoryEntity);

        leftAdapter.setOnItemClickListener(new FilterLeftAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int i) {
                leftSelectedCategoryEntity = filterData.getCategory().get(i);
                leftAdapter.setSelectedEntity(leftSelectedCategoryEntity);
                // 右边列表视图
                rightAdapter = new FilterRightAdapter(context, leftSelectedCategoryEntity.getList());
                RecyclerViewUtil.initNoDecoration(context, mRv[1], rightAdapter);
                rightAdapter.setSelectedEntity(rightSelectedCategoryEntity);
                rightAdapter.setOnItemClickListener(new FilterRightAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int i) {
                        rightSelectedCategoryEntity = leftSelectedCategoryEntity.getList().get(i);
                        rightAdapter.setSelectedEntity(rightSelectedCategoryEntity);
                        if (onItemCategoryClickListener != null) {
                            onItemCategoryClickListener.onItemCategoryClick(leftSelectedCategoryEntity, rightSelectedCategoryEntity);
                        }
                        hide();
                    }
                });
            }
        });


        // 右边列表视图
        rightAdapter = new FilterRightAdapter(context, leftSelectedCategoryEntity.getList());
        RecyclerViewUtil.initNoDecoration(context, mRv[1], rightAdapter);
        rightAdapter.setSelectedEntity(rightSelectedCategoryEntity);

        rightAdapter.setOnItemClickListener(new FilterRightAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int i) {
                rightSelectedCategoryEntity = leftSelectedCategoryEntity.getList().get(i);
                rightAdapter.setSelectedEntity(rightSelectedCategoryEntity);
                if (onItemCategoryClickListener != null) {
                    onItemCategoryClickListener.onItemCategoryClick(leftSelectedCategoryEntity, rightSelectedCategoryEntity);
                }
                hide();
            }
        });
    }


    // 旋转箭头向上
    private void rotateArrowUp(int position) {
        switch (position) {
            case POSITION_CATEGORY:
                rotateArrowUpAnimation(iv[0]);
                break;
            case POSITION_SORT:
                rotateArrowUpAnimation(iv[1]);
                break;
        }
    }

    // 旋转箭头向下
    private void rotateArrowDown(int position) {
        switch (position) {
            case POSITION_CATEGORY:
                rotateArrowDownAnimation(iv[0]);
                break;
            case POSITION_SORT:
                rotateArrowDownAnimation(iv[1]);
                break;
        }
    }

    // 隐藏动画
    public void hide() {
        isShowing = false;
        resetViewStatus();
        rotateArrowDown(filterPosition);
        rotateArrowDown(lastFilterPosition);
        filterPosition = -1;
        lastFilterPosition = -1;
        viewMaskBg.setVisibility(View.GONE);
        ll[3].setVisibility(GONE);
        ObjectAnimator.ofFloat(ll[3], "translationY", 0, -panelHeight).setDuration(200).start();
    }

    // 复位筛选的显示状态
    public void resetViewStatus() {
        tv[0].setTextColor(ContextCompat.getColor(context, R.color.black));
        iv[0].setImageResource(R.mipmap.home_down_arrow);

        tv[1].setTextColor(ContextCompat.getColor(context, R.color.black));
        iv[1].setImageResource(R.mipmap.home_down_arrow);

        tv[2].setTextColor(ContextCompat.getColor(context, R.color.black));
    }

    // 复位所有的状态
    public void resetAllStatus() {
        resetViewStatus();
        hide();
    }


    // 旋转箭头向上
    public static void rotateArrowUpAnimation(final ImageView iv) {
        if (iv == null) return;
        RotateAnimation animation = new RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(200);
        animation.setFillAfter(true);
        iv.startAnimation(animation);
    }

    // 旋转箭头向下
    public static void rotateArrowDownAnimation(final ImageView iv) {
        if (iv == null) return;
        RotateAnimation animation = new RotateAnimation(180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(200);
        animation.setFillAfter(true);
        iv.startAnimation(animation);
    }


    // Filter筛选视图点击
    private OnFilterClickListener onFilterClickListener;

    public void setOnFilterClickListener(OnFilterClickListener onFilterClickListener) {
        this.onFilterClickListener = onFilterClickListener;
    }

    public interface OnFilterClickListener {
        void onFilterClick(int position);
    }


    // 分类Item点击
    private OnItemCategoryClickListener onItemCategoryClickListener;

    public void setOnItemCategoryClickListener(OnItemCategoryClickListener onItemCategoryClickListener) {
        this.onItemCategoryClickListener = onItemCategoryClickListener;
    }

    public interface OnItemCategoryClickListener {
        void onItemCategoryClick(FilterTwoEntity leftEntity, FilterEntity rightEntity);
    }

    // 排序Item点击
    private OnItemSortClickListener onItemSortClickListener;

    public void setOnItemSortClickListener(OnItemSortClickListener onItemSortClickListener) {
        this.onItemSortClickListener = onItemSortClickListener;
    }

    public interface OnItemSortClickListener {
        void onItemSortClick(FilterEntity entity);
    }

    // 价格Item点击
    private OnItemFilterClickListener onItemFilterClickListener;

    public void setOnItemFilterClickListener(OnItemFilterClickListener onItemFilterClickListener) {
        this.onItemFilterClickListener = onItemFilterClickListener;
    }

    public interface OnItemFilterClickListener {
        void onItemFilterClick(FilterEntity entity);
    }


    //换RecyclerView布局方式
    private OnItemStyleClickListener onItemStyleClickListener;

    public void setOnItemStyleClickListener(OnItemStyleClickListener onItemStyleClickListener) {
        this.onItemStyleClickListener = onItemStyleClickListener;
    }

    public interface OnItemStyleClickListener {
        void onItemStyleClick(boolean b);
    }
}
