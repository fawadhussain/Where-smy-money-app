package com.example.wsmm.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.wsmm.R;
import com.example.wsmm.adapter.ExpenseAdapter;
import com.example.wsmm.fragment.BaseFragment;
import com.example.wsmm.model.Expense;
import com.github.fabtransitionactivity.SheetLayout;

import java.util.ArrayList;


public class PrimaryFragment extends BaseFragment implements SheetLayout.OnFabAnimationEndListener,View.OnClickListener{
    OnUpdateToolBar updateToolBar;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private ExpenseAdapter expenseAdapter;
    private ArrayList<Expense> expenseList;
    private Expense expense;
    private SheetLayout sheetLayout;
    private FloatingActionButton mFab;


    public PrimaryFragment(){

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        updateToolBar = (OnUpdateToolBar) activity;

    }

    @Override
    public int getLayoutId() {
        return R.layout.primary_layout;
    }

    @Override
    public void initViews(View parent, Bundle savedInstanceState) {
        super.initViews(parent, savedInstanceState);
        mRecyclerView = (RecyclerView) parent.findViewById(R.id.expense_list);
        mProgressBar = (ProgressBar) parent.findViewById(R.id.progressBar);
        sheetLayout = (SheetLayout)parent.findViewById(R.id.bottom_sheet);
        mFab = (FloatingActionButton)parent.findViewById(R.id.fab);
        sheetLayout.setFab(mFab);
        sheetLayout.setFabAnimationEndListener(this);
        mProgressBar.setVisibility(View.VISIBLE);
        mFab.setOnClickListener(this);
        expenseList = new ArrayList<Expense>();


        for (int i = 0 ; i < 100 ; i++){
            expense = new Expense();
            expense.setTitle("COFFEE");
            expense.setPrice("$10.00");
            expenseList.add(expense);
        }

        //updateToolBar.onUpdatePrice("100");
        mRecyclerView = (RecyclerView) parent.findViewById(R.id.expense_list);
        mProgressBar = (ProgressBar) parent.findViewById(R.id.progressBar);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mProgressBar.setVisibility(View.GONE);
        expenseAdapter = new ExpenseAdapter(getActivity(), expenseList);
        mRecyclerView.setAdapter(expenseAdapter);

    }

    @Override
    public void onFabAnimationEnd() {
        getHelper().replaceFragment(new AddTransactionFragment(),false,"AddTransaction");
        sheetLayout.contractFab();
        sheetLayout.hide();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:

                sheetLayout.expandFab();
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public interface OnUpdateToolBar {
        public void onUpdatePrice(String price);
    }


}
