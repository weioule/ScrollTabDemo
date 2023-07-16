package com.e.scrolltabdemo;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.e.scrolltabdemo.adapter.CategoryListAdapter;
import com.e.scrolltabdemo.adapter.HomeViewPagerAdapter;
import com.e.scrolltabdemo.adapter.MyBannerImageAdapter;
import com.e.scrolltabdemo.bean.HomeTab;
import com.e.scrolltabdemo.bean.ProductBean;
import com.e.scrolltabdemo.bean.SelectItem;
import com.e.scrolltabdemo.widget.ScrollTabView;
import com.e.scrolltabdemo.widget.decoration.SpacesItemDecoration;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnPageChangeListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by weioule
 * on 2021/1/2.
 */
public class MainActivity extends FragmentActivity {
    private SmartRefreshLayout refreshLayout;
    private ImageView ivTopBg;
    private LinearLayout topRlFloatSearch;
    private View floatPaddingTopView;
    private RelativeLayout rlSearch;
    private RelativeLayout rlCategory;
    private RelativeLayout rlTopGroup;
    private RelativeLayout rlAddressGroup;
    private RelativeLayout rlAddressSearchGroup;
    private TextView tvSearchBtn;
    private TextView tvDeliveryTime;
    private TextView tvFloatDeliveryTime;
    private Banner mBanner;
    private RecyclerView rvCategoryList;
    private RelativeLayout rlIndicatorBg;
    private View indicator;
    private Toolbar toolbar;
    private AppBarLayout mAppBarLayout;
    private ScrollTabView mTabView;
    private ViewPager mViewPager;
    private int totalScrollRange, oldVerticalOffset = -1;
    private HomeViewPagerAdapter fragmentAdapter;
    private List<Fragment> fragmentList;
    private ArrayList<ProductBean> list = new ArrayList<>();
    private float proportion;
    private int tvDeliveryTimeRawWidth;
    private Integer[] topBgs = {R.drawable.top_bg01, R.drawable.top_bg02, R.drawable.top_bg03};
    private Integer[] btnColors = {0xff347a34, 0xff1b4c1c, 0xff006855};
    private Integer[] banners = {R.drawable.banner_img01, R.drawable.banner_img02, R.drawable.banner_img03};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setStatusBar();
        resetStatusBarMode();
        initBannerView();
        addProductBeans();
        initCategoryList();
        initViewpager();
        addListener();
    }

    /**
     * 沉侵式状态栏相关设置
     */
    private void setStatusBar() {
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) floatPaddingTopView.getLayoutParams();
        linearParams.height = Utils.getStatusBarHeight(this);
        floatPaddingTopView.setLayoutParams(linearParams);
    }

    private void initView() {
        refreshLayout = findViewById(R.id.refresh_layout);
        ivTopBg = findViewById(R.id.iv_top_bg);
        mAppBarLayout = findViewById(R.id.app_bar);
        mTabView = findViewById(R.id.tab_view);
        mViewPager = findViewById(R.id.view_pager);
        toolbar = findViewById(R.id.toolbar);
        floatPaddingTopView = findViewById(R.id.float_padding_top_view);
        topRlFloatSearch = findViewById(R.id.ll_top_float_search_group);

        rlTopGroup = findViewById(R.id.rl_top_group);
        rlAddressGroup = findViewById(R.id.rl_address_group);
        rlAddressSearchGroup = findViewById(R.id.rl_address_search_group);

        tvDeliveryTime = findViewById(R.id.tv_delivery_time);
        tvFloatDeliveryTime = findViewById(R.id.tv_float_delivery_time);
        rlSearch = findViewById(R.id.rl_search);
        tvSearchBtn = findViewById(R.id.tv_search_btn);

        mBanner = findViewById(R.id.banner);
        rlCategory = findViewById(R.id.rl_category_list);
        rvCategoryList = findViewById(R.id.rv_category_list);
        rlIndicatorBg = findViewById(R.id.rl_indicator_bg);
        indicator = findViewById(R.id.indicator);

        tvDeliveryTime.setText("可约10:30配送");
        tvFloatDeliveryTime.setText("可约10:30配送");
        //设置文本后手动测量宽度
        tvDeliveryTime.measure(0, 0);
        tvDeliveryTimeRawWidth = tvDeliveryTime.getMeasuredWidth();
    }

    private void initBannerView() {
        int width = Utils.measureScreenWidth(this);
        //背景图规格：780 x 720
        proportion = width / 780f;
        int topImgHeight = Math.round(720 * proportion);
        ivTopBg.setLayoutParams(new RelativeLayout.LayoutParams(width, topImgHeight));

        //按比例计算出banner距离顶部的高度：banner图片是从上往下第276像素开始裁剪（背景图透到状态栏，故减去状态栏高度）
        ((RelativeLayout.LayoutParams) mBanner.getLayoutParams()).topMargin = Math.round(276 * proportion) - Utils.getStatusBarHeight(this);

        //banner图片的规格：740x240 （在屏幕上展示都需要乘以图片与屏幕的比例）
        mBanner.getLayoutParams().width = Math.round(740 * proportion);
        //四舍五入，尽量减少误差
        mBanner.getLayoutParams().height = Math.round(240 * proportion);

        mBanner.setBannerRound(Utils.dp2px(10));

        List<Integer> bannerList = Arrays.asList(banners);
        mBanner.setAdapter(new MyBannerImageAdapter<Integer>(bannerList) {
                    @Override
                    public void onBindView(BannerImageHolder holder, Integer drawable, int position, int size) {
                        holder.imageView.setImageResource(drawable);
                    }
                }).addBannerLifecycleObserver(this)//添加生命周期观察者
                .setIndicator(new CircleIndicator(this))//设置指示器
                .setOnBannerListener((data, position) -> {
                    Toast.makeText(this, "banner " + position, Toast.LENGTH_SHORT).show();
                });

        mBanner.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                changeTopBg(topBgs[position], btnColors[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        changeTopBg(topBgs[0], btnColors[0]);
    }

    private void changeTopBg(Integer topBg, int btnColor) {
        ivTopBg.setImageResource(topBg);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(btnColor);
        gradientDrawable.setCornerRadius(50);
        tvSearchBtn.setBackground(gradientDrawable);
    }

    private void initCategoryList() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);

        rvCategoryList.setLayoutManager(layoutManager);
        int spaces = Utils.dp2px(8);
        rvCategoryList.addItemDecoration(new SpacesItemDecoration(spaces, spaces, 0x00000000));
        CategoryListAdapter adapter = new CategoryListAdapter(this, list);
        rvCategoryList.setAdapter(adapter);

        rvCategoryList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int width = recyclerView.getWidth();
                //内容总长度
                int horizontalScrollRange = recyclerView.computeHorizontalScrollRange();
                if (horizontalScrollRange > width) {
                    width = horizontalScrollRange;
                }

                //滑块的偏移量
                int offset = recyclerView.computeHorizontalScrollOffset();
                //可视区域长度
                int extent = recyclerView.computeHorizontalScrollExtent();
                //滑出部分在剩余范围的比例
                float proportion = (float) (offset * 1.0 / (width - extent));
                //计算滚动条宽度
                float transMaxRange = rlIndicatorBg.getWidth() - indicator.getWidth();
                //设置滚动条移动
                indicator.setTranslationX(transMaxRange * proportion);
            }
        });

        rlCategory.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rlCategory.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int range = rvCategoryList.getWidth();
                int temp = rvCategoryList.computeHorizontalScrollRange();
                int indicatorHeight = 0;
                if (temp > range) {
                    rlIndicatorBg.setVisibility(View.VISIBLE);
                } else {
                    rlIndicatorBg.setVisibility(View.GONE);
                    //13dp为指示器3dp+指示器底部外边距10dp
                    indicatorHeight = Utils.dp2px(13);
                }

                //top的总高度: 276=banner往上空包区域 + 240=banner图片高度(均乘以图片比例得到实际高度) - 指示器及其底部外边距的高度(若指示器不显示时)
                rlTopGroup.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Math.round((276 + 240) * proportion) + rlCategory.getHeight() - indicatorHeight));
                rlTopGroup.setPadding(0, Utils.getStatusBarHeight(MainActivity.this), 0, 0);
            }
        });
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

    private void addListener() {
        topRlFloatSearch.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                topRlFloatSearch.removeOnLayoutChangeListener(this);
                int height = topRlFloatSearch.getHeight();
                toolbar.setLayoutParams(new CollapsingToolbarLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
            }
        });

        mAppBarLayout.addOnOffsetChangedListener(offsetChangedListener);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadData();
            }
        });
    }

    private void loadData() {
        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.finishRefresh();
                refreshLayout.setEnabled(true);

                tvDeliveryTime.setText("可约明日配送");
                tvFloatDeliveryTime.setText("可约明日配送");

                //更新文本后重新手动测量宽度
                tvDeliveryTime.measure(0, 0);
                tvDeliveryTimeRawWidth = tvDeliveryTime.getMeasuredWidth();
            }
        }, 800);
    }

    private AppBarLayout.OnOffsetChangedListener offsetChangedListener = new AppBarLayout.OnOffsetChangedListener() {

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffse) {
            totalScrollRange = appBarLayout.getTotalScrollRange();
            if (oldVerticalOffset == verticalOffse) return;

            int MaxVerticalOffset = Utils.dp2px(40);
            if (verticalOffse <= -MaxVerticalOffset) {
                topRlFloatSearch.setVisibility(View.VISIBLE);
            } else {
                topRlFloatSearch.setVisibility(View.GONE);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tvDeliveryTime.getLayoutParams();
                double PxWidth = Math.abs(tvDeliveryTimeRawWidth * verticalOffse / MaxVerticalOffset);
                layoutParams.width = (int) PxWidth;
                layoutParams.rightMargin = (int) (Utils.dp2px(10) * PxWidth / tvDeliveryTimeRawWidth);
                tvDeliveryTime.setLayoutParams(layoutParams);

                int alpha = (int) (225 * PxWidth / tvDeliveryTimeRawWidth);
                tvDeliveryTime.getBackground().setAlpha(alpha);
                tvDeliveryTime.setAlpha((float) (alpha / 225.0));
                rlAddressGroup.setAlpha(1 - (float) (alpha / 225.0));
                rlSearch.getBackground().setAlpha(alpha);

                if (verticalOffse <= -Utils.getStatusBarHeight(MainActivity.this)) {
                    int start = Math.abs(verticalOffse) - Utils.getStatusBarHeight(MainActivity.this);
                    int total = MaxVerticalOffset - Utils.getStatusBarHeight(MainActivity.this);

                    double bili = start * 1.0 / total;
                    int a = (int) (225 * bili);

                    rlAddressSearchGroup.setBackgroundColor(getResources().getColor(R.color.white_color));
                    rlAddressSearchGroup.getBackground().setAlpha(a);
//                    rlSearch.setBackgroundResource(R.drawable.white_bg_theme_color_border_radius_50);
//                    rlSearch.getBackground().setAlpha(a + 50);
                } else {
                    rlAddressSearchGroup.setBackgroundColor(0x00000000);
//                    rlSearch.setBackgroundResource(R.drawable.white_bg_radius_50);
                }
            }

            resetStatusBarMode();

            int v = totalScrollRange + verticalOffse;
            if (v > 0) {
                //展开时
                mTabView.notifyDataSetChanged(3, 0, 0);
            }

            oldVerticalOffset = verticalOffse;
        }
    };

    private ScrollTabView.OnTabSelectListener onTabSelectListener = new ScrollTabView.OnTabSelectListener() {
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

    private void addProductBeans() {
        for (int i = 0; i < 12; i++) {
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
                case 10:
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
                case 11:
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

    /**
     * 沉侵式状态栏相关，重新设置StatusBar Mode
     */
    private void resetStatusBarMode() {
        if (topRlFloatSearch != null && topRlFloatSearch.getVisibility() == View.VISIBLE) {
            StatusBarUtil.setColor(this, getResources().getColor(R.color.white_color), 0);
            StatusBarUtil.setLightMode(this);
        } else {
            StatusBarUtil.setColor(this, getResources().getColor(R.color.transparent), 0);
            StatusBarUtil.setDarkMode(this);
        }
    }


    private void scrollToTop() {
        if (mTabView.isOpen()) {
            for (Fragment fragment : fragmentList) {
                HomeFragment commodityFragment = (HomeFragment) fragment;
                commodityFragment.scrollToPosition(0);
            }
        }
    }
}
