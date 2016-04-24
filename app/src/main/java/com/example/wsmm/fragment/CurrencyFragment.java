package com.example.wsmm.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.example.wsmm.R;
import com.example.wsmm.adapter.CategoryAdapter;
import com.example.wsmm.adapter.CurrencyAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by abubaker on 24/04/2016.
 */
public class CurrencyFragment extends BaseFragment{

    private RecyclerView mRecyclerView;
    List<String> currencyList;
    private CurrencyAdapter currencyAdapter;


    public CurrencyFragment(){

    }

    @Override
    public int getLayoutId() {
        return R.layout.currency_fragment_layout;
    }

    @Override
    public void initViews(View parent, Bundle savedInstanceState) {
        super.initViews(parent, savedInstanceState);
        setHasOptionsMenu(true);

        mRecyclerView = (RecyclerView) parent.findViewById(R.id.currency_list);
        // realmConfig = new RealmConfiguration.Builder(getActivity()).build();
        // Open the Realm for the UI thread.
        //realm = Realm.getInstance(realmConfig);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        currencyList = Arrays.asList(getResources().getStringArray(R.array.currency));
        currencyAdapter = new CurrencyAdapter(getActivity(), currencyList);
        mRecyclerView.setAdapter(currencyAdapter);

    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        menu.clear();
        inflater.inflate(R.menu.add_transction, menu);

    }
}
