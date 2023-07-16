package com.e.scrolltabdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.e.scrolltabdemo.R;
import com.e.scrolltabdemo.Utils;
import com.e.scrolltabdemo.bean.HomeTab;
import com.e.scrolltabdemo.bean.SelectItem;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by weioule
 * on 2021/1/2.
 * <p>
 * 上次发布《仿京东首页双层吸顶效果demo》后，现在看到叮咚买菜安卓端滑首页动事件不穿透的问题也解决了，后来偶然发现叮咚买菜IOS版首页的分类tab有个滑动效果，个人感觉用户体验挺好，所以就计划着空闲时候来实现一下。
 * <p>
 * 这里的核心是ScrollTabView，它是一个自定义控件，它在实现了TabLayout功能基础上添加了滑动效果处理，在我的当前首页架构上，要结合外层的AppBarLayout和内层RecyclerView的滚动调用它的notifyDataSetChanged函数实现。
 * <p>
 * tab上面我直接放了一张叮咚买菜首页的截图，只是头部做了一下吸顶处理，还有底部商品列表的点击事件也都没去实现，因为这些内容都是辅助的，所以就没详细写。
 * <p>
 * 主要思路是，当内层RecyclerView在滑动时，记录距离顶部的距离，在临界点上下滑动相应的内范围（根据实际高度做调整）进行透明度与高度的改动，实现tab下方的副标题与横线的切换效果。当外层的AppBarLayout下拉时将TabLayout
 */
public class ScrollTabView extends HorizontalScrollView {

    private List<SelectItem> items;
    private LinearLayout mTabLayout;
    private OnTabSelectListener onTabSelectListener;
    private List<TabView> tabViewList = new ArrayList<>();

    public ScrollTabView(Context context) {
        this(context, null);
    }

    public ScrollTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHorizontalScrollBarEnabled(false);

        mTabLayout = new LinearLayout(context);
        setBackgroundColor(getContext().getResources().getColor(R.color.transparent));
        addView(mTabLayout, new ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
    }

    public void addTab(List<SelectItem> items) {
        this.items = items;

        mTabLayout.removeAllViews();
        tabViewList.clear();

        final int count = items.size();
        int position = 0;

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Utils.getScreenWidth(getContext()) / 4, MATCH_PARENT);
        lp.setMargins(0, 0, 0, 0);

        for (int i = 0; i < count; i++) {
            TabView tabView = createTabView(i, items.get(i));
            mTabLayout.addView(tabView, lp);
            tabViewList.add(tabView);

            if (items.get(i).isSelected()) {
                position = i;
            }
        }

