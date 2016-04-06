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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class TabFragment extends Fragment {

    public static ViewPager viewPager;
    DBClient db;
    private List<Category> expenseList;
    private ArrayList<Long> dateList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View x = inflater.inflate(R.layout.tab_layout, null);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);
        db = new DBClient();
        expenseList = db.getRecords();
        dateList = new ArrayList<Long>();

        for (int i=0; i<expenseList.size(); i++){
            dateList.add(expenseList.get(i).getDate());
        }



        //Collections.sort(dateList, Collections.reverseOrder());


        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
        viewPager.setCurrentItem(expenseList.size()-1, true);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

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


            PrimaryFragment primaryFragment = new PrimaryFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("day", getDay(dateList.get(position)));
            bundle.putInt("month", getMonth(dateList.get(position)));
            bundle.putInt("year", getYear(dateList.get(position)));
            primaryFragment = new PrimaryFragment();
            primaryFragment.setArguments(bundle);

            Log.d("TabFragment", "getItem: =" + viewPager.getCurrentItem());



            return primaryFragment;

        }

        @Override
        public int getCount() {

            return expenseList.size();

        }

        private int getDay(long date){

            Calendar calendar =Calendar.getInstance();
            calendar.setTimeInMillis(date);
            return calendar.get(Calendar.DAY_OF_MONTH);

        }


        private int getMonth(long date){

            Calendar calendar =Calendar.getInstance();
            calendar.setTimeInMillis(date);
            return calendar.get(Calendar.MONTH);

        }


        private int getYear(long date){

            Calendar calendar =Calendar.getInstance();
            calendar.setTimeInMillis(date);
            return calendar.get(Calendar.YEAR);

        }



    }





}
