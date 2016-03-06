package com.example.wsmm.fragment;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.wsmm.R;


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
                getHelper().replaceFragment(new Transactions(),false);
                break;
            case R.id.settings:
                closeNav();
                getHelper().replaceFragment(new Settings(),false);
                break;
            case R.id.export_csv:
                closeNav();
                getHelper().replaceFragment(new ExportFile(),false);
                break;
            case R.id.how_to_us:
                closeNav();
                getHelper().replaceFragment(new AboutUs(),false);
                break;
        }


    }

    public  void closeNav() {

        mDrawerLayout.closeDrawer(mContainerView);
    }



}
