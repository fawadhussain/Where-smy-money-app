package com.example.wsmm.activity;


import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.example.wsmm.R;
import com.example.wsmm.TabFragment;
import com.example.wsmm.db.DBClient;
import com.example.wsmm.fragment.AddTransactionFragment;
import com.example.wsmm.fragment.ChartFragment;
import com.example.wsmm.fragment.NavigationDrawerFragment;
import com.example.wsmm.fragment.PrimaryFragment;
import com.example.wsmm.model.Category;
import com.example.wsmm.model.CategoryItem;
import com.example.wsmm.util.GeneralUtils;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends BaseActivity implements View.OnClickListener, PrimaryFragment.OnUpdateToolBar, PopupMenu.OnMenuItemClickListener, OnDateSelectedListener, DatePickerDialog.OnDateSetListener {


    Calendar cal;
    TextView datePickerText;
    MaterialCalendarView widget;
    private boolean week = false;
    private boolean month = false;
    private PrimaryFragment primaryFragment;
    private Bundle bundle;
    private Dialog dialog;
    DBClient db;
    FragmentTransaction mFragmentTransaction;
    private List<Category> categories;
    public static boolean checkPreviousRecords = false;
    private List<String> categoryList;
    private CategoryItem categoryItem;
    private HashMap<String, ArrayList<Category>> map;
    ArrayList<Category> childCategoryList;
    ArrayList<String> headerList = new ArrayList<String>();
    String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        datePickerText = (TextView) findViewById(R.id.date_picker_text);
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawerLayout), mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        cal = Calendar.getInstance();
        bundle = new Bundle();
        datePickerText.setText("TODAY " + getMonth(cal.get(Calendar.MONTH)) + " " + cal.get(Calendar.DAY_OF_MONTH));
        datePickerText.setOnClickListener(this);

        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */

        DBClient dbClient = new DBClient();
        categoryList = Arrays.asList(getResources().getStringArray(R.array.categories));

        if (dbClient.getCategoryList().size() > 0) {

            replaceFragment(new TabFragment(), false, "TabFragment");

        } else {

            for (int i = 0; i < categoryList.size(); i++) {

                categoryItem = new CategoryItem();
                categoryItem.setCategoryItemName(categoryList.get(i));
                categoryItem.setCategoryTitle(categoryList.get(i));
                dbClient.saveCategoryList(categoryItem);
            }

            replaceFragment(new TabFragment(), false, "TabFragment");

        }


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.date_picker_text:

                datePickerDialog();

                break;

        }

    }


    private String getMonth(int i) {

        switch (i) {
            case 1:
                return "FEB";

            case 2:
                return "MAR";

            case 3:
                return "APR";

            case 4:
                return "MAY";

            case 5:
                return "JUN";

            case 6:
                return "JUL";

            case 7:
                return "AUG";

            case 8:
                return "SEP";

            case 9:
                return "OCT";

            case 10:
                return "NOV";
            case 11:
                return "DEC";
            default:
                return "JAN";

        }


    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_day:
                week = false;
                month = true;
                return true;
            case R.id.item_week:
                // Toast.makeText(this, "Week", Toast.LENGTH_SHORT).show();
                week = true;
                month = false;
                //widget.setCalendarDisplayMode(CalendarMode.WEEKS);
                return true;
            case R.id.item_seven_days:
                checkPreviousRecords = true;
                db = new DBClient();
                categories = db.getLastSevenDaysData();
                if (map!=null)
                map.clear();
                if (headerList!=null)
                headerList.clear();
                map = new HashMap<>();



                for (int i = 0; i < categories.size(); i++) {

                    if (!map.containsKey(categories.get(i).getCategoryName())) {
                        categoryName = categories.get(i).getCategoryName();
                        childCategoryList = new ArrayList<Category>();
                        for (int j = 0; j < categories.size(); j++) {

                            if (categories.get(j).getCategoryName().equals(categoryName)) {
                                childCategoryList.add(categories.get(j));
                            }
                        }
                        headerList.add(categoryName);
                        map.put(categoryName, childCategoryList);
                    }
                }

                primaryFragment = new PrimaryFragment();

                primaryFragment.setGroupedTransactions(map);
                primaryFragment.setHeaderList(headerList);
                primaryFragment.setPreviousRecords(categories);

                //replaceFragment(primaryFragment, true, "PrimaryFragment");

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                transaction.replace(R.id.containerView, primaryFragment, "PrimaryFragment");
                transaction.addToBackStack(null);
                transaction.commit();

                datePickerText.setText(getResources().getString(R.string.lastSevenDays));


                return true;
            case R.id.item_thirty_days:

                checkPreviousRecords = true;
                db = new DBClient();

                categories = db.getLastMonthDaysData();
                map = new HashMap<>();
                map.clear();
                headerList.clear();


                for (int i = 0; i < categories.size(); i++) {

                    if (!map.containsKey(categories.get(i).getCategoryName())) {
                        categoryName = categories.get(i).getCategoryName();
                        childCategoryList = new ArrayList<Category>();
                        for (int j = 0; j < categories.size(); j++) {

                            if (categories.get(j).getCategoryName().equals(categoryName)) {
                                childCategoryList.add(categories.get(j));
                            }
                        }
                        headerList.add(categoryName);
                        map.put(categoryName, childCategoryList);
                    }
                }

                primaryFragment = new PrimaryFragment();
                primaryFragment.setGroupedTransactions(map);
                primaryFragment.setHeaderList(headerList);
                primaryFragment.setPreviousRecords(categories);

               // replaceFragment(primaryFragment, true, "PrimaryFragment");

                FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                transaction1.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                transaction1.replace(R.id.containerView, primaryFragment, "PrimaryFragment");
                transaction1.addToBackStack(null);
                transaction1.commit();

                datePickerText.setText(getResources().getString(R.string.lastThirtyDays));

                return true;
            case R.id.item_custom_range:

                checkPreviousRecords = true;

                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = com.borax12.materialdaterangepicker.date.DatePickerDialog.newInstance(
                        MainActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
                return true;
        }

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.day_range:
                View dayRange = findViewById(R.id.day_range);
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, dayRange);
                popupMenu.setOnMenuItemClickListener(MainActivity.this);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.show();

                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void datePickerDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.date_picker_dialog_fragment);
        widget = (MaterialCalendarView) dialog.findViewById(R.id.calendarView);
        widget.setOnDateChangedListener(this);
        Calendar mDataCalendar = Calendar.getInstance();
        widget.setSelectedDate(mDataCalendar);
        widget.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        if (week) {
            widget.setCalendarDisplayMode(CalendarMode.WEEKS);
        } else {
            widget.setCalendarDisplayMode(CalendarMode.MONTHS);
        }

        dialog.show();

    }


    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay day, boolean selected) {

        dialog.dismiss();

        setDate(day.getDay(), day.getMonth(), day.getYear());

        FragmentManager fm = getSupportFragmentManager();

        AddTransactionFragment fragment = (AddTransactionFragment) fm.findFragmentByTag("AddTransaction");
        TabFragment tabFragment = (TabFragment) fm.findFragmentByTag("TabFragment");

        if (fragment != null && fragment.isVisible()) {
            fragment.setChangeDate(day.getCalendar().getTimeInMillis());
        } else if (tabFragment != null && tabFragment.isVisible()) {
            checkPreviousRecords = false;
            TabFragment tabF = new TabFragment();
            bundle.putLong("date", day.getCalendar().getTimeInMillis());
            tabF.setArguments(bundle);
            replaceFragment(tabF, true, "TabFragment");
        } else {
            checkPreviousRecords = false;
            TabFragment tabF = new TabFragment();
            bundle.putLong("date", day.getCalendar().getTimeInMillis());
            tabF.setArguments(bundle);
            replaceFragment(tabF, true, "TabFragment");

        }


    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onUpdateDate(int day, int month, int year) {

        setDate(day, month, year);

    }


    private void setDate(int day, int month, int year) {
        Calendar today = Calendar.getInstance();
        today.setTimeInMillis(System.currentTimeMillis());

        Log.d("MainActivity", "setDate " + " month " + month + " day " + day + " year " + year);

        if (today.get(Calendar.MONTH) == month && today.get(Calendar.DAY_OF_MONTH) == day) {
            datePickerText.setText("TODAY " + getMonth(month) + "  " + day);
        } else {
            datePickerText.setText(getMonth(month) + "  " + day + " " + year);
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {


        db = new DBClient();
        categories = db.getCustomDateData(GeneralUtils.getTimeInMillis(dayOfMonth - 1, monthOfYear, year)
                , GeneralUtils.getTimeInMillis(dayOfMonthEnd + 1, monthOfYearEnd, yearEnd));

        map = new HashMap<>();
        map.clear();
        headerList.clear();


        for (int i = 0; i < categories.size(); i++) {

            if (!map.containsKey(categories.get(i).getCategoryName())) {
                categoryName = categories.get(i).getCategoryName();
                childCategoryList = new ArrayList<Category>();
                for (int j = 0; j < categories.size(); j++) {

                    if (categories.get(j).getCategoryName().equals(categoryName)) {
                        childCategoryList.add(categories.get(j));
                    }
                }
                headerList.add(categoryName);
                map.put(categoryName, childCategoryList);
            }
        }

        primaryFragment = new PrimaryFragment();

        primaryFragment.setGroupedTransactions(map);
        primaryFragment.setHeaderList(headerList);
        primaryFragment.setPreviousRecords(categories);

        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        transaction1.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction1.replace(R.id.containerView, primaryFragment, "PrimaryFragment");
        transaction1.addToBackStack(null);
        transaction1.commit();

       // replaceFragment(primaryFragment, true, "PrimaryFragment");
        datePickerText.setText(getResources().getString(R.string.custom_range));

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();

            replaceFragment(new ChartFragment(), false, "ChartFragment");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

            FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
            }
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }
}
