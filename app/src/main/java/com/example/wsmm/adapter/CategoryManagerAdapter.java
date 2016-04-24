package com.example.wsmm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wsmm.R;
import com.example.wsmm.model.CategoryItem;

import java.util.List;

/**
 * Created by abubaker on 24/04/2016.
 */
public class CategoryManagerAdapter extends RecyclerView.Adapter<CategoryManagerAdapter.ViewHolder>{


    private List<CategoryItem> mDataSet;
    private Context mContext;


    private ClickListener itemClickListener;


    public interface ClickListener {

        void onItemClicked(int itemPosition);

    }


    public void setClickListener(ClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    public CategoryManagerAdapter(Context context , List<CategoryItem> list){
        this.mDataSet = list;
        this.mContext = context;

    }


    @Override
    public CategoryManagerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CategoryManagerAdapter.ViewHolder holder, final int position) {

        final CategoryItem category = mDataSet.get(position);
        holder.title.setText(category.getCategoryTitle());
        int id = mContext.getResources().getIdentifier(category.getCategoryItemName().replaceAll("\\s+","").toLowerCase() ,"drawable", mContext.getPackageName());
        holder.icon.setImageResource(id);
        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itemClickListener.onItemClicked(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private ImageView icon;
        private View viewHolder;

        public ViewHolder(View itemView) {
            super(itemView);
            viewHolder = itemView;
            title = (TextView)viewHolder.findViewById(R.id.category_title);
            icon = (ImageView)viewHolder.findViewById(R.id.category_item_icon);
        }
    }
}


