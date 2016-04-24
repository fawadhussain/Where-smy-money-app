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
 * Created by abubaker on 24/04/2016.
 */
public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder>{


    private List<String> mDataSet;
    private Context mContext;


    public CurrencyAdapter(Context context , List<String> list){
        this.mDataSet = list;
        this.mContext = context;

    }

    @Override
    public CurrencyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_item_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CurrencyAdapter.ViewHolder holder, int position) {

        final String currency = mDataSet.get(position);
        holder.title.setText(currency);
        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
