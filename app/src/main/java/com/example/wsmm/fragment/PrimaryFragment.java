package com.example.wsmm.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wsmm.R;
import com.example.wsmm.TabFragment;
import com.example.wsmm.activity.MainActivity;
import com.example.wsmm.adapter.ExpenseAdapter;
import com.example.wsmm.db.DBClient;
import com.example.wsmm.model.Category;
import com.example.wsmm.util.GeneralUtils;
import com.example.wsmm.util.SPManager;
import com.github.fabtransitionactivity.SheetLayout;

import java.util.Calendar;
import java.util.List;

import io.realm.Realm;



public class PrimaryFragment extends BaseFragment implements SheetLayout.OnFabAnimationEndListener, View.OnClickListener {

    public static final String PRIMARY_FRAGMENT_TAG = "PrimaryFragment";
    public static final String DESCRIBABLE_KEY = "editTransaction";

    OnUpdateToolBar updateToolBar;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private ExpenseAdapter expenseAdapter;
    private List<Category> expenseList;
    private List<Category> distinctRecords;
    private SheetLayout sheetLayout;
    private FloatingActionButton mFab;
    private List<Category> previousRecordsList = null;
    DBClient db;
    private int day, month, year;
    TextView price;
    private int currentPosition=-1;



    public PrimaryFragment() {

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

        price = (TextView) parent.findViewById(R.id.price);
        mRecyclerView = (RecyclerView) parent.findViewById(R.id.expense_list);
        mProgressBar = (ProgressBar) parent.findViewById(R.id.progressBar);
        sheetLayout = (SheetLayout) parent.findViewById(R.id.bottom_sheet);
        mFab = (FloatingActionButton) parent.findViewById(R.id.fab);
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
        Bundle args = getArguments();

        if (getPreviousRecords()!= null) {
            setTotalAmount(getPreviousRecords());
            expenseAdapter = new ExpenseAdapter(getActivity(), getPreviousRecords());
            mRecyclerView.setAdapter(expenseAdapter);
            expenseAdapter.setLongClickListener(new ExpenseAdapter.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClicked(int position) {

                    dialogOption(getPreviousRecords(),position);
                    return true;
                }
            });

        } else {

            if (args != null){
                db = new DBClient();
                distinctRecords = db.getRecords();
                int position = getArguments().getInt("position");
                setCurrentPostionAndData(position);

            }

        }


    }

    public void setCurrentPostionAndData(int position) {

        day = GeneralUtils.getDay(distinctRecords.get(position).getDate());
        month = GeneralUtils.getMonth(distinctRecords.get(position).getDate());
        year = GeneralUtils.getYear(distinctRecords.get(position).getDate());
        setDataByDate(day, month, year);

    }

    public void setDataByDate(int day, int month, int year) {
        updateToolBar.onUpdateDate(day, month, year);
        expenseList = db.getParticularRecord(day, month, year);

        int totalAmount = 0;
        for (int i = 0; i < expenseList.size(); i++) {

            totalAmount += Integer.parseInt(expenseList.get(i).getPrice());

        }

        if (SPManager.getCurrency(getActivity())!= -1){
            price.setText(totalAmount+" "+GeneralUtils.getCurrencySymbol(getActivity(),SPManager.getCurrency(getActivity())));
        }else {
            price.setText("$ " + totalAmount);
        }


        expenseAdapter = new ExpenseAdapter(getActivity(), expenseList);
        mRecyclerView.setAdapter(expenseAdapter);

        expenseAdapter.setLongClickListener(new ExpenseAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(int position) {

                dialogOption(expenseList,position);
                return true;
            }
        });

    }

    @Override
    public void onFabAnimationEnd() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        updateToolBar.onUpdateDate(day, month, year);
        getHelper().replaceFragment(new AddTransactionFragment(), false, "AddTransaction");

        sheetLayout.contractFab();
        sheetLayout.hide();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
        public void onUpdateDate(int day, int month, int year);
    }


    private void dialogOption(final List<Category> list, final int position) {
        final CharSequence[] items = {"Edit Transaction", "Remove Transaction",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Please Select");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Edit Transaction")) {

                    editTransaction(list,position);

                } else if (items[item].equals("Remove Transaction")) {

                    removeTransaction(list,position);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void removeTransaction(final List<Category> list, int position) {
        Category category = list.get(position);

        if(MainActivity.checkPreviousRecords){

            expenseAdapter.removeItem(position);

        }else {

            expenseAdapter.remove(position);
        }

        db = new DBClient();


        db.deleteTransaction(category.getCategoryId(), new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(getActivity(), "Transaction Deleted", Toast.LENGTH_SHORT).show();
                int totalAmount = 0;
                if (expenseAdapter.getDataList() != null && expenseAdapter.getDataList().size() > 0) {

                    for (int i = 0; i < expenseAdapter.getDataList().size(); i++) {

                        totalAmount += Integer.parseInt(expenseAdapter.getDataList().get(i).getPrice());

                    }
                    if (SPManager.getCurrency(getActivity())!= -1){
                        price.setText(totalAmount+" "+GeneralUtils.getCurrencySymbol(getActivity(),SPManager.getCurrency(getActivity())));
                    }else {
                        price.setText("$ " + totalAmount);
                    }

                } else {
                    getHelper().replaceFragment(new TabFragment(),true,"TabFragment");

                }


            }
        });

    }


    private void editTransaction(List<Category> list,int position) {
        db = new DBClient();
        Category transaction = new Category();
        transaction = db.getResultFromId(list.get(position).getCategoryId());
        AddTransactionFragment add = new AddTransactionFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DESCRIBABLE_KEY, transaction);
        add.setArguments(bundle);
        getHelper().replaceFragment(add, false, "AddTransaction");

    }

    public void setPreviousRecords(List<Category> categoryList){
        this.previousRecordsList = categoryList;


    }


    public List<Category> getPreviousRecords(){
        return previousRecordsList;
    }


    public void setTotalAmount(List<Category> categoryList){

        int totalAmount = 0;
        for (int i = 0; i < categoryList.size(); i++) {

            totalAmount += Integer.parseInt(categoryList.get(i).getPrice());

        }
        if (SPManager.getCurrency(getActivity())!= -1){
            price.setText(totalAmount+" "+GeneralUtils.getCurrencySymbol(getActivity(),SPManager.getCurrency(getActivity())));
        }else {
            price.setText("$ " + totalAmount);
        }


    }




}
