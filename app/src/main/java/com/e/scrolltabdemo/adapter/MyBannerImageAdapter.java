package com.e.scrolltabdemo.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.holder.BannerImageHolder;

import java.util.List;

/**
 * Created by weioule
 * on 2023/7/8.
 */
public abstract class MyBannerImageAdapter<T> extends BannerAdapter<T, BannerImageHolder> {

    public MyBannerImageAdapter(List<T> mData) {
        super(mData);
    }

    @Override
    public BannerImageHolder onCreateHolder(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return new BannerImageHolder(imageView);
    }

}
