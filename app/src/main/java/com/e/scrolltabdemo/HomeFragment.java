package com.e.scrolltabdemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.e.scrolltabdemo.adapter.FragmentAdapter;
import com.e.scrolltabdemo.bean.ProductBean;
import com.e.scrolltabdemo.widget.OffsetLinearLayoutManager;
import com.e.scrolltabdemo.widget.ScrollTabView;

import java.util.ArrayList;

/**
 * Created by weioule
 * on 2021/1/2.
 */
public class HomeFragment extends Fragment {

    private int index;
    //距离顶部距离
    private int scrollY = 0;
    private int oldScrollY = 0;
    private RecyclerView mRecyclerView;
    private ArrayList<ProductBean> list;
    private ScrollTabView mTabView;
    private ViewPager mViewPager;

    public int getScrollY() {
        return scrollY;
    }

    public void setScrollY(int scrollY) {
        this.scrollY = scrollY;
        if (0 == scrollY) oldScrollY = 0;
    }

    public HomeFragment(ViewPager mViewPager, int index, ArrayList<ProductBean> list, ScrollTabView mTabView) {
        this.list = list;
        this.index = index;
        this.mViewPager = mViewPager;
        this.mTabView = mTabView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        FragmentAdapter adapter = new FragmentAdapter(list);

        View topPaddingView = new View(getContext());
        topPaddingView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dp2px(8)));
        adapter.addHeaderView(topPaddingView);

        OffsetLinearLayoutManager layoutManager = new OffsetLinearLayoutManager();
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ProductBean productBean = (ProductBean) adapter.getItem(position);
                Toast.makeText(getContext(), productBean.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        //下面的设置是防止第一次打开，当最后一行的子条目不是平齐的，导致最后一个子条目显示不全
        //防止item 交换位置
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrollY += dy;

                if (mViewPager.getCurrentItem() == index) {
                    mTabView.notifyDataSetChanged(scrollY < oldScrollY ? 1 : 2, Math.min(scrollY / 30f, 1), Math.min(scrollY, 30));
                }

                oldScrollY = scrollY;
            }
        });
    }

    public void scrollToPosition(int position) {
        if (null == mRecyclerView) return;
        setScrollY(position);
        mRecyclerView.scrollToPosition(position);
    }
}
