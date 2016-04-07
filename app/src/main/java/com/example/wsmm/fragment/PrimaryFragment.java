package com.example.wsmm.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.wsmm.R;
import com.example.wsmm.adapter.ExpenseAdapter;
import com.example.wsmm.db.DBClient;
import com.example.wsmm.model.Category;
import com.example.wsmm.model.Expense;
import com.example.wsmm.util.SPManager;
import com.github.fabtransitionactivity.SheetLayout;


import java.util.Calendar;
import java.util.List;

import io.realm.RealmResults;


public class PrimaryFragment extends BaseFragment implements SheetLayout.OnFabAnimationEndListener,View.OnClickListener{

    public static final String PRIMARY_FRAGMENT_TAG = "PrimaryFragment";

    OnUpdateToolBar updateToolBar;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private ExpenseAdapter expenseAdapter;
    private List<Category> expenseList;
    private List<Category> distinctRecords;
    private Expense expense;
    private SheetLayout sheetLayout;
    private FloatingActionButton mFab;
    DBClient db;
    private int day , month , year;


    RealmResults<Category> realmResults;


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


       Bundle args = getArguments();

  /*     if (SPManager.getDay(getActivity()) != -1 && SPManager.getMonth(getActivity()) != -1 && SPManager.getYear(getActivity()) != -1){

           day = SPManager.getDay(getActivity());
           month = SPManager.getMonth(getActivity());
           year = SPManager.getYear(getActivity());

        }else {
           Calendar calendar = Calendar.getInstance();
           calendar.setTimeInMillis(System.currentTimeMillis());
           day = calendar.get(Calendar.DAY_OF_MONTH);
           month = calendar.get(Calendar.MONTH);
           year = calendar.get(Calendar.YEAR);

       }*/

        if (args != null){
            db = new DBClient();
            distinctRecords = db.getRecords();


            int position = getArguments().getInt("position");
            day = getDay(distinctRecords.get(position).getDate());
            month = getMonth(distinctRecords.get(position).getDate());
            year = getYear(distinctRecords.get(position).getDate());


        }else {

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH);
            year = calendar.get(Calendar.YEAR);

        }




        updateToolBar.onUpdateDate(day , month , year);

        TextView price = (TextView) parent.findViewById(R.id.price);
        mRecyclerView = (RecyclerView) parent.findViewById(R.id.expense_list);
        mProgressBar = (ProgressBar) parent.findViewById(R.id.progressBar);
        sheetLayout = (SheetLayout)parent.findViewById(R.id.bottom_sheet);
        mFab = (FloatingActionButton)parent.findViewById(R.id.fab);
        sheetLayout.setFab(mFab);
        sheetLayout.setFabAnimationEndListener(this);
        mProgressBar.setVisibility(View.VISIBLE);
        mFab.setOnClickListener(this);
        db = new DBClient();
        mRecyclerView = (RecyclerView) parent.findViewById(R.id.expense_list);
        mProgressBar = (ProgressBar) parent.findViewById(R.id.progressBar);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mProgressBar.setVisibility(View.GONE);

        expenseList= db.getParticularRecord(day, month, year);


        int totalAmount = 0;
        for (int i = 0; i <expenseList.size();i++){

            totalAmount +=Integer.parseInt(expenseList.get(i).getPrice());

        }
        price.setText("$ " + totalAmount);
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
        public void onUpdateDate(int day , int month , int year);
    }

    public int getDay(long date){

        Calendar calendar =Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return calendar.get(Calendar.DAY_OF_MONTH);

    }


    public int getMonth(long date){

        Calendar calendar =Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return calendar.get(Calendar.MONTH);

    }


    public int getYear(long date){

        Calendar calendar =Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return calendar.get(Calendar.YEAR);

    }


}
