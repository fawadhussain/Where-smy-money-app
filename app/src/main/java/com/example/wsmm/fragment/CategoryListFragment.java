package com.example.wsmm.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import com.example.wsmm.R;
import com.example.wsmm.adapter.CategoryManagerAdapter;
import com.example.wsmm.db.DBClient;
import com.example.wsmm.model.CategoryItem;

import java.util.List;

/**
 * Created by abubaker on 24/04/2016.
 */
public class CategoryListFragment extends BaseFragment{

    private RecyclerView mRecyclerView;
    private CategoryManagerAdapter categoryManagerAdapter;
    DBClient db;
    private List<CategoryItem> itemList;


    public CategoryListFragment (){

    }


    @Override
    public int getLayoutId() {
        return R.layout.category_list_fragment;
    }


    @Override
    public void initViews(View parent, Bundle savedInstanceState) {
        super.initViews(parent, savedInstanceState);

        mRecyclerView = (RecyclerView) parent.findViewById(R.id.category_list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        setHasOptionsMenu(true);
        db = new DBClient();

        itemList = db.getCategoryList();


        categoryManagerAdapter = new CategoryManagerAdapter(getActivity(), itemList);
        mRecyclerView.setAdapter(categoryManagerAdapter);
        categoryManagerAdapter.setClickListener(new CategoryManagerAdapter.ClickListener() {
            @Override
            public void onItemClicked(int itemPosition) {

                CategoryEditFragment categoryEditFragment = new CategoryEditFragment();
                categoryEditFragment.setCategoryId(itemList.get(itemPosition).getCategoryId());
                categoryEditFragment.setCategoryTitle(itemList.get(itemPosition).getCategoryTitle());
                categoryEditFragment.setCategoryName(itemList.get(itemPosition).getCategoryItemName());
                getHelper().replaceFragment(categoryEditFragment,false,true,"CategoryEditFragment");



            }
        });

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        menu.clear();
        inflater.inflate(R.menu.add_transction, menu);

    }

    @Override
    public void onResume() {
        super.onResume();
       /* db = new DBClient();

        itemList = db.getCategoryList();
        categoryManagerAdapter = new CategoryManagerAdapter(getActivity(), itemList);
        mRecyclerView.setAdapter(categoryManagerAdapter);*/
    }
}
