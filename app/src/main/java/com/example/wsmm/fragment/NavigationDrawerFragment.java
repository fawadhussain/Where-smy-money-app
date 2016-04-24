package com.example.wsmm.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.wsmm.R;
import com.example.wsmm.TabFragment;
import com.example.wsmm.activity.MainActivity;
import com.example.wsmm.db.DBClient;
import com.example.wsmm.util.GeneralUtils;

import io.realm.Realm;


/**
 * Created by abubaker on 2/25/16.
 */
public class NavigationDrawerFragment extends BaseFragment implements View.OnClickListener{


    private ActionBarDrawerToggle mDrawerToggle;
    private  DrawerLayout mDrawerLayout;
    private  View mContainerView;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_navigation_drawer;
    }

    @Override
    public void initViews(View parent, Bundle savedInstanceState) {
        super.initViews(parent, savedInstanceState);
        parent.findViewById(R.id.transactions).setOnClickListener(this);
        parent.findViewById(R.id.settings).setOnClickListener(this);
        parent.findViewById(R.id.export_csv).setOnClickListener(this);
        parent.findViewById(R.id.how_to_us).setOnClickListener(this);
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        mContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {

                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.transactions:
                closeNav();
                getHelper().replaceFragment(new TabFragment(),true,"TabFragment");
                MainActivity.checkPreviousRecords =false;
                break;
            case R.id.settings:
                closeNav();
                getHelper().replaceFragment(new Settings(),true,"Settings");
                break;
            case R.id.export_csv:
                closeNav();
                //getHelper().replaceFragment(new ExportFile(),false,"ExportFile");
                exportCsvData();

                break;
            case R.id.how_to_us:
                closeNav();
                getHelper().replaceFragment(new AboutUs(),true,"AboutUs");
                break;
        }


    }

    public  void closeNav() {

        mDrawerLayout.closeDrawer(mContainerView);
    }


    private void exportCsvData(){

       DBClient db = new DBClient();

        db.exportDataToCSV(getActivity(),new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {

                if (GeneralUtils.getUri()!=null){


                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Expense Detail");
                    sendIntent.putExtra(Intent.EXTRA_STREAM, GeneralUtils.getUri());
                    sendIntent.setType("text/html");
                    startActivity(sendIntent);

                }else {
                    Toast.makeText(getActivity(),"No Record Found",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }



}
