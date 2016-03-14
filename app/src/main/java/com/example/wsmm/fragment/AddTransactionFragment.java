package com.example.wsmm.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;


import com.example.wsmm.R;
import com.example.wsmm.adapter.CategoryAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by abubaker on 13/03/2016.
 */
public class AddTransactionFragment extends BaseFragment {

    private RecyclerView mRecyclerView;

    List<String> categoryList;
    private CategoryAdapter categoryAdapter;

    public AddTransactionFragment() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.add_transaction_fragment;
    }

    @Override
    public void initViews(View parent, Bundle savedInstanceState) {
        super.initViews(parent, savedInstanceState);

        mRecyclerView = (RecyclerView) parent.findViewById(R.id.category_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        categoryList = Arrays.asList(getResources().getStringArray(R.array.categories));
        categoryAdapter = new CategoryAdapter(getActivity(),categoryList);
        mRecyclerView.setAdapter(categoryAdapter);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        menu.clear();
        inflater.inflate(R.menu.add_transction, menu);

    }
}
