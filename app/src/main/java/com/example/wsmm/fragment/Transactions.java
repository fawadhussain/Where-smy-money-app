package com.example.wsmm.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.wsmm.R;
import com.example.wsmm.adapter.ExpenseAdapter;
import com.example.wsmm.db.DBClient;
import com.example.wsmm.model.Category;

import java.util.List;


/**
 * Created by abubaker on 2/26/16.
 */
public class Transactions extends BaseFragment{

    DBClient db;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private ExpenseAdapter expenseAdapter;
    private List<Category> expenseList;


    public Transactions(){

    }

    @Override
    public int getLayoutId() {
        return R.layout.transaction_fragment_layout;
    }

    @Override
    public void initViews(View parent, Bundle savedInstanceState) {
        super.initViews(parent, savedInstanceState);


        mRecyclerView = (RecyclerView) parent.findViewById(R.id.transaction_list);
        mProgressBar = (ProgressBar) parent.findViewById(R.id.transaction_progress);
        db = new DBClient();
        expenseList = db.getAllItems();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        expenseAdapter = new ExpenseAdapter(getActivity(), expenseList);
        mRecyclerView.setAdapter(expenseAdapter);
    }
}
