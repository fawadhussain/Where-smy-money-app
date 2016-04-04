package com.example.wsmm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wsmm.R;
import com.example.wsmm.model.Category;
import com.example.wsmm.model.Expense;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abubaker on 05/03/2016.
 */
public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {
    private List<Category> mDataSet;
    private Context mContext;

    public ExpenseAdapter(Context context , List<Category> expenseList){
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

        Category expenseObject = mDataSet.get(position);
        holder.title.setText(expenseObject.getCategoryTitle());
        holder.price.setText(expenseObject.getPrice()+" $");

        if (expenseObject.getCategoryName()!=null){
            int id = mContext.getResources().getIdentifier(expenseObject.getCategoryName().replaceAll("\\s+", "").toLowerCase(), "drawable", mContext.getPackageName());
            holder.icon.setImageResource(id);

        }


    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public List<Category> getDataList() {
        return this.mDataSet;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView price;
        private ImageView icon;
        private View viewHolder;

        public ViewHolder(View itemView) {
            super(itemView);
            viewHolder = itemView;
            price = (TextView)viewHolder.findViewById(R.id.text_price);
            title = (TextView)viewHolder.findViewById(R.id.row_title);
            icon = (ImageView)viewHolder.findViewById(R.id.category_icon);
        }
    }
}
