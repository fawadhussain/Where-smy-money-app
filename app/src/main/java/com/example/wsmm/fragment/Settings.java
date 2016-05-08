package com.example.wsmm.fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import com.example.wsmm.R;
import com.example.wsmm.util.GeneralUtils;
import com.example.wsmm.util.SPManager;


/**
 * Created by abubaker on 2/26/16.
 */
public class Settings extends BaseFragment implements View.OnClickListener {


    TextView textSymbol;

    public Settings(){

    }

    @Override
    public int getLayoutId() {
        return R.layout.setting_fragment_layout;
    }

    @Override
    public void initViews(View parent, Bundle savedInstanceState) {
        super.initViews(parent, savedInstanceState);
        textSymbol = (TextView)parent.findViewById(R.id.currency_symbol);

        if (SPManager.getCurrency(getActivity()) != -1 ){
            textSymbol.setText(GeneralUtils.getCurrencySymbol(getActivity(),SPManager.getCurrency(getActivity())));
        }else {
            textSymbol.setText("$USD");
        }

        parent.findViewById(R.id.currency_layout).setOnClickListener(this);
        parent.findViewById(R.id.categories_layout).setOnClickListener(this);
        parent.findViewById(R.id.reminder_layout).setOnClickListener(this);
        setHasOptionsMenu(true);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.currency_layout:
                getHelper().replaceFragment(new CurrencyFragment(),false,"CurrencyFragment");
                break;
            case R.id.categories_layout:
                getHelper().replaceFragment(new CategoryListFragment(),false,"CategoryListFragment");
                break;
            case R.id.reminder_layout:
                break;
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        menu.clear();
        inflater.inflate(R.menu.add_transction, menu);

    }
}
