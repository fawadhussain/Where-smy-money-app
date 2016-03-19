package com.example.wsmm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wsmm.R;

import java.util.List;

/**
 * Created by abubaker on 14/03/2016.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    private List<String> mDataSet;
    private Context mContext;
    private SparseBooleanArray selectedItems;
    int previous;


    private ClickListener itemClickListener;


    public interface ClickListener {

        void onItemClicked(int itemPosition);

    }

    public void setClickListener(ClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CategoryAdapter(Context context , List<String> list){
        this.mDataSet = list;
        this.mContext = context;
        selectedItems = new SparseBooleanArray();
    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_category_row_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        final String category = mDataSet.get(position);
        holder.title.setText(category);
        int id = mContext.getResources().getIdentifier(category.replaceAll("\\s+","").toLowerCase() ,"drawable", mContext.getPackageName());
        holder.categoryIcon.setImageResource(id);
        holder.myRelativeBackground.setSelected(selectedItems.get(position, false));
        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedItems.get(position, false)) {
                    selectedItems.delete(position);
                    holder.myRelativeBackground.setSelected(false);
                }
                else {

                    selectedItems.put(position, true);
                    holder.myRelativeBackground.setSelected(true);

                }

                if (selectedItems.size() >= 2){
                    selectedItems.delete(previous);
                    selectedItems.put(previous, false);
                    notifyDataSetChanged();
                }


                if (itemClickListener != null){
                    itemClickListener.onItemClicked(position);
                }

                previous = position;
            }
        });

    }


    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private ImageView categoryIcon;
        public RelativeLayout myRelativeBackground;
        private View viewHolder;

        public ViewHolder(View itemView) {
            super(itemView);
            viewHolder = itemView;
            categoryIcon = (ImageView)viewHolder.findViewById(R.id.category_icon);
            title = (TextView)viewHolder.findViewById(R.id.category_text);
            myRelativeBackground = (RelativeLayout)viewHolder.findViewById(R.id.relative_background);
        }
    }
}