package com.example.wsmm.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.wsmm.R;
import com.example.wsmm.db.DBClient;
import com.example.wsmm.util.GeneralUtils;

import java.io.IOException;

import io.realm.Realm;


/**
 * Created by abubaker on 2/26/16.
 */
public class ExportFile extends BaseFragment{

    DBClient db;


    public ExportFile(){

    }

    @Override
    public int getLayoutId() {
        return R.layout.export_fragment_layout;
    }

    @Override
    public void initViews(View parent, Bundle savedInstanceState) {
        super.initViews(parent, savedInstanceState);



    }
}
