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
import com.example.wsmm.util.SPManager;

import java.util.List;

/**
 * Created by abubaker on 24/04/2016.
 */
public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder>{


    private List<String> mDataSet;
    private Context mContext;
    private SparseBooleanArray selectedItems;
    int previous;



    public CurrencyAdapter(Context context , List<String> list){
        this.mDataSet = list;
        this.mContext = context;
        selectedItems = new SparseBooleanArray();


    }

    @Override
    public CurrencyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_item_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CurrencyAdapter.ViewHolder holder, final int position) {

        final String currency = mDataSet.get(position);
        holder.title.setText(currency);

       if (SPManager.getCurrency(mContext)!= -1 && SPManager.getCurrency(mContext) == position) {
           holder.icon.setImageResource(R.drawable.ticked_checkbox_icon);
           selectedItems.put(position,true);
        } else {
           holder.icon.setImageResource(R.drawable.checkbox_hollow_icon);
        }

        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (selectedItems.get(position, false)) {
                    selectedItems.delete(position);
                    SPManager.setCurrency(mContext,-1);
                    holder.icon.setImageResource(R.drawable.checkbox_hollow_icon);
                } else {

                    selectedItems.put(position, true);
                    holder.icon.setImageResource(R.drawable.ticked_checkbox_icon);

                }

                if (selectedItems.size() >= 2) {
                    selectedItems.delete(previous);
                    notifyDataSetChanged();
                }

                if (selectedItems.size()>0){
                    SPManager.setCurrency(mContext,position);
                }else {
                    SPManager.setCurrency(mContext,-1);
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
        private ImageView icon;
        private View viewHolder;

        public ViewHolder(View itemView) {
            super(itemView);
            viewHolder = itemView;
            title = (TextView)viewHolder.findViewById(R.id.currency_title);
            icon = (ImageView)viewHolder.findViewById(R.id.selection_icon);
        }
    }
}
