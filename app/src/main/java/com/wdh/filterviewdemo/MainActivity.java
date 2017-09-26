package com.wdh.filterviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.wdh.filterviewdemo.adapter.MainAdapter;
import com.wdh.filterviewdemo.model.FilterData;
import com.wdh.filterviewdemo.model.FilterEntity;
import com.wdh.filterviewdemo.model.FilterTwoEntity;
import com.wdh.filterviewdemo.util.DensityUtil;
import com.wdh.filterviewdemo.util.ModelUtil;
import com.wdh.filterviewdemo.model.TravelingEntity;
import com.wdh.filterviewdemo.util.RecyclerViewUtil;
import com.wdh.filterviewdemo.view.FilterView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FilterView realFilterView;
    private RecyclerView mRv;
    private Toolbar mTb;
    private int filterPosition = -1; // 点击FilterView的位置：分类(0)、排序(1)、筛选(2)
    private int mScreenHeight; // 屏幕高度
    private MainAdapter adapter;
    private List<TravelingEntity> travelingList = new ArrayList<>(); // 数据
    private FilterData filterData; // 筛选数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        initData();
    }

    private void initView() {
        realFilterView = (FilterView) findViewById(R.id.filterview);
        mRv = (RecyclerView) findViewById(R.id.rv);
        mTb = (Toolbar) findViewById(R.id.toobar);
        mTb.setTitle("DEMO");
    }

    private void initData() {
        mScreenHeight = DensityUtil.getWindowHeight(this);
        travelingList = ModelUtil.getTravelingData();
        // 筛选数据
        filterData = new FilterData();
        filterData.setCategory(ModelUtil.getCategoryData());
        filterData.setSorts(ModelUtil.getSortData());
        // 设置FilterView数据
        realFilterView.setFilterData(this, filterData);
        adapter = new MainAdapter(this, travelingList);
        RecyclerViewUtil.init(this, mRv, adapter);
    }

    private void initListener() {
        // 筛选视图点击
        realFilterView.setOnFilterClickListener(new FilterView.OnFilterClickListener() {
            @Override
            public void onFilterClick(int position) {
                filterPosition = position;
                realFilterView.show(position);

            }
        });

        // 分类Item点击
        realFilterView.setOnItemCategoryClickListener(new FilterView.OnItemCategoryClickListener() {
            @Override
            public void onItemCategoryClick(FilterTwoEntity leftEntity, FilterEntity rightEntity) {
                fillAdapter(ModelUtil.getCategoryTravelingData(leftEntity, rightEntity));
            }
        });

        // 排序Item点击
        realFilterView.setOnItemSortClickListener(new FilterView.OnItemSortClickListener() {
            @Override
            public void onItemSortClick(FilterEntity entity) {
                fillAdapter(ModelUtil.getSortTravelingData(entity));
            }
        });

        // 价格Item点击
        realFilterView.setOnItemFilterClickListener(new FilterView.OnItemFilterClickListener() {
            @Override
            public void onItemFilterClick(FilterEntity entity) {
//                entity.getKey().equals("排序从低到高");
//                Log.i("toast",entity.getKey().toString());
                fillAdapter(ModelUtil.getSortTravelingData(entity));
            }
        });
        //RecyclerView
        realFilterView.setOnItemStyleClickListener(new FilterView.OnItemStyleClickListener() {
            @Override
            public void onItemStyleClick(boolean b) {
                if (b) {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                    mRv.setLayoutManager(layoutManager);
                    mRv.setHasFixedSize(true);
                    FilterView.flag = false;
                } else {
                    mRv.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                    mRv.setHasFixedSize(true);
                    FilterView.flag = true;
                }
            }
        });
    }


    // 填充数据
    private void fillAdapter(List<TravelingEntity> list) {
        if (list == null || list.size() == 0) {
            int height = mScreenHeight - DensityUtil.dip2px(this, 95); // 95 = 标题栏高度 ＋ FilterView的高度
            adapter.setData(ModelUtil.getNoDataEntity(height));
        } else {
            adapter.setData(list);
        }
    }


}
