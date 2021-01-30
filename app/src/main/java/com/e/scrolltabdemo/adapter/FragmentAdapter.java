package com.e.scrolltabdemo.adapter;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.e.scrolltabdemo.R;
import com.e.scrolltabdemo.Utils;
import com.e.scrolltabdemo.bean.ProductBean;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * Created by weioule
 * on 2021/1/2.
 */
public class FragmentAdapter extends BaseQuickAdapter<ProductBean, BaseViewHolder> {

    public FragmentAdapter(@Nullable List<ProductBean> data) {
        super(R.layout.frmagent_adapter_item, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ProductBean item) {
        helper.setImageResource(R.id.img, item.getImageResource());
        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.tv_price, item.getPrice());
        helper.setText(R.id.tv_content, item.getContent());
        helper.addOnClickListener(R.id.content_view);

        //30是spacing: 3x10
        int actual_width = (Utils.getScreenWidth(mContext) - Utils.dp2px(30)) / 2;
        Drawable drawable = mContext.getResources().getDrawable(item.getImageResource());
        int image_width = drawable.getIntrinsicWidth();
        int image_height = drawable.getIntrinsicHeight();

        int iamgHeight = image_height * actual_width / image_width;

        RoundedImageView img = helper.getView(R.id.img);
        ViewGroup.LayoutParams params = img.getLayoutParams();
        params.height = iamgHeight;
        img.setImageDrawable(drawable);

        TextView tag = helper.getView(R.id.tag);
        if (TextUtils.isEmpty(item.getTag())) {
            tag.setVisibility(View.GONE);
        } else {
            tag.setText(item.getTag());
            tag.setVisibility(View.VISIBLE);
        }

        TextView oldPrice = helper.getView(R.id.old_price);
        if (helper.getAdapterPosition() % 2 == 0) {
            oldPrice.setVisibility(View.VISIBLE);
            oldPrice.setText(item.getOldPrice());
            oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else oldPrice.setVisibility(View.GONE);
    }
}
