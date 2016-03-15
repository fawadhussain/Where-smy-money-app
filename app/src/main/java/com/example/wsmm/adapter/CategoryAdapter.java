package com.example.wsmm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wsmm.R;

import java.util.List;

/**
 * Created by abubaker on 14/03/2016.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    private List<String> mDataSet;
    private Context mContext;

    public CategoryAdapter(Context context , List<String> list){
        this.mDataSet = list;
        this.mContext = context;
    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_category_row_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String category = mDataSet.get(position);
        holder.title.setText(category);
        int id = mContext.getResources().getIdentifier(category.replaceAll("\\s+","").toLowerCase() ,"drawable", mContext.getPackageName());
        holder.categoryIcon.setImageResource(id);

    }


    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private ImageView categoryIcon;
        private View viewHolder;

        public ViewHolder(View itemView) {
            super(itemView);
            viewHolder = itemView;
            categoryIcon = (ImageView)viewHolder.findViewById(R.id.category_icon);
            title = (TextView)viewHolder.findViewById(R.id.category_text);
        }
    }
}
