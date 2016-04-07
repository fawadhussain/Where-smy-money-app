package com.example.wsmm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wsmm.db.DBClient;
import com.example.wsmm.fragment.PrimaryFragment;
import com.example.wsmm.model.Category;

import java.util.List;


public class TabFragment extends Fragment{

    public static ViewPager viewPager;
    DBClient db;
    private List<Category> distinctRecords;
    private List<Category> particularRecords;
    private  int day , month, year;
    PrimaryFragment primaryFragment;
    Bundle bundle = null;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View x = inflater.inflate(R.layout.tab_layout, null);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);
        db = new DBClient();
        distinctRecords = db.getRecords();

        Bundle args = getArguments();

        if (args != null){

            day = getArguments().getInt("day");
            month = getArguments().getInt("month");
            year = getArguments().getInt("year");

        }

        particularRecords = db.getParticularRecord(day, month,year);



        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        if (distinctRecords.size() > 0){
            viewPager.setCurrentItem(distinctRecords.size()-1, true);

        }else {

            viewPager.setCurrentItem(0, true);

        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {



            }

            @Override
            public void onPageSelected(int position) {

                bundle = new Bundle();
                bundle.putInt("position", position);
                primaryFragment.setCurrentPostionAndData(position);
                Log.d("onPageSelected","positio "+position);



            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return x;

    }


    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position) {

            Log.d("getItem","position "+position);


             if (distinctRecords.size() > 0){

                 primaryFragment = new PrimaryFragment();
                 if(bundle == null)
                 {bundle = new Bundle();
                 bundle.putInt("position", position);}
                 primaryFragment.setArguments(bundle);
                 Log.d("TabFragment", "getItem: =" + viewPager.getCurrentItem());
                 return primaryFragment;
             }else  {
                 return new PrimaryFragment();
             }

        }

        @Override
        public int getCount() {


            if (distinctRecords.size() > 0){
                return distinctRecords.size();
            }else {
                return 1;
            }


        }




    }




}