        setCurrentItem(position);
        requestLayout();
    }

    private TabView createTabView(int index, SelectItem item) {
        final TabView tabView = new TabView(getContext());
        tabView.mIndex = index;
        tabView.item = item;
        tabView.setFocusable(true);
        tabView.setOnClickListener(mTabClickListener);
        tabView.setData(item);
        return tabView;
    }

    public void setCurrentItem(int position) {
        if (position < 0 || position > items.size()) {
            return;
        }

        TabView child;
        int tabCount = mTabLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            child = (TabView) mTabLayout.getChildAt(i);
            boolean isSelected = (i == position);
            child.setSelected(isSelected);
            if (isSelected) {
                animateToTab(position);
            }
        }
    }

    private Runnable mTabSelector;

    private void animateToTab(final int position) {
        final View tabView = mTabLayout.getChildAt(position);
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
        mTabSelector = new Runnable() {
            public void run() {
                final int scrollPos = tabView.getLeft() - (getWidth() - tabView.getWidth()) / 2;
                smoothScrollTo(scrollPos, 0);
                mTabSelector = null;
            }
        };
        post(mTabSelector);
    }

    private final OnClickListener mTabClickListener = new OnClickListener() {
        public void onClick(View view) {
            TabView tabView = (TabView) view;
            setCurrentItem(tabView.getIndex());
            if (onTabSelectListener != null) {
                onTabSelectListener.onTabSelect(tabView, tabView.getIndex());
            }
        }
    };

    public void setOnTabSelectListener(OnTabSelectListener onTabSelectListener) {
        this.onTabSelectListener = onTabSelectListener;
    }

    public interface OnTabSelectListener {
        void onTabSelect(TabView tabView, int position);
    }


    public class TabView extends LinearLayout {
        private int mIndex;
        private SelectItem item;

        private View tab_line;
        private TextView tv_title;
        private TextView tv_subTitle;
        private ViewGroup tab_line_rl;

        public TabView(Context context) {
            super(context);
            init();
        }

        public TabView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public TabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        private void init() {
            inflate(getContext(), R.layout.home_tab_view, this);
            initView(getRootView());
        }

        private void initView(View view) {
            tv_title = view.findViewById(R.id.tab_title);
            tv_subTitle = view.findViewById(R.id.sub_title);
            tab_line = view.findViewById(R.id.tab_line);
            tab_line_rl = view.findViewById(R.id.tab_line_rl);
        }

        public void setData(SelectItem item) {
            if (item != null && item.getObject() != null) {
                HomeTab tab = (HomeTab) item.getObject();
                tv_title.setText(tab.getTitle());
                tv_subTitle.setText(tab.getSubTitle());
            }
        }

        public void setSelected(boolean isSelected) {
            item.setSelected(isSelected);

            if (isSelected) {
                tv_title.setTextColor(getContext().getResources().getColor(R.color.theme_color));
                tv_subTitle.setTextColor(getContext().getResources().getColor(R.color.white_color));
                tv_subTitle.setBackgroundResource(R.drawable.green_radius_50_bg);

                tab_line.setAlpha(item.getAlpha());
                ViewGroup.LayoutParams layoutParams = tab_line_rl.getLayoutParams();
                layoutParams.height = (int) Math.max(0, item.getVerticalOffse());
                tab_line_rl.setLayoutParams(layoutParams);

            } else {
                tv_title.setTextColor(getContext().getResources().getColor(R.color.txt_blank_color));
                tv_subTitle.setTextColor(getContext().getResources().getColor(R.color.gray_light));
                tv_subTitle.setBackgroundColor(getContext().getResources().getColor(R.color.transparent));

                tab_line.setAlpha(0f);
                ViewGroup.LayoutParams layoutParams = tab_line_rl.getLayoutParams();
                layoutParams.height = 0;
                tab_line_rl.setLayoutParams(layoutParams);
            }

            tv_subTitle.setAlpha(1 - item.getAlpha());
        }

        public int getIndex() {
            return mIndex;
        }

        public SelectItem getItem() {
            return item;
        }
    }

    /**
     * 刷新tabView的滑动效果
     * @param scrollState
     * @param alpha 线条与副标题的透明度调节
     * @param scrollY 纵向滚动值，控制副标题下方的线条滑动
     */
    public void notifyDataSetChanged(int scrollState, float alpha, int scrollY) {
        TabView child;
        final int tabCount = mTabLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            child = (TabView) mTabLayout.getChildAt(i);

            SelectItem item = items.get(i);

            //避免
            if (1 == scrollState && item.getAlpha() == 0) return;
            if (2 == scrollState && item.getAlpha() == 1) return;
            if (3 == scrollState && item.getAlpha() == 0) return;

            item.setAlpha(alpha);
            item.setVerticalOffse(scrollY);

            if (child.getItem().isSelected() || item.isSelected()) {

                child.findViewById(R.id.tab_line).setAlpha(alpha);
                ViewGroup line_rl = child.findViewById(R.id.tab_line_rl);
                ViewGroup.LayoutParams layoutParams = line_rl.getLayoutParams();
                layoutParams.height = Math.max(0, scrollY);
                line_rl.setLayoutParams(layoutParams);

            } else {
                child.findViewById(R.id.tab_line).setAlpha(0f);
            }

            child.findViewById(R.id.sub_title).setAlpha(1 - alpha);
            child.findViewById(R.id.sub_title).setTranslationY(scrollY / 2);
            child.findViewById(R.id.tab_title).setTranslationY(scrollY / 2);
        }
    }

    //判断tab是否是展开状态
    public boolean isOpen() {
        if (null == items || items.size() <= 0) return false;
        return items.get(0).getAlpha() == 0;
    }
}
