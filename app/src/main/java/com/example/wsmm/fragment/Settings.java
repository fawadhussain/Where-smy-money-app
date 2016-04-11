package com.example.wsmm.fragment;

import android.os.Bundle;
import android.view.View;

import com.example.wsmm.R;


/**
 * Created by abubaker on 2/26/16.
 */
public class Settings extends BaseFragment implements View.OnClickListener{

    public Settings(){

    }

    @Override
    public int getLayoutId() {
        return R.layout.setting_fragment_layout;
    }

    @Override
    public void initViews(View parent, Bundle savedInstanceState) {
        super.initViews(parent, savedInstanceState);
        parent.findViewById(R.id.currency_layout).setOnClickListener(this);
        parent.findViewById(R.id.categories_layout).setOnClickListener(this);
        parent.findViewById(R.id.reminder_layout).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.currency_layout:
                break;
            case R.id.categories_layout:
                break;
            case R.id.reminder_layout:
                break;
        }

    }
}
