package com.e.scrolltabdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.e.scrolltabdemo.adapter.HomeViewPagerAdapter;
import com.e.scrolltabdemo.bean.HomeTab;
import com.e.scrolltabdemo.bean.ProductBean;
import com.e.scrolltabdemo.bean.SelectItem;
import com.e.scrolltabdemo.widget.ScrollTabView;
import com.google.android.material.appbar.AppBarLayout;
import com.leaf.library.StatusBarUtil;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by weioule
 * on 2021/1/2.
 */
public class MainActivity extends FragmentActivity {
    private SmartRefreshLayout refreshLayout;
    private AppBarLayout mAppBarLayout;
    private RelativeLayout mFloatSearchRl;
    private ScrollTabView mTabView;
    private ViewPager mViewPager;

    private int totalScrollRange, oldVerticalOffset = -1;
    private HomeViewPagerAdapter fragmentAdapter;
    private List<Fragment> fragmentList;
    private ArrayList<ProductBean> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        addListener();
    }

    private void initView() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.theme_color));
        refreshLayout = findViewById(R.id.refresh_layout);
        mFloatSearchRl = findViewById(R.id.rl_float_search);
        mAppBarLayout = findViewById(R.id.app_bar);
        mTabView = findViewById(R.id.tab_view);
        mViewPager = findViewById(R.id.view_pager);

        addProductBeans();
        initViewpager();
    }

    private void initViewpager() {
        if (fragmentList == null) {
            fragmentList = new ArrayList<>();
        } else {
            fragmentList.clear();
        }

        ArrayList<HomeTab> homeTabs = new ArrayList<>();
        homeTabs.add(new HomeTab("100", "猜你喜欢", "精选好货"));
        homeTabs.add(new HomeTab("101", "特价促销", "今日折扣"));
        homeTabs.add(new HomeTab("102", "惬意下午茶", "享午后时光"));
        homeTabs.add(new HomeTab("103", "火锅到家", "好吃省事"));
        homeTabs.add(new HomeTab("104", "时令新品", "应季鲜货"));

        for (int i = 0; i <= homeTabs.size() - 1; i++) {
            fragmentList.add(new HomeFragment(mViewPager, i, list, mTabView));
        }

        fragmentAdapter = new HomeViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(fragmentAdapter);
        mTabView.setOnTabSelectListener(onTabSelectListener);
        mViewPager.addOnPageChangeListener(onPageChangeListener);

        List<SelectItem> tabList = getTabList(homeTabs);
        mTabView.addTab(tabList);
    }

    private List<SelectItem> getTabList(List<HomeTab> tabList) {
        List<SelectItem> itemList = new ArrayList<>();
        for (HomeTab tab : tabList) {
            SelectItem item = new SelectItem(tab.getId(), tab.getTitle(), tab, false);
            itemList.add(item);
        }
        itemList.get(0).setSelected(true);
        return itemList;
    }

    private void addProductBeans() {
        for (int i = 0; i < 10; i++) {
            ProductBean bean = new ProductBean();
            switch (i) {
                case 0:
                    bean.setName("湘西铁骨黑猪冷鲜去皮五花肉 280g");
                    bean.setImageResource(R.drawable.img01);
                    bean.setContent("低温排酸 | 肥瘦均衡");
                    break;
                case 1:
                    bean.setTag("2件7.5折");
                    bean.setName("大瀛崇明生鲜琵琶腿 380g");
                    bean.setImageResource(R.drawable.img02);
                    bean.setContent("五谷喂养肉质嫩，可炖可烤啃着香");
                    break;
                case 2:
                    bean.setName("活杀花鲢鱼头一个 约750g");
                    bean.setImageResource(R.drawable.img03);
                    bean.setContent("下单后现杀，鱼眼凸起且明亮");
                    break;
                case 3:
                    bean.setTag("新品");
                    bean.setName("凡凡屋手擀式面 300g/盒");
                    bean.setImageResource(R.drawable.img04);
                    bean.setContent("劲道爽滑 | 久煮不烂");
                    break;
                case 4:
                    bean.setTag("买一送一");
                    bean.setName("西兰花 约500g");
                    bean.setImageResource(R.drawable.img05);
                    bean.setContent("吃法多样 | 新鲜脆嫩");
                    break;
                case 5:
                    bean.setTag("限时抢");
                    bean.setName("智利进口车厘子XL 450g");
                    bean.setImageResource(R.drawable.img06);
                    bean.setContent("通过核酸检测抽检，受运输影响表面可能有轻微坑洼痕迹，但不影响口感，介意慎拍");
                    break;
                case 6:
                    bean.setName("爆浆小蜜千禧 500g");
                    bean.setImageResource(R.drawable.img07);
                    bean.setContent("果皮薄果肉脆，一咬满嘴爆甜汁");
                    break;
                case 7:
                    bean.setTag("新鲜特价");
                    bean.setName("云南珍珠枣 500g");
                    bean.setImageResource(R.drawable.img08);
                    bean.setContent("肉厚核小，圆润饱满");
                    break;
                case 8:
                    bean.setTag("限时特惠");
                    bean.setImageResource(R.drawable.img09);
                    bean.setName("圣牧有机纯牛奶品醇 200ml*12盒/箱");
                    bean.setContent("中国欧盟双重认证，更适合老人孩子");
                    break;
                case 9:
                    bean.setTag("满减优惠");
                    bean.setName("洋鸡蛋 10枚");
                    bean.setImageResource(R.drawable.img10);
                    bean.setContent("营养均衡 | 胆固醇低");
                    break;
            }

            bean.setPrice("19.90");
            bean.setOldPrice("29.90");
            list.add(bean);
        }
    }

    private void addListener() {
        mAppBarLayout.addOnOffsetChangedListener(offsetChangedListener);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadData();
            }
        });
    }

    ScrollTabView.OnTabSelectListener onTabSelectListener = new ScrollTabView.OnTabSelectListener() {
        @Override
        public void onTabSelect(ScrollTabView.TabView tabView, int position) {
            mViewPager.setCurrentItem(position);
        }
    };

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            scrollToTop();
            mTabView.setCurrentItem(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private void loadData() {
        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.finishRefresh();
                refreshLayout.setEnabled(true);
            }
        }, 800);
    }


    private AppBarLayout.OnOffsetChangedListener offsetChangedListener = new AppBarLayout.OnOffsetChangedListener() {

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffse) {
            totalScrollRange = appBarLayout.getTotalScrollRange();

            if (oldVerticalOffset == verticalOffse) return;

            if (verticalOffse <= -Utils.dp2px(35)) {
                mFloatSearchRl.setVisibility(View.VISIBLE);
            } else {
                mFloatSearchRl.setVisibility(View.GONE);
            }

            int v = totalScrollRange + verticalOffse;
            if (v > 0) {
                //展开时
                mTabView.notifyDataSetChanged(3, 0, 0);
            }

            oldVerticalOffset = verticalOffse;
        }
    };

    private void scrollToTop() {
        if (mTabView.isOpen()) {
            for (Fragment fragment : fragmentList) {
                HomeFragment commodityFragment = (HomeFragment) fragment;
                commodityFragment.scrollToPosition(0);
            }
        }
    }
}
