package com.example.wsmm.fragment;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;

import com.example.wsmm.R;
import com.example.wsmm.db.DBClient;
import com.example.wsmm.model.CategoryItem;

import io.realm.Realm;

/**
 * Created by abubaker on 24/04/2016.
 */
public class CategoryEditFragment extends BaseFragment{

    EditText categoryField;

    private int categoryId;
    private String categoryName = null;
    DBClient dbClient;

    @Override
    public int getLayoutId() {
        return R.layout.edit_category_fragment;
    }


    @Override
    public void initViews(View parent, Bundle savedInstanceState) {
        super.initViews(parent, savedInstanceState);
        categoryField = (EditText)parent.findViewById(R.id.et_category_name);
        setHasOptionsMenu(true);
        if (categoryName!= null){
            categoryField.setText(getCategoryName());
        }



        parent.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CategoryItem item = new CategoryItem();
                item.setCategoryId(categoryId);
                item.setCategoryTitle(categoryField.getText().toString());
                item.setCategoryItemName(categoryName);

                dbClient = new DBClient();
                dbClient.updateCategoryItem(item,new Realm.Transaction.OnSuccess(){
                    @Override
                    public void onSuccess() {

                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        if (fm.getBackStackEntryCount() > 0) {
                            fm.popBackStack();
                        }




                    }
                });




            }
        });

    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        menu.clear();
        inflater.inflate(R.menu.add_transction, menu);

    }


    public void setCategoryName(String name){

        categoryName = name;

    }


    public void setCategoryId(int id){
        categoryId = id;


    }


    private int getCategoryId(){

        return categoryId;

    }


    private String getCategoryName(){
        return categoryName;
    }


}
