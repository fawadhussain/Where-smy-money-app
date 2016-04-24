package com.example.wsmm.fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.example.wsmm.R;


/**
 * Created by abubaker on 2/26/16.
 */
public class AboutUs extends BaseFragment{

    public AboutUs(){

    }

    @Override
    public int getLayoutId() {
        return R.layout.about_fragment_layout;
    }

    @Override
    public void initViews(View parent, Bundle savedInstanceState) {
        super.initViews(parent, savedInstanceState);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        menu.clear();
        inflater.inflate(R.menu.add_transction, menu);

    }
}
