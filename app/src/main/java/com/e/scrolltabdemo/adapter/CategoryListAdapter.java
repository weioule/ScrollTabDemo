package com.e.scrolltabdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.scrolltabdemo.R;
import com.e.scrolltabdemo.bean.ProductBean;

import java.util.List;

/**
 * Created by weioule
 * on 2023/7/8.
 */
public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.MyHolder> {

    private Context context;
    private List<ProductBean> list;

    public CategoryListAdapter(Context mContext, List<ProductBean> mList) {
        this.context = mContext;
        this.list = mList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_indicator, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.img.setImageResource(list.get(position).getImageResource());
        holder.name.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    //清除数据
    public void clearData() {
        if (list != null) {
            this.list.clear();
            notifyDataSetChanged();
        }
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView img;


        public MyHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            img = itemView.findViewById(R.id.img);
        }
    }

}
