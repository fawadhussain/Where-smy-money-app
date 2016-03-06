package com.example.wsmm.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;


import com.example.wsmm.R;
import com.example.wsmm.fragment.BaseFragment;

import java.util.Stack;



/**
 * Created by abubaker on 7/30/15.
 */
public class BaseActivity extends AppCompatActivity implements BaseFragment.FragmentNavigationHelper {

    public BaseFragment mCurrentFragment;
    private Stack<Fragment> mFragments = new Stack<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void addFragment(BaseFragment f, boolean clearBackStack) {

        if (clearBackStack) {
            clearFragmentBackStack();
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.add(R.id.containerView, f);
        transaction.addToBackStack(null);
        transaction.commit();

        mCurrentFragment = f;
        mFragments.push(f);

        onFragmentBackStackChanged();

    }

    @Override
    public void replaceFragment(BaseFragment f, boolean clearBackStack) {

        if (clearBackStack) {
            clearFragmentBackStack();
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.replace(R.id.containerView, f);
        transaction.addToBackStack(null);
        transaction.commit();

        mCurrentFragment = f;
        mFragments.push(f);

        onFragmentBackStackChanged();
    }

    @Override
    public void onBack() {
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            finish();
            return;
        }

        getSupportFragmentManager().popBackStack();
        mFragments.pop();
        mCurrentFragment = (BaseFragment) (mFragments.isEmpty() ? null : ((mFragments.peek() instanceof BaseFragment) ? mFragments.peek() : null));

        onFragmentBackStackChanged();
    }

    public void clearFragmentBackStack() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
            fm.popBackStack();
        }

        if (!mFragments.isEmpty()) {
            Fragment homeFragment = mFragments.get(0);
            mFragments.clear();
            mFragments.push(homeFragment);
        }

    }

    public void onFragmentBackStackChanged() {

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            boolean flag = false;
            if (mCurrentFragment != null) {
                flag = mCurrentFragment.onKeyDown(keyCode, event);
                //******
            }
            if (flag) {
                return flag;
            }
            if (getSupportFragmentManager().getBackStackEntryCount() <= 0) {
                finish();
                return true;
            } else {
                onBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


}
