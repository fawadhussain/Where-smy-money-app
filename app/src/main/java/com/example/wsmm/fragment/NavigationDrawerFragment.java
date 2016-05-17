package com.example.wsmm.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
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
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;


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
                getHelper().replaceFragment(new TabFragment(),true,false,"TabFragment");
              // fragmentTransaction(new TabFragment(),"TabFragment");
                MainActivity.checkPreviousRecords =false;
                break;
            case R.id.settings:
                closeNav();
               // fragmentTransaction(new Settings(),"Settings");
                getHelper().replaceFragment(new Settings(),false,true,"Settings");
                break;
            case R.id.export_csv:
                closeNav();

                if (Build.VERSION.SDK_INT >= 23) {

                    checkPermissionReadExternalStorage();

                } else {

                    exportCsvData();

                }




                break;
            case R.id.how_to_us:
                closeNav();
                getHelper().replaceFragment(new AboutUs(),true,false,"AboutUs");

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


    private void fragmentTransaction(Fragment fragment,String tag){

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.replace(R.id.containerView, fragment, tag);
        //transaction.addToBackStack(null);
        transaction.commit();

    }


    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissionReadExternalStorage() {
        int hasWriteContactsPermission = getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showMessageOKCancel("You need to allow access to Read External Storage",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                        REQUEST_CODE_ASK_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }

        exportCsvData();

    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case REQUEST_CODE_ASK_PERMISSIONS: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    exportCsvData();
                } else {
                    // Permission Denied
                    Toast.makeText(getActivity(), "Permission READ EXTERNAL STORAGE Denied", Toast.LENGTH_SHORT)
                            .show();
                }

                break;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



}
