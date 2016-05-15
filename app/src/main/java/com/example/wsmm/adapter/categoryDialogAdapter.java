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
import com.example.wsmm.util.SPManager;

import java.util.HashMap;
import java.util.List;

/**
 * Created by abubaker on 15/05/2016.
 */
public class CategoryDialogAdapter extends RecyclerView.Adapter<CategoryDialogAdapter.ViewHolder>{
    private List<CategoryItem> mDataSet;
    private Context mContext;
    private SparseBooleanArray selectedItems;
    private HashMap<Integer, Integer> selectedMap;
    int previous;



    public CategoryDialogAdapter(Context context , List<CategoryItem> list){
        this.mDataSet = list;
        this.mContext = context;
        selectedItems = new SparseBooleanArray();
        selectedMap = new HashMap<>();


    }

    @Override
    public CategoryDialogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_item_layout, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final CategoryDialogAdapter.ViewHolder holder, final int position) {

        final CategoryItem item = mDataSet.get(position);
        holder.title.setText(item.getCategoryTitle());

        if (selectedMap.containsKey(item.getCategoryId())) {
            holder.icon.setImageResource(R.drawable.ticked_checkbox_icon);
        } else {
            holder.icon.setImageResource(R.drawable.checkbox_hollow_icon);
        }

        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedMap.containsKey(item.getCategoryId())) {
                    selectedMap.remove(item.getCategoryId());
                    notifyDataSetChanged();
                    holder.icon.setImageResource(R.drawable.checkbox_hollow_icon);
                } else {
                    selectedMap.put(item.getCategoryId(),item.getCategoryId());
                    holder.icon.setImageResource(R.drawable.ticked_checkbox_icon);
                }

/*
                if (selectedItems.get(position, false)) {
                    selectedItems.delete(position);
                    SPManager.setCurrency(mContext,-1);
                    holder.icon.setImageResource(R.drawable.checkbox_hollow_icon);
                } else {

                    selectedItems.put(position, true);
                    holder.icon.setImageResource(R.drawable.ticked_checkbox_icon);

                }*/



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
            title = (TextView)viewHolder.findViewById(R.id.currency_title);
            icon = (ImageView)viewHolder.findViewById(R.id.selection_icon);
        }
    }


    public HashMap<Integer, Integer> getSelectedMap() {
        return selectedMap;
    }
}