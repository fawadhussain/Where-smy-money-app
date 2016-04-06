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
    private List<Category> distinctRecords;
    private List<Category> particularRecords;
    private  int day , month, year;
    PrimaryFragment primaryFragment;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View x = inflater.inflate(R.layout.tab_layout, null);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);
        db = new DBClient();
        distinctRecords = db.getRecords();
        //expenseList = db.getRecords();
        //dateList = new ArrayList<Long>();

        /*for (int i=0; i<expenseList.size(); i++){
            dateList.add(expenseList.get(i).getDate());
        }*/

        Bundle args = getArguments();

        if (args != null){

            day = getArguments().getInt("day");
            month = getArguments().getInt("month");
            year = getArguments().getInt("year");

        }

        particularRecords = db.getParticularRecord(day, month,year);





        //Collections.sort(dateList, Collections.reverseOrder());


        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        if (distinctRecords.size() > 0){
            viewPager.setCurrentItem(distinctRecords.size()-1, true);

        }else {

            viewPager.setCurrentItem(0, true);

        }

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



             if (distinctRecords.size() > 0 && particularRecords.size() > 0){

                 Bundle bundle = new Bundle();
                 bundle.putInt("day", day);
                 bundle.putInt("month", month);
                 bundle.putInt("year", year);
                 primaryFragment = new PrimaryFragment();
                 primaryFragment.setArguments(bundle);


                 Log.d("TabFragment", "getItem: =" + viewPager.getCurrentItem());
                 return primaryFragment;
             }else {
                 return new PrimaryFragment();
             }






        }

        @Override
        public int getCount() {

            //return expenseList.size();

            if (distinctRecords.size() > 0){
                return distinctRecords.size();
            }else {
                return 1;
            }


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
