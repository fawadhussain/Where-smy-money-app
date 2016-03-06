package com.example.wsmm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wsmm.R;
import com.example.wsmm.model.Expense;

import java.util.ArrayList;

/**
 * Created by abubaker on 05/03/2016.
 */
public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {
    private ArrayList<Expense> mDataSet;
    private Context mContext;

    public ExpenseAdapter(Context context , ArrayList<Expense> expenseList){
        this.mDataSet = expenseList;
        this.mContext = context;
    }

    @Override
    public ExpenseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ExpenseAdapter.ViewHolder holder, int position) {

        Expense expenseObject = mDataSet.get(position);
        holder.title.setText(expenseObject.getTitle());
        holder.price.setText(expenseObject.getPrice());

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public ArrayList<Expense> getDataList() {
        return this.mDataSet;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView price;
        private View viewHolder;

        public ViewHolder(View itemView) {
            super(itemView);
            viewHolder = itemView;
            price = (TextView)viewHolder.findViewById(R.id.text_price);
            title = (TextView)viewHolder.findViewById(R.id.row_title);
        }
    }
}
